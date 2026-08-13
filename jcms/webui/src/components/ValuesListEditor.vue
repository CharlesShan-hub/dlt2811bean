<template>
  <div class="values-list">
    <div v-for="(m, i) in members" :key="i" class="value-row">
      <span class="value-index">{{ i + 1 }}</span>
      <span class="value-ref" :title="m.reference">{{ m.reference }} <span class="value-fc">[{{ m.fc }}]</span></span>
      <span class="value-type">{{ m.typeHint || 'string' }}</span>
      <button
        type="button"
        class="glass glass-accent value-btn"
        :class="{ 'value-btn--has': modelValue[i] }"
        :title="'点击编辑值' + (m.typeHint ? ' (类型: ' + m.typeHint + ')' : '')"
        @click="openEditor(i)"
      >
        {{ modelValue[i] || '点击输入值' }}
      </button>
    </div>
    <p v-if="!members.length" class="empty-tip">请先选择数据集</p>

    <ComplexValueEditor
      :model-value="editingValue"
      :visible="editorVisible"
      :type="editorType"
      @update:visible="editorVisible = $event"
      @confirm="onEditorConfirm"
    />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import ComplexValueEditor from './ComplexValueEditor.vue'

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

// ── 弹窗编辑状态 ──
const editorVisible = ref(false)
const editingIndex = ref(-1)
const editingValue = ref('')
const editorType = ref('')

function openEditor(index) {
  editingIndex.value = index
  editingValue.value = props.modelValue[index] ?? ''
  editorType.value = props.members[index]?.typeHint || ''
  editorVisible.value = true
}

function onEditorConfirm(val) {
  const arr = [...props.modelValue]
  while (arr.length < props.members.length) arr.push('')
  arr[editingIndex.value] = val
  emit('update:modelValue', arr)
}

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

.value-type {
  flex-shrink: 0;
  font-size: 10px;
  font-weight: 600;
  color: var(--accent, #4fc3f7);
  background: color-mix(in srgb, var(--accent, #4fc3f7) 15%, transparent);
  padding: 2px 6px;
  border-radius: 3px;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}

.value-btn {
  flex: 1;
  min-width: 0;
  height: 32px;
  padding: 0 12px;
  font-size: 13px;
  font-family: var(--font-mono, monospace);
  text-align: left;
  cursor: pointer;
  border-radius: 6px;
  color: var(--text-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.value-btn--has {
  color: var(--text-primary);
}

.empty-tip {
  font-size: 13px;
  color: var(--text-muted);
  text-align: center;
  padding: 12px 0;
}
</style>