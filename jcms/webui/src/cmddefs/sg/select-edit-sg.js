export default {
  title: '选择编辑定值组 select-edit-sg (8.6.2)',
  desc: '选择一个定值组作为当前编辑组',
  asn1: `SelectEditSG-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT ObjectReference,
    settingGroupNumber  [1] IMPLICIT INT8U
} — 8.6.2

SelectEditSG-ResponsePDU ::= NULL — 8.6.2

SelectEditSG-ErrorPDU ::= ServiceError — 8.6.2`,
}