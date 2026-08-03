<template>
  <div class="ui-collapse">
    <button type="button" class="ui-collapse__head" @click="open = !open">
      <slot name="title" />
      <span class="ui-collapse__arrow" :class="{ open }">▾</span>
    </button>
    <transition name="ui-collapse">
      <div v-show="open" class="ui-collapse__body">
        <slot />
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  /** 初始是否展开（默认收起） */
  defaultOpen: { type: Boolean, default: false },
})

const open = ref(props.defaultOpen)
</script>

<style scoped>
.ui-collapse {
  border-bottom: 1px solid var(--border);
}

.ui-collapse:last-child {
  border-bottom: none;
}

.ui-collapse__head {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 4px;
  background: transparent;
  border: none;
  cursor: pointer;
  color: var(--text-primary);
  font-size: 13px;
  text-align: left;
  transition: color 0.15s;
}

.ui-collapse__head:hover {
  color: var(--accent);
}

.ui-collapse__arrow {
  margin-left: auto;
  color: var(--text-muted);
  font-size: 12px;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.ui-collapse__arrow.open {
  transform: rotate(180deg);
}

.ui-collapse__body {
  padding: 0 4px 12px;
}

.ui-collapse-enter-active,
.ui-collapse-leave-active {
  transition: opacity 0.18s;
}

.ui-collapse-enter-from,
.ui-collapse-leave-to {
  opacity: 0;
}
</style>
