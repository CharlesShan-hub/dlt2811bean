import { asn1Associate, asn1Negotiate } from './asn1.js'
import { P_AP, P_APDU, P_ASDU, P_IP, P_VERSION } from './common.js'

export default {
  title: '连接 connect',
  desc: 'TCP → 协商 → 关联三步一体',
  asn1: `${asn1Negotiate}\n\n${asn1Associate}`,
  params: [
    P_IP,
    P_AP,
    { key: 'secure', label: '安全连接（TLS）', type: 'switch' },
    { key: 'apsecure', label: '应用层安全认证', type: 'switch' },
    P_APDU,
    P_ASDU,
    P_VERSION,
  ],
}

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
