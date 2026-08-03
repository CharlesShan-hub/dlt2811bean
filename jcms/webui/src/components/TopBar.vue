<template>
  <header class="topbar">
    <div class="topbar-brand">
      <span class="logo">⚡</span>
      <span class="title">CMS Console</span>
    </div>
    <div class="topbar-right">
      <!-- 连接状态：TCP 层（TLS 加密 🔒 / 明文 🔓） -->
      <span class="status-badge" :class="tcpConnected ? 'ok' : 'bad'">
        <span class="lock">{{ tls ? '🔒' : '🔓' }}</span>
        {{ tcpConnected ? '已连接' : '未连接' }}
      </span>
      <!-- 关联状态：已关联时显示访问点（安全认证 🔒 / 普通 🔓） -->
      <span class="status-badge" :class="ap ? 'ok' : 'bad'">
        <span class="lock">{{ apSecure ? '🔒' : '🔓' }}</span>
        {{ ap || '未关联' }}
      </span>
    </div>
  </header>
</template>

<script setup>
defineProps({
  /** TCP 层是否已连接 */
  tcpConnected: Boolean,
  /** 已关联的访问点引用（IED/AP），空 = 未关联 */
  ap: String,
  /** TCP 连接是否 TLS 加密 */
  tls: Boolean,
  /** 关联是否使用应用层安全认证 */
  apSecure: Boolean,
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

.topbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
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

.status-badge.ok {
  color: var(--green);
  background: var(--green-bg);
}

.status-badge.bad {
  color: var(--red);
  background: var(--red-bg);
}

.lock {
  font-size: 13px;
  line-height: 1;
}
</style>
