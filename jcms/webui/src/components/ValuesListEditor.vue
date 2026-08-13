<template>
  <div class="values-list">
    <div v-for="(m, i) in members" :key="i" class="value-row">
      <span class="value-index">{{ i + 1 }}</span>
      <span class="value-ref" :title="m.reference">{{ m.reference }} <span class="value-fc">[{{ m.fc }}]</span></span>
      <UiInput
        :model-value="modelValue[i]"
        :placeholder="'输入值 (' + (m.typeHint || 'string') + ')'"
        @update:model-value="onInput(i, $event)"
      />
    </div>
    <p v-if="!members.length" class="empty-tip">请先选择数据集</p>
  </div>
</template>

<script setup>
import { watch } from 'vue'
import UiInput from './ui/UiInput.vue'

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
  members: {
    type: Array,
    default: () => [],
  },
})

const emit = defineEmits(['update:modelValue'])

// 当成员列表变化时，确保 modelValue 数组长度匹配
watch(() => props.members.length, (len) => {
  if (len > 0 && props.modelValue.length !== len) {
    const arr = new Array(len).fill('')
    for (let i = 0; i < Math.min(props.modelValue.length, len); i++) {
      arr[i] = props.modelValue[i] ?? ''
    }
    emit('update:modelValue', arr)
  }
})

function onInput(index, value) {
  const arr = [...props.modelValue]
  // 确保数组长度足够
  while (arr.length < props.members.length) arr.push('')
  arr[index] = value
  emit('update:modelValue', arr)
}
</script>

<style scoped>
.values-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.value-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.value-index {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 600;
  color: var(--text-muted);
  background: var(--bg-tertiary, rgba(255,255,255,0.05));
  border-radius: 4px;
  border: 1px solid var(--border);
}

.value-ref {
  flex-shrink: 0;
  font-size: 12px;
  color: var(--text-secondary);
  font-family: var(--font-mono, monospace);
  min-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.value-fc {
  font-size: 10px;
  color: var(--text-muted);
  background: var(--bg-tertiary, rgba(255,255,255,0.05));
  padding: 1px 5px;
  border-radius: 3px;
}

.value-row .ui-input {
  flex: 1;
  min-width: 0;
}

.empty-tip {
  font-size: 13px;
  color: var(--text-muted);
  text-align: center;
  padding: 12px 0;
}
</style>