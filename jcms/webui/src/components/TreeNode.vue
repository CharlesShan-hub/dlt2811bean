<template>
  <div class="tree-node">
    <div
      class="node-row"
      :class="{ leaf: isLeaf }"
      :style="{ paddingLeft: depth * 16 + 12 + 'px' }"
      @click="handleClick"
    >
      <span v-if="isLeaf" class="node-icon leaf-icon">◌</span>
      <span v-else class="node-arrow" :class="{ expanded: node.expanded }">
        {{ node.loading ? '○' : '▸' }}
      </span>
      <span class="node-label" :class="labelClass">{{ node.label }}</span>
      <span v-if="node.type === 'ld'" class="node-type">LD</span>
      <span v-else-if="node.type === 'ln'" class="node-type">LN</span>
    </div>
    <div v-if="node.expanded && node.children" class="node-children">
      <TreeNode
        v-for="child in node.children"
        :key="child.name"
        :node="child"
        :depth="depth + 1"
        @toggle="$emit('toggle', $event)"
      />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  node: Object,
  depth: { type: Number, default: 0 },
})

const emit = defineEmits(['toggle'])

const isLeaf = props.node.isLeaf

/** LD 节点按是否含 LN 着色：有 LN 绿色，无 LN 灰色 */
const labelClass = computed(() => {
  if (props.node.type === 'ld') {
    return props.node.hasLn ? 'ld-has' : 'ld-empty'
  }
  return ''
})

function handleClick() {
  emit('toggle', props.node)
}
</script>

<style scoped>
.tree-node {
  user-select: none;
}

.node-row {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  cursor: pointer;
  border-radius: 4px;
  margin: 1px 4px;
  transition: background 0.1s;
}

.node-row:hover {
  background: var(--bg-hover);
}

.node-row.leaf {
  cursor: default;
  opacity: 0.85;
}

.node-arrow {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 14px;
  font-size: 11px;
  color: var(--text-muted);
  transition: transform 0.15s;
  flex-shrink: 0;
}

.node-arrow.expanded {
  transform: rotate(90deg);
}

.node-icon {
  width: 14px;
  font-size: 11px;
  flex-shrink: 0;
  text-align: center;
}

.leaf-icon {
  color: var(--text-muted);
}

.node-label {
  font-size: 13px;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* LD 按是否含 LN 着色 */
.node-label.ld-has {
  color: var(--green);
}

.node-label.ld-empty {
  color: var(--text-muted);
}

.node-type {
  font-size: 10px;
  font-weight: 600;
  color: var(--text-muted);
  background: var(--bg-tertiary);
  padding: 0 5px;
  border-radius: 3px;
  margin-left: auto;
  flex-shrink: 0;
  line-height: 1.6;
}
</style>
