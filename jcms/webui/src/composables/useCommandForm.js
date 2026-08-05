/**
 * 命令表单逻辑 composable
 * 负责：表单初始化、动态引用列表管理、级联选择（LD/LN/DO/SDO/DA）、
 *       DO/SDO/DA 选项懒加载、negotiate 默认值回填
 */
import { reactive, watch } from 'vue'
import { ldCache, ldLns, allLnRefs, ensureLdLns, ensureAllLnRefs, ensureLnDirRefs, ensureAllDefRefs, ensureAllCbRefs } from '../ldCache.js'
import { executeJson } from '../api/cms.js'

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
      } else if (p.type === 'ln-cascade') {
        form[p.key] = { ld: '', ln: '' }
      } else if (p.type === 'refs-list') {
        form[p.key] = p.cascade ? [{ ld: '', ln: '', do: '', sdo: '', da: '', fc: '', value: '', type: 'visible-string' }] : ['']
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
      const res = await executeJson(`all-def --ln ${row.ld}/${row.ln} --fc ${fc} --json`)
      rowDoRefs[key] = res.success && Array.isArray(res.data) ? res.data.map((d) => d.ref).filter(Boolean) : []
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
      if (res.success && Array.isArray(res.data)) {
        // 只取不带 [FC] 前缀的条目（SDO），带 [FC] 的是 DA
        const sdos = []
        const das = []
        for (const s of res.data) {
          const m = String(s).match(/^\[(\w+)\]\s+(.+)$/)
          if (m) {
            das.push(m[2].trim())
          } else {
            sdos.push(String(s).trim())
          }
        }
        rowSdoRefs[key] = sdos
        rowDaRefs[key] = das
      } else {
        rowSdoRefs[key] = []
        rowDaRefs[key] = []
      }
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
      if (res.success && Array.isArray(res.data)) {
        rowDaRefs[key] = res.data
          .map((s) => {
            const m = String(s).match(/^\[(\w+)\]\s+(.+)$/)
            return m ? { value: m[2].trim(), fc: m[1] } : null
          })
          .filter(Boolean)
      } else {
        rowDaRefs[key] = []
      }
    } catch {
      rowDaRefs[key] = []
    }
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
      if (neg.success && neg.data) {
        form.apdu = neg.data.apduSize
        form.asdu = neg.data.asduSize
        form.version = neg.data.protocolVersion
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