#include "data/block/cms_sgcb.h"
#include "data/scalar/cms_boolean.h"

int cms_sgcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_sgcb_t *pdu = (const cms_sgcb_t*)ptr;

    /* 1. numOfSG — INT8U */
    if (!pdu->numOfSG) return CMS_ERR;
    int err = cms_int8u_encode_stream(s, pdu->numOfSG);
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

    /* 5. resvTms — INT16U OPTIONAL */
    {
        int present = (pdu->resvTms_present && pdu->resvTms_present->value) && pdu->resvTms;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_int16u_encode_stream(s, pdu->resvTms);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_sgcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_sgcb_t *pdu = (cms_sgcb_t*)ptr;

    /* 1. numOfSG */
    if (!pdu->numOfSG) return CMS_ERR;
    int err = cms_int8u_decode_stream(s, pdu->numOfSG);
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

    /* 5. resvTms — INT16U OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (bit.value && pdu->resvTms) {
            err = cms_int16u_decode_stream(s, pdu->resvTms);
            if (err) return err;
        }
        if (pdu->resvTms_present) pdu->resvTms_present->value = bit.value;
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
