export default {
  title: '服务器目录 server-dir (8.3.1)',
  desc: '获取服务器全部逻辑设备目录（GetServerDirectory）',
  asn1: `GetServerDirectory-RequestPDU ::= SEQUENCE {
    referenceAfter VisibleString OPTIONAL
}

GetServerDirectory-ResponsePDU ::= SEQUENCE {
    logicalDeviceReference SET SIZE (1..maxLd) OF VisibleString OPTIONAL
} — 8.3.1`,
  params: [
    { key: 'after', label: '起始引用 after', type: 'ld-select', placeholder: '选择起始 LD（可选，不选则从头开始）' },
  ],
}
