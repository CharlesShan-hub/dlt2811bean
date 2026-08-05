<template>
  <div class="app">
    <TopBar
      :tcp-connected="tcpConnected"
      :ap="associatedAp"
      :tls="tlsConnected"
      :ap-secure="apSecure"
      :terminal-open="showTerminal"
      @toggle-terminal="showTerminal = !showTerminal"
    />
    <div class="app-body">
      <Sidebar
        :items="navItems"
        :active="activeView"
        @select="onSidebarSelect($event, false)"
        @select-duplicate="onSidebarSelect($event, true)"
      />
      <div class="app-main">
        <TabBar
          :tabs="tabs"
          :active-id="activeTab"
          @switch="switchTab"
          @close="closeTab"
          @close-left="closeLeft"
          @close-right="closeRight"
          @close-others="closeOthers"
          @close-all="closeAll"
          @toggle-pin="togglePin"
          @reorder="reorderTab"
        />
        <div class="main-content">
          <!-- 空状态：无标签时显示默认背景 -->
          <div v-if="tabs.length === 0" class="empty-state">
            <div class="empty-bg">
              <div class="empty-icon">
                <svg viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <circle cx="50" cy="50" r="42" stroke="currentColor" stroke-width="2" opacity="0.15"/>
                  <circle cx="50" cy="50" r="28" stroke="currentColor" stroke-width="1.5" opacity="0.1"/>
                  <path d="M50 8 L50 92" stroke="currentColor" stroke-width="1" opacity="0.08"/>
                  <path d="M8 50 L92 50" stroke="currentColor" stroke-width="1" opacity="0.08"/>
                  <path d="M50 20 C50 20 60 40 70 50 C60 60 50 80 50 80" stroke="currentColor" stroke-width="1.5" opacity="0.12" stroke-linecap="round"/>
                  <path d="M50 20 C50 20 40 40 30 50 C40 60 50 80 50 80" stroke="currentColor" stroke-width="1.5" opacity="0.12" stroke-linecap="round"/>
                  <text x="50" y="55" text-anchor="middle" fill="currentColor" opacity="0.25" font-size="14" font-weight="600" font-family="system-ui">CMS Console</text>
                </svg>
              </div>
              <p class="empty-text">从左侧导航打开功能页面</p>
            </div>
          </div>
          <div
            v-for="tab in tabs"
            :key="tab.id"
            v-show="tab.id === activeTab"
            class="tab-pane"
          >
            <CommandDebug v-if="tab.viewId === 'connect-root'" cmd="connect" :connected="connected" :tcp-connected="tcpConnected" />
            <CommandDebug v-else-if="cmdViews.includes(tab.viewId)" :cmd="tab.viewId" />
            <ServerDir v-else-if="tab.viewId === 'dir-tree'" :connected="connected" />
          </div>
        </div>

        <!-- 底部终端面板 -->
        <div v-show="showTerminal" class="terminal-panel">
          <div class="terminal-panel-head">
            <span class="terminal-panel-title">⊢ 终端</span>
            <button class="terminal-panel-close" title="关闭终端" @click="showTerminal = false">✕</button>
          </div>
          <Terminal embedded class="terminal-panel-body" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import TopBar from './components/TopBar.vue'
import Sidebar from './components/Sidebar.vue'
import TabBar from './components/TabBar.vue'
import CommandDebug from './views/CommandDebug.vue'
import Terminal from './views/Terminal.vue'
import ServerDir from './views/ServerDir.vue'
import { getStatus } from './api/cms.js'
import { CMD_DEFS, CMD_IDS } from './cmddefs/index.js'
import { refreshLds, setLds } from './ldCache.js'

const cmdViews = CMD_IDS

const connected = ref(false)
const tcpConnected = ref(false)
const associatedAp = ref('')
const tlsConnected = ref(false)
const apSecure = ref(false)
const showTerminal = ref(false)

// 从当前标签推导 activeView，用于侧边栏高亮
const activeView = computed(() => {
  const tab = tabs.value.find((t) => t.id === activeTab.value)
  return tab ? tab.viewId : ''
})

// ── 标签页管理 ──
const tabs = ref([
  { id: 'tab-connect', viewId: 'connect-root', title: '连接管理', pinned: false },
  { id: 'tab-dir', viewId: 'dir-tree', title: '目录与数据', pinned: false },
])
const activeTab = ref('tab-connect')
let nextTabId = 3

/** 侧边栏子项只显示中文短名（title 格式 "中文 英文 (章节)" → 取中文部分）。 */
const cnTitle = (id) => {
  const t = CMD_DEFS[id] && CMD_DEFS[id].title ? CMD_DEFS[id].title : id
  return t.split(' ')[0] || t
}

/** 根据 viewId 获取标签标题 */
function tabTitle(viewId) {
  if (viewId === 'dir-tree') return '目录与数据'
  if (viewId === 'connect-root') return '连接管理'
  return cnTitle(viewId)
}

/** 打开/切换标签。forceNew=true 强制新建（多开）。 */
function openTab(viewId, forceNew) {
  if (!forceNew) {
    // 单击：查找已有相同 viewId 的标签，有则切换
    const existing = tabs.value.find((t) => t.viewId === viewId)
    if (existing) {
      activeTab.value = existing.id
      return
    }
  }
  // 没找到或双击强制新建
  const id = 'tab-' + (nextTabId++)
  tabs.value.push({ id, viewId, title: tabTitle(viewId), pinned: false })
  activeTab.value = id
}

function switchTab(id) {
  activeTab.value = id
}

function closeTab(id) {
  const idx = tabs.value.findIndex((t) => t.id === id)
  if (idx === -1) return
  // 至少保留一个标签
  if (tabs.value.length <= 1) return
  tabs.value.splice(idx, 1)
  // 如果关闭的是当前标签，切换到相邻标签
  if (activeTab.value === id) {
    // 优先选前一个，否则后一个
    const newIdx = idx > 0 ? idx - 1 : 0
    activeTab.value = tabs.value[newIdx]?.id || ''
  }
}

// ── 右键菜单操作 ──

/** 关闭指定索引左边的所有非固定标签 */
function closeLeft(index) {
  const toRemove = tabs.value.slice(0, index).filter((t) => !t.pinned)
  const activeId = toRemove.find((t) => t.id === activeTab.value) ? null : activeTab.value
  toRemove.forEach((t) => {
    const i = tabs.value.findIndex((x) => x.id === t.id)
    if (i !== -1) tabs.value.splice(i, 1)
  })
  if (activeId === null) {
    // 当前标签被删了，选前一个
    const newIdx = Math.max(0, index - toRemove.length)
    activeTab.value = tabs.value[newIdx]?.id || ''
  }
}

/** 关闭指定索引右边的所有非固定标签 */
function closeRight(index) {
  const toRemove = tabs.value.slice(index + 1).filter((t) => !t.pinned)
  const activeId = toRemove.find((t) => t.id === activeTab.value) ? null : activeTab.value
  toRemove.forEach((t) => {
    const i = tabs.value.findIndex((x) => x.id === t.id)
    if (i !== -1) tabs.value.splice(i, 1)
  })
  if (activeId === null) {
    activeTab.value = tabs.value[index]?.id || ''
  }
}

/** 关闭除指定索引以外的所有非固定标签 */
function closeOthers(index) {
  const toRemove = tabs.value.filter((t, i) => i !== index && !t.pinned)
  const target = tabs.value[index]
  toRemove.forEach((t) => {
    const i = tabs.value.findIndex((x) => x.id === t.id)
    if (i !== -1) tabs.value.splice(i, 1)
  })
  activeTab.value = target?.id || tabs.value[0]?.id || ''
}

/** 关闭所有非固定标签 */
function closeAll() {
  const toRemove = tabs.value.filter((t) => !t.pinned)
  toRemove.forEach((t) => {
    const i = tabs.value.findIndex((x) => x.id === t.id)
    if (i !== -1) tabs.value.splice(i, 1)
  })
  activeTab.value = tabs.value[0]?.id || ''
}

/** 切换固定状态 */
function togglePin(id) {
  const tab = tabs.value.find((t) => t.id === id)
  if (tab) {
    tab.pinned = !tab.pinned
  }
}

/** 拖动排序 */
function reorderTab({ from, to }) {
  const [moved] = tabs.value.splice(from, 1)
  tabs.value.splice(to, 0, moved)
}

function onSidebarSelect(viewId, forceNew) {
  openTab(viewId, forceNew)
}

// ── 导航配置 ──
const navItems = [
  {
    id: 'connect-root',
    label: '连接管理',
    icon: '🔌',
    children: ['negotiate', 'associate', 'release', 'abort', 'test'].map((id) => ({ id, label: cnTitle(id) })),
  },
  { id: 'dir-tree', label: '目录与数据', icon: '⊞', children: [
    { id: 'server-dir', label: cnTitle('server-dir') },
    { id: 'ld-dir', label: cnTitle('ld-dir') },
    { id: 'ln-dir', label: cnTitle('ln-dir') },
    { id: 'all-data', label: cnTitle('all-data') },
    { id: 'all-def', label: cnTitle('all-def') },
    { id: 'all-cb', label: cnTitle('all-cb') },
    { id: 'get-data-values', label: cnTitle('get-data-values') },
    { id: 'set-data-values', label: cnTitle('set-data-values') },
    { id: 'data-dir', label: cnTitle('data-dir') },
    { id: 'get-data-def', label: cnTitle('get-data-def') },
  ] },
  { id: 'dataset', label: '数据集', icon: '⧉' },
  { id: 'sg', label: '定值组', icon: '⚙' },
  { id: 'report', label: '报告', icon: '📋' },
  { id: 'file', label: '文件', icon: '📁' },
  { id: 'log', label: '日志', icon: '📝' },
]

// ── 状态轮询 ──
let timer
let prevConnected = false

async function pollStatus() {
  try {
    const status = await getStatus()
    connected.value = status.connected
    tcpConnected.value = !!status.tcpConnected
    associatedAp.value = status.ap || ''
    tlsConnected.value = !!status.tls
    apSecure.value = !!status.apSecure
  } catch {
    connected.value = false
    tcpConnected.value = false
    associatedAp.value = ''
    tlsConnected.value = false
    apSecure.value = false
  }
  if (connected.value && !prevConnected) {
    refreshLds()
  } else if (!connected.value && prevConnected) {
    setLds([])
  }
  prevConnected = connected.value
}

onMounted(() => {
  pollStatus()
  timer = setInterval(pollStatus, 3000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.app-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.app-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
  position: relative;
}

.tab-pane {
  height: 100%;
}

/* ── 空状态背景 ── */
.empty-state {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(160deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
}

.empty-bg {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  user-select: none;
}

.empty-icon {
  width: 160px;
  height: 160px;
  color: var(--text-primary);
}

.empty-text {
  font-size: 14px;
  color: var(--text-muted);
  letter-spacing: 0.5px;
}

/* ── 底部终端面板 ── */
.terminal-panel {
  height: 300px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--border);
  background: var(--bg-secondary);
}

.terminal-panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 14px;
  border-bottom: 1px solid var(--border);
  flex-shrink: 0;
}

.terminal-panel-title {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
}

.terminal-panel-close {
  background: transparent;
  border: none;
  color: var(--text-muted);
  font-size: 12px;
  cursor: pointer;
  padding: 2px 8px;
  border-radius: 4px;
}

.terminal-panel-close:hover {
  color: var(--red);
  background: var(--red-bg);
}

.terminal-panel-body {
  flex: 1;
  min-height: 0;
}
</style>