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

/* number 输入：隐藏浏览器原生白色 spinner（无法换肤），改用键盘上下键调节 */
.ui-input[type='number']::-webkit-inner-spin-button,
.ui-input[type='number']::-webkit-outer-spin-button {
  -webkit-appearance: none;
  appearance: none;
  margin: 0;
}

.ui-input[type='number'] {
  -moz-appearance: textfield;
  appearance: textfield;
}
</style>
