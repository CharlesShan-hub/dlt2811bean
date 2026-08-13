/** 定值组（SG）工作流状态图数据。 */
export const SG_FLOW = {
  states: [
    { id: 'idle', label: '初始' },
    { id: 'edit', label: '编辑中' },
    { id: 'done', label: '已提交' },
  ],
  edges: [
    { from: 'idle', to: 'edit', label: 'select-edit-sg' },
    { from: 'edit', to: 'done', label: 'confirm-edit-sg' },
    { from: 'done', to: 'edit', label: 'select-edit-sg', back: true, lane: 0 },
    { from: 'done', to: 'idle', label: 'select-active-sg', back: true, side: 'left' },
  ],
}