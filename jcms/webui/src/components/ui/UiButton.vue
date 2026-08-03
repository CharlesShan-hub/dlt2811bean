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
  border: 1px solid var(--border);
  background: var(--bg-primary);
  color: var(--text-primary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
}

.ui-btn:hover:not(:disabled) {
  background: var(--bg-hover);
}

.ui-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  box-shadow: none;
}

.ui-btn--primary {
  background: linear-gradient(135deg, var(--accent), var(--accent-hover));
  border-color: transparent;
  color: #fff;
  box-shadow: 0 2px 10px rgba(91, 141, 239, 0.3);
}

.ui-btn--primary:hover:not(:disabled) {
  filter: brightness(1.08);
}

.ui-btn--danger {
  color: var(--red);
}

.ui-btn--danger:hover:not(:disabled) {
  background: var(--red-bg);
  border-color: rgba(229, 85, 90, 0.4);
}

.ui-btn__spinner {
  width: 12px;
  height: 12px;
  border: 2px solid rgba(255, 255, 255, 0.4);
  border-top-color: #fff;
  border-radius: 50%;
  animation: ui-spin 0.7s linear infinite;
}

.ui-btn--ghost .ui-btn__spinner,
.ui-btn--danger .ui-btn__spinner {
  border-color: var(--text-muted);
  border-top-color: var(--text-primary);
}

@keyframes ui-spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
