export default {
  title: '逻辑设备目录 ld-dir (8.3.2)',
  desc: '获取指定逻辑设备下的逻辑节点目录（GetLogicalDeviceDirectory）',
  asn1: `GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE {
    ldName         [0] IMPLICIT ObjectName OPTIONAL,
    referenceAfter [1] IMPLICIT ObjectReference OPTIONAL
}

GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE {
    lnReference     [0] IMPLICIT SEQUENCE OF SubReference,
    moreFollows     [1] IMPLICIT Boolean DEFAULT 1
} — 8.3.2`,
  params: [
    { key: 'ld', label: '逻辑设备 ld', type: 'ld-select', placeholder: '选择逻辑设备' },
    { key: 'after', label: '起始引用 after', type: 'ln-select', placeholder: '选择起始 LN（可选）' },
  ],
}
