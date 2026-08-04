/** connect 状态机图示数据（Dashboard / CommandDebug 共用）。 */
export const CONNECT_FLOW = {
  states: [
    { id: 'init', label: '未连接' },
    { id: 'tcp', label: '已连接' },
    { id: 'neg', label: '已协商' },
    { id: 'assoc', label: '已关联' },
  ],
  edges: [
    { from: 'init', to: 'tcp', label: 'connect' },
    { from: 'tcp', to: 'neg', label: 'negotiate' },
    { from: 'neg', to: 'assoc', label: 'associate' },
    { from: 'assoc', to: 'neg', label: 'release', back: true, lane: 1 },
    { from: 'assoc', to: 'init', label: 'abort', back: true, lane: 0 },
    { from: 'tcp', to: 'init', label: 'disconnect', back: true, side: 'left' },
  ],
}
