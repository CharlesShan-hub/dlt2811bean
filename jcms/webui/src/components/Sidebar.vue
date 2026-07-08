<template>
  <nav class="sidebar">
    <div class="nav-section">
      <span class="section-label">导航</span>
    </div>
    <a
      v-for="item in items"
      :key="item.id"
      class="nav-item"
      :class="{ active: active === item.id }"
      @click="$emit('select', item.id)"
    >
      <span class="nav-icon">{{ item.icon }}</span>
      <span class="nav-label">{{ item.label }}</span>
    </a>
  </nav>
</template>

<script setup>
defineProps({
  items: Array,
  active: String,
})

defineEmits(['select'])
</script>

<style scoped>
.sidebar {
  width: var(--sidebar-width);
  background: var(--bg-secondary);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  padding: 12px 8px;
  flex-shrink: 0;
  overflow-y: auto;
}

.nav-section {
  padding: 4px 12px 8px;
}

.section-label {
  font-size: 11px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  color: var(--text-secondary);
  text-decoration: none;
  transition: all 0.15s;
  margin-bottom: 2px;
}

.nav-item:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.nav-item.active {
  background: var(--accent-muted);
  color: var(--accent);
}

.nav-icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
}

.nav-label {
  font-size: 13px;
  font-weight: 500;
}
</style>
