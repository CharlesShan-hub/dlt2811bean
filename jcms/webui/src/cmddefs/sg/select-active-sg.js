export default {
  title: '选择激活定值组 select-active-sg (8.6.1)',
  desc: '选择一个定值组作为当前激活组',
  asn1: `SelectActiveSG-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT ObjectReference,
    settingGroupNumber  [1] IMPLICIT INT8U
} — 8.6.1

SelectActiveSG-ResponsePDU ::= NULL — 8.6.1

SelectActiveSG-ErrorPDU ::= ServiceError — 8.6.1`,
}