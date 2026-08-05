import { reactive } from 'vue'

/**
 * 终端共享输出：终端输入与页面（命令调试）执行共用同一份记录，
 * 打开底部终端面板即可看到全部操作历史。
 */
export const terminalLog = reactive(['// CMS Console — 输入命令开始'])

/** 追加若干行到终端输出。 */
export function pushTerminal(lines) {
  const arr = Array.isArray(lines) ? lines : [lines]
  terminalLog.push(...arr)
}

/** 清空终端输出（clear 命令）。 */
export function clearTerminal() {
  terminalLog.splice(0)
}

const ansiStyles = {
  '0': {},
  '1': { fontWeight: 'bold' },
  '30': { color: '#333' },
  '31': { color: '#e5555a' },
  '32': { color: '#4caf7d' },
  '33': { color: '#e5b955' },
  '34': { color: '#5b8def' },
  '35': { color: '#c975dd' },
  '36': { color: '#5bc0de' },
  '37': { color: '#e1e3ec' },
  '90': { color: '#5c6078' },
  '91': { color: '#e5555a' },
  '92': { color: '#4caf7d' },
  '93': { color: '#e5b955' },
  '94': { color: '#7aa3ff' },
  '95': { color: '#c975dd' },
  '96': { color: '#5bc0de' },
}

/**
 * 将包含 ANSI 转义码的文本解析为带 style 的片段数组。
 * 每个片段有 { text, style }，style 是 CSS 样式对象。
 */
export function parseAnsi(text) {
  const parts = []
  const regex = /\x1b\[(\d+)m/g
  let lastIdx = 0
  let currentStyle = {}

  while (true) {
    const match = regex.exec(text)
    if (!match) break
    if (match.index > lastIdx) {
      parts.push({ text: text.slice(lastIdx, match.index), style: { ...currentStyle } })
    }
    const code = match[1]
    if (code === '0') {
      currentStyle = {}
    } else if (ansiStyles[code]) {
      currentStyle = { ...currentStyle, ...ansiStyles[code] }
    }
    lastIdx = regex.lastIndex
  }
  if (lastIdx < text.length) {
    parts.push({ text: text.slice(lastIdx), style: { ...currentStyle } })
  }
  return parts.length ? parts : [{ text, style: {} }]
}
