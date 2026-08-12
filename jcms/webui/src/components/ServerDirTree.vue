<template>
  <div class="tree-panel">
    <h2 class="panel-title">目录树</h2>
    <p v-if="!connected" class="panel-hint">请先连接服务器</p>
    <div v-else class="tree-scroll">
      <TreeNode
        v-for="ld in lds"
        :key="ld.name"
        :node="ld"
        @toggle="(n) => $emit('toggle', n)"
        @toggle-acsi="(p) => $emit('toggle-acsi', p)"
      />
    </div>
  </div>
</template>

<script setup>
import TreeNode from './TreeNode.vue'

defineProps({
  connected: Boolean,
  lds: { type: Array, default: () => [] },
})

defineEmits(['toggle', 'toggle-acsi'])
</script>

<style scoped>
.tree-panel {
  width: 320px;
  min-width: 240px;
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.panel-title {
  font-size: 14px;
  font-weight: 600;
  padding: 20px 20px 12px;
  color: var(--text-primary);
  flex-shrink: 0;
}

.panel-hint {
  color: var(--text-muted);
  font-size: 13px;
  padding: 0 20px;
  flex-shrink: 0;
}

.tree-scroll {
  overflow-y: auto;
  flex: 1;
  padding-bottom: 20px;
}
</style>