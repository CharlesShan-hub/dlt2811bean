<template>
  <section class="ui-card" :class="{ fill }">
    <header
      v-if="title || $slots.header"
      class="ui-card__header"
      :class="{ clickable: collapsible }"
      @click="collapsible && toggle()"
    >
      <span v-if="icon" class="ui-card__icon">{{ icon }}</span>
      <span class="ui-card__title">{{ title }}</span>
      <span v-if="collapsible" class="ui-card__arrow" :class="{ open }">▾</span>
      <slot name="header" />
    </header>
    <div v-show="!collapsible || open" class="ui-card__body">
      <slot />
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  title: String,
  icon: String,
  /** 头部可点击折叠 */
  collapsible: Boolean,
  /** 填充剩余高度，内容区内部滚动 */
  fill: Boolean,
  /** 初始是否展开（collapsible 时生效） */
  defaultOpen: { type: Boolean, default: true },
})

const open = ref(props.defaultOpen)

function toggle() {
  open.value = !open.value
}
</script>

<style scoped>
.ui-card {
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.ui-card__header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
}

.ui-card__header.clickable {
  cursor: pointer;
  user-select: none;
}

.ui-card__header.clickable:hover .ui-card__title {
  color: var(--text-primary);
}

.ui-card__icon {
  font-size: 15px;
}

.ui-card__title {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.8px;
  transition: color 0.15s;
}

.ui-card__arrow {
  margin-left: auto;
  color: var(--text-muted);
  font-size: 12px;
  transition: transform 0.2s;
}

.ui-card__arrow.open {
  transform: rotate(180deg);
}

/* 填充模式：占满父容器剩余高度，内容区内部滚动 */
.ui-card.fill {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.ui-card.fill .ui-card__body {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}
</style>
