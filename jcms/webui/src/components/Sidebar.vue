<template>
  <nav class="sidebar">
    <div class="nav-section">
      <span class="section-label">导航</span>
    </div>
    <template v-for="item in items" :key="item.id">
      <a
        class="nav-item"
        :class="{ active: isParentActive(item) }"
        @click="handleClick(item)"
      >
        <span class="nav-icon">{{ item.icon }}</span>
        <span class="nav-label">{{ item.label }}</span>
        <span v-if="item.children" class="nav-arrow" :class="{ open: expanded === item.id }">▾</span>
      </a>
      <div v-if="item.children && expanded === item.id" class="nav-children">
        <a
          v-for="child in item.children"
          :key="child.id"
          class="nav-item nav-child"
          :class="{ active: active === child.id }"
          @click="$emit('select', child.id)"
          @dblclick="$emit('select-duplicate', child.id)"
        >
          <span class="nav-icon">{{ child.icon || '·' }}</span>
          <span class="nav-label">{{ child.label }}</span>
        </a>
      </div>
    </template>
  </nav>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  items: Array,
  active: String,
})

const emit = defineEmits(['select', 'select-duplicate'])

const expanded = ref(null)

/** 父节点高亮：自身激活，或任一子项激活。 */
function isParentActive(item) {
  if (item.children) {
    return props.active === item.id || item.children.some((c) => c.id === props.active)
  }
  return props.active === item.id
}

function handleClick(item) {
  if (item.children) {
    expanded.value = expanded.value === item.id ? null : item.id
    emit('select', item.id)
  } else {
    emit('select', item.id)
  }
}
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
  user-select: none;
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
  flex-shrink: 0;
}

.nav-label {
  font-size: 13px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.nav-arrow {
  margin-left: auto;
  color: var(--text-muted);
  font-size: 11px;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.nav-arrow.open {
  transform: rotate(180deg);
}

.nav-children {
  display: flex;
  flex-direction: column;
  margin-left: 16px;
  padding-left: 12px;
  border-left: 1px solid var(--border);
}

.nav-child {
  padding: 6px 10px;
  font-size: 12px;
}

.nav-child .nav-label {
  font-weight: 400;
}
</style>
