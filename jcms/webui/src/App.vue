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
        @select="activeView = $event"
      />
      <div class="app-main">
        <main class="main-content">
          <CommandDebug v-if="activeView === 'connect-root'" cmd="connect" :connected="connected" :tcp-connected="tcpConnected" />
          <CommandDebug v-else-if="isCmdView" :cmd="activeView" />
          <ServerDir v-else-if="activeView === 'dir-tree'" :connected="connected" />
        </main>

        <!-- 底部终端面板（调试窗口，类似 IDE 的下方面板；v-show 保活，关闭再开内容不丢） -->
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
import CommandDebug from './views/CommandDebug.vue'
import Terminal from './views/Terminal.vue'
import ServerDir from './views/ServerDir.vue'
import { getStatus } from './api/cms.js'
import { CMD_DEFS, CMD_IDS } from './cmddefs/index.js'
import { refreshLds, setLds } from './ldCache.js'

const activeView = ref('connect-root')
const connected = ref(false)
const tcpConnected = ref(false)
const associatedAp = ref('')
const tlsConnected = ref(false)
const apSecure = ref(false)
const showTerminal = ref(false)

/** 侧边栏子项只显示中文短名（title 格式 "中文 英文 (章节)" → 取中文部分）。 */
const cnTitle = (id) => {
  const t = CMD_DEFS[id] && CMD_DEFS[id].title ? CMD_DEFS[id].title : id
  return t.split(' ')[0] || t
}

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

const cmdViews = CMD_IDS
const isCmdView = computed(() => cmdViews.includes(activeView.value))

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
  // 关联状态变化时维护 LD 缓存：建立后拉取，断开时清空
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
