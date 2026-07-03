#include "data/block/cms_lcb.h"
#include "per/cms_sequence.h"

int cms_lcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_lcb_t *pdu = (const cms_lcb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: optFlds, bufTm) — X.691 §22 */
    bool opt_present[2] = {
        pdu->optFlds_present && pdu->optFlds_present->value && pdu->optFlds,
        pdu->bufTm_present   && pdu->bufTm_present->value   && pdu->bufTm
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 2);
    if (err) return err;

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

    /* 6. optFlds — LCBOptFlds OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_lcb_opt_flds_encode_stream(s, pdu->optFlds);
        if (err) return err;
    }

    /* 7. bufTm — INT32U OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_int32u_encode_stream(s, pdu->bufTm);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_lcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_lcb_t *pdu = (cms_lcb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: optFlds, bufTm) — X.691 §22 */
    bool opt_present[2] = {false, false};
    err = (int)per_decode_optional_bitmap(s, opt_present, 2);
    if (err) return err;
    if (pdu) {
        if (pdu->optFlds_present) pdu->optFlds_present->value = opt_present[0] ? 1 : 0;
        if (pdu->bufTm_present)   pdu->bufTm_present->value   = opt_present[1] ? 1 : 0;
    }

    /* 1. logEna */
    if (pdu && !pdu->logEna) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu ? pdu->logEna : NULL);
    if (err) return err;

    /* 2. datSet */
    if (pdu && !pdu->datSet) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu ? pdu->datSet : NULL);
    if (err) return err;

    /* 3. trgOps */
    if (pdu && !pdu->trgOps) return CMS_ERR;
    err = cms_trigger_conditions_decode_stream(s, pdu ? pdu->trgOps : NULL);
    if (err) return err;

    /* 4. intgPd */
    if (pdu && !pdu->intgPd) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu ? pdu->intgPd : NULL);
    if (err) return err;

    /* 5. logRef */
    if (pdu && !pdu->logRef) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu ? pdu->logRef : NULL);
    if (err) return err;

    /* 6. optFlds — LCBOptFlds OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_lcb_opt_flds_decode_stream(s, pdu ? pdu->optFlds : NULL);
        if (err) return err;
    }

    /* 7. bufTm — INT32U OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_int32u_decode_stream(s, pdu ? pdu->bufTm : NULL);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_lcb_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_lcb_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_lcb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_lcb_decode_stream(&s, ptr);
}
