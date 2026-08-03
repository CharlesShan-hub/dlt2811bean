<template>
  <div class="ui-switch" :class="{ on: modelValue }" @click="$emit('update:modelValue', !modelValue)">
    <span class="ui-switch__knob"></span>
  </div>
</template>

<script setup>
defineProps({
  modelValue: { type: Boolean, default: false },
})

defineEmits(['update:modelValue'])
</script>

<style scoped>
.ui-switch {
  width: 42px;
  height: 24px;
  border-radius: 12px;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  position: relative;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.ui-switch__knob {
  position: absolute;
  top: 2px;
  left: 2px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: var(--text-muted);
  transition: all 0.2s;
}

.ui-switch.on {
  background: var(--accent);
  border-color: var(--accent);
}

.ui-switch.on .ui-switch__knob {
  left: 20px;
  background: #fff;
}
</style>
