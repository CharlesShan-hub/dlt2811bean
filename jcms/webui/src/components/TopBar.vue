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
      <!-- 终端面板开关（调试窗口） -->
      <button class="topbar-btn" :class="{ active: terminalOpen }" title="打开/关闭终端" @click="$emit('toggle-terminal')">
        <span class="tb-icon">⊢</span>
        <span>终端</span>
      </button>
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
  /** 底部终端面板是否打开 */
  terminalOpen: Boolean,
})

defineEmits(['toggle-terminal'])
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

.topbar-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 5px 12px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: var(--bg-primary);
  color: var(--text-secondary);
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
}

.topbar-btn:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.topbar-btn.active {
  color: var(--accent);
  border-color: var(--accent);
  background: var(--accent-muted);
}

</style>
