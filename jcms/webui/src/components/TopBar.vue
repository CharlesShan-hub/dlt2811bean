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
      <!-- 深色/浅色模式切换 -->
      <button
        class="topbar-btn mode-btn"
        :class="{ active: !darkMode }"
        :title="darkMode ? '切换到浅色模式' : '切换到深色模式'"
        @click="$emit('toggle-mode')"
      >
        <span class="tb-icon">{{ darkMode ? '☀️' : '🌙' }}</span>
      </button>
      <!-- 主题切换 -->
      <div class="theme-wrap" ref="themeWrapRef">
        <button class="topbar-btn theme-btn" :title="'主题：' + themeLabel" @click="toggleThemeMenu">
          <span class="tb-icon">◈</span>
        </button>
        <div v-if="themeOpen" class="theme-dropdown">
          <button
            v-for="t in themes"
            :key="t.id"
            class="theme-option"
            :class="{ active: t.id === theme }"
            @click="selectTheme(t.id)"
          >
            <span class="theme-dot" :style="{ background: t.color }"></span>
            <span class="theme-name">{{ t.label }}</span>
          </button>
        </div>
      </div>
      <!-- 终端面板开关（调试窗口） -->
      <button class="topbar-btn" :class="{ active: terminalOpen }" title="打开/关闭终端" @click="$emit('toggle-terminal')">
        <span class="tb-icon">⊢</span>
        <span>终端</span>
      </button>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
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
  /** 当前主题 id */
  theme: { type: String, default: 'blue' },
  /** 是否深色模式 */
  darkMode: { type: Boolean, default: true },
})

const emit = defineEmits(['toggle-terminal', 'set-theme', 'toggle-mode'])

const themes = [
  { id: 'blue', label: '蓝色', color: '#5b8def' },
  { id: 'green', label: '绿色', color: '#4caf7d' },
  { id: 'orange', label: '橙色', color: '#e8893c' },
  { id: 'red', label: '红色', color: '#e5555a' },
  { id: 'purple', label: '紫色', color: '#8a5ce0' },
  { id: 'cyan', label: '青色', color: '#5bc0de' },
  { id: 'pink', label: '粉色', color: '#ff7eb6' },
]

const themeLabel = computed(() => themes.find(t => t.id === props.theme)?.label || '蓝色')

const themeOpen = ref(false)
const themeWrapRef = ref(null)

function toggleThemeMenu() {
  themeOpen.value = !themeOpen.value
}

function selectTheme(id) {
  emit('set-theme', id)
  themeOpen.value = false
}

// 点击外部关闭下拉
function onDocClick(e) {
  if (themeWrapRef.value && !themeWrapRef.value.contains(e.target)) {
    themeOpen.value = false
  }
}

if (typeof window !== 'undefined') {
  document.addEventListener('click', onDocClick)
}
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

/* ── 主题切换 ── */
.theme-wrap {
  position: relative;
}

.theme-btn .tb-icon {
  font-size: 16px;
  line-height: 1;
}

.theme-dropdown {
  position: absolute;
  top: calc(100% + 6px);
  right: 0;
  z-index: 100;
  min-width: 110px;
  padding: 6px;
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: 10px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.theme-option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 10px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: var(--text-secondary);
  font-size: 12px;
  cursor: pointer;
  transition: all 0.12s;
  text-align: left;
  width: 100%;
}

.theme-option:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.theme-option.active {
  color: var(--text-primary);
  background: var(--accent-muted);
}

.theme-dot {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  flex-shrink: 0;
  border: 2px solid var(--glass-border);
  transition: transform 0.12s;
}

.theme-option:hover .theme-dot {
  transform: scale(1.15);
}

.theme-option.active .theme-dot {
  border-color: var(--glass-hover-border);
}

.theme-name {
  white-space: nowrap;
}
</style>