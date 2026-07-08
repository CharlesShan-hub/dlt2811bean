<template>
  <div class="dashboard">
    <h1 class="page-title">仪表盘</h1>
    <p class="page-desc">CMS 协议调试与管理工具</p>

    <div class="cards">
      <div class="card">
        <div class="card-title">连接状态</div>
        <div class="card-value" :class="connected ? 'ok' : 'muted'">
          {{ connected ? '已连接' : '未连接' }}
        </div>
      </div>
      <div class="card">
        <div class="card-title">关联 AP</div>
        <div class="card-value muted">—</div>
      </div>
      <div class="card">
        <div class="card-title">服务端口</div>
        <div class="card-value muted">7899</div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  connected: Boolean,
})
</script>

<style scoped>
.dashboard {
  padding: 32px;
  max-width: 800px;
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 4px;
}

.page-desc {
  color: var(--text-secondary);
  font-size: 13px;
  margin-bottom: 28px;
}

.cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.card {
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 20px;
}

.card-title {
  font-size: 12px;
  font-weight: 500;
  color: var(--text-muted);
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.card-value {
  font-size: 16px;
  font-weight: 600;
}

.card-value.ok {
  color: var(--green);
}

.card-value.muted {
  color: var(--text-secondary);
}
</style>
