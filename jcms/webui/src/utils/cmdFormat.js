/**
 * 命令格式化 & 工具函数
 */

export function escHtml(s) {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
}

/** 命令高亮：命令名 → 蓝色，参数 → 绿色，值 → 橙色 */
export function highlightCmdStr(cmd) {
  if (!cmd) return ''
  const tokens = cmd.match(/(?:--?\w[\w-]*|"[^"]*"|[^\s"]+)/g) || []
  let first = true
  return tokens.map((t) => {
    if (first) {
      first = false
      return `<span style="color:var(--accent)">${escHtml(t)}</span>`
    }
    if (t.startsWith('--')) {
      return `<span style="color:#34d399">${escHtml(t)}</span>`
    }
    return `<span style="color:#fb923c">${escHtml(t)}</span>`
  }).join(' ')
}

/** 给 JSON 字符串添加语法高亮 HTML */
export function syntaxHighlightJson(str) {
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(
      /("(?:[^"\\]|\\.)*")\s*:/g,
      '<span class="json-key">$1</span>:'
    )
    .replace(
      /:\s*("(?:[^"\\]|\\.)*")/g,
      ': <span class="json-string">$1</span>'
    )
    .replace(
      /:\s*(true|false)/g,
      ': <span class="json-bool">$1</span>'
    )
    .replace(
      /:\s*(null)/g,
      ': <span class="json-null">$1</span>'
    )
    .replace(
      /:\s*(-?\d+(?:\.\d+)?(?:[eE][+-]?\d+)?)/g,
      ': <span class="json-num">$1</span>'
    )
}

/** 从原始响应中提取 JSON 结果，无 JSON 时返回 null */
export function parseResult(text) {
  const clean = text.replace(/\x1b\[\d+m/g, '').trim()
  const jsonStart = clean.indexOf('{')
  if (jsonStart >= 0) {
    try {
      return JSON.parse(clean.slice(jsonStart))
    } catch {
      // fall through
    }
  }
  return null
}

/**
 * 拼接命令字符串
 * @param {string} cmd - 命令名
 * @param {Array} params - 参数定义（CMD_DEFS 的 params）
 * @param {object} form - 表单值
 * @param {object} opts - 额外选项
 * @param {string} opts.cmdProp - 当前命令名（用于 ld-dir 等特殊判断）
 */
export function buildCmd(cmd, params, form, opts = {}) {
  const parts = [cmd]
  for (const p of params) {
    const v = form[p.key]
    if (p.type === 'switch') {
      if (v) {
        parts.push(`--${p.key}`)
      }
    } else if (p.type === 'auto-pull-switch') {
      if (v) {
        parts.push(`--${p.key}`)
      }
    } else if (p.type === 'select') {
      if (p.disabled) continue
      if (v) {
        parts.push(`--${p.key}`, String(v).split(':')[0])
      }
    } else if (p.type === 'ap-select') {
      if (v) {
        parts.push(`--${p.key}`, String(v))
      }
    } else if (p.type === 'ld-select') {
      if (v) {
        parts.push(`--${p.key}`, String(v))
      }
    } else if (p.type === 'ln-cascade') {
      const o = v || {}
      // 如果存在依赖该 ln-cascade 的 dataset-select 参数，则跳过（由 dataset-select 产生 --ds）
      const hasDatasetSelect = params.some(x => x.type === 'dataset-select' && x.dependsOn === p.key)
      if (hasDatasetSelect) {
        // 不输出 --ln
      } else if (o.ld) {
        const ref = opts.cmdProp === 'ld-dir' && p.key === 'after' && form.ld
          ? o.ln
          : (o.ln ? `${o.ld}/${o.ln}` : o.ld)
        if (ref) {
          parts.push(`--${p.key}`, ref)
        }
      }
    } else if (p.type === 'dataset-select') {
      // dataset-select: 与 ln-cascade 组合为 --ds "LD/LN.dsName"
      const lnKey = p.dependsOn || 'ln'
      const lnVal = form[lnKey]
      if (v && lnVal && lnVal.ld && lnVal.ln) {
        parts.push('--ds', `"${lnVal.ld}/${lnVal.ln}.${v}"`)
      }
    } else if (p.type === 'ds-ref-input') {
      // ds-ref-input: LD + LN + 数据集名称 → --ds "LD/LN.dsName"
      if (v && v.ld && v.ln && v.name) {
        parts.push('--ds', `"${v.ld}/${v.ln}.${v.name}"`)
      }
    } else if (p.type === 'ln-ref-select') {
      if (v) {
        parts.push(`--${p.key}`, String(v))
      }
    } else if (p.type === 'refs-list') {
      const rows = (v || []).filter(Boolean)
      const refs = []
      const afterParts = []
      const fcs = []
      const values = []
      const types = []
      if (p.cascade) {
        for (const row of rows) {
          if (!row.ld || !row.ln) continue
          let ref = `${row.ld}/${row.ln}`
          if (row.do) ref += `.${row.do}`
          // data-dir: SDO/DA 作为 --after 分页游标，不拼入 ref
          if (cmd === 'data-dir') {
            if (row.sdo) afterParts.push(row.sdo)
            if (row.da) afterParts.push(row.da)
          } else {
            if (row.sdo) ref += `.${row.sdo}`
            if (row.da) ref += `.${row.da}`
          }
          refs.push(ref)
          fcs.push(row.fc || '')
          values.push(row.value || '')
          types.push(row.type || '')
        }
      } else {
        for (const r of rows) {
          if (typeof r === 'string' && r) {
            refs.push(r)
            fcs.push('')
            values.push('')
            types.push('')
          }
        }
      }
      if (refs.length) {
        // 自动选择分隔符：依次尝试常见分隔符，然后永无止境递增 |||...
        const allVals = [...refs, ...values.filter(v => v), ...fcs.filter(f => f)]
        const safeDelim = (candidates) => candidates.find(d => allVals.every(v => !v.includes(d)))
        let delim = safeDelim([' ', ',', ';', '|', '::'])
        if (!delim) {
          let n = 2
          while (!delim) {
            const d = '|'.repeat(n)
            if (allVals.every(v => !v.includes(d))) delim = d
            else n++
          }
        }
        if (delim !== ' ') {
          parts.push('--delimiter', `"${delim}"`)
        }
        parts.push(`--${p.key}`, `"${refs.join(delim)}"`)
        const hasFc = fcs.some(f => f)
        if (hasFc) {
          const fcParam = p.membersFormat ? '--fcs' : '--fc'
          parts.push(fcParam, `"${fcs.join(delim)}"`)
        }
        // set-data-values 额外输出 --values 列表
        // 注意：JSON 值中的 " 需要转义为 \"，否则 tokenizer 会误判引号边界
        if (cmd === 'set-data-values') {
          const nonEmptyValues = values.filter(v => v)
          if (nonEmptyValues.length) {
            const escapedVals = values.join(delim).replace(/"/g, '\\"')
            parts.push('--values', `"${escapedVals}"`)
          }
        }
        // data-dir: SDO/DA 作为 --after 分页游标
        if (cmd === 'data-dir' && afterParts.length) {
          parts.push('--after', `"${afterParts.join(delim)}"`)
        }
      }
    } else if (p.key === 'refs' && v !== '' && v !== null && v !== undefined) {
      parts.push(`--${p.key}`, `"${String(v)}"`)
    } else if (v !== '' && v !== null && v !== undefined) {
      parts.push(`--${p.key}`, String(v))
    }
  }
  return parts.join(' ')
}

/**
 * 解析命令字符串回表单值（反向 buildCmd）
 * @param {string} cmdStr - 完整命令字符串
 * @param {Array} params - 参数定义
 * @param {object} opts - 选项
 * @param {string} opts.cmdName - 命令名（可选，不传则从 cmdStr 提取）
 * @returns {{ form: object, valid: boolean, errors: string[] }}
 */
export function parseCmd(cmdStr, params, opts = {}) {
  const form = {}
  const errors = []

  // 分词：支持引号包裹的值
  const tokens = []
  const re = /(?:--?\w[\w-]*|"(?:[^"\\]|\\.)*"|[^\s"]+)/g
  let m
  while ((m = re.exec(cmdStr)) !== null) {
    tokens.push(m[0])
  }

  // 跳过命令名
  const cmdName = opts.cmdName || tokens[0] || ''
  let i = cmdName ? 1 : 0

  while (i < tokens.length) {
    const t = tokens[i]
    if (t.startsWith('--')) {
      const key = t.slice(2)
      const param = params.find((p) => p.key === key)
      if (!param) {
        errors.push(`未知参数：--${key}`)
        i++
        continue
      }

      if (param.type === 'switch') {
        form[key] = true
        i++
      } else if (param.type === 'select' && param.disabled) {
        // 跳过禁用参数
        i++
      } else if (param.type === 'ln-cascade') {
        // ln-cascade：值可能是 "LD/LN" 或纯 "LN"
        const val = tokens[i + 1]
        if (val === undefined || val.startsWith('--')) {
          errors.push(`参数 --${key} 缺少值`)
          i++
        } else {
          const raw = val.startsWith('"') && val.endsWith('"') ? val.slice(1, -1) : val
          if (raw.includes('/')) {
            const parts = raw.split('/')
            form[key] = { ld: parts[0], ln: parts.slice(1).join('/') }
          } else {
            form[key] = { ld: '', ln: raw }
          }
          i += 2
        }
      } else {
        // 取值（可能被引号包裹）
        const val = tokens[i + 1]
        if (val === undefined || val.startsWith('--')) {
          errors.push(`参数 --${key} 缺少值`)
          i++
        } else {
          const raw = val.startsWith('"') && val.endsWith('"') ? val.slice(1, -1).replace(/\\"/g, '"') : val
          form[key] = raw
          i += 2
        }
      }
    } else {
      errors.push(`无法识别的输入：${t}`)
      i++
    }
  }

  return { form, valid: errors.length === 0, errors }
}

/** 剪贴板 SVG 图标 */
export const clipIcon = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>'
export const checkIcon = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#4caf7d" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>'