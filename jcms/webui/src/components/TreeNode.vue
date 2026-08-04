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
      <!-- LN：右侧 9 个 ACSI 分类圆点，点哪个加载哪个 -->
      <span v-else-if="node.type === 'ln'" class="acsi-dots">
        <span
          v-for="d in ACSI_DEFS"
          :key="d.key"
          class="acsi-dot"
          :class="{ on: isAcsiOn(d.key) }"
          :style="{ background: d.color }"
          :title="d.label"
          @click.stop="onAcsiClick(d.key)"
        ></span>
      </span>
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
import { ACSI_DEFS } from '../acsiDefs.js'

const props = defineProps({
  node: Object,
  depth: { type: Number, default: 0 },
})

const emit = defineEmits(['toggle', 'toggle-acsi'])

const isLeaf = props.node.isLeaf

/** LD 节点固定绿色（不再按是否有内容区分） */
const labelClass = computed(() => (props.node.type === 'ld' ? 'ld-has' : ''))

function handleClick() {
  emit('toggle', props.node)
}

/** ACSI 分类是否已展开（圆点高亮） */
function isAcsiOn(acsi) {
  const c = (props.node.children || []).find((c) => c.acsi === acsi)
  return !!(c && c.expanded)
}

function onAcsiClick(acsi) {
  emit('toggle-acsi', { node: props.node, acsi })
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

/* LD 固定绿色 */
.node-label.ld-has {
  color: var(--green);
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

/* ── LN 的 9 个 ACSI 分类圆点 ── */
.acsi-dots {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-left: auto;
  flex-shrink: 0;
}

.acsi-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  opacity: 0.35;
  cursor: pointer;
  flex-shrink: 0;
  transition: opacity 0.15s, transform 0.15s;
}

.acsi-dot:hover {
  opacity: 0.95;
  transform: scale(1.25);
}

.acsi-dot.on {
  opacity: 1;
  box-shadow: 0 0 6px currentColor;
}
</style>
