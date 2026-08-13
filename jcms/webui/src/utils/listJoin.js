/**
 * 列表拼接工具
 *
 * 将列表值拼接为字符串，自动选择不会与任何值冲突的分隔符。
 * 所有需要拼接列表参数的地方都应使用此工具，避免重复实现和 bug。
 */

/**
 * 从一组候选分隔符中，找到第一个不与任何值冲突的安全分隔符
 *
 * @param {string[]} values - 要检查的值列表（支持空值，会被自动过滤）
 * @param {object} [options]
 * @param {string[]} [options.candidates] - 候选分隔符列表，默认 [' ', ',', ';', '|', '::']
 * @returns {string} 安全的分隔符
 */
export function findSafeDelimiter(values, options = {}) {
  const candidates = options.candidates || [' ', ',', ';', '|', '::']
  const filtered = values.filter(x => x !== '' && x !== null && x !== undefined)

  // 从候选列表中找第一个不与任何值冲突的分隔符
  let delim = candidates.find(d => filtered.every(v => !v.includes(d)))

  // 所有候选都冲突时，递增 | 数量直到不冲突
  if (!delim) {
    let n = 2
    while (!delim) {
      const d = '|'.repeat(n)
      if (filtered.every(v => !v.includes(d))) delim = d
      else n++
    }
  }

  return delim
}

/**
 * 自动选择安全分隔符，将列表拼接为字符串
 *
 * @param {string[]} values - 要拼接的字符串数组（空值会被自动过滤）
 * @param {object} [options]
 * @param {string[]} [options.candidates] - 候选分隔符列表，默认 [' ', ',', ';', '|', '::']
 * @returns {{ joined: string, delimiter: string }}
 *   - joined:  拼接后的完整字符串
 *   - delimiter: 实际使用的分隔符（若为空格，调用方可省略 --delimiter 参数）
 */
export function joinList(values, options = {}) {
  const filtered = values.filter(x => x !== '' && x !== null && x !== undefined)
  const delim = findSafeDelimiter(values, options)
  return {
    joined: filtered.join(delim),
    delimiter: delim,
  }
}