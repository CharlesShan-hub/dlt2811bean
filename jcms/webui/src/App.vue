<template>
  <div class="app">
    <TopBar :tcp-connected="tcpConnected" :ap="associatedAp" :tls="tlsConnected" :ap-secure="apSecure" />
    <div class="app-body">
      <Sidebar
        :items="navItems"
        :active="activeView"
        @select="activeView = $event"
      />
      <main class="main-content">
        <Dashboard v-if="activeView === 'connect-root'" :connected="connected" :tcp-connected="tcpConnected" />
        <CommandDebug v-else-if="isCmdView" :cmd="activeView" />
        <Terminal v-else-if="activeView === 'terminal'" />
        <ServerDir v-else-if="activeView === 'server-dir'" :connected="connected" />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import TopBar from './components/TopBar.vue'
import Sidebar from './components/Sidebar.vue'
import Dashboard from './views/Dashboard.vue'
import CommandDebug from './views/CommandDebug.vue'
import Terminal from './views/Terminal.vue'
import ServerDir from './views/ServerDir.vue'
import { getStatus } from './api/cms.js'
import { CMD_DEFS, CMD_IDS } from './cmddefs/index.js'

const activeView = ref('connect-root')
const connected = ref(false)
const tcpConnected = ref(false)
const associatedAp = ref('')
const tlsConnected = ref(false)
const apSecure = ref(false)

const navItems = [
  {
    id: 'connect-root',
    label: '连接管理',
    icon: '🔌',
    children: CMD_IDS.map((id) => ({ id, label: CMD_DEFS[id].title })),
  },
  { id: 'terminal', label: '终端', icon: '⊢' },
  { id: 'server-dir', label: '目录树', icon: '⊞' },
  { id: 'data', label: '数据浏览', icon: '☰' },
  { id: 'dataset', label: '数据集', icon: '⧉' },
  { id: 'sg', label: '定值组', icon: '⚙' },
  { id: 'report', label: '报告', icon: '📋' },
  { id: 'file', label: '文件', icon: '📁' },
  { id: 'log', label: '日志', icon: '📝' },
]

const cmdViews = CMD_IDS
const isCmdView = computed(() => cmdViews.includes(activeView.value))

let timer

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

.main-content {
  flex: 1;
  overflow-y: auto;
}
</style>
