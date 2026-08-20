import { reactive, computed } from 'vue'
import { FC_OPTIONS } from '../../cmddefs/common.js'
import { ldLns, cbRefs, ensureLdLns, ensureCbRefs, ensureSgcbData, ensureAllDefData } from '../../ldCache.js'
import { executeJson } from '../../api/cms.js'
import { cascadeRow, plainRow } from './refsUtil.js'

const sgcbCmds = ['sgcb-vals', 'select-active-sg', 'select-edit-sg', 'confirm-edit-sg']
const rcbCmds = ['get-brcb-vals', 'get-urcb-vals', 'get-gocb-vals', 'get-lcb-vals', 'get-msvcb-vals']

/** Map CMS choiceType to readable type name. */
function choiceTypeToType(choiceType) {
  const map = {
    1: 'boolean', 2: 'int8', 3: 'int16', 4: 'int32', 5: 'int64',
    6: 'int8u', 7: 'int16u', 8: 'int32u', 9: 'int64u',
    10: 'float32', 11: 'float64',
    12: 'octet-string', 13: 'visible-string', 14: 'unicode-string',
    15: 'timestamp', 16: 'quality', 17: 'check',
  }
  return map[choiceType] || 'visible-string'
}

/**
 * refs-list 行级逻辑：行增删、级联 DO/SDO/DA 选项懒加载、设值类型解析、SGCB 固定 LLN0 加载。
 *
 * @param {object}   form - 响应式表单（reactive）
 * @param {object}   opts
 * @param {Function} opts.getDef - () => 当前命令定义
 * @param {Function} opts.getCmd - () => 当前命令名
 */
export function useRefsRows(form, { getDef, getCmd }) {
  const cmd = computed(() => getCmd())
  const isSgcbCmd = computed(() => sgcbCmds.includes(cmd.value))
  const isRcbCmd = computed(() => {
    if (!rcbCmds.includes(cmd.value)) return false
    const p = getDef().params.find((x) => x.type === 'refs-list')
    return !!(p && p.cb)
  })
  /** 当前 refs-list 参数的 cb 类型（'brcb' / 'urcb' / ''）。 */
  function cbKind() {
    const p = getDef().params.find((x) => x.type === 'refs-list')
    return (p && p.cb) || ''
  }
  const fcRowOptions = computed(() => FC_OPTIONS)

  // ── 行增删 ──
  function addRefs() {
    const p = getDef().params.find((x) => x.type === 'refs-list')
    if (!p) return
    form[p.key].push(p.cascade ? cascadeRow() : plainRow())
  }
  function removeRefs(i) {
    const p = getDef().params.find((x) => x.type === 'refs-list')
    if (p) form[p.key].splice(i, 1)
  }

  // ── 级联 DO 选项 ──
  const rowDoRefs = reactive({})
  function fcBase() {
    return String(form.fc || '').split(':')[0] || 'XX'
  }
  function rowDoKey(row) {
    return `${row.ld}/${row.ln}|${fcBase()}`
  }
  function rowDoOptions(row) {
    if (!row.ld || !row.ln) return []
    return rowDoRefs[rowDoKey(row)] || []
  }
  async function loadRowDo(row) {
    if (!row.ld || !row.ln) return
    const key = rowDoKey(row)
    if (rowDoRefs[key]) return
    const fc = fcBase()
    try {
      const res = await executeJson(`all-def --ln ${row.ld}/${row.ln} --fc ${fc} --auto-pull true --json`)
      rowDoRefs[key] = res && Array.isArray(res.data) ? res.data.map((d) => d.reference).filter(Boolean) : []
    } catch {
      rowDoRefs[key] = []
    }
  }
  function rowLnOptions(row) {
    if (!row.ld) return []
    const allLns = ldLns[row.ld] || []
    const fc = fcBase()
    return allLns.filter((ln) => {
      const key = `${row.ld}/${ln}|${fc}`
      if (key in rowDoRefs && !rowDoRefs[key].length) return false
      return true
    })
  }
  function onRowLd(row) {
    if (isRcbCmd.value) {
      row.ln = ''
      row.do = ''
      if (row.ld) ensureLdLns(row.ld)
      return
    }
    if (isSgcbCmd.value) {
      // SGCB：固定 LLN0 + 加载 SGCB 名称
      row.ln = 'LLN0'
      row.do = ''
      row.sdo = ''
      row.da = ''
      row._sgcbNames = []
      row._sgcbName = ''
      if (row.ld) {
        ensureLdLns(row.ld)
        loadSgcbNames(row)
      }
      return
    }
    row.ln = ''
    row.do = ''
    row.sdo = ''
    row.da = ''
    if (row.ld) {
      rowLnOptions(row).forEach(() => {}) // 占位：预加载逻辑见下
      ensureLdLns(row.ld).then((lns) => {
        if (lns && lns.length) {
          Promise.all(lns.map((ln) => loadRowDo({ ld: row.ld, ln }).catch(() => {})))
        }
      })
    }
  }
  function onRowLn(row) {
    if (isRcbCmd.value) {
      row.do = ''
      return
    }
    row.do = ''
    row.sdo = ''
    row.da = ''
    loadRowDo(row)
  }
  function onRowDo(row) {
    row.sdo = ''
    row.da = ''
    row.fc = ''
    loadRowSdo(row)
    loadRowType(row)
  }
  function onRowSdo(row) {
    row.da = ''
    row.fc = ''
    loadRowDa(row)
  }
  function onRowDa(row) {
    const opts = rowDaOptions(row)
    const opt = opts.find((o) => o.value === row.da)
    row.fc = opt && opt.fc ? opt.fc : ''
    loadRowType(row)
  }

  // ── 级联 SDO / DA 选项 ──
  const rowSdoRefs = reactive({})
  const rowDaRefs = reactive({})
  function rowSdoKey(row) {
    if (!row.ld || !row.ln || !row.do) return ''
    return `${row.ld}/${row.ln}.${row.do}`
  }
  function rowSdoOptions(row) {
    const key = rowSdoKey(row)
    return key ? rowSdoRefs[key] || [] : []
  }
  function rowDaKey(row) {
    if (!row.ld || !row.ln || !row.do) return ''
    const base = `${row.ld}/${row.ln}.${row.do}`
    return row.sdo ? `${base}.${row.sdo}` : base
  }
  function rowDaOptions(row) {
    const key = rowDaKey(row)
    return key ? ['', ...(rowDaRefs[key] || [])] : ['']
  }
  async function loadRowSdo(row) {
    const key = rowSdoKey(row)
    if (!key || rowSdoRefs[key]) return
    try {
      const res = await executeJson(`data-dir --ref ${key} --json`)
      const dirList = Array.isArray(res) ? res : (res?.dataAttribute || [])
      const sdos = []
      const das = []
      for (const d of dirList) {
        const attr = d.reference ? (d.reference.includes('.') ? d.reference.split('.').pop() : d.reference) : ''
        if (d.reference && d.fc) {
          das.push({ value: attr, label: attr, fc: d.fc })
        } else if (d.reference) {
          sdos.push(attr)
        }
      }
      rowSdoRefs[key] = sdos
      rowDaRefs[key] = das
    } catch {
      rowSdoRefs[key] = []
      rowDaRefs[key] = []
    }
  }
  async function loadRowDa(row) {
    const key = rowDaKey(row)
    if (!key || rowDaRefs[key]) return
    try {
      const res = await executeJson(`data-dir --ref ${key} --json`)
      const dirList = Array.isArray(res) ? res : (res?.dataAttribute || [])
      rowDaRefs[key] = dirList
        .filter((d) => d.reference && d.fc)
        .map((d) => {
          const attr = d.reference.includes('.') ? d.reference.split('.').pop() : d.reference
          return { value: attr, label: attr, fc: d.fc }
        })
    } catch {
      rowDaRefs[key] = []
    }
  }

  // ── 自动解析字段类型（set-data-values / set-edit-sg 显示用） ──
  let _typeReqId = 0
  async function loadRowType(row) {
    const reqId = ++_typeReqId
    if (!row.ld || !row.ln || !row.do) {
      row._resolvedType = ''
      return
    }
    let ref = `${row.ld}/${row.ln}.${row.do}`
    if (row.sdo) ref += `.${row.sdo}`
    if (row.da) ref += `.${row.da}`

    let defType = ''
    try {
      const defRes = await executeJson(`get-data-def --refs "${ref}" --json`)
      if (reqId !== _typeReqId) return
      const defList = Array.isArray(defRes) ? defRes : (defRes?.data || [])
      if (defList.length > 0 && defList[0]) {
        const entry = defList[0]
        if (row.da || row.sdo) {
          if (entry.definition && typeof entry.definition === 'object') {
            const keys = Object.keys(entry.definition)
            if (keys.length === 1 && keys[0] !== 'structure') defType = keys[0]
          }
        } else if (entry.cdcType) {
          defType = entry.cdcType
        }
      }
    } catch { /* ignore */ }

    if (reqId !== _typeReqId) return

    if (!defType && row.do) {
      try {
        const lnRef = `${row.ld}/${row.ln}`
        const allDef = await ensureAllDefData(lnRef)
        if (reqId !== _typeReqId) return
        const doEntry = allDef[row.do]
        if (doEntry && doEntry.structure) {
          const daName = row.da || row.sdo || ''
          if (daName) {
            const da = doEntry.structure.find((s) => s.name === daName)
            if (da && da.type) defType = da.type
          } else {
            defType = doEntry.cdcType
          }
        }
      } catch { /* ignore */ }
    }

    if (reqId !== _typeReqId) return

    let valType = ''
    let valFromChoice = false
    try {
      const valRes = await executeJson(`get-data-values --refs "${ref}" --json`)
      if (reqId !== _typeReqId) return
      const valList = Array.isArray(valRes) ? valRes : (valRes?.value || [])
      if (valList.length > 0) {
        const item = valList[0]
        if (item && typeof item === 'object') {
          if (item.choice != null) {
            valType = choiceTypeToType(item.choice)
            valFromChoice = true
          } else if (item.valueString != null) {
            valType = choiceTypeToType(item.choiceType)
          } else {
            const typeKeys = ['boolean', 'int8', 'int16', 'int32', 'int64', 'int8u', 'int16u', 'int32u', 'int64u',
              'float32', 'float64', 'octet-string', 'visible-string', 'unicode-string', 'timestamp', 'quality', 'check']
            for (const k of typeKeys) {
              if (Object.prototype.hasOwnProperty.call(item, k)) {
                valType = k
                break
              }
            }
          }
        }
      }
    } catch { /* ignore */ }

    if (reqId !== _typeReqId) return
    row._resolvedType = defType || (valFromChoice ? valType : '') || (valType !== 'visible-string' ? valType : '') || ''
  }

  // ── RCB 控制块名选项（brcb/urcb 命令的 LN 变更时懒加载） ──
  function rowCbOptions(row) {
    if (!row.ld || !row.ln) return []
    const kind = cbKind()
    if (!kind) return []
    const ref = `${row.ld}/${row.ln}`
    if (!cbRefs[`${ref}|${kind}`]) ensureCbRefs(ref, kind)
    return cbRefs[`${ref}|${kind}`] || []
  }

  // ── SGCB 名称加载（sgcb 命令的 LD 变更时触发） ──
  function loadSgcbNames(row) {
    if (!row.ld) return
    const ref = `${row.ld}/LLN0`
    ensureCbRefs(ref, 'sgcb').then((names) => {
      row._sgcbNames = names || []
      row._sgcbName = names && names.length > 0 ? names[0] : 'SG'
      if (isSgcbCmd.value) ensureSgcbData(ref)
    })
  }

  return {
    addRefs,
    removeRefs,
    isSgcbCmd,
    isRcbCmd,
    rowCbOptions,
    fcRowOptions,
    rowLnOptions,
    rowDoOptions,
    onRowLd,
    onRowLn,
    onRowDo,
    rowSdoOptions,
    onRowSdo,
    rowDaOptions,
    onRowDa,
  }
}