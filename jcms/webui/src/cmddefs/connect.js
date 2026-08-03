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
    P_APDU,
    P_ASDU,
    P_VERSION,
  ],
}
