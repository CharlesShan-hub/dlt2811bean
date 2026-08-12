<template>
  <button
    type="button"
    class="ui-btn"
    :class="`ui-btn--${variant}`"
    :disabled="disabled || loading"
    @click="$emit('click')"
  >
    <span v-if="loading" class="ui-btn__spinner"></span>
    <slot />
  </button>
</template>

<script setup>
defineProps({
  /** primary / ghost / danger */
  variant: { type: String, default: 'ghost' },
  loading: Boolean,
  disabled: Boolean,
})

defineEmits(['click'])
</script>

<style scoped>
.ui-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 9px 22px;
  border-radius: 8px;
  border: 1px solid var(--glass-border);
  background: var(--glass-bg);
  color: var(--text-primary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.2s;
}

.ui-btn:hover:not(:disabled) {
  background: var(--glass-hover-bg);
  border-color: var(--glass-hover-border);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.ui-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  box-shadow: none;
}

.ui-btn--primary {
  background: rgba(91, 141, 239, 0.18);
  border-color: rgba(91, 141, 239, 0.3);
  color: var(--btn-primary-fg);
  box-shadow: 0 4px 20px rgba(91, 141, 239, 0.15);
}

.ui-btn--primary:hover:not(:disabled) {
  background: rgba(91, 141, 239, 0.28);
  border-color: rgba(91, 141, 239, 0.5);
  box-shadow: 0 6px 28px rgba(91, 141, 239, 0.25);
  color: var(--btn-primary-fg-hover);
}

.ui-btn--danger {
  color: var(--red);
  border-color: rgba(229, 85, 90, 0.2);
  background: rgba(229, 85, 90, 0.06);
}

.ui-btn--danger:hover:not(:disabled) {
  background: rgba(229, 85, 90, 0.15);
  border-color: rgba(229, 85, 90, 0.4);
  box-shadow: 0 4px 20px rgba(229, 85, 90, 0.15);
}

.ui-btn__spinner {
  width: 12px;
  height: 12px;
  border: 2px solid var(--glass-border);
  border-top-color: var(--text-primary);
  border-radius: 50%;
  animation: ui-spin 0.7s linear infinite;
}

.ui-btn--ghost .ui-btn__spinner,
.ui-btn--danger .ui-btn__spinner {
  border-color: var(--glass-border);
  border-top-color: var(--text-primary);
}

@keyframes ui-spin {
  to {
    transform: rotate(360deg);
  }
}
</style>