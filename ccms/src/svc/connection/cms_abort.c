#include "svc/connection/cms_abort.h"
#include "svc/other/cms_association_id.h"
#include "svc/connection/cms_abort_reason.h"

int cms_abort_encode_stream(per_stream_t *s, const cms_abort_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(s, pdu->req_id);
    if (err) return err;

    /* 2. assocId — AssociationId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_encode_stream(s, pdu->assoc_id);
    if (err) return err;

    /* 3. reason — AbortReason */
    if (!pdu->reason) return CMS_ERR;
    err = cms_abort_reason_encode_stream(s, pdu->reason);
    if (err) return err;

    return CMS_OK;
}

int cms_abort_decode_stream(per_stream_t *s, cms_abort_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(s, pdu->req_id);
    if (err) return err;

    /* 2. assocId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_decode_stream(s, pdu->assoc_id);
    if (err) return err;

    /* 3. reason */
    if (!pdu->reason) return CMS_ERR;
    err = cms_abort_reason_decode_stream(s, pdu->reason);
    if (err) return err;

    return CMS_OK;
}

int cms_abort_encode(const cms_abort_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_abort_encode_stream(&s, pdu);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_abort_decode(cms_abort_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_abort_decode_stream(&s, pdu);
}
