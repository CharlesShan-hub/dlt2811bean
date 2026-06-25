#include "data/block/cms_brcb.h"
#include "data/string/cms_visible_string.h"
#include "data/string/cms_octet_string.h"
#include "per/cms_sequence.h"

int cms_brcb_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_brcb_t *pdu = (const cms_brcb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: resvTms, owner) — X.691 §22 */
    bool opt_present[2] = {
        pdu->resvTms_present && pdu->resvTms_present->value && pdu->resvTms,
        pdu->owner_present  && pdu->owner_present->value  && pdu->owner
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 2);
    if (err) return err;

    /* 1. rptID — VisibleString (SIZE(129)) */
    if (!pdu->rptID) return CMS_ERR;
    err = cms_visible_string_encode_stream(s, pdu->rptID, CMS_BRCB_RPT_ID_MAX_LEN);
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

    /* 11. purgeBuf — BOOLEAN */
    if (!pdu->purgeBuf) return CMS_ERR;
    err = cms_boolean_encode_stream(s, pdu->purgeBuf);
    if (err) return err;

    /* 12. entryID — EntryID */
    if (!pdu->entryID) return CMS_ERR;
    err = cms_entry_id_encode_stream(s, pdu->entryID);
    if (err) return err;

    /* 13. timeOfEntry — EntryTime */
    if (!pdu->timeOfEntry) return CMS_ERR;
    err = cms_entry_time_encode_stream(s, pdu->timeOfEntry);
    if (err) return err;

    /* 14. resvTms — INT16 OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_int16_encode_stream(s, pdu->resvTms);
        if (err) return err;
    }

    /* 15. owner — OCTET STRING (SIZE(0..64)) OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_octet_string_encode_stream(s, pdu->owner, CMS_BRCB_OWNER_MAX_LEN);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_brcb_decode_stream(per_stream_t *s, void *ptr) {
    cms_brcb_t *pdu = (cms_brcb_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: resvTms, owner) — X.691 §22 */
    bool opt_present[2] = {false, false};
    err = (int)per_decode_optional_bitmap(s, opt_present, 2);
    if (err) return err;
    if (pdu->resvTms_present)
        pdu->resvTms_present->value = opt_present[0] ? 1 : 0;
    if (pdu->owner_present)
        pdu->owner_present->value = opt_present[1] ? 1 : 0;

    /* 1. rptID */
    if (!pdu->rptID) return CMS_ERR;
    err = cms_visible_string_decode_stream(s, pdu->rptID, CMS_BRCB_RPT_ID_MAX_LEN);
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

    /* 11. purgeBuf */
    if (!pdu->purgeBuf) return CMS_ERR;
    err = cms_boolean_decode_stream(s, pdu->purgeBuf);
    if (err) return err;

    /* 12. entryID */
    if (!pdu->entryID) return CMS_ERR;
    err = cms_entry_id_decode_stream(s, pdu->entryID);
    if (err) return err;

    /* 13. timeOfEntry */
    if (!pdu->timeOfEntry) return CMS_ERR;
    err = cms_entry_time_decode_stream(s, pdu->timeOfEntry);
    if (err) return err;

    /* 14. resvTms — INT16 OPTIONAL (bitmap[0]) */
    if (opt_present[0] && pdu->resvTms) {
        err = cms_int16_decode_stream(s, pdu->resvTms);
        if (err) return err;
    }

    /* 15. owner — OCTET STRING (SIZE(0..64)) OPTIONAL (bitmap[1]) */
    if (opt_present[1] && pdu->owner) {
        err = cms_octet_string_decode_stream(s, pdu->owner, CMS_BRCB_OWNER_MAX_LEN);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_brcb_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_brcb_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_brcb_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_brcb_decode_stream(&s, ptr);
}
