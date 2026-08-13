<template>
  <div class="tree-page">
    <ServerDirTree
      :connected="connected"
      :lds="lds"
      :search-query="searchQuery"
      :search-results="searchResults"
      :search-loading="searchLoading"
      @toggle="onToggle"
      @toggle-acsi="onToggleAcsi"
      @update:search-query="searchQuery = $event"
      @select-result="onSelectResult"
    />
    <ServerDirDetail
      :selected="selected"
      :detail-loading="detailLoading"
      :dir-entries="dirEntries"
      :detail-raw="detailRaw"
      :edit-error="editError"
      :editing-ref="editingRef"
      @edit-entry="startEdit"
    />
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
import ServerDirTree from '../components/ServerDirTree.vue'
import ServerDirDetail from '../components/ServerDirDetail.vue'
import ComplexValueEditor from '../components/ComplexValueEditor.vue'
import { ldCache, refreshLds, ensureAllDefData, ensureAllLnRefs, allLnRefs } from '../ldCache.js'
import { ACSI_DEFS } from '../acsiDefs.js'
import { buildDoTree } from '../utils/treeBuilder.js'
import { findSafeDelimiter } from '../utils/listJoin.js'

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

// 搜索状态
const searchQuery = ref('')
const searchResults = ref([])
const searchLoading = ref(false)
let searchTimer = 0

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

        const defList = Array.isArray(defRes) ? defRes : (defRes?.data || [])
        const defMap = {}
        const typeKeys = ['boolean','int8','int16','int32','int64','int8u','int16u','int32u','int64u',
          'float32','float64','octet-string','visible-string','unicode-string',
          'timestamp','quality','check']
        for (let i = 0; i < defList.length && i < attrs.length; i++) {
          const entry = defList[i]
          if (entry) {
            if (entry.cdcType) {
              defMap[attrs[i].fullRef] = entry.cdcType
            } else if (entry.definition && typeof entry.definition === 'object') {
              for (const k of typeKeys) {
                if (Object.prototype.hasOwnProperty.call(entry.definition, k)) {
                  defMap[attrs[i].fullRef] = k
                  break
                }
              }
            }
          }
        }
        // fallback: 从 all-def 缓存查 DA 类型
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

        const valList = Array.isArray(valRes) ? valRes : (valRes?.value || [])
        const valMap = {}
        for (let i = 0; i < valList.length && i < attrs.length; i++) {
          const item = valList[i]
          if (item) {
            let valueStr = ''
            let typeKey = ''
            if (typeof item === 'string') {
              valueStr = item
            } else if (typeof item === 'object') {
              // 新格式：结构化 JSON 对象（quality、utc-time 等）
              valueStr = JSON.stringify(item)
            } else if (item.choice != null) {
              typeKey = choiceTypeToType(item.choice)
              valueStr = Object.entries(item)
                .filter(([k]) => k !== 'choice')
                .map(([, v]) => v)
                .join('')
            } else if (item.valueString != null) {
              valueStr = item.valueString
              typeKey = choiceTypeToType(item.choiceType)
            } else {
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
        ? `get-dataset-dir --ds ${node.ref} --auto-pull true --json`
        : `get-${node.type}-vals --refs "${node.ref}" --json`
      const res = await executeJson(cmd)
      detailRaw.value = JSON.stringify(res, null, 2)

      // data-set 节点：将 memberData 映射到表格，并解析成员数据类型（data-def 为主，all-def 缓存兜底）
      if (node.type === 'data-set' && res && Array.isArray(res.memberData)) {
        const attrs = res.memberData.map(m => ({
          fc: m.fc,
          attr: m.reference,
          fullRef: m.reference,
        }))
        const defMap = await resolveDataSetMemberTypes(attrs)
        dirEntries.value = attrs.map(a => ({
          ...a,
          value: null,
          type: defMap[a.fullRef] || '',
        }))
      }
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
          preCheckLnAcsis(lnNode)
          return lnNode
        })
      }
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
      if (!node.contentAcsis.includes(acsi)) {
        node.contentAcsis.push(acsi)
      }
      const idx = node.emptyAcsis.indexOf(acsi)
      if (idx !== -1) node.emptyAcsis.splice(idx, 1)
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
      if (!node.emptyAcsis.includes(acsi)) {
        node.emptyAcsis.push(acsi)
      }
      node.children = []
    }
  } finally {
    node.loading = false
  }
}

// ── 目录树搜索：输入防抖后全量匹配 LD/LN，扁平展示命中结果 ──
watch(searchQuery, (q) => {
  clearTimeout(searchTimer)
  const query = q.trim()
  if (!query) {
    searchResults.value = []
    searchLoading.value = false
    return
  }
  searchLoading.value = true
  searchTimer = setTimeout(async () => {
    try {
      await ensureAllLnRefs()
      const lq = query.toLowerCase()
      const ldRes = ldCache
        .filter((ld) => ld.toLowerCase().includes(lq))
        .map((ld) => ({ type: 'ld', name: ld, label: ld }))
      const lnRes = allLnRefs
        .filter((r) => r.toLowerCase().includes(lq))
        .map((r) => ({ type: 'ln', name: r, label: r }))
      searchResults.value = [...ldRes, ...lnRes]
    } catch {
      searchResults.value = []
    } finally {
      searchLoading.value = false
    }
  }, 300)
})

/** 点击搜索结果：清空搜索，定位并展开树中对应位置（LN 自动打开 data-object）。 */
async function onSelectResult(res) {
  searchQuery.value = ''
  if (res.type === 'ld') {
    const ldNode = lds.value.find((l) => l.name === res.name)
    if (ldNode && !ldNode.expanded) await onToggle(ldNode)
    return
  }
  const [ldName] = res.name.split('/')
  const ldNode = lds.value.find((l) => l.name === ldName)
  if (!ldNode) return
  // 未展开或已展开但无数据 → 触发展开加载 LN
  if (!ldNode.expanded || ldNode.children === null) {
    if (ldNode.expanded) ldNode.expanded = false
    await onToggle(ldNode)
  } else {
    ldNode.expanded = true
  }
  // 自动打开该 LN 的 data-object 视图
  const lnNode = ldNode.children?.find((c) => c.name === res.name)
  if (lnNode) {
    lnNode.activeAcsi = null
    await onToggleAcsi({ node: lnNode, acsi: 'data-object' })
  }
}

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
    const delim = findSafeDelimiter([entry.fullRef, val])
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

/** 数据集成员类型解析：优先 get-data-def，缺省时用 all-def 缓存兜底（FCD 取 cdcType，FDCA 取 DA 结构类型）。 */
async function resolveDataSetMemberTypes(attrs) {
  const defMap = {}
  const typeKeys = ['boolean','int8','int16','int32','int64','int8u','int16u','int32u','int64u',
    'float32','float64','octet-string','visible-string','unicode-string',
    'timestamp','quality','check']
  const refs = attrs.map(a => a.fullRef).join(' ')
  if (refs) {
    try {
      const defRes = await executeJson(`get-data-def --refs "${refs}" --json`)
      const defList = Array.isArray(defRes) ? defRes : (defRes?.data || [])
      for (let i = 0; i < defList.length && i < attrs.length; i++) {
        const entry = defList[i]
        if (!entry) continue
        if (entry.cdcType) {
          defMap[attrs[i].fullRef] = entry.cdcType
        } else if (entry.definition && typeof entry.definition === 'object') {
          for (const k of typeKeys) {
            if (Object.prototype.hasOwnProperty.call(entry.definition, k)) {
              defMap[attrs[i].fullRef] = k
              break
            }
          }
        }
      }
    } catch { /* data-def 失败时走缓存兜底 */ }
  }
  // 兜底：按 LN 分组查 all-def 缓存
  const missing = attrs.filter(a => !defMap[a.fullRef])
  const byLn = {}
  for (const a of missing) {
    const slashIdx = a.fullRef.indexOf('/')
    const dotIdx = a.fullRef.indexOf('.', slashIdx)
    if (slashIdx < 0 || dotIdx < 0) continue
    const lnRef = a.fullRef.slice(0, dotIdx)
    const path = a.fullRef.slice(dotIdx + 1).split('.')
    ;(byLn[lnRef] = byLn[lnRef] || []).push({ a, path })
  }
  for (const [lnRef, items] of Object.entries(byLn)) {
    const allDefData = await ensureAllDefData(lnRef)
    for (const { a, path } of items) {
      const doInfo = allDefData[path[0]]
      if (!doInfo) continue
      if (path.length === 1) {
        // FCD：DO 级别 → cdcType
        if (doInfo.cdcType) defMap[a.fullRef] = doInfo.cdcType
      } else {
        // FDCA：DO.DA → structure 里 DA 的类型
        const member = doInfo.structure?.find(s => s.name === path[1])
        if (member && member.type) defMap[a.fullRef] = member.type
      }
    }
  }
  return defMap
}
</script>

<style scoped>
.tree-page {
  display: flex;
  height: 100%;
}
</style>