#include "data/block/cms_brcb.h"

int cms_brcb_encode_stream(per_stream_t *s, const cms_brcb_t *v){
    int rc;
    /* preamble: presence bits for OPTIONAL fields */
    rc = cms_boolean_encode_stream(s, &v->resvTms_is_present);
    if (rc) return rc;
    rc = cms_boolean_encode_stream(s, &v->owner_is_present);
    if (rc) return rc;
    /* mandatory fields */
    cms_visible_string_fixed_t _rptID = { v->rptID.value, CMS_RPT_ID_MAX_LEN };
    rc = cms_visible_string_fixed_encode_stream(s, &_rptID);
    if (rc) return rc;
    rc = cms_boolean_encode_stream(s, &v->rptEna);
    if (rc) return rc;
    rc = cms_object_reference_encode_stream(s, &v->datSet);
    if (rc) return rc;
    rc = cms_int32u_encode_stream(s, &v->confRev);
    if (rc) return rc;
    rc = cms_rcb_opt_flds_encode_stream(s, &v->optFlds);
    if (rc) return rc;
    rc = cms_int32u_encode_stream(s, &v->bufTm);
    if (rc) return rc;
    rc = cms_int16u_encode_stream(s, &v->sqNum);
    if (rc) return rc;
    rc = cms_trigger_conditions_encode_stream(s, &v->trgOps);
    if (rc) return rc;
    rc = cms_int32u_encode_stream(s, &v->intgPd);
    if (rc) return rc;
    rc = cms_boolean_encode_stream(s, &v->gi);
    if (rc) return rc;
    rc = cms_boolean_encode_stream(s, &v->purgeBuf);
    if (rc) return rc;
    rc = cms_entry_id_encode_stream(s, &v->entryID);
    if (rc) return rc;
    rc = cms_binary_time_encode_stream(s, &v->timeOfEntry);
    if (rc) return rc;
    if (v->resvTms_is_present.value){
        rc = cms_int16_encode_stream(s, &v->resvTms);
        if (rc) return rc;
    }
    if (v->owner_is_present.value){
        cms_octet_string_var_t _owner = { v->owner.value, v->owner.len, CMS_OWNER_MAX_LEN };
        rc = cms_octet_string_var_encode_stream(s, &_owner);
        if (rc) return rc;
    }
    return CMS_OK;
}

int cms_brcb_decode_stream(per_stream_t *s, cms_brcb_t *v){
    int rc;
    /* preamble: presence bits for OPTIONAL fields */
    rc = cms_boolean_decode_stream(s, &v->resvTms_is_present);
    if (rc) return rc;
    rc = cms_boolean_decode_stream(s, &v->owner_is_present);
    if (rc) return rc;
    /* fields */
    cms_visible_string_fixed_t _rptID = { v->rptID.value, CMS_RPT_ID_MAX_LEN };
    rc = cms_visible_string_fixed_decode_stream(s, &_rptID);
    if (rc) return rc;
    rc = cms_boolean_decode_stream(s, &v->rptEna);
    if (rc) return rc;
    rc = cms_object_reference_decode_stream(s, &v->datSet);
    if (rc) return rc;
    rc = cms_int32u_decode_stream(s, &v->confRev);
    if (rc) return rc;
    rc = cms_rcb_opt_flds_decode_stream(s, &v->optFlds);
    if (rc) return rc;
    rc = cms_int32u_decode_stream(s, &v->bufTm);
    if (rc) return rc;
    rc = cms_int16u_decode_stream(s, &v->sqNum);
    if (rc) return rc;
    rc = cms_trigger_conditions_decode_stream(s, &v->trgOps);
    if (rc) return rc;
    rc = cms_int32u_decode_stream(s, &v->intgPd);
    if (rc) return rc;
    rc = cms_boolean_decode_stream(s, &v->gi);
    if (rc) return rc;
    rc = cms_boolean_decode_stream(s, &v->purgeBuf);
    if (rc) return rc;
    rc = cms_entry_id_decode_stream(s, &v->entryID);
    if (rc) return rc;
    rc = cms_binary_time_decode_stream(s, &v->timeOfEntry);
    if (rc) return rc;
    if (v->resvTms_is_present.value){
        rc = cms_int16_decode_stream(s, &v->resvTms);
        if (rc) return rc;
    }
    if (v->owner_is_present.value){
        cms_octet_string_var_t _owner = { v->owner.value, 0, CMS_OWNER_MAX_LEN };
        rc = cms_octet_string_var_decode_stream(s, &_owner);
        if (rc) return rc;
        v->owner.len = _owner.len;
    }
    return CMS_OK;
}

CMS_EXPORT int cms_brcb_encode(const cms_brcb_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_brcb_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_brcb_decode(cms_brcb_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_brcb_decode_stream(&r, v);
}
