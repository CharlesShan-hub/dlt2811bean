<template>
  <div class="app">
    <TopBar
      :tcp-connected="tcpConnected"
      :ap="associatedAp"
      :tls="tlsConnected"
      :ap-secure="apSecure"
      :terminal-open="showTerminal"
      :theme="theme"
      :dark-mode="darkMode"
      :collapsed="sidebarCollapsed"
      @toggle-terminal="showTerminal = !showTerminal"
      @set-theme="setTheme"
      @toggle-mode="setMode(!darkMode)"
      @toggle-sidebar="toggleSidebar"
    />
    <div class="app-body">
      <Sidebar
        :items="navItems"
        :active="sidebarActive"
        :collapsed="sidebarCollapsed"
        @select="onSidebarSelect($event, false)"
        @select-duplicate="onSidebarSelect($event, true)"
        @toggle-collapse="toggleSidebar"
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
            <DataSetView v-else-if="tab.viewId === 'dataset-view'" />
            <SgView v-else-if="tab.viewId === 'sg-view'" />
            <ReportView v-else-if="tab.viewId === 'report-view'" />
            <LogView v-else-if="tab.viewId === 'log-view'" />
            <GooseView v-else-if="tab.viewId === 'goose-view'" />
            <MsvView v-else-if="tab.viewId === 'msv-view'" />
            <FileView v-else-if="tab.viewId === 'file-view'" />
            <RpcView v-else-if="tab.viewId === 'rpc-view'" />
          </div>
        </div>

        <!-- 底部终端面板（顶部手柄拖拽调高，双击还原） -->
        <div v-show="showTerminal" class="terminal-panel" :style="{ height: terminalHeight + 'px' }">
          <div class="terminal-drag" title="拖拽调整高度 · 双击还原" @mousedown.prevent="startTerminalDrag" @dblclick="resetTerminalHeight"></div>
          <div class="terminal-panel-head">
            <span class="terminal-panel-title"><TerminalIcon :size="13" /> 终端</span>
            <button class="terminal-panel-close" title="关闭终端" @click="showTerminal = false">✕</button>
          </div>
          <Terminal embedded class="terminal-panel-body" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import TerminalIcon from '@lucide/vue/dist/esm/icons/terminal.mjs'
import TopBar from './components/TopBar.vue'
import Sidebar from './components/Sidebar.vue'
import TabBar from './components/TabBar.vue'
import CommandDebug from './views/CommandDebug.vue'
import Terminal from './views/Terminal.vue'
import ServerDir from './views/ServerDir.vue'
import DataSetView from './views/DataSetView.vue'
import SgView from './views/SgView.vue'
import ReportView from './views/ReportView.vue'
import LogView from './views/LogView.vue'
import GooseView from './views/GooseView.vue'
import MsvView from './views/MsvView.vue'
import FileView from './views/FileView.vue'
import RpcView from './views/RpcView.vue'
import { getStatus } from './api/cms.js'
import { CMD_IDS } from './cmddefs/index.js'
import { refreshLds, setLds } from './ldCache.js'
import { useTabs, cnTitle } from './composables/useTabs.js'

const cmdViews = CMD_IDS

const {
  tabs,
  activeTab,
  activeView,
  openTab,
  switchTab,
  closeTab,
  closeLeft,
  closeRight,
  closeOthers,
  closeAll,
  togglePin,
  reorderTab,
} = useTabs()

const connected = ref(false)
const tcpConnected = ref(false)
const associatedAp = ref('')
const tlsConnected = ref(false)
const apSecure = ref(false)
const showTerminal = ref(false)

// 主题（默认蓝色，存 localStorage 持久化）
const savedTheme = localStorage.getItem('cms-theme')
const theme = ref(savedTheme || 'blue')

function setTheme(id) {
  theme.value = id
  document.documentElement.setAttribute('data-theme', id)
  localStorage.setItem('cms-theme', id)
}

// 深浅模式（默认深色，存 localStorage 持久化）
const savedMode = localStorage.getItem('cms-mode')
const darkMode = ref(savedMode !== 'light')

function setMode(dark) {
  darkMode.value = dark
  document.documentElement.setAttribute('data-mode', dark ? 'dark' : 'light')
  localStorage.setItem('cms-mode', dark ? 'dark' : 'light')
}

// 侧边栏折叠状态（存 localStorage 持久化）
const sidebarCollapsed = ref(localStorage.getItem('cms-sidebar-collapsed') === '1')

function toggleSidebar() {
  sidebarCollapsed.value = !sidebarCollapsed.value
  localStorage.setItem('cms-sidebar-collapsed', sidebarCollapsed.value ? '1' : '0')
}

// 终端面板高度（顶部手柄拖拽调整，双击还原，存 localStorage）
const DEFAULT_TERMINAL_H = 300
const terminalHeight = ref(Number(localStorage.getItem('cms-terminal-height')) || DEFAULT_TERMINAL_H)
watch(terminalHeight, (v) => localStorage.setItem('cms-terminal-height', String(v)))

let terminalDrag = null

function startTerminalDrag(e) {
  terminalDrag = { startY: e.clientY, startH: terminalHeight.value }
  document.addEventListener('mousemove', onTerminalDrag)
  document.addEventListener('mouseup', stopTerminalDrag)
  document.body.style.cursor = 'row-resize'
  document.body.style.userSelect = 'none'
}

function onTerminalDrag(e) {
  if (!terminalDrag) return
  // 向上拖（clientY 减小）→ 面板变高
  const dy = terminalDrag.startY - e.clientY
  terminalHeight.value = Math.max(100, Math.min(600, terminalDrag.startH + dy))
}

function stopTerminalDrag() {
  terminalDrag = null
  document.removeEventListener('mousemove', onTerminalDrag)
  document.removeEventListener('mouseup', stopTerminalDrag)
  document.body.style.cursor = ''
  document.body.style.userSelect = ''
}

function resetTerminalHeight() {
  terminalHeight.value = DEFAULT_TERMINAL_H
}

// 当前年份（空状态版权）
const currentYear = new Date().getFullYear()

// ── 服务视图路由：侧边栏大块（含其子命令）→ 对应的服务视图 ──
const SVC_VIEWS = {
  dataset: 'dataset-view',
  sg: 'sg-view',
  report: 'report-view',
  log: 'log-view',
  goose: 'goose-view',
  msv: 'msv-view',
  file: 'file-view',
  rpc: 'rpc-view',
}
// 反向：服务视图 viewId → 侧边栏分组 id（用于侧边栏高亮）
const VIEW_TO_GROUP = Object.fromEntries(Object.entries(SVC_VIEWS).map(([k, v]) => [v, k]))
const sidebarActive = computed(() => VIEW_TO_GROUP[activeView.value] || activeView.value)

/** 子命令不在此路由：直接打开 CommandDebug（各命令的"卷子"页面） */
function onSidebarSelect(viewId, forceNew) {
  // 大块分组 → 服务视图；其余（含子命令）→ 按自身 viewId 打开
  const svcView = SVC_VIEWS[viewId]
  openTab(svcView || viewId, forceNew)
}

// ── 导航配置 ──
// done: true  → 绿色圆点（已实现），false → 红色圆点（尚未实现）
const navItems = [
  {
    id: 'connect-root',
    label: '连接管理',
    icon: 'Plug',
    children: ['negotiate', 'associate', 'release', 'abort', 'test'].map((id) => ({ id, label: cnTitle(id), done: true })),
  },
  { id: 'dir-tree', label: '目录与数据', icon: 'FolderTree', children: [
    { id: 'server-dir', label: cnTitle('server-dir'), done: true },
    { id: 'ld-dir', label: cnTitle('ld-dir'), done: true },
    { id: 'ln-dir', label: cnTitle('ln-dir'), done: true },
    { id: 'all-data', label: cnTitle('all-data'), done: true },
    { id: 'all-def', label: cnTitle('all-def'), done: true },
    { id: 'all-cb', label: cnTitle('all-cb'), done: true },
    { id: 'get-data-values', label: cnTitle('get-data-values'), done: true },
    { id: 'set-data-values', label: cnTitle('set-data-values'), done: true },
    { id: 'data-dir', label: cnTitle('data-dir'), done: true },
    { id: 'get-data-def', label: cnTitle('get-data-def'), done: true },
  ] },
  { id: 'dataset', label: '数据集', icon: 'Table', children: [
    { id: 'get-dataset-values', label: cnTitle('get-dataset-values'), done: true },
    { id: 'set-dataset-values', label: cnTitle('set-dataset-values'), done: false },
    { id: 'create-dataset', label: cnTitle('create-dataset'), done: false },
    { id: 'delete-dataset', label: cnTitle('delete-dataset'), done: false },
    { id: 'get-dataset-dir', label: cnTitle('get-dataset-dir'), done: false },
  ] },
  { id: 'sg', label: '定值组', icon: 'Settings', done: false, children: [
    { id: 'select-active-sg', label: cnTitle('select-active-sg'), done: false },
    { id: 'select-edit-sg', label: cnTitle('select-edit-sg'), done: false },
    { id: 'set-edit-sg', label: cnTitle('set-edit-sg'), done: false },
    { id: 'confirm-edit-sg', label: cnTitle('confirm-edit-sg'), done: false },
    { id: 'get-edit-sg', label: cnTitle('get-edit-sg'), done: false },
    { id: 'sgcb-vals', label: cnTitle('sgcb-vals'), done: false },
  ] },
  { id: 'report', label: '报告服务', icon: 'FileText', done: false, children: [
    { id: 'get-brcb-vals', label: cnTitle('get-brcb-vals'), done: false },
    { id: 'set-brcb-vals', label: cnTitle('set-brcb-vals'), done: false },
    { id: 'get-urcb-vals', label: cnTitle('get-urcb-vals'), done: false },
    { id: 'set-urcb-vals', label: cnTitle('set-urcb-vals'), done: false },
  ] },
  { id: 'log', label: '日志服务', icon: 'Scroll', done: false, children: [
    { id: 'get-lcb-vals', label: cnTitle('get-lcb-vals'), done: false },
    { id: 'set-lcb-vals', label: cnTitle('set-lcb-vals'), done: false },
    { id: 'query-log-by-time', label: cnTitle('query-log-by-time'), done: false },
    { id: 'query-log-after', label: cnTitle('query-log-after'), done: false },
    { id: 'get-log-status', label: cnTitle('get-log-status'), done: false },
  ] },
  { id: 'goose', label: 'GOOSE', icon: 'Radio', done: false, children: [
    { id: 'get-go-ref', label: cnTitle('get-go-ref'), done: false },
    { id: 'get-goose-elem', label: cnTitle('get-goose-elem'), done: false },
    { id: 'get-gocb-vals', label: cnTitle('get-gocb-vals'), done: false },
    { id: 'set-gocb-vals', label: cnTitle('set-gocb-vals'), done: false },
  ] },
  { id: 'msv', label: '多播采样值', icon: 'Activity', done: false, children: [
    { id: 'get-msvcb-vals', label: cnTitle('get-msvcb-vals'), done: false },
    { id: 'set-msvcb-vals', label: cnTitle('set-msvcb-vals'), done: false },
  ] },
  { id: 'file', label: '文件服务', icon: 'Folder', done: false, children: [
    { id: 'get-file', label: cnTitle('get-file'), done: false },
    { id: 'set-file', label: cnTitle('set-file'), done: false },
    { id: 'delete-file', label: cnTitle('delete-file'), done: false },
    { id: 'get-file-attrs', label: cnTitle('get-file-attrs'), done: false },
    { id: 'get-file-dir', label: cnTitle('get-file-dir'), done: false },
  ] },
  { id: 'rpc', label: 'RPC接口', icon: 'ArrowLeftRight', done: false, children: [
    { id: 'rpc-iface-dir', label: cnTitle('rpc-iface-dir'), done: false },
    { id: 'rpc-method-dir', label: cnTitle('rpc-method-dir'), done: false },
    { id: 'rpc-iface-def', label: cnTitle('rpc-iface-def'), done: false },
    { id: 'rpc-method-def', label: cnTitle('rpc-method-def'), done: false },
    { id: 'rpc-call', label: cnTitle('rpc-call'), done: false },
  ] },
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
  // 初始化主题与深浅模式
  document.documentElement.setAttribute('data-theme', theme.value)
  document.documentElement.setAttribute('data-mode', darkMode.value ? 'dark' : 'light')
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

/* 浅色模式下光晕加深一点，保持氛围感 */
[data-mode="light"] .glow-spot-1 {
  background: radial-gradient(circle, rgba(91, 141, 239, 0.2), transparent 70%);
}
[data-mode="light"] .glow-spot-2 {
  background: radial-gradient(circle, rgba(91, 141, 239, 0.14), transparent 70%);
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
  background: var(--glass-bg);
  border: 1px solid var(--glass-border);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  box-shadow: 0 12px 64px rgba(0, 0, 0, 0.4), inset 0 1px 0 var(--glass-border);
  user-select: none;
  max-width: 440px;
  width: 100%;
  transition: border-color 0.4s, box-shadow 0.4s;
}

.glass-card:hover {
  border-color: var(--glass-hover-border);
  box-shadow: 0 16px 72px rgba(0, 0, 0, 0.45), inset 0 1px 0 var(--glass-hover-border);
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
  color: var(--text-primary);
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
  background: var(--glass-hover-bg);
  color: var(--text-secondary);
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
  background: linear-gradient(90deg, transparent, var(--border), transparent);
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
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 1.2px;
  font-weight: 500;
}

.info-value {
  font-size: 14px;
  color: var(--text-primary);
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
  color: var(--text-muted);
  margin: 18px 0 0;
  letter-spacing: 0.5px;
  font-weight: 400;
}

/* ── 底部终端面板 ── */
.terminal-panel {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--border);
  background: var(--bg-secondary);
}

/* 顶部拖拽手柄 */
.terminal-drag {
  height: 6px;
  flex-shrink: 0;
  cursor: row-resize;
  position: relative;
  z-index: 5;
  transition: background 0.15s;
}
.terminal-drag:hover,
.terminal-drag:active {
  background: var(--accent);
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
  display: inline-flex;
  align-items: center;
  gap: 6px;
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