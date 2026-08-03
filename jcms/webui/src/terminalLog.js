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
