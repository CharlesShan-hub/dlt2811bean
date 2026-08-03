import { asn1Negotiate } from './asn1.js'
import { P_APDU, P_ASDU, P_VERSION } from './common.js'

export default {
  title: '协商 negotiate (8.15)',
  desc: '单独执行参数协商',
  asn1: asn1Negotiate,
  params: [
    { ...P_APDU, default: 65535 },
    { ...P_ASDU, default: 65531 },
    { ...P_VERSION, default: 1, readonly: true },
  ],
}
