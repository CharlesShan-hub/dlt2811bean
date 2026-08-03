<template>
  <input
    class="ui-input"
    :type="type"
    :value="modelValue"
    :placeholder="placeholder"
    :readonly="readonly"
    :disabled="disabled"
    @input="$emit('update:modelValue', $event.target.value)"
  />
</template>

<script setup>
defineProps({
  modelValue: [String, Number],
  type: { type: String, default: 'text' },
  placeholder: String,
  readonly: Boolean,
  disabled: Boolean,
})

defineEmits(['update:modelValue'])
</script>

<style scoped>
.ui-input {
  width: 100%;
  box-sizing: border-box;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 10px 12px;
  color: var(--text-primary);
  font-size: 13px;
  outline: none;
  transition: border-color 0.15s;
}

.ui-input:focus {
  border-color: var(--accent);
}

.ui-input[readonly],
.ui-input:disabled {
  color: var(--text-muted);
  cursor: not-allowed;
}
</style>
