import { asn1Associate } from './asn1.js'
import { P_AP } from './common.js'

export default {
  title: '关联 associate (8.2.1)',
  desc: '手动建立应用层关联（connect 已自动包含）',
  asn1: asn1Associate,
  params: [P_AP, { key: 'secure', label: '应用层安全认证', type: 'switch' }],
}
