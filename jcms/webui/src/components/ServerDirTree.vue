<template>
  <div class="tree-panel">
    <!-- 搜索框：输入即过滤 LD/LN，扁平展示命中结果 -->
    <div class="tree-search">
      <Search :size="14" class="tree-search-icon" />
      <input
        v-model="query"
        class="tree-search-input"
        type="text"
        placeholder="搜索 LD / LN…"
        spellcheck="false"
      />
      <button v-if="query" class="tree-search-clear" title="清空搜索" @click="query = ''">✕</button>
    </div>
    <p v-if="!connected" class="panel-hint">请先连接服务器</p>
    <div v-else-if="query" class="tree-scroll">
      <div v-if="searchLoading" class="search-status">搜索中…</div>
      <div v-else-if="searchResults.length === 0" class="search-status">未找到匹配节点</div>
      <div
        v-for="r in searchResults"
        :key="r.type + ':' + r.name"
        class="search-row"
        :title="r.name"
        @click="$emit('select-result', r)"
      >
        <span class="search-badge" :class="r.type">{{ r.type === 'ld' ? 'LD' : 'LN' }}</span>
        <span class="search-label" v-html="highlight(r.label)"></span>
      </div>
    </div>
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
import { computed } from 'vue'
import Search from '@lucide/vue/dist/esm/icons/search.mjs'
import TreeNode from './TreeNode.vue'
import { escHtml } from '../utils/cmdFormat.js'

const props = defineProps({
  connected: Boolean,
  lds: { type: Array, default: () => [] },
  searchQuery: { type: String, default: '' },
  searchResults: { type: Array, default: () => [] },
  searchLoading: Boolean,
})

const emit = defineEmits(['toggle', 'toggle-acsi', 'update:searchQuery', 'select-result'])

const query = computed({
  get: () => props.searchQuery,
  set: (v) => emit('update:searchQuery', v),
})

/** 命中关键词用 <mark> 高亮（先转义再匹配，避免注入）。 */
function highlight(text) {
  const q = query.value.trim()
  if (!q) return escHtml(text)
  const esc = escHtml(text)
  const idx = esc.toLowerCase().indexOf(q.toLowerCase())
  if (idx < 0) return esc
  return (
    esc.slice(0, idx) +
    '<mark>' + esc.slice(idx, idx + q.length) + '</mark>' +
    esc.slice(idx + q.length)
  )
}
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

.panel-hint {
  color: var(--text-muted);
  font-size: 13px;
  padding: 0 20px;
  flex-shrink: 0;
}

/* ── 搜索框 ── */
.tree-search {
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 0 16px 10px;
  padding: 6px 10px;
  border: 1px solid var(--border);
  border-radius: 8px;
  background: var(--bg-primary);
  flex-shrink: 0;
  transition: border-color 0.15s;
}
.tree-search:focus-within {
  border-color: var(--accent);
}
.tree-search-icon {
  color: var(--text-muted);
  flex-shrink: 0;
}
.tree-search-input {
  flex: 1;
  min-width: 0;
  border: none;
  outline: none;
  background: transparent;
  color: var(--text-primary);
  font-size: 13px;
}
.tree-search-input::placeholder {
  color: var(--text-muted);
}
.tree-search-clear {
  border: none;
  background: transparent;
  color: var(--text-muted);
  font-size: 11px;
  cursor: pointer;
  padding: 2px 4px;
  border-radius: 4px;
  flex-shrink: 0;
}
.tree-search-clear:hover {
  color: var(--text-primary);
  background: var(--bg-hover);
}

/* ── 搜索结果 ── */
.tree-scroll {
  overflow-y: auto;
  flex: 1;
  padding-bottom: 20px;
}

.search-status {
  color: var(--text-muted);
  font-size: 12px;
  text-align: center;
  padding: 20px 0;
}

.search-row {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  margin: 1px 4px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.1s;
}
.search-row:hover {
  background: var(--bg-hover);
}

.search-badge {
  flex-shrink: 0;
  font-size: 10px;
  font-weight: 600;
  padding: 1px 6px;
  border-radius: 4px;
  line-height: 1.6;
}
.search-badge.ld {
  color: var(--green);
  background: var(--green-bg);
}
.search-badge.ln {
  color: var(--accent-hover);
  background: var(--accent-muted);
}

.search-label {
  font-size: 13px;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-family: var(--font-mono);
}
.search-label :deep(mark) {
  color: var(--doc-strong);
  background: rgba(229, 185, 85, 0.22);
  border-radius: 2px;
  padding: 0 1px;
}
</style>
