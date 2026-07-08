<template>
  <header class="topbar">
    <div class="topbar-brand">
      <span class="logo">⚡</span>
      <span class="title">CMS Console</span>
    </div>
    <div class="topbar-right">
      <span class="status-badge" :class="connected ? 'connected' : 'disconnected'">
        <span class="dot"></span>
        {{ connected ? '已连接' : '未连接' }}
      </span>
    </div>
  </header>
</template>

<script setup>
defineProps({
  connected: Boolean,
})
</script>

<style scoped>
.topbar {
  height: var(--topbar-height);
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  flex-shrink: 0;
}

.topbar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo {
  font-size: 20px;
}

.title {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: 0.5px;
}

.status-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.connected {
  color: var(--green);
  background: var(--green-bg);
}

.status-badge.disconnected {
  color: var(--text-muted);
  background: transparent;
  border: 1px solid var(--border);
}

.dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
}

.connected .dot {
  background: var(--green);
  box-shadow: 0 0 6px var(--green);
}

.disconnected .dot {
  background: var(--text-muted);
}
</style>
