/**
 * 命令表单逻辑 composable
 * 负责：表单初始化、动态引用列表管理、级联选择（LD/LN/DO/SDO/DA）、
 *       DO/SDO/DA 选项懒加载、negotiate 默认值回填
 */
import { reactive, watch } from 'vue'
import { ldCache, ldLns, allLnRefs, ensureLdLns, ensureAllLnRefs, ensureLnDirRefs, ensureAllDefRefs, ensureAllCbRefs, ensureAllDefData } from '../ldCache.js'
import { executeJson } from '../api/cms.js'

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
 * @param {object}   form       - 响应式表单对象（reactive）
 * @param {object}   opts
 * @param {Function} opts.getDef   - () => 当前命令定义（从 CMD_DEFS 取）
 * @param {Function} opts.getCmd   - () => props.cmd
 * @param {Function} opts.getLnRef - () => 当前 lnRef 值
 * @param {string[]} opts.lnRequiredCmds - 需要自动选中 LN 的命令列表
 */
export function useCommandForm(form, opts = {}) {
  const { getDef, getCmd, getLnRef, lnRequiredCmds = [] } = opts

  // ── 表单初始化 ──

  /** 根据当前命令定义重置表单 */
  function initForm() {
    const def = getDef()
    for (const k of Object.keys(form)) {
      delete form[k]
    }
    for (const p of def.params) {
      if (p.type === 'select') {
        const first = p.options[0]
        form[p.key] = first && typeof first === 'object' ? first.value : (first || '')
      } else if (p.type === 'ap-select') {
        form[p.key] = ''
      } else if (p.type === 'ld-select') {
        form[p.key] = p.required && ldCache.length ? ldCache[0] : ''
      } else if (p.type === 'auto-pull-switch') {
        form[p.key] = true
      } else if (p.type === 'ln-cascade') {
        form[p.key] = { ld: '', ln: '' }
      } else if (p.type === 'dataset-select') {
        form[p.key] = ''
      } else if (p.type === 'sgcb-select') {
        form[p.key] = ''
      } else if (p.type === 'ds-ref-input') {
        form[p.key] = { ld: '', ln: '', name: '' }
      } else if (p.type === 'cb-ref-input') {
        form[p.key] = { ld: '', ln: '', cb: '' }
      } else if (p.type === 'refs-list') {
        form[p.key] = p.cascade ? [{ ld: '', ln: '', do: '', sdo: '', da: '', fc: '', value: '', type: 'visible-string' }] : ['']
      } else if (p.type === 'values-list') {
        form[p.key] = []
      } else {
        form[p.key] = p.default ?? (p.type === 'switch' ? false : '')
      }
    }
  }

  // ── 动态引用列表 ──

  function addRefs() {
    const p = getDef().params.find((x) => x.type === 'refs-list')
    if (!p) return
    form[p.key].push(p.cascade ? { ld: '', ln: '', do: '', sdo: '', da: '', fc: '', value: '', type: 'visible-string' } : '')
  }

  function removeRefs(i) {
    const key = getDef().params.find((p) => p.type === 'refs-list')?.key
    if (key) form[key].splice(i, 1)
  }

  // ── 级联 DO 选项 ──

  const rowDoRefs = reactive({})
  function rowDoKey(row) {
    const fc = String(form.fc || '').split(':')[0] || 'XX'
    return `${row.ld}/${row.ln}|${fc}`
  }
  function rowDoOptions(row) {
    if (!row.ld || !row.ln) return []
    return rowDoRefs[rowDoKey(row)] || []
  }
  async function loadRowDo(row) {
    if (!row.ld || !row.ln) return
    const key = rowDoKey(row)
    if (rowDoRefs[key]) return
    const fc = String(form.fc || '').split(':')[0] || 'XX'
    try {
      const res = await executeJson(`all-def --ln ${row.ld}/${row.ln} --fc ${fc} --auto-pull true --json`)
      rowDoRefs[key] = res && Array.isArray(res.data) ? res.data.map((d) => d.reference).filter(Boolean) : []
    } catch {
      rowDoRefs[key] = []
    }
  }

  function onRowLd(row) {
    row.ln = ''
    row.do = ''
    row.sdo = ''
    row.da = ''
    if (row.ld) ensureLdLns(row.ld)
  }
  function onRowLn(row) {
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
    // DA 已选定时，从选项自动补上 FC
    const opts = rowDaOptions(row)
    const opt = opts.find(o => o.value === row.da)
    if (opt && opt.fc) {
      row.fc = opt.fc
    } else {
      row.fc = ''
    }
    loadRowType(row)
  }

  // ── 级联 SDO 选项 ──

  const rowSdoRefs = reactive({})
  function rowSdoKey(row) {
    if (!row.ld || !row.ln || !row.do) return ''
    return `${row.ld}/${row.ln}.${row.do}`
  }
  function rowSdoOptions(row) {
    const key = rowSdoKey(row)
    if (!key) return []
    return rowSdoRefs[key] || []
  }
  async function loadRowSdo(row) {
    const key = rowSdoKey(row)
    if (!key || rowSdoRefs[key]) return
    try {
      const res = await executeJson(`data-dir --ref ${key} --json`)
      // 新格式: {dataAttribute: [...]}，旧格式: [...]
      const dirList = Array.isArray(res) ? res : (res?.dataAttribute || [])
      // 只取不带 fc 的条目（SDO），带 fc 的是 DA
      const sdos = []
      const das = []
      for (const d of dirList) {
        if (d.reference && d.fc) {
          // DA: 有 fc 属性
          const attr = d.reference.includes('.') ? d.reference.split('.').pop() : d.reference
          das.push({ value: attr, label: attr, fc: d.fc })
        } else if (d.reference) {
          // SDO: 无 fc 属性
          const attr = d.reference.includes('.') ? d.reference.split('.').pop() : d.reference
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

  // ── 级联 DA 选项 ──

  const rowDaRefs = reactive({})
  function rowDaKey(row) {
    if (!row.ld || !row.ln || !row.do) return ''
    const base = `${row.ld}/${row.ln}.${row.do}`
    return row.sdo ? `${base}.${row.sdo}` : base
  }
  function rowDaOptions(row) {
    const key = rowDaKey(row)
    if (!key) return ['']
    return ['', ...(rowDaRefs[key] || [])]
  }
  async function loadRowDa(row) {
    const key = rowDaKey(row)
    if (!key || rowDaRefs[key]) return
    try {
      const res = await executeJson(`data-dir --ref ${key} --json`)
      // 新格式: {dataAttribute: [...]}，旧格式: [...]
      const dirList = Array.isArray(res) ? res : (res?.dataAttribute || [])
      rowDaRefs[key] = dirList
        .filter(d => d.reference && d.fc)
        .map(d => {
          const attr = d.reference.includes('.') ? d.reference.split('.').pop() : d.reference
          return { value: attr, label: attr, fc: d.fc }
        })
    } catch {
      rowDaRefs[key] = []
    }
  }

  // ── 自动解析字段类型（供 set-data-values 显示） ──

  let _typeReqId = 0

  async function loadRowType(row) {
    // 捕获当前请求 ID，用于忽略过期响应（竞态条件防护）
    const reqId = ++_typeReqId
    if (!row.ld || !row.ln || !row.do) {
      row._resolvedType = ''
      return
    }
    let ref = `${row.ld}/${row.ln}.${row.do}`
    if (row.sdo) ref += `.${row.sdo}`
    if (row.da) ref += `.${row.da}`

    // 1. 从 get-data-def 拿定义类型（SCL bType，始终准确）
    // DO 级别: {data: [{cdcType: "INS", definition: {structure: [...]}}]}
    // DA 级别: {data: [{definition: {int32: null}}]} — cdcType 为空，类型在 definition 的 key 名
    let defType = ''
    try {
      const defRes = await executeJson(`get-data-def --refs "${ref}" --json`)
      if (reqId !== _typeReqId) return
      const defList = Array.isArray(defRes) ? defRes : (defRes?.data || [])
      if (defList.length > 0 && defList[0]) {
        const entry = defList[0]
        // DA 级别：跳过 cdcType，只从 definition 的 key 名获取
        if (row.da || row.sdo) {
          if (entry.definition && typeof entry.definition === 'object') {
            const keys = Object.keys(entry.definition)
            if (keys.length === 1 && keys[0] !== 'structure') {
              defType = keys[0]
            }
          }
        } else {
          // DO 级别：使用 CDC 类型
          if (entry.cdcType) {
            defType = entry.cdcType
          }
        }
      }
    } catch { /* ignore */ }

    if (reqId !== _typeReqId) return

    // 2. 如果 get-data-def 没拿到，从 all-def 缓存拿（处理 stVal、q、t 等预定义 DA）
    if (!defType && row.do) {
      try {
        const lnRef = `${row.ld}/${row.ln}`
        const allDef = await ensureAllDefData(lnRef)
        if (reqId !== _typeReqId) return
        const doEntry = allDef[row.do]
        if (doEntry && doEntry.structure) {
          const daName = row.da || row.sdo || ''
          if (daName) {
            const da = doEntry.structure.find(s => s.name === daName)
            if (da && da.type) defType = da.type
          } else {
            // DO 级别：使用 cdcType
            defType = doEntry.cdcType
          }
        }
      } catch { /* ignore */ }
    }

    if (reqId !== _typeReqId) return

    // 3. 从 get-data-values 拿实际值类型（仅辅助，优先级低于 defType）
    let valType = ''
    let valFromChoice = false
    try {
      const valRes = await executeJson(`get-data-values --refs "${ref}" --json`)
      if (reqId !== _typeReqId) return
      const valList = Array.isArray(valRes) ? valRes : (valRes?.value || [])
      if (valList.length > 0) {
        const item = valList[0]
        if (item) {
          if (typeof item === 'string') {
            // 新格式：简单字符串值，无类型信息
          } else if (typeof item === 'object') {
            // 新格式：结构化 JSON 对象（quality 等），无类型 key 名
          } else if (item.choice != null) {
            valType = choiceTypeToType(item.choice)
            valFromChoice = true
          } else if (item.valueString != null) {
            valType = choiceTypeToType(item.choiceType)
          } else {
            const typeKeys = ['boolean','int8','int16','int32','int64','int8u','int16u','int32u','int64u',
              'float32','float64','octet-string','visible-string','unicode-string',
              'timestamp','quality','check']
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

    // 优先级：defType（get-data-def / all-def 缓存）> choice 字段 > 值 key 名
    row._resolvedType = defType || (valFromChoice ? valType : '') || (valType !== 'visible-string' ? valType : '') || ''
  }

  // ── fc 变化时重新加载 DO 选项 ──

  function setupFcWatch() {
    return watch(() => form.fc, () => {
      const p = getDef().params.find((x) => x.type === 'refs-list' && x.cascade)
      const rows = p ? form[p.key] : null
      if (!Array.isArray(rows)) return
      for (const row of rows) {
        if (row && row.ld && row.ln) loadRowDo(row)
      }
    })
  }

  // ── negotiate 默认值 ──

  async function loadNegotiateDefaults() {
    try {
      const neg = await executeJson('neg-cfg --json')
      if (neg && typeof neg.apduSize === 'number') {
        form.apdu = neg.apduSize
        form.asdu = neg.asduSize
        form.version = neg.protocolVersion
      }
    } catch {
      // 配置读取失败时保留默认值
    }
  }

  // ── LN 必填命令的自动初始化 ──

  function setupLnRequiredWatch() {
    return watch(() => getCmd(), async (cmd) => {
      if (!lnRequiredCmds.includes(cmd)) return
      await ensureAllLnRefs()
      const p = getDef().params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
      const o = p ? form[p.key] : null
      if (o && !o.ld && ldCache.length) {
        o.ld = ldCache[0]
        if (cmd !== 'ln-dir') {
          const lns = ldLns[o.ld] || []
          if (lns.length) o.ln = lns[0]
        }
      }
    }, { immediate: true })
  }

  // ── ld-dir 的 LD 级联 ──

  function setupLdDirWatch() {
    return watch([() => form.ld, () => ldCache.length], async ([ld]) => {
      if (getCmd() !== 'ld-dir') return
      if (ld) {
        await ensureLdLns(ld)
        const o = form.after
        if (o) {
          o.ld = ld
          o.ln = ''
        }
      } else {
        await ensureAllLnRefs()
        const o = form.after
        if (o && !o.ln) o.ld = ''
      }
    }, { immediate: true })
  }

  // ── 缓存就绪后自动选中第一个 LD ──

  function setupLazyLnWatch() {
    return watch([() => allLnRefs.length, () => ldCache.length], async () => {
      if (!lnRequiredCmds.includes(getCmd())) return
      const p = getDef().params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
      const o = p ? form[p.key] : null
      if (!o || o.ld) return
      await ensureAllLnRefs()
      if (ldCache.length && !o.ld) {
        o.ld = ldCache[0]
        if (getCmd() !== 'ln-dir') {
          const lns = ldLns[o.ld] || []
          if (lns.length) o.ln = lns[0]
        }
      }
    })
  }

  // ── ln-dir / all-cb 的引用懒加载 ──

  function setupRefsWatch() {
    return watch([getLnRef, () => form.acsi], async () => {
      const cmd = getCmd()
      const lnRef = getLnRef()
      if (!lnRef) return
      if (cmd === 'ln-dir') {
        const acsi = String(form.acsi || '').split(':')[0] || '1'
        await ensureLnDirRefs(lnRef, acsi)
      } else if (cmd === 'all-cb') {
        const acsi = String(form.acsi || '').split(':')[0] || 'brcb'
        await ensureAllCbRefs(lnRef, acsi)
      }
    }, { immediate: true })
  }

  // ── all-data / all-def 的引用懒加载 ──

  function setupAllDataRefsWatch() {
    return watch([getLnRef, () => form.fc], async () => {
      const cmd = getCmd()
      if (!['all-data', 'all-def'].includes(cmd)) return
      const lnRef = getLnRef()
      if (!lnRef) return
      const fc = String(form.fc || '').split(':')[0] || 'XX'
      await ensureAllDefRefs(lnRef, fc)
    }, { immediate: true })
  }

  return {
    initForm,
    addRefs,
    removeRefs,
    rowDoRefs,
    rowDoOptions,
    loadRowDo,
    onRowLd,
    onRowLn,
    onRowDo,
    onRowSdo,
    onRowDa,
    rowSdoRefs,
    rowSdoOptions,
    loadRowSdo,
    rowDaRefs,
    rowDaOptions,
    loadRowDa,
    loadNegotiateDefaults,
    // 设置 watcher 的方法（由组件在 setup 中调用）
    setupFcWatch,
    setupLnRequiredWatch,
    setupLdDirWatch,
    setupLazyLnWatch,
    setupRefsWatch,
    setupAllDataRefsWatch,
  }
}