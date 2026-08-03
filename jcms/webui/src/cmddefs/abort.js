import { asn1Abort } from './asn1.js'

const abortReasons = [
  { value: 0, label: 'other' },
  { value: 1, label: 'unrecognized-service' },
  { value: 2, label: 'invalid-reqID' },
  { value: 3, label: 'invalid-argument' },
  { value: 4, label: 'invalid-result' },
  { value: 5, label: 'max-serv-outstanding-exceeded' },
]

export default {
  title: '中止 abort (8.2.3)',
  desc: '中止当前关联，服务器直接关闭（无需响应）',
  asn1: asn1Abort,
  params: [
    { key: 'reason', label: '中止原因', type: 'select', options: abortReasons.map((r) => `${r.value} (${r.label})`) },
  ],
}
