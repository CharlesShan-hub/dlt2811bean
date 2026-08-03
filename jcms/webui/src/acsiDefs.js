// 逻辑节点下的 9 种 ACSI 分类（ln-dir --acsi 类型），目录树 LN 行上的彩色圆点对应这些。
export const ACSI_DEFS = [
  { key: 'data-object', label: '数据对象', color: '#5b8def' },
  { key: 'data-set', label: '数据集', color: '#c975dd' },
  { key: 'brcb', label: '缓存报告', color: '#4caf7d' },
  { key: 'urcb', label: '非缓存报告', color: '#e5b955' },
  { key: 'lcb', label: '日志控制块', color: '#e5555a' },
  { key: 'log', label: '日志', color: '#5bc0de' },
  { key: 'sgcb', label: '定值组', color: '#ff9f43' },
  { key: 'gocb', label: 'GOOSE', color: '#9aa7ff' },
  { key: 'msvcb', label: '采样值', color: '#ff7eb6' },
]
