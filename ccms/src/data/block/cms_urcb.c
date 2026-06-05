#include "data/block/cms_urcb.h"
#include "data/basic/cms_string.h"

int cms_urcb_encode_stream(per_stream_t *s, const cms_urcb_t *v){
    int rc;
    /* preamble: presence bit for OPTIONAL owner */
    rc = cms_boolean_encode_stream(s, &v->owner_present);
    if (rc) return rc;
    /* mandatory fields */
    cms_visible_string_fixed_t _rptID = { v->rptID.value, CMS_URCB_RPT_ID_MAX_LEN };
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
    rc = cms_boolean_encode_stream(s, &v->resv);
    if (rc) return rc;
    /* optional fields */
    if (v->owner_present.value){
        cms_octet_string_var_t _owner = { v->owner.value, v->owner.len, CMS_URCB_OWNER_MAX_LEN };
        rc = cms_octet_string_var_encode_stream(s, &_owner);
        if (rc) return rc;
    }
    return CMS_OK;
}

int cms_urcb_decode_stream(per_stream_t *s, cms_urcb_t *v){
    int rc;
    /* preamble: presence bit for OPTIONAL owner */
    rc = cms_boolean_decode_stream(s, &v->owner_present);
    if (rc) return rc;
    /* mandatory fields */
    cms_visible_string_fixed_t _rptID = { v->rptID.value, CMS_URCB_RPT_ID_MAX_LEN };
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
    rc = cms_boolean_decode_stream(s, &v->resv);
    if (rc) return rc;
    /* optional fields */
    if (v->owner_present.value){
        cms_octet_string_var_t _owner = { v->owner.value, 0, CMS_URCB_OWNER_MAX_LEN };
        rc = cms_octet_string_var_decode_stream(s, &_owner);
        if (rc) return rc;
        v->owner.len = _owner.len;
    }
    return CMS_OK;
}

CMS_EXPORT int cms_urcb_encode(const cms_urcb_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_urcb_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_urcb_decode(cms_urcb_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_urcb_decode_stream(&r, v);
}
