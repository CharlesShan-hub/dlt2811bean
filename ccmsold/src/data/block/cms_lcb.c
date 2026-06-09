#include "data/block/cms_lcb.h"

int cms_lcb_encode_stream(per_stream_t *s, const cms_lcb_t *v){
    int rc;
    /* preamble: presence bits for OPTIONAL fields */
    rc = cms_boolean_encode_stream(s, &v->optFlds_present);
    if (rc) return rc;
    rc = cms_boolean_encode_stream(s, &v->bufTm_present);
    if (rc) return rc;
    /* mandatory fields */
    rc = cms_boolean_encode_stream(s, &v->logEna);
    if (rc) return rc;
    rc = cms_object_reference_encode_stream(s, &v->datSet);
    if (rc) return rc;
    rc = cms_trigger_conditions_encode_stream(s, &v->trgOps);
    if (rc) return rc;
    rc = cms_int32u_encode_stream(s, &v->intgPd);
    if (rc) return rc;
    rc = cms_object_reference_encode_stream(s, &v->logRef);
    if (rc) return rc;
    /* optional fields */
    if (v->optFlds_present.value){
        rc = cms_lcb_opt_flds_encode_stream(s, &v->optFlds);
        if (rc) return rc;
    }
    if (v->bufTm_present.value){
        rc = cms_int32u_encode_stream(s, &v->bufTm);
        if (rc) return rc;
    }
    return CMS_OK;
}

int cms_lcb_decode_stream(per_stream_t *s, cms_lcb_t *v){
    int rc;
    /* preamble: presence bits for OPTIONAL fields */
    rc = cms_boolean_decode_stream(s, &v->optFlds_present);
    if (rc) return rc;
    rc = cms_boolean_decode_stream(s, &v->bufTm_present);
    if (rc) return rc;
    /* mandatory fields */
    rc = cms_boolean_decode_stream(s, &v->logEna);
    if (rc) return rc;
    rc = cms_object_reference_decode_stream(s, &v->datSet);
    if (rc) return rc;
    rc = cms_trigger_conditions_decode_stream(s, &v->trgOps);
    if (rc) return rc;
    rc = cms_int32u_decode_stream(s, &v->intgPd);
    if (rc) return rc;
    rc = cms_object_reference_decode_stream(s, &v->logRef);
    if (rc) return rc;
    /* optional fields */
    if (v->optFlds_present.value){
        rc = cms_lcb_opt_flds_decode_stream(s, &v->optFlds);
        if (rc) return rc;
    }
    if (v->bufTm_present.value){
        rc = cms_int32u_decode_stream(s, &v->bufTm);
        if (rc) return rc;
    }
    return CMS_OK;
}

CMS_EXPORT int cms_lcb_encode(const cms_lcb_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_lcb_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_lcb_decode(cms_lcb_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_lcb_decode_stream(&r, v);
}
