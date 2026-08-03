<template>
  <div class="ui-segmented">
    <button
      v-for="opt in options"
      :key="opt.value"
      type="button"
      class="ui-segmented__item"
      :class="{ active: modelValue === opt.value }"
      @click="$emit('update:modelValue', opt.value)"
    >
      {{ opt.label }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  /** [{ value, label }] */
  options: { type: Array, default: () => [] },
  modelValue: { type: String, default: '' },
})

defineEmits(['update:modelValue'])
</script>

<style scoped>
.ui-segmented {
  display: flex;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 3px;
  gap: 3px;
}

.ui-segmented__item {
  flex: 1;
  padding: 8px 12px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s;
}

.ui-segmented__item:hover {
  color: var(--text-primary);
}

.ui-segmented__item.active {
  background: var(--accent);
  color: #fff;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(91, 141, 239, 0.35);
}
</style>
