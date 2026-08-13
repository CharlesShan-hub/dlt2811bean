<template>
  <div class="detail-panel">
    <div v-if="!selected" class="detail-empty">
      <span class="detail-icon">⊞</span>
      <p>选择一个节点查看详情</p>
    </div>
    <div v-else-if="detailLoading" class="detail-empty">
      <span class="detail-icon">○</span>
      <p>加载中...</p>
    </div>
    <div v-else class="detail-content">
      <h3 class="detail-ref">{{ selected.ref }}</h3>

      <!-- Data directory entries -->
      <div v-if="dirEntries.length" class="data-section">
        <h4 class="section-title">属性</h4>
        <table class="data-table">
          <thead>
            <tr>
              <th>FC</th>
              <th>属性名</th>
              <th>值</th>
              <th>类型</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="entry in dirEntries"
              :key="entry.attr"
              :class="{ 'row-selected': selectedAttr === entry.attr }"
              @click="selectRow(entry)"
            >
              <td><span class="fc-badge" :title="FC_DESC_MAP[entry.fc] || entry.fc">{{ entry.fc }}</span></td>
              <td class="cell-attr">{{ entry.attr }}</td>
              <td class="cell-value">
                <button
                  type="button"
                  class="val-btn"
                  :class="{ 'val-btn--has': entry.value, 'val-btn--long': isLongValue(entry.value) }"
                  @click.stop="$emit('edit-entry', entry)"
                >
                  <span class="val-text">{{ truncateValue(entry.value) }}</span>
                  <EyeIcon v-if="isLongValue(entry.value)" class="val-view-icon" :size="14" />
                </button>
                <span v-if="editError && editingRef === entry.fullRef" class="edit-err">{{ editError }}</span>
              </td>
              <td class="cell-type">{{ entry.type ?? '—' }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Selected entry detail -->
      <div v-if="selectedEntry" class="entry-detail">
        <h4 class="section-title">值详情 — {{ selectedEntry.attr }}</h4>
        <div class="entry-detail-meta">
          <span class="entry-detail-tag">FC: {{ selectedEntry.fc }}</span>
          <span class="entry-detail-tag">类型: {{ selectedEntry.type }}</span>
          <span class="entry-detail-tag">引用: {{ selectedEntry.fullRef }}</span>
        </div>
      </div>

      <!-- JSON block -->
      <div v-if="selectedEntry" class="json-block">
        <h4 class="section-title">JSON</h4>
        <pre class="json-block-content">{{ formatEntryValue(selectedEntry.value) }}</pre>
      </div>

      <!-- Raw JSON for debugging -->
      <details class="raw-toggle">
        <summary>原始响应</summary>
        <pre class="detail-raw">{{ detailRaw }}</pre>
      </details>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import EyeIcon from '@lucide/vue/dist/esm/icons/eye.mjs'
import { FC_DESC_MAP } from '../cmddefs/common.js'

const MAX_VAL_LEN = 40

const props = defineProps({
  selected: { type: Object, default: null },
  detailLoading: Boolean,
  dirEntries: { type: Array, default: () => [] },
  detailRaw: { type: String, default: '' },
  editError: { type: String, default: '' },
  editingRef: { type: String, default: '' },
})

const emit = defineEmits(['edit-entry'])

const selectedAttr = ref(null)

/** The currently selected entry (for detail block below table). */
const selectedEntry = computed(() => {
  if (!selectedAttr.value) return null
  return props.dirEntries.find(e => e.attr === selectedAttr.value) || null
})

function selectRow(entry) {
  selectedAttr.value = selectedAttr.value === entry.attr ? null : entry.attr
}

function isLongValue(val) {
  return val && val.length > MAX_VAL_LEN
}

function truncateValue(val) {
  if (!val) return '—'
  if (val.length <= MAX_VAL_LEN) return val
  return val.slice(0, MAX_VAL_LEN) + '…'
}

function formatEntryValue(val) {
  if (!val) return '(空)'
  try {
    const parsed = JSON.parse(val)
    return JSON.stringify(parsed, null, 2)
  } catch {
    return val
  }
}
</script>

<style scoped>
.detail-panel {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.detail-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
}

.detail-icon {
  font-size: 40px;
  display: block;
  margin-bottom: 12px;
  opacity: 0.3;
}

.detail-content {
  padding: 24px 32px;
}

.detail-ref {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border);
  font-family: var(--font-mono);
}

.data-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 12px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-family: var(--font-mono);
  font-size: 13px;
}

.data-table th {
  text-align: left;
  font-size: 11px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  padding: 6px 12px;
  border-bottom: 1px solid var(--border);
  letter-spacing: 0.5px;
}

.data-table td {
  padding: 6px 12px;
  border-bottom: 1px solid var(--border);
  color: var(--text-secondary);
}

.data-table tbody tr {
  cursor: pointer;
  transition: background 0.1s;
}

.data-table tbody tr:hover {
  background: var(--bg-hover);
}

.data-table tbody tr.row-selected {
  background: var(--accent-muted);
  outline: 1px solid var(--accent);
  outline-offset: -1px;
}

.fc-badge {
  display: inline-block;
  font-size: 10px;
  font-weight: 600;
  padding: 1px 6px;
  border-radius: 3px;
  background: var(--accent-muted);
  color: var(--accent);
}

.cell-attr {
  color: var(--text-primary);
  font-weight: 500;
}

.cell-value {
  color: var(--text-primary);
}

.val-btn {
  background: none;
  border: 1px solid transparent;
  border-radius: 4px;
  color: var(--text-muted);
  font-family: inherit;
  font-size: 13px;
  padding: 2px 8px;
  cursor: pointer;
  width: 100%;
  text-align: left;
  transition: border-color 0.12s, color 0.12s, background 0.12s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.val-btn:hover {
  border-color: var(--border);
  color: var(--text-primary);
  background: var(--bg-hover);
}

.val-btn--has {
  color: var(--text-primary);
}

.val-btn--long .val-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
  min-width: 0;
}

.val-view-icon {
  flex-shrink: 0;
  color: var(--accent);
  opacity: 0.6;
  transition: opacity 0.12s;
}

.val-btn:hover .val-view-icon {
  opacity: 1;
}

.edit-err {
  font-size: 11px;
  color: var(--red);
  margin-left: 6px;
}

.cell-type {
  font-size: 12px;
  color: var(--text-muted);
}

/* ── Selected entry detail block ── */
.entry-detail {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid var(--border);
}

.entry-detail-meta {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.entry-detail-tag {
  font-size: 11px;
  font-weight: 500;
  padding: 2px 8px;
  border-radius: 4px;
  background: var(--bg-tertiary);
  color: var(--text-muted);
  font-family: var(--font-mono);
}

/* ── JSON block ── */
.json-block {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid var(--border);
}

.json-block-content {
  font-family: var(--font-mono);
  font-size: 12px;
  color: var(--text-secondary);
  white-space: pre-wrap;
  background: var(--bg-tertiary);
  padding: 14px;
  border-radius: 8px;
  border: 1px solid var(--border);
  max-height: 400px;
  overflow: auto;
  margin: 0;
}

.raw-toggle {
  margin-top: 16px;
}

.raw-toggle summary {
  font-size: 12px;
  color: var(--text-muted);
  cursor: pointer;
  padding: 4px 0;
}

.detail-raw {
  font-family: var(--font-mono);
  font-size: 12px;
  color: var(--text-secondary);
  white-space: pre-wrap;
  background: var(--bg-tertiary);
  padding: 12px;
  border-radius: 8px;
  border: 1px solid var(--border);
  margin-top: 8px;
  max-height: 300px;
  overflow: auto;
}
</style>