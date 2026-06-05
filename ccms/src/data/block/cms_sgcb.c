#include "data/block/cms_sgcb.h"

int cms_sgcb_encode_stream(per_stream_t *s, const cms_sgcb_t *v){
    int rc;
    /* preamble: presence bit for OPTIONAL resvTms */
    rc = cms_boolean_encode_stream(s, &v->resvTms_present);
    if (rc) return rc;
    /* mandatory fields */
    rc = cms_int8u_encode_stream(s, &v->numOfSG);
    if (rc) return rc;
    rc = cms_int8u_encode_stream(s, &v->actSG);
    if (rc) return rc;
    rc = cms_int8u_encode_stream(s, &v->editSG);
    if (rc) return rc;
    rc = cms_time_stamp_encode_stream(s, &v->tActEdt);
    if (rc) return rc;
    /* optional fields */
    if (v->resvTms_present.value){
        rc = cms_int16u_encode_stream(s, &v->resvTms);
        if (rc) return rc;
    }
    return CMS_OK;
}

int cms_sgcb_decode_stream(per_stream_t *s, cms_sgcb_t *v){
    int rc;
    /* preamble: presence bit for OPTIONAL resvTms */
    rc = cms_boolean_decode_stream(s, &v->resvTms_present);
    if (rc) return rc;
    /* mandatory fields */
    rc = cms_int8u_decode_stream(s, &v->numOfSG);
    if (rc) return rc;
    rc = cms_int8u_decode_stream(s, &v->actSG);
    if (rc) return rc;
    rc = cms_int8u_decode_stream(s, &v->editSG);
    if (rc) return rc;
    rc = cms_time_stamp_decode_stream(s, &v->tActEdt);
    if (rc) return rc;
    /* optional fields */
    if (v->resvTms_present.value){
        rc = cms_int16u_decode_stream(s, &v->resvTms);
        if (rc) return rc;
    }
    return CMS_OK;
}

CMS_EXPORT int cms_sgcb_encode(const cms_sgcb_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_sgcb_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_sgcb_decode(cms_sgcb_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_sgcb_decode_stream(&r, v);
}
