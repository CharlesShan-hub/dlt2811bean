import { computed } from 'vue'

/**
 * 命令表单整体有效性判定（决定执行的按钮变绿/变红）。
 *
 * @param {object}   form - 响应式表单（reactive）
 * @param {object}   opts
 * @param {Function} opts.getDef
 * @param {Function} opts.getCmd
 * @param {Function} opts.dsRefExists
 * @param {Function} opts.dsRefInvalid
 */
export function useFormValidation(form, { getDef, getCmd, dsRefExists, dsRefInvalid }) {
  const formValid = computed(() => {
    const params = getDef().params || []
    for (const p of params) {
      const v = form[p.key]

      // ln-cascade 级联一致性：选了 LN 但没选 LD → 非法；ld-dir 的 after 特殊
      if (p.type === 'ln-cascade') {
        if (v && v.ln && !v.ld) return false
        if (getCmd() === 'ld-dir' && p.key === 'after' && !form.ld) {
          if (v && v.ld && !v.ln) return false
        }
      }

      // refs-list 级联一致性：选了 LN 但没选 LD → 非法
      if (p.type === 'refs-list' && p.cascade) {
        const rows = v || []
        for (const r of rows) {
          if (r && r.ln && !r.ld) return false
        }
      }

      if (!p.required) continue
      if (p.type === 'ln-cascade') {
        if (!v || !v.ld) return false
      } else if (p.type === 'ld-select') {
        if (!v) return false
      } else if (p.type === 'dataset-select') {
        if (!v) return false
      } else if (p.type === 'ds-ref-input') {
        if (!v || !v.ld || !v.ln || !v.name) return false
        if (p.selectOnly ? !dsRefExists(v) : dsRefInvalid(v)) return false
      } else if (p.type === 'refs-list') {
        const rows = v || []
        const hasValid = p.cascade
          ? (p.cb ? rows.some((r) => r && r.ld && r.ln && r.do) : rows.some((r) => r && r.ld && r.ln))
          : rows.some((r) => r && typeof r === 'string' && r)
        if (!hasValid) return false
      } else if (v === '' || v === null || v === undefined) {
        return false
      }
    }
    return true
  })

  return { formValid }
}