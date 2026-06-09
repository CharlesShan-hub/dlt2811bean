#include "data/block/cms_lcb.h"

int cms_lcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_lcb_t *pdu = (const cms_lcb_t*)ptr;
    int err;

    /* 1. logEna — BOOLEAN */
    if (!pdu->logEna) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->logEna);
    if (err) return err;

    /* 2. datSet — ObjectReference */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_encode_stream(s, pdu->datSet);
    if (err) return err;

    /* 3. trgOps — TriggerConditions */
    if (!pdu->trgOps) return CMS_ERR;
    err = cms_trigger_conditions_encode_stream(s, pdu->trgOps);
    if (err) return err;

    /* 4. intgPd — INT32U */
    if (!pdu->intgPd) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->intgPd);
    if (err) return err;

    /* 5. logRef — ObjectReference */
    if (!pdu->logRef) return CMS_ERR;
    err = cms_object_reference_encode_stream(s, pdu->logRef);
    if (err) return err;

    /* 6. optFlds — LCBOptFlds OPTIONAL */
    {
        int present = (pdu->optFlds_present && pdu->optFlds_present->value) && pdu->optFlds;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_lcb_opt_flds_encode_stream(s, pdu->optFlds);
            if (err) return err;
        }
    }

    /* 7. bufTm — INT32U OPTIONAL */
    {
        int present = (pdu->bufTm_present && pdu->bufTm_present->value) && pdu->bufTm;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_int32u_encode_stream(s, pdu->bufTm);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_lcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_lcb_t *pdu = (cms_lcb_t*)ptr;
    int err;

    /* 1. logEna */
    if (!pdu->logEna) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->logEna);
    if (err) return err;

    /* 2. datSet */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu->datSet);
    if (err) return err;

    /* 3. trgOps */
    if (!pdu->trgOps) return CMS_ERR;
    err = cms_trigger_conditions_decode_stream(s, pdu->trgOps);
    if (err) return err;

    /* 4. intgPd */
    if (!pdu->intgPd) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu->intgPd);
    if (err) return err;

    /* 5. logRef */
    if (!pdu->logRef) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu->logRef);
    if (err) return err;

    /* 6. optFlds — LCBOptFlds OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (bit.value && pdu->optFlds) {
            err = cms_lcb_opt_flds_decode_stream(s, pdu->optFlds);
            if (err) return err;
        }
        if (pdu->optFlds_present) pdu->optFlds_present->value = bit.value;
    }

    /* 7. bufTm — INT32U OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (bit.value && pdu->bufTm) {
            err = cms_int32u_decode_stream(s, pdu->bufTm);
            if (err) return err;
        }
        if (pdu->bufTm_present) pdu->bufTm_present->value = bit.value;
    }

    return CMS_OK;
}

int cms_lcb_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_lcb_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_lcb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_lcb_decode_stream(&s, ptr);
}
