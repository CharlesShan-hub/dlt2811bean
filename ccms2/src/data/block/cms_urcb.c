#include "data/block/cms_urcb.h"
#include "data/string/cms_visible_string.h"
#include "data/string/cms_octet_string.h"

int cms_urcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_urcb_t *pdu = (const cms_urcb_t*)ptr;
    int err;

    /* 1. rptID — VisibleString (SIZE(129)) */
    if (!pdu->rptID) return CMS_ERR;
    err = cms_visible_string_encode_stream(s, pdu->rptID, CMS_URCB_RPT_ID_MAX_LEN);
    if (err) return err;

    /* 2. rptEna — BOOLEAN */
    if (!pdu->rptEna) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->rptEna);
    if (err) return err;

    /* 3. datSet — ObjectReference */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_encode_stream(s, pdu->datSet);
    if (err) return err;

    /* 4. confRev — INT32U */
    if (!pdu->confRev) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->confRev);
    if (err) return err;

    /* 5. optFlds — RCBOptFlds */
    if (!pdu->optFlds) return CMS_ERR;
    err = cms_rcb_opt_flds_encode_stream(s, pdu->optFlds);
    if (err) return err;

    /* 6. bufTm — INT32U */
    if (!pdu->bufTm) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->bufTm);
    if (err) return err;

    /* 7. sqNum — INT16U */
    if (!pdu->sqNum) return CMS_ERR;
    err = cms_int16u_encode_stream(s, pdu->sqNum);
    if (err) return err;

    /* 8. trgOps — TriggerConditions */
    if (!pdu->trgOps) return CMS_ERR;
    err = cms_trigger_conditions_encode_stream(s, pdu->trgOps);
    if (err) return err;

    /* 9. intgPd — INT32U */
    if (!pdu->intgPd) return CMS_ERR;
    err = cms_int32u_encode_stream(s, pdu->intgPd);
    if (err) return err;

    /* 10. gi — BOOLEAN */
    if (!pdu->gi) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->gi);
    if (err) return err;

    /* 11. resv — BOOLEAN [14] */
    if (!pdu->resv) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->resv);
    if (err) return err;

    /* 12. owner — OCTET STRING (SIZE(0..64)) OPTIONAL */
    {
        int present = (pdu->owner_present && pdu->owner_present->value) && pdu->owner;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_octet_string_encode_stream(s, pdu->owner, CMS_URCB_OWNER_MAX_LEN);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_urcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_urcb_t *pdu = (cms_urcb_t*)ptr;
    int err;

    /* 1. rptID */
    if (!pdu->rptID) return CMS_ERR;
    err = cms_visible_string_decode_stream(s, pdu->rptID, CMS_URCB_RPT_ID_MAX_LEN);
    if (err) return err;

    /* 2. rptEna */
    if (!pdu->rptEna) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->rptEna);
    if (err) return err;

    /* 3. datSet */
    if (!pdu->datSet) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu->datSet);
    if (err) return err;

    /* 4. confRev */
    if (!pdu->confRev) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu->confRev);
    if (err) return err;

    /* 5. optFlds */
    if (!pdu->optFlds) return CMS_ERR;
    err = cms_rcb_opt_flds_decode_stream(s, pdu->optFlds);
    if (err) return err;

    /* 6. bufTm */
    if (!pdu->bufTm) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu->bufTm);
    if (err) return err;

    /* 7. sqNum */
    if (!pdu->sqNum) return CMS_ERR;
    err = cms_int16u_decode_stream(s, pdu->sqNum);
    if (err) return err;

    /* 8. trgOps */
    if (!pdu->trgOps) return CMS_ERR;
    err = cms_trigger_conditions_decode_stream(s, pdu->trgOps);
    if (err) return err;

    /* 9. intgPd */
    if (!pdu->intgPd) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu->intgPd);
    if (err) return err;

    /* 10. gi */
    if (!pdu->gi) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->gi);
    if (err) return err;

    /* 11. resv */
    if (!pdu->resv) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->resv);
    if (err) return err;

    /* 12. owner — OCTET STRING OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (bit.value && pdu->owner) {
            err = cms_octet_string_decode_stream(s, pdu->owner, CMS_URCB_OWNER_MAX_LEN);
            if (err) return err;
        }
        if (pdu->owner_present) pdu->owner_present->value = bit.value;
    }

    return CMS_OK;
}

int cms_urcb_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_urcb_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_urcb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_urcb_decode_stream(&s, ptr);
}
