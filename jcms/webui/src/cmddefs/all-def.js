// 8.3.5 读所有数据定义（GetAllDataDefinition）
import { FC_OPTIONS } from './common.js'

export default {
  title: '读所有数据定义 all-def (8.3.5)',
  desc: '获取指定逻辑设备或逻辑节点下所有数据对象的定义（GetAllDataDefinition）',
  asn1: `GetAllDataDefinition-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT CHOICE {
        ldName         [0] IMPLICIT ObjectName,
        lnReference    [1] IMPLICIT ObjectReference
    },
    fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
}

GetAllDataDefinition-ResponsePDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT SubReference,
        cdcType       [1] IMPLICIT VisibleString OPTIONAL,
        definition    [2] IMPLICIT DataDefinition
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetAllDataDefinition-ErrorPDU ::= ServiceError — 8.3.5`,
  params: [
    { key: 'ln', label: '逻辑设备/节点 ln', type: 'ln-select', placeholder: '选择逻辑节点（必填）', required: true },
    { key: 'fc', label: '功能约束 fc', type: 'select', options: FC_OPTIONS },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
  ],
}
