<template>
  <div class="tree-page">
    <div class="tree-panel">
      <h2 class="panel-title">目录树</h2>
      <p v-if="!connected" class="panel-hint">请先连接服务器</p>
      <div v-else class="tree-scroll">
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
                <td class="cell-value">
                  <button
                    type="button"
                    class="val-btn"
                    :class="{ 'val-btn--has': entry.value }"
                    @click="startEdit(entry)"
                  >
                    {{ entry.value ?? '—' }}
                  </button>
                  <span v-if="editError && editingRef === entry.fullRef" class="edit-err">{{ editError }}</span>
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
  <ComplexValueEditor
    v-model="editValue"
    :visible="editorVisible"
    :type="editorType"
    @update:visible="editorVisible = $event"
    @confirm="onEditorConfirm"
  />
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { executeJson } from '../api/cms.js'
import TreeNode from '../components/TreeNode.vue'
import ComplexValueEditor from '../components/ComplexValueEditor.vue'
import { ldCache, refreshLds, ensureAllDefData } from '../ldCache.js'
import { ACSI_DEFS } from '../acsiDefs.js'
import { buildDoTree } from '../utils/treeBuilder.js'

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
const editError = ref('')
const editorVisible = ref(false)
const editorType = ref('')
const editorEntry = ref(null)

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

      // Handle both old format (array) and new format ({dataAttribute: [...]})
      const dirData = Array.isArray(dirRes) ? dirRes : (dirRes?.dataAttribute || [])
      if (dirData.length) {
        const attrs = dirData
          .filter(d => d.reference && d.fc)
          .map(d => {
            // d.reference 可能是短名（ctlModel）或长名（LD0/GGIO5.Mod.ctlModel）
            // 统一转为完整引用
            const fullRef = d.reference.includes('/')
              ? d.reference
              : node.ref + '.' + d.reference
            const attr = fullRef.startsWith(node.ref + '.')
              ? fullRef.slice(node.ref.length + 1)
              : fullRef
            return { fc: d.fc, attr, fullRef }
          })

        const refs = attrs.map(a => a.fullRef).join(' ')

        // Get values and definitions in parallel
        const [valRes, defRes] = await Promise.all([
          executeJson(`get-data-values --refs "${refs}" --json`),
          executeJson(`get-data-def --refs "${refs}" --json`),
        ])

        // Handle both old format (array) and new format ({data: [...]})
        const defList = Array.isArray(defRes) ? defRes : (defRes?.data || [])
        const defMap = {}
        const typeKeys = ['boolean','int8','int16','int32','int64','int8u','int16u','int32u','int64u',
          'float32','float64','octet-string','visible-string','unicode-string',
          'timestamp','quality','check']
        for (let i = 0; i < defList.length && i < attrs.length; i++) {
          const entry = defList[i]
          if (entry) {
            if (entry.cdcType) {
              // DO 级别: cdcType 表示类型（如 "INC"）
              defMap[attrs[i].fullRef] = entry.cdcType
            } else if (entry.definition && typeof entry.definition === 'object') {
              // DA 级别: 从 definition 的 key 名推断（如 {"int32": null}）
              for (const k of typeKeys) {
                if (Object.prototype.hasOwnProperty.call(entry.definition, k)) {
                  defMap[attrs[i].fullRef] = k
                  break
                }
              }
            }
          }
        }
        // fallback: 从 all-def 缓存查 DA 类型（标准预定义属性，如 stVal→int32）
        const doName = node.ref.split('.').pop()
        const lnRef = node.ref.slice(0, node.ref.lastIndexOf('.'))
        if (lnRef) {
          const allDefData = await ensureAllDefData(lnRef)
          const doInfo = allDefData[doName]
          if (doInfo && doInfo.structure.length) {
            for (const a of attrs) {
              if (!defMap[a.fullRef]) {
                const member = doInfo.structure.find(s => s.name === a.attr)
                if (member && member.type) {
                  defMap[a.fullRef] = member.type
                }
              }
            }
          }
        }

        // Handle both old format (array) and new format ({value: [...]})
        const valList = Array.isArray(valRes) ? valRes : (valRes?.value || [])
        const valMap = {}
        for (let i = 0; i < valList.length && i < attrs.length; i++) {
          const item = valList[i]
          if (item) {
            // 新格式: {choice: N, "visible-string": "..."} 或 {"visible-string": "..."}
            // 旧格式: {valueString, choiceType}
            let valueStr = ''
            let typeKey = ''
            if (item.choice != null) {
              // 新格式有 choice: 类型从 choice 取，值从余下字段取
              typeKey = choiceTypeToType(item.choice)
              valueStr = Object.entries(item)
                .filter(([k]) => k !== 'choice')
                .map(([, v]) => v)
                .join('')
            } else if (item.valueString != null) {
              // 旧格式
              valueStr = item.valueString
              typeKey = choiceTypeToType(item.choiceType)
            } else {
              // 新格式无 choice: 从 key 名推断类型
              for (const k of typeKeys) {
                if (Object.prototype.hasOwnProperty.call(item, k)) {
                  typeKey = k
                  valueStr = String(item[k])
                  break
                }
              }
            }
            valMap[attrs[i].fullRef] = { value: valueStr, type: typeKey }
          }
        }

        dirEntries.value = attrs.map(a => {
          const v = valMap[a.fullRef]
          const valType = v?.type
          const defType = defMap[a.fullRef]
          const actualType = defType && (valType === 'visible-string' || !valType) ? defType : (valType || '')
          return { ...a, value: v?.value, type: actualType, fullRef: a.fullRef }
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
      const res = await executeJson(`ld-dir --ld ${node.name} --auto-pull true --json`)
      if (res && Array.isArray(res.lnReference)) {
        node.children = res.lnReference.map(name => {
          const lnName = name.includes('/') ? name.split('/')[1] : name
          const lnNode = reactive({
            name: `${node.name}/${lnName}`,
            type: 'ln',
            label: lnName,
            parentLd: node.name,
            children: null,
            loading: false,
            expanded: false,
            emptyAcsis: [],
            contentAcsis: [],
          })
          // 并发预检所有 ACSI 分类（不阻塞，标记圆点有无内容）
          preCheckLnAcsis(lnNode)
          return lnNode
        })
      }
    } else if (node.type === 'ln') {
      // 首次点击 LN 滚动到该行（预检已在 LD 展开时触发）
    }
  } finally {
    node.loading = false
  }
}

/** 并发预检 LN 的所有 9 个 ACSI 分类，标记有/无内容的圆点。不阻塞 UI。 */
function preCheckLnAcsis(node) {
  Promise.allSettled(
    ACSI_DEFS.map(d =>
      executeJson(`ln-dir --ln ${node.name} --acsi ${d.key} --auto-pull true --json`)
    )
  ).then(results => {
    for (let i = 0; i < ACSI_DEFS.length; i++) {
      const d = ACSI_DEFS[i]
      const res = results[i]
      if (res.status === 'fulfilled' && res.value && Array.isArray(res.value.reference) && res.value.reference.length > 0) {
        if (!node.contentAcsis.includes(d.key)) node.contentAcsis.push(d.key)
      } else {
        if (!node.emptyAcsis.includes(d.key)) node.emptyAcsis.push(d.key)
      }
    }
  })
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
    const res = await executeJson(`ln-dir --ln ${node.name} --acsi ${acsi} --auto-pull true --json`)
    if (res && Array.isArray(res.reference) && res.reference.length > 0) {
      // 有内容：记录到 contentAcsis
      if (!node.contentAcsis.includes(acsi)) {
        node.contentAcsis.push(acsi)
      }
      // 从空列表中移除（如果之前被标记过）
      const idx = node.emptyAcsis.indexOf(acsi)
      if (idx !== -1) node.emptyAcsis.splice(idx, 1)
      // 找到该 ACSI 分类的颜色，传给子节点
      const acsiDef = ACSI_DEFS.find(d => d.key === acsi)
      const acsiColor = acsiDef ? acsiDef.color : '#888'
      const childType = acsi === 'data-object' ? 'do' : acsi
      node.children = acsi === 'data-object'
        ? addDotColor(buildDoTree(node.name, res.reference), acsiColor)
        : res.reference.map(name => ({
            name: `${node.name}/${name}`,
            type: childType,
            label: name,
            ref: `${node.name}.${name}`,
            children: null,
            loading: false,
            expanded: false,
            isLeaf: true,
            dotColor: acsiColor,
          }))
    } else {
      // 没有内容：记录为空
      if (!node.emptyAcsis.includes(acsi)) {
        node.emptyAcsis.push(acsi)
      }
      node.children = []
    }
  } finally {
    node.loading = false
  }
}

// buildDoTree 实现在 utils/treeBuilder.js 中

/** 递归给树节点加上 dotColor，并默认展开 SDO */
function addDotColor(nodes, color) {
  return nodes.map(n => ({
    ...n,
    dotColor: color,
    expanded: !!n.children,
    children: n.children ? addDotColor(n.children, color) : null,
  }))
}

function startEdit(entry) {
  editingRef.value = entry.fullRef
  editValue.value = entry.value && entry.value !== '(unavailable)' ? entry.value : ''
  editError.value = ''
  editorType.value = entry.type || ''
  editorEntry.value = entry
  editorVisible.value = true
}

async function onEditorConfirm(val) {
  if (!editorEntry.value) return
  const entry = editorEntry.value
  if (val === entry.value) {
    editorVisible.value = false
    return
  }
  editError.value = ''
  try {
    const allVals = [entry.fullRef, val]
    const safeDelim = (candidates) => candidates.find(d => allVals.every(v => !v.includes(d)))
    let delim = safeDelim([' ', ',', ';', '|', '::'])
    if (!delim) {
      // 如果在空格分隔的多个引用里，值中包含特殊字符，需要使用更长的分隔符
      // 单值场景下，使用空格分隔即可
      delim = ' '
    }
    let cmd = `set-data-values --refs "${entry.fullRef}" --values "${val}"`
    if (delim !== ' ') {
      cmd = `set-data-values --delimiter "${delim}" --refs "${entry.fullRef}" --values "${val}"`
    }
    cmd += ' --json'
    const res = await executeJson(cmd)
    if (res && res.success) {
      entry.value = val
    } else {
      editError.value = res?.error || '保存失败'
    }
  } catch {
    editError.value = '请求失败'
  }
}

function cancelEdit() {
  editingRef.value = null
  editError.value = ''
}

/** Map CMS choiceType to readable type name. */
function choiceTypeToType(choiceType) {
  const map = {
    1: 'boolean',
    2: 'int8',
    3: 'int16',
    4: 'int32',
    5: 'int64',
    6: 'int8u',
    7: 'int16u',
    8: 'int32u',
    9: 'int64u',
    10: 'float32',
    11: 'float64',
    12: 'octet-string',
    13: 'visible-string',
    14: 'unicode-string',
    15: 'timestamp',
    16: 'quality',
    17: 'check',
  }
  return map[choiceType] || 'visible-string'
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
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.val-btn:hover {
  border-color: var(--border);
  color: var(--text-primary);
  background: var(--bg-hover);
}

.val-btn--has {
  color: var(--text-primary);
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
