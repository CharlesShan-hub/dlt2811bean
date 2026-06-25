#include "data/block/cms_sgcb.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_sequence.h"

int cms_sgcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_sgcb_t *pdu = (const cms_sgcb_t*)ptr;

    /* 0. OPTIONAL bitmap (1 field: resvTms) — X.691 §22 */
    bool opt_present[1] = {
        pdu->resvTms_present && pdu->resvTms_present->value && pdu->resvTms
    };
    int err = (int)per_encode_optional_bitmap(s, opt_present, 1);
    if (err) return err;

    /* 1. numOfSG — INT8U */
    if (!pdu->numOfSG) return CMS_ERR;
    err = cms_int8u_encode_stream(s, pdu->numOfSG);
    if (err) return err;

    /* 2. actSG — INT8U */
    if (!pdu->actSG) return CMS_ERR;
    err = cms_int8u_encode_stream(s, pdu->actSG);
    if (err) return err;

    /* 3. editSG — INT8U */
    if (!pdu->editSG) return CMS_ERR;
    err = cms_int8u_encode_stream(s, pdu->editSG);
    if (err) return err;

    /* 4. tActEdt — TimeStamp (UtcTime, 8-byte OCTET STRING) */
    if (!pdu->tActEdt) return CMS_ERR;
    err = cms_time_stamp_encode_stream(s, pdu->tActEdt);
    if (err) return err;

    /* 5. resvTms — INT16U OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_int16u_encode_stream(s, pdu->resvTms);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_sgcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_sgcb_t *pdu = (cms_sgcb_t*)ptr;

    /* 0. OPTIONAL bitmap (1 field: resvTms) — X.691 §22 */
    bool opt_present[1] = {false};
    int err = (int)per_decode_optional_bitmap(s, opt_present, 1);
    if (err) return err;
    if (pdu->resvTms_present)
        pdu->resvTms_present->value = opt_present[0] ? 1 : 0;

    /* 1. numOfSG */
    if (!pdu->numOfSG) return CMS_ERR;
    err = cms_int8u_decode_stream(s, pdu->numOfSG);
    if (err) return err;

    /* 2. actSG */
    if (!pdu->actSG) return CMS_ERR;
    err = cms_int8u_decode_stream(s, pdu->actSG);
    if (err) return err;

    /* 3. editSG */
    if (!pdu->editSG) return CMS_ERR;
    err = cms_int8u_decode_stream(s, pdu->editSG);
    if (err) return err;

    /* 4. tActEdt — TimeStamp */
    if (!pdu->tActEdt) return CMS_ERR;
    err = cms_time_stamp_decode_stream(s, pdu->tActEdt);
    if (err) return err;

    /* 5. resvTms — INT16U OPTIONAL (bitmap[0]) */
    if (opt_present[0] && pdu->resvTms) {
        err = cms_int16u_decode_stream(s, pdu->resvTms);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_sgcb_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_sgcb_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_sgcb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_sgcb_decode_stream(&s, ptr);
}
