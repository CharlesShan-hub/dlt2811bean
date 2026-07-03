#include "data/block/cms_urcb.h"
#include "data/string/cms_visible_string.h"
#include "data/string/cms_octet_string.h"
#include "per/cms_sequence.h"

int cms_urcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_urcb_t *pdu = (const cms_urcb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (1 field: owner) — X.691 §22 */
    bool opt_present[1] = {
        pdu->owner_present && pdu->owner_present->value && pdu->owner
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 1);
    if (err) return err;

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

    /* 12. owner — OCTET STRING (SIZE(0..64)) OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_octet_string_encode_stream(s, pdu->owner, CMS_URCB_OWNER_MAX_LEN);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_urcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_urcb_t *pdu = (cms_urcb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (1 field: owner) — X.691 §22 */
    bool opt_present[1] = {false};
    err = (int)per_decode_optional_bitmap(s, opt_present, 1);
    if (err) return err;
    if (pdu && pdu->owner_present)
        pdu->owner_present->value = opt_present[0] ? 1 : 0;

    /* 1. rptID */
    if (pdu && !pdu->rptID) return CMS_ERR;
    err = cms_visible_string_decode_stream(s, pdu ? pdu->rptID : NULL, CMS_URCB_RPT_ID_MAX_LEN);
    if (err) return err;

    /* 2. rptEna */
    if (pdu && !pdu->rptEna) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu ? pdu->rptEna : NULL);
    if (err) return err;

    /* 3. datSet */
    if (pdu && !pdu->datSet) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, pdu ? pdu->datSet : NULL);
    if (err) return err;

    /* 4. confRev */
    if (pdu && !pdu->confRev) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu ? pdu->confRev : NULL);
    if (err) return err;

    /* 5. optFlds */
    if (pdu && !pdu->optFlds) return CMS_ERR;
    err = cms_rcb_opt_flds_decode_stream(s, pdu ? pdu->optFlds : NULL);
    if (err) return err;

    /* 6. bufTm */
    if (pdu && !pdu->bufTm) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu ? pdu->bufTm : NULL);
    if (err) return err;

    /* 7. sqNum */
    if (pdu && !pdu->sqNum) return CMS_ERR;
    err = cms_int16u_decode_stream(s, pdu ? pdu->sqNum : NULL);
    if (err) return err;

    /* 8. trgOps */
    if (pdu && !pdu->trgOps) return CMS_ERR;
    err = cms_trigger_conditions_decode_stream(s, pdu ? pdu->trgOps : NULL);
    if (err) return err;

    /* 9. intgPd */
    if (pdu && !pdu->intgPd) return CMS_ERR;
    err = cms_int32u_decode_stream(s, pdu ? pdu->intgPd : NULL);
    if (err) return err;

    /* 10. gi */
    if (pdu && !pdu->gi) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu ? pdu->gi : NULL);
    if (err) return err;

    /* 11. resv */
    if (pdu && !pdu->resv) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu ? pdu->resv : NULL);
    if (err) return err;

    /* 12. owner — OCTET STRING OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_octet_string_decode_stream(s, pdu ? pdu->owner : NULL, CMS_URCB_OWNER_MAX_LEN);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_urcb_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_urcb_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_urcb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_urcb_decode_stream(&s, ptr);
}
