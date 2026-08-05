<template>
  <div class="tree-page">
    <div class="tree-panel">
      <h2 class="panel-title">目录树</h2>
      <p v-if="!connected" class="panel-hint">请先连接服务器</p>
      <div v-else class="tree">
        <TreeNode
          v-for="ld in lds"
          :key="ld.name"
          :node="ld"
          @toggle="onToggle"
          @toggle-acsi="onToggleAcsi"
        />
      </div>
    </div>
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
              <tr v-for="entry in dirEntries" :key="entry.attr">
                <td><span class="fc-badge">{{ entry.fc }}</span></td>
                <td class="cell-attr">{{ entry.attr }}</td>
                <td class="cell-value" @click="startEdit(entry)">
                  <input
                    v-if="editingRef === entry.fullRef"
                    v-model="editValue"
                    class="edit-input"
                    :placeholder="entry.value ?? ''"
                    autofocus
                    @keydown.enter="saveEdit(entry)"
                    @keydown.escape="cancelEdit"
                    @blur="saveEdit(entry)"
                  />
                  <span v-else class="val-text">{{ entry.value ?? '—' }}</span>
                  <span v-if="editingRef === entry.fullRef && saving" class="saving">保存中...</span>
                  <span v-else-if="editError && editingRef === entry.fullRef" class="edit-err">{{ editError }}</span>
                </td>
                <td class="cell-type">{{ entry.type ?? '—' }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Raw JSON for debugging -->
        <details class="raw-toggle">
          <summary>原始响应</summary>
          <pre class="detail-raw">{{ detailRaw }}</pre>
        </details>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { executeJson } from '../api/cms.js'
import TreeNode from '../components/TreeNode.vue'
import { ldCache, refreshLds } from '../ldCache.js'
import { ACSI_DEFS } from '../acsiDefs.js'

const props = defineProps({
  connected: Boolean,
})

const lds = ref([])
const selected = ref(null)
const detailLoading = ref(false)
const dirEntries = ref([])
const detailRaw = ref('')

// Editing state
const editingRef = ref(null)
const editValue = ref('')
const saving = ref(false)
const editError = ref('')
const editInputRef = ref(null)

// load LDs when connected（复用共享缓存，连接后已由 App 拉取）
watch(() => props.connected, async (val) => {
  if (val) {
    await refreshLds()
    lds.value = ldCache.map(name => ({
      name,
      type: 'ld',
      label: name,
      children: null,
      loading: false,
      expanded: false,
    }))
  } else {
    lds.value = []
  }
}, { immediate: true })

async function onToggle(node) {
  // DO nodes: show data directory + values
  if (node.type === 'do') {
    selected.value = node
    detailLoading.value = true
    dirEntries.value = []
    detailRaw.value = ''

    try {
      const dirRes = await executeJson(`data-dir --ref ${node.ref} --json`)
      detailRaw.value = JSON.stringify(dirRes, null, 2)

      if (dirRes.success && dirRes.data.length) {
        const attrs = dirRes.data.map(s => {
          const m = s.match(/^\[(\w+)\]\s+(.+)$/)
          return m ? { fc: m[1], attr: m[2] } : { fc: '?', attr: s }
        })

        const refs = attrs.map(a => `${node.ref}.${a.attr}`).join(' ')

        // Get values and definitions in parallel
        const [valRes, defRes] = await Promise.all([
          executeJson(`get-data-values --refs "${refs}" --json`),
          executeJson(`get-data-def --refs "${refs}" --json`),
        ])

        // Build type map from data-def: "ref  [type]" → { ref, type }
        const defMap = {}
        if (defRes.success && defRes.data) {
          for (const entry of defRes.data) {
            const m = entry.match(/^(\S+)\s+\[(\w+)\]/)
            if (m) defMap[m[1]] = m[2]
          }
        }

        // Build value map
        const valMap = {}
        if (valRes.success && valRes.data) {
          for (const item of valRes.data) {
            valMap[item.ref] = item
          }
        }

        dirEntries.value = attrs.map(a => {
          const fullRef = `${node.ref}.${a.attr}`
          const v = valMap[fullRef]
          const valType = v?.type
          // Use definition type if value type is the generic fallback "visible-string"
          const actualType = defMap[fullRef] && (valType === 'visible-string' || !valType)
            ? defMap[fullRef]
            : valType
          return { ...a, value: v?.value, type: actualType || valType, fullRef }
        })
      }
    } finally {
      detailLoading.value = false
    }
    // DO 节点：显示 data-dir 详情，如果有 SDO 子节点则切换展开/收起
    if (node.children && node.children.length > 0) {
      node.expanded = !node.expanded
    }
    return
  }

  // Other types: show raw info
  if (node.isLeaf) {
    selected.value = node
    detailLoading.value = true
    dirEntries.value = []
    detailRaw.value = ''

    try {
      const cmd = node.type === 'data-set'
        ? `get-dataset-dir --ds ${node.ref} --json`
        : `get-${node.type}-vals --refs "${node.ref}" --json`
      const res = await executeJson(cmd)
      detailRaw.value = JSON.stringify(res, null, 2)
    } finally {
      detailLoading.value = false
    }
    return
  }

  // Non-leaf nodes: toggle expand/collapse
  node.expanded = !node.expanded
  if (!node.expanded) return
  if (node.children !== null) return

  node.loading = true

  try {
    if (node.type === 'ld') {
      const res = await executeJson(`ld-dir --ld ${node.name} --json`)
      if (res.success) {
        node.children = res.data.map(name => ({
          name: `${node.name}/${name}`,
          type: 'ln',
          label: name,
          parentLd: node.name,
          children: null,
          loading: false,
          expanded: false,
        }))
      }
    } else if (node.type === 'ln') {
      // LN 内容由右侧 ACSI 圆点单选驱动，行点击不做事
    }
  } finally {
    node.loading = false
  }
}

/** LN 行上的 ACSI 圆点：单选切换视图，懒加载分类成员直接展示在 LN 下方（无中间分类层）。 */
async function onToggleAcsi({ node, acsi }) {
  // 再次点击已选中的分类：取消视图
  if (node.activeAcsi === acsi) {
    node.activeAcsi = null
    node.children = []
    return
  }
  node.activeAcsi = acsi
  node.loading = true
  try {
    const res = await executeJson(`ln-dir --ln ${node.name} --acsi ${acsi} --json`)
    if (res.success) {
      const childType = acsi === 'data-object' ? 'do' : acsi
      node.children = acsi === 'data-object'
        ? buildDoTree(node.name, res.data)
        : res.data.map(name => ({
            name: `${node.name}/${name}`,
            type: childType,
            label: name,
            ref: `${node.name}.${name}`,
            children: null,
            loading: false,
            expanded: false,
            isLeaf: true,
          }))
    } else {
      node.children = []
    }
  } finally {
    node.loading = false
  }
}

/** 将扁平引用列表解析为 DO/SDO 层级树 */
function buildDoTree(nodeName, refs) {
  const prefix = nodeName + '.'
  // 构建嵌套 map：{ "Mod": { "Beh": {}, "Mag": {} } }
  const root = {}
  for (const ref of refs) {
    const relative = ref.startsWith(prefix) ? ref.substring(prefix.length) : ref
    const parts = relative.split('.')
    let current = root
    for (const part of parts) {
      if (!current[part]) current[part] = {}
      current = current[part]
    }
  }
  // 递归转树节点
  function toNodes(obj) {
    return Object.entries(obj).map(([key, children]) => {
      const childKeys = Object.keys(children)
      const hasChildren = childKeys.length > 0
      return {
        name: `${nodeName}/${key}`,
        type: 'do',
        label: key,
        ref: `${nodeName}.${key}`,
        children: hasChildren ? toNodes(children) : null,
        loading: false,
        expanded: false,
        isLeaf: !hasChildren,
      }
    })
  }
  return toNodes(root)
}

function startEdit(entry) {
  editingRef.value = entry.fullRef
  editValue.value = entry.value && entry.value !== '(unavailable)' ? entry.value : ''
  editError.value = ''
}

async function saveEdit(entry) {
  if (editingRef.value !== entry.fullRef) return
  if (editValue.value === entry.value) {
    cancelEdit()
    return
  }
  saving.value = true
  editError.value = ''
  try {
    const cmd = `set-data-values --pairs "${entry.fullRef}=${editValue.value}" --json`
    const res = await executeJson(cmd)
    if (res.success) {
      entry.value = editValue.value
      editingRef.value = null
    } else {
      editError.value = res.error || '保存失败'
    }
  } catch {
    editError.value = '请求失败'
  } finally {
    saving.value = false
  }
}

function cancelEdit() {
  editingRef.value = null
  editError.value = ''
}
</script>

<style scoped>
.tree-page {
  display: flex;
  height: 100%;
}

.tree-panel {
  width: 320px;
  min-width: 240px;
  border-right: 1px solid var(--border);
  overflow-y: auto;
  padding: 20px 0;
  flex-shrink: 0;
}

.panel-title {
  font-size: 14px;
  font-weight: 600;
  padding: 0 20px 12px;
  color: var(--text-primary);
}

.panel-hint {
  color: var(--text-muted);
  font-size: 13px;
  padding: 0 20px;
}

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

.data-table tbody tr:hover {
  background: var(--bg-hover);
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
  cursor: pointer;
}

.val-text {
  min-height: 20px;
  display: inline-block;
}

.edit-input {
  background: var(--bg-primary);
  border: 1px solid var(--accent);
  border-radius: 4px;
  color: var(--text-primary);
  font-family: inherit;
  font-size: 13px;
  padding: 2px 6px;
  width: 100%;
  outline: none;
}

.edit-input:focus {
  box-shadow: 0 0 0 2px var(--accent-muted);
}

.saving {
  font-size: 11px;
  color: var(--accent);
  margin-left: 6px;
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
