<template>
  <teleport to="body">
    <transition name="ui-modal">
      <div v-if="modelValue" class="ui-modal" @click.self="close">
        <div class="ui-modal__panel" :class="{ 'ui-modal__panel--wide': wide }">
          <div class="ui-modal__head">
            <span class="ui-modal__title">{{ title }}</span>
            <button type="button" class="ui-modal__close" title="关闭" @click="close">✕</button>
          </div>
          <div class="ui-modal__body">
            <slot />
          </div>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup>
const props = defineProps({
  modelValue: { type: Boolean, default: false },
  title: { type: String, default: '' },
  wide: { type: Boolean, default: false },
})

const emit = defineEmits(['update:modelValue'])

function close() {
  emit('update:modelValue', false)
}
</script>

<style scoped>
.ui-modal {
  position: fixed;
  inset: 0;
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  padding: 24px;
}

.ui-modal__panel {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 720px;
  max-height: 80vh;
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.5);
  overflow: hidden;
}

.ui-modal__panel--wide {
  max-width: 900px;
}

.ui-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 18px;
  border-bottom: 1px solid var(--border);
  flex-shrink: 0;
}

.ui-modal__title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ui-modal__close {
  flex-shrink: 0;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: var(--text-muted);
  font-size: 14px;
  cursor: pointer;
  transition: background 0.12s, color 0.12s;
}

.ui-modal__close:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.ui-modal__body {
  padding: 18px;
  overflow-y: auto;
}

.ui-modal-enter-active,
.ui-modal-leave-active {
  transition: opacity 0.15s;
}

.ui-modal-enter-active .ui-modal__panel,
.ui-modal-leave-active .ui-modal__panel {
  transition: transform 0.15s;
}

.ui-modal-enter-from,
.ui-modal-leave-to {
  opacity: 0;
}

.ui-modal-enter-from .ui-modal__panel,
.ui-modal-leave-to .ui-modal__panel {
  transform: translateY(12px) scale(0.98);
}
</style>
