import { asn1Abort } from './asn1.js'

const abortReasons = [
  { value: 0, label: 'other', cn: '其他' },
  { value: 1, label: 'unrecognized-service', cn: '无法识别的服务' },
  { value: 2, label: 'invalid-reqID', cn: '无效请求ID' },
  { value: 3, label: 'invalid-argument', cn: '无效参数' },
  { value: 4, label: 'invalid-result', cn: '无效结果' },
  { value: 5, label: 'max-serv-outstanding-exceeded', cn: '超出最大未完成服务数' },
]

export default {
  title: '中止 abort (8.2.3)',
  desc: '中止当前关联，服务器直接关闭（无需响应）',
  asn1: asn1Abort,
  params: [
    { key: 'reason', label: '中止原因', type: 'select', options: abortReasons.map((r) => `${r.value}: ${r.cn}（${r.label}）`) },
  ],
}
