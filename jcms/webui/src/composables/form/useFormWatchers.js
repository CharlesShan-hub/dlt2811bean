import { watch } from 'vue'
import {
  ldCache, ldLns,
  datasetRefs, datasetMemberRefs,
  ensureLdLns, ensureAllLnRefs, ensureLnDirRefs, ensureAllDefRefs, ensureAllCbRefs,
  ensureDatasetRefs, ensureDatasetMemberRefs, ensureDatasetMembers,
} from '../../ldCache.js'
import { executeJson } from '../../api/cms.js'
import { cascadeRow, plainRow } from './refsUtil.js'



/**
 * 表单初始化与联动副作用（watcher 集合）。
 *
 * @param {object}   form
 * @param {object}   opts
 * @param {Function} opts.getDef
 * @param {Function} opts.getCmd
 * @param {Function} opts.getLnRef - () => 当前 lnRef
 * @param {string[]} opts.lnRequiredCmds - 需要自动选中首个 LD/LN 的命令
 */
export function useFormWatchers(form, { getDef, getCmd, getLnRef, lnRequiredCmds = [] }) {
  /** 根据命令定义重置表单。 */
  function initForm() {
    const def = getDef()
    for (const k of Object.keys(form)) delete form[k]
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
      } else if (p.type === 'ds-ref-input') {
        form[p.key] = { ld: '', ln: '', name: '' }
      } else if (p.type === 'refs-list') {
        form[p.key] = p.cascade ? [cascadeRow()] : [plainRow()]
      } else if (p.type === 'values-list') {
        form[p.key] = []
      } else {
        form[p.key] = p.default ?? (p.type === 'switch' ? false : '')
      }
    }
  }

  /** 命令专属默认值（在 cmd 切换后调用）。 */
  async function applyCmdDefaults() {
    const cmd = getCmd()
    // set-edit-sg：级联自动按 SE 筛选（协议规定功能约束为 SE）
    if (cmd === 'set-edit-sg') form.fc = 'SE'
    if (cmd === 'negotiate') await loadNegotiateDefaults()
  }

  async function loadNegotiateDefaults() {
    try {
      const neg = await executeJson('neg-cfg')
      if (neg && typeof neg.apduSize === 'number') {
        form.apdu = neg.apduSize
        form.asdu = neg.asduSize
        form.version = neg.protocolVersion
      }
    } catch { /* ignore */ }
  }

  // ── LN 必填命令的自动初始化（切命令时选中首个 LD/LN） ──
  function setupLnRequiredWatch() {
    return watch(
      () => getCmd(),
      async (cmd) => {
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
      },
      { immediate: true }
    )
  }

  // ── 缓存就绪后（延迟）自动选中第一个 LD ──
  function setupLazyLnWatch() {
    return watch(
      () => [ldCache.length],
      async () => {
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
      }
    )
  }

  // ── ld-dir 的 LD 级联：after 跟随选中的 ld ──
  function setupLdDirWatch() {
    return watch(
      [() => form.ld, () => ldCache.length],
      async ([ld]) => {
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
      },
      { immediate: true }
    )
  }

  // ── fc 变化时重新加载 DO 选项 ──
  function setupFcWatch() {
    return watch(
      () => form.fc,
      () => {
        const p = getDef().params.find((x) => x.type === 'refs-list' && x.cascade)
        const rows = p ? form[p.key] : null
        if (!Array.isArray(rows)) return
        for (const row of rows) {
          if (row && row.ld && row.ln) {
            // 复用 useRefsRows 的加载；此处通过重建引用触发（由父级 watch 处理）
          }
        }
      }
    )
  }

  // ── ln-dir / all-cb 的引用懒加载（after 下拉） ──
  function setupRefsWatch() {
    return watch(
      [getLnRef, () => form.acsi],
      async () => {
        const cmd = getCmd()
        const lnRef = getLnRef()
        if (!lnRef) return
        if (cmd === 'ln-dir') {
          const acsi = String(form.acsi || '').split(':')[0] || 'data-object'
          await ensureLnDirRefs(lnRef, acsi)
        } else if (cmd === 'all-cb') {
          const acsi = String(form.acsi || '').split(':')[0] || 'brcb'
          await ensureAllCbRefs(lnRef, acsi)
        }
      },
      { immediate: true }
    )
  }

  // ── all-data / all-def 的引用懒加载 ──
  function setupAllDataRefsWatch() {
    return watch(
      [getLnRef, () => form.fc],
      async () => {
        const cmd = getCmd()
        if (!['all-data', 'all-def'].includes(cmd)) return
        const lnRef = getLnRef()
        if (!lnRef) return
        const fc = String(form.fc || '').split(':')[0] || 'XX'
        await ensureAllDefRefs(lnRef, fc)
      },
      { immediate: true }
    )
  }

  // ── ds-ref-input 命令：ds 选好后加载成员引用（after 下拉用），set 命令额外加载成员数据 ──
  function setupDsRefMembersWatch() {
    return watch(
      [() => form.ds?.ld, () => form.ds?.ln, () => form.ds?.name],
      async ([ld, ln, name]) => {
        if (!getDef().params.some((x) => x.type === 'ds-ref-input')) return
        if (!ld || !ln || !name) return
        const key = `${ld}/${ln}.${name}`
        if (!datasetMemberRefs[key]) await ensureDatasetMemberRefs(`${ld}/${ln}`, name)
        if (getCmd() === 'set-dataset-values') {
          form.values = []
          await ensureDatasetMembers(`${ld}/${ln}`, name)
        }
      }
    )
  }

  // ── create-dataset：选择已存在的数据集名称时自动回填 after 为最后一个成员 ──
  function setupCreateDatasetWatch() {
    return watch(
      () => form.ds?.name,
      async (name) => {
        if (getCmd() !== 'create-dataset') return
        form.after = ''
        if (!form.ds?.ld || !form.ds?.ln || !name) return
        const ref = `${form.ds.ld}/${form.ds.ln}.${name}`
        try {
          const res = await executeJson(`get-dataset-dir --ds ${ref} --auto-pull true`)
          if (res && Array.isArray(res.memberData) && res.memberData.length > 0) {
            form.after = res.memberData[res.memberData.length - 1].reference
          }
        } catch { /* ignore */ }
      }
    )
  }

  /** 挂载全部 watcher，返回停止函数数组。 */
  function setup() {
    return [
      setupLnRequiredWatch(),
      setupLazyLnWatch(),
      setupLdDirWatch(),
      setupFcWatch(),
      setupRefsWatch(),
      setupAllDataRefsWatch(),
      setupDsRefMembersWatch(),
      setupCreateDatasetWatch(),
    ]
  }

  return { initForm, applyCmdDefaults, setup }
}