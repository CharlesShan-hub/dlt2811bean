// 8.3.4 读所有数据值（GetAllDataValues）
import { FC_OPTIONS } from './common.js'

export default {
  title: '读所有数据值 all-data (8.3.4)',
  desc: '获取指定逻辑设备或逻辑节点下所有数据对象的值（GetAllDataValues）',
  asn1: `GetAllDataValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT CHOICE {
        ldName         [0] IMPLICIT ObjectName,
        lnReference    [1] IMPLICIT ObjectReference
    },
    fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
}

GetAllDataValues-ResponsePDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT SubReference,
        value         [1] IMPLICIT Data
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetAllDataValues-ErrorPDU ::= ServiceError — 8.3.4`,
  params: [
    { key: 'ln', label: '逻辑设备/节点 ln', type: 'ln-select', placeholder: '选择逻辑节点（必填）', required: true },
    { key: 'fc', label: '功能约束 fc', type: 'select', options: FC_OPTIONS },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
  ],
}
