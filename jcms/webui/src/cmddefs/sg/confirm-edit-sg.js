export default {
  title: '确认编辑定值组值 confirm-edit-sg (8.6.4)',
  desc: '将编辑缓冲区的定值提交到定值组',
  asn1: `ConfirmEditSGValues-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT ObjectReference
} — 8.6.4

ConfirmEditSGValues-ResponsePDU ::= NULL — 8.6.4

ConfirmEditSGValues-ErrorPDU ::= ServiceError — 8.6.4`,
}