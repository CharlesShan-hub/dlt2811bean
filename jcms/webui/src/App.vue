<template>
  <div class="app">
    <TopBar :connected="connected" />
    <div class="app-body">
      <Sidebar
        :items="navItems"
        :active="activeView"
        @select="activeView = $event"
      />
      <main class="main-content">
        <Dashboard v-if="activeView === 'dashboard'" :connected="connected" />
        <Terminal v-else-if="activeView === 'terminal'" />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import TopBar from './components/TopBar.vue'
import Sidebar from './components/Sidebar.vue'
import Dashboard from './views/Dashboard.vue'
import Terminal from './views/Terminal.vue'
import { getStatus } from './api/cms.js'

const activeView = ref('dashboard')
const connected = ref(false)

const navItems = [
  { id: 'dashboard', label: '仪表盘', icon: '◉' },
  { id: 'terminal', label: '终端', icon: '⊢' },
  { id: 'server-dir', label: '目录树', icon: '⊞' },
  { id: 'data', label: '数据浏览', icon: '☰' },
  { id: 'dataset', label: '数据集', icon: '⧉' },
  { id: 'sg', label: '定值组', icon: '⚙' },
  { id: 'report', label: '报告', icon: '📋' },
  { id: 'file', label: '文件', icon: '📁' },
  { id: 'log', label: '日志', icon: '📝' },
]

let timer

async function pollStatus() {
  try {
    const status = await getStatus()
    connected.value = status.connected
  } catch {
    connected.value = false
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
