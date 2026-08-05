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
          <!-- 空状态：毛玻璃卡片 -->
          <div v-if="tabs.length === 0" class="empty-state">
            <!-- 背景装饰光晕 -->
            <div class="glow-spot glow-spot-1"></div>
            <div class="glow-spot glow-spot-2"></div>
            <!-- 毛玻璃卡片 -->
            <div class="glass-card">
              <div class="glass-icon">
                <svg viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <defs>
                    <linearGradient id="bolt-grad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="0%" stop-color="#fbbf24"/>
                      <stop offset="100%" stop-color="#f59e0b"/>
                    </linearGradient>
                    <filter id="bolt-glow">
                      <feDropShadow dx="0" dy="0" stdDeviation="6" flood-color="#f59e0b" flood-opacity="0.6"/>
                    </filter>
                  </defs>
                  <path d="M42 6 L18 44 L38 44 L32 74 L62 36 L40 36 Z" fill="url(#bolt-grad)" opacity="0.9" filter="url(#bolt-glow)"/>
                </svg>
              </div>
              <h2 class="glass-title"><span class="title-accent">C</span>ommunication <span class="title-accent">M</span>essage <span class="title-accent">S</span>pecification</h2>
              <div class="glass-tags">
                <a class="badge" href="https://std.samr.gov.cn/hb/search/stdHBDetailed?id=2FA2BA0490F589C3E06397BE0A0A088A" target="_blank" rel="noopener">
                  <span class="badge-label">DL/T 2811</span>
                  <span class="badge-value badge-blue">2024</span>
                </a>
                <a class="badge" href="https://openstd.samr.gov.cn/bzgk/std/newGbInfo?hcno=74223AF4CD83CFF63AFA1CA04B9CCFF9" target="_blank" rel="noopener">
                  <span class="badge-label">GB/T 45906.3</span>
                  <span class="badge-value badge-teal">2025</span>
                </a>
              </div>
              <div class="glass-divider"></div>
              <div class="glass-info">
                <span class="info-item">
                  <span class="info-label">状态</span>
                  <span
                    class="info-value status-dot"
                    :class="connected ? 'online' : 'offline'"
                    :style="connected ? {} : { cursor: 'pointer' }"
                    @click="!connected && openTab('connect-root')"
                  >{{ connected ? '已连接' : '未连接' }}</span>
                </span>
              </div>
              <p class="glass-footer">© {{ currentYear }} CMS Console</p>
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

// 当前年份（空状态版权）
const currentYear = new Date().getFullYear()

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
  // 允许关闭最后一个标签，显示空状态背景
  tabs.value.splice(idx, 1)
  if (tabs.value.length === 0) {
    activeTab.value = ''
    return
  }
  // 如果关闭的是当前标签，切换到相邻标签
  if (activeTab.value === id) {
    const newIdx = idx > 0 ? idx - 1 : 0
    activeTab.value = tabs.value[newIdx]?.id || ''
  }
}

// ── 右键菜单操作 ──

/** 关闭指定索引左边的所有非固定标签 */
function closeLeft(index) {
  const toRemove = tabs.value.slice(0, index).filter((t) => !t.pinned)
  const activeLost = toRemove.some((t) => t.id === activeTab.value)
  toRemove.forEach((t) => {
    const i = tabs.value.findIndex((x) => x.id === t.id)
    if (i !== -1) tabs.value.splice(i, 1)
  })
  if (activeLost) {
    activeTab.value = tabs.value[0]?.id || ''
  }
}

/** 关闭指定索引右边的所有非固定标签 */
function closeRight(index) {
  const toRemove = tabs.value.slice(index + 1).filter((t) => !t.pinned)
  const activeLost = toRemove.some((t) => t.id === activeTab.value)
  toRemove.forEach((t) => {
    const i = tabs.value.findIndex((x) => x.id === t.id)
    if (i !== -1) tabs.value.splice(i, 1)
  })
  if (activeLost) {
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

/** 关闭所有非固定标签（有 pin 的保留） */
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

/* ── 空状态：毛玻璃卡片 ── */
.empty-state {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary);
  position: relative;
  overflow: hidden;
}

/* 背景光晕 */
.glow-spot {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(100px);
}

.glow-spot-1 {
  width: 500px;
  height: 500px;
  top: -15%;
  left: -10%;
  background: radial-gradient(circle, rgba(91, 141, 239, 0.08), transparent 70%);
  animation: glowFloat 10s ease-in-out infinite;
}

.glow-spot-2 {
  width: 400px;
  height: 400px;
  bottom: -15%;
  right: -10%;
  background: radial-gradient(circle, rgba(91, 141, 239, 0.06), transparent 70%);
  animation: glowFloat 12s ease-in-out infinite reverse;
}

@keyframes glowFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(20px, -15px) scale(1.05); }
}

/* 毛玻璃卡片 */
.glass-card {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0;
  padding: 44px 56px 40px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  box-shadow: 0 12px 64px rgba(0, 0, 0, 0.4), inset 0 1px 0 rgba(255, 255, 255, 0.08);
  user-select: none;
  max-width: 440px;
  width: 100%;
  transition: border-color 0.4s, box-shadow 0.4s;
}

.glass-card:hover {
  border-color: rgba(255, 255, 255, 0.12);
  box-shadow: 0 16px 72px rgba(0, 0, 0, 0.45), inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

.glass-icon {
  width: 100px;
  height: 100px;
  margin-bottom: 12px;
  filter: drop-shadow(0 0 60px rgba(245, 158, 11, 0.25));
}

.glass-title {
  font-size: 19px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.92);
  margin: 0;
  letter-spacing: 0.8px;
  white-space: nowrap;
}

.title-accent {
  text-shadow: 0 0 14px rgba(91, 141, 239, 0.6), 0 0 28px rgba(91, 141, 239, 0.25);
}

.glass-tags {
  display: flex;
  gap: 8px;
  margin: 12px 0 0;
  flex-wrap: wrap;
  justify-content: center;
}

.tag {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  font-size: 11px;
  font-weight: 500;
  font-family: 'SF Mono', 'Fira Code', 'Cascadia Code', monospace;
  color: rgba(91, 141, 239, 0.85);
  background: rgba(91, 141, 239, 0.08);
  border: 1px solid rgba(91, 141, 239, 0.15);
  border-radius: 6px;
  letter-spacing: 0.3px;
  text-decoration: none;
  cursor: pointer;
  transition: border-color 0.2s, background 0.2s;
}

.tag:hover {
  background: rgba(91, 141, 239, 0.14);
  border-color: rgba(91, 141, 239, 0.3);
}

/* ── GitHub 风格 Badge ── */
.badge {
  display: inline-flex;
  align-items: center;
  text-decoration: none;
  cursor: pointer;
  font-family: 'SF Mono', 'Fira Code', 'Cascadia Code', monospace;
  font-size: 11px;
  line-height: 1;
  border-radius: 6px;
  overflow: hidden;
  transition: opacity 0.2s;
}

.badge:hover {
  opacity: 0.85;
}

.badge-label {
  padding: 4px 8px;
  background: rgba(255, 255, 255, 0.09);
  color: rgba(255, 255, 255, 0.65);
  letter-spacing: 0.3px;
}

.badge-value {
  padding: 4px 8px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.badge-blue {
  background: rgba(59, 130, 246, 0.7);
  color: #fff;
}

.badge-teal {
  background: rgba(249, 115, 22, 0.75);
  color: #fff;
}

.glass-divider {
  width: 48px;
  height: 1px;
  margin: 22px 0;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.15), transparent);
}

.glass-info {
  display: flex;
  gap: 24px;
  align-items: center;
}

.info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.info-label {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.3);
  text-transform: uppercase;
  letter-spacing: 1.2px;
  font-weight: 500;
}

.info-value {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.75);
  font-weight: 500;
}

.status-dot {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.status-dot::before {
  content: '';
  width: 6px;
  height: 6px;
  border-radius: 50%;
  flex-shrink: 0;
}

.status-dot.online::before {
  background: #22c55e;
  filter: drop-shadow(0 0 4px rgba(34, 197, 94, 0.6));
}

.status-dot.offline::before {
  background: #6b7280;
}

.status-dot.offline:hover {
  color: #60a5fa;
}
.status-dot.offline:hover::before {
  background: #60a5fa;
  filter: drop-shadow(0 0 4px rgba(96, 165, 250, 0.5));
}

.glass-footer {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.18);
  margin: 18px 0 0;
  letter-spacing: 0.5px;
  font-weight: 400;
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