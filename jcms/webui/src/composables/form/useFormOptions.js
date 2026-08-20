import { computed } from 'vue'
import {
  ldCache, ldLns, allLnRefs, lnDirRefs, allDefRefs, allCbRefs,
  datasetRefs, cbRefs, sgcbData, datasetMemberRefs, datasetMembers,
  ensureLdLns, ensureDatasetRefs, ensureCbRefs, ensureSgcbData,
} from '../../ldCache.js'

const sgcbCmds = ['sgcb-vals', 'select-active-sg', 'select-edit-sg', 'confirm-edit-sg']

/**
 * 命令表单的动态选项与级联处理器。
 * 通过 ctx 提供给各字段组件。所有依赖 form / def 的选项用 computed、
 * 依赖响应式缓存（ldCache / datasetRefs / cbRefs 等）直接从模块读取，保证追踪。
 *
 * @param {object} form - 响应式表单（reactive）
 * @param {object}   opts
 * @param {Function} opts.getDef - () => 当前命令定义
 * @param {Function} opts.getCmd - () => 当前命令名
 */
export function useFormOptions(form, { getDef, getCmd }) {
  const cmd = computed(() => getCmd())
  const isSgcbCmd = computed(() => sgcbCmds.includes(cmd.value))

  /** 当前 ln-cascade 解析出的 LN 引用（如 "LD0/LLN0" 或 "LD0"）。 */
  const lnRef = computed(() => {
    const p = getDef().params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
    const o = p ? form[p.key] : null
    return o && o.ld ? (o.ln ? `${o.ld}/${o.ln}` : o.ld) : ''
  })

  // ── ln-cascade 派生 ──
  function cascadeLns(o) {
    return o && o.ld ? ldLns[o.ld] || [] : []
  }
  function onCascadeLd(key) {
    const o = form[key]
    if (!o) return
    o.ln = ''
    if (o.ld) ensureLdLns(o.ld)
  }
  function cascadeLdDisabled(p) {
    return cmd.value === 'ld-dir' && p.key === 'after' && !!form.ld
  }

  // ── ds-ref-input 派生 ──
  function dsRefLns(o) {
    return o && o.ld ? ldLns[o.ld] || [] : []
  }
  function dsRefNameList(o) {
    return o && o.ld && o.ln ? datasetRefs[`${o.ld}/${o.ln}`] || [] : []
  }
  function dsRefExists(o) {
    if (!o || !o.ld || !o.ln || !o.name) return false
    return (datasetRefs[`${o.ld}/${o.ln}`] || []).includes(o.name)
  }
  /** ds-ref-input 无效判定：无 after 时名称不能已存在，有 after 时名称必须已存在。 */
  function dsRefInvalid(o) {
    if (!o || !o.ld || !o.ln || !o.name) return false
    const hasAfter = form.after && String(form.after).trim() !== ''
    return hasAfter ? !dsRefExists(o) : dsRefExists(o)
  }
  function onDsRefLd(key) {
    const o = form[key]
    if (!o) return
    o.ln = ''
    o.name = ''
    if (o.ld) ensureLdLns(o.ld)
  }
  function onDsRefLn(key) {
    const o = form[key]
    if (!o) return
    o.name = ''
    if (o.ld && o.ln) ensureDatasetRefs(`${o.ld}/${o.ln}`)
  }

  // ── 下拉选项 ──
  /** dataset-select：依赖 ln-cascade 选中的 LN。 */
  const datasetOptions = computed(() => {
    const p = getDef().params.find((x) => x.type === 'dataset-select')
    if (!p) return []
    const o = form[p.dependsOn || 'ln']
    if (!o || !o.ld || !o.ln) return []
    return datasetRefs[`${o.ld}/${o.ln}`] || []
  })

  /** SGCB 控制块名下拉：只挂 LLN0 下。 */
  const sgcbOptions = computed(() => {
    const o = form.ln
    if (!o || !o.ld || !o.ln) return []
    const ref = `${o.ld}/${o.ln}`
    if (!cbRefs[`${ref}|sgcb`]) ensureCbRefs(ref, 'sgcb')
    return cbRefs[`${ref}|sgcb`] || []
  })

  /** SGCB 定值组号下拉：从 sgcbData 缓存读取 numOfSG 生成 1~numOfSG。 */
  const sgcbNumOptions = computed(() => {
    const refs = form.ref
    if (!Array.isArray(refs) || refs.length === 0) return []
    const row = refs[0]
    if (!row || !row.ld || !row.ln || !row._sgcbName) return []
    const lnRefK = `${row.ld}/${row.ln}`
    if (!sgcbData[lnRefK]) ensureSgcbData(lnRefK)
    const sg = sgcbData[lnRefK]?.[row._sgcbName]
    const n = sg?.numOfSG || 0
    const opts = []
    for (let i = 1; i <= n; i++) opts.push(String(i))
    return opts
  })

  /** cb-select：依赖 ln-cascade 选中的 LN，按 ACSI 类拉取控制块名。 */
  const cbOptions = computed(() => {
    const p = getDef().params.find((x) => x.type === 'cb-select')
    if (!p) return []
    const o = form[p.dependsOn || 'ln']
    if (!o || !o.ld || !o.ln || !p.cb) return []
    const ref = `${o.ld}/${o.ln}`
    if (!cbRefs[`${ref}|${p.cb}`]) ensureCbRefs(ref, p.cb)
    return cbRefs[`${ref}|${p.cb}`] || []
  })

  /** ds-member-after 下拉选项：当前 LN + 数据集下的成员引用。 */
  const dsMemberAfterOptions = computed(() => {
    const p = getDef().params.find((x) => x.type === 'ds-member-after')
    if (!p) return []
    let ld, ln, dsName
    const dsVal = form.ds
    if (dsVal && typeof dsVal === 'object') {
      // ds-ref-input 模式：form.ds = { ld, ln, name }
      if (!dsVal.ld || !dsVal.ln || !dsVal.name) return []
      ld = dsVal.ld; ln = dsVal.ln; dsName = dsVal.name
    } else {
      // ln-cascade + dataset-select 模式：form.ds 为数据集名字符串
      const o = form[p.dependsOn || 'ln']
      if (!o || !o.ld || !o.ln) return []
      ld = o.ld; ln = o.ln; dsName = dsVal
      if (!dsName) return []
    }
    return datasetMemberRefs[`${ld}/${ln}.${dsName}`] || []
  })

  /** ds-member（values-list 用）：数据集成员完整数据。 */
  const dsMemberOptions = computed(() => {
    if (cmd.value !== 'set-dataset-values') return []
    const o = form.ds
    if (!o || !o.ld || !o.ln || !o.name) return []
    return datasetMembers[`${o.ld}/${o.ln}.${o.name}`] || []
  })

  /** ln-ref-select（after 下拉）选项源。 */
  const refOptions = computed(() => {
    if (cmd.value === 'ln-dir') return lnDirRefs
    if (cmd.value === 'all-cb') return allCbRefs
    return allDefRefs
  })

  /** 普通 refs-list 的引用下拉选项。 */
  const refsListOptions = computed(() => allLnRefs)

  return {
    ldCache,
    lnRef,
    isSgcbCmd,
    cascadeLns,
    onCascadeLd,
    cascadeLdDisabled,
    dsRefLns,
    dsRefNameList,
    dsRefExists,
    dsRefInvalid,
    onDsRefLd,
    onDsRefLn,
    datasetOptions,
    sgcbOptions,
    sgcbNumOptions,
    cbOptions,
    dsMemberAfterOptions,
    dsMemberOptions,
    refOptions,
    refsListOptions,
  }
}