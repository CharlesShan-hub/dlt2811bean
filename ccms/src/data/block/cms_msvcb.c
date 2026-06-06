#include "data/block/cms_msvcb.h"
#include "data/basic/cms_string.h"

int cms_msvcb_encode_stream(per_stream_t *s, const cms_msvcb_t *v){
    int rc;
    /* preamble: presence bits for OPTIONAL fields */
    rc = cms_boolean_encode_stream(s, &v->smpMod_present);
    if (rc) return rc;
    rc = cms_boolean_encode_stream(s, &v->dstAddress_present);
    if (rc) return rc;
    /* fields */
    rc = cms_boolean_encode_stream(s, &v->svEna);
    if (rc) return rc;
    cms_visible_string_fixed_t _msvID = { v->msvID.value, CMS_MSV_ID_MAX_LEN };
    rc = cms_visible_string_fixed_encode_stream(s, &_msvID);
    if (rc) return rc;
    rc = cms_object_reference_encode_stream(s, &v->datSet);
    if (rc) return rc;
    rc = cms_int32u_encode_stream(s, &v->confRev);
    if (rc) return rc;
    if (v->smpMod_present.value){
        rc = cms_smp_mod_encode_stream(s, &v->smpMod);
        if (rc) return rc;
    }
    rc = cms_int16u_encode_stream(s, &v->smpRate);
    if (rc) return rc;
    rc = cms_msvcb_opt_flds_encode_stream(s, &v->optFlds);
    if (rc) return rc;
    if (v->dstAddress_present.value){
        rc = cms_phy_com_addr_encode_stream(s, &v->dstAddress);
        if (rc) return rc;
    }
    return CMS_OK;
}

int cms_msvcb_decode_stream(per_stream_t *s, cms_msvcb_t *v){
    int rc;
    /* preamble: presence bits for OPTIONAL fields */
    rc = cms_boolean_decode_stream(s, &v->smpMod_present);
    if (rc) return rc;
    rc = cms_boolean_decode_stream(s, &v->dstAddress_present);
    if (rc) return rc;
    /* mandatory fields */
    rc = cms_boolean_decode_stream(s, &v->svEna);
    if (rc) return rc;
    cms_visible_string_fixed_t _msvID = { v->msvID.value, CMS_MSV_ID_MAX_LEN };
    rc = cms_visible_string_fixed_decode_stream(s, &_msvID);
    if (rc) return rc;
    v->msvID.len = CMS_MSV_ID_MAX_LEN;
    rc = cms_object_reference_decode_stream(s, &v->datSet);
    if (rc) return rc;
    rc = cms_int32u_decode_stream(s, &v->confRev);
    if (rc) return rc;
    /* optional smpMod */
    if (v->smpMod_present.value){
        rc = cms_smp_mod_decode_stream(s, &v->smpMod);
        if (rc) return rc;
    }
    rc = cms_int16u_decode_stream(s, &v->smpRate);
    if (rc) return rc;
    rc = cms_msvcb_opt_flds_decode_stream(s, &v->optFlds);
    if (rc) return rc;
    /* optional dstAddress */
    if (v->dstAddress_present.value){
        rc = cms_phy_com_addr_decode_stream(s, &v->dstAddress);
        if (rc) return rc;
    }
    return CMS_OK;
}

CMS_EXPORT int cms_msvcb_encode(const cms_msvcb_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_msvcb_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_msvcb_decode(cms_msvcb_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_msvcb_decode_stream(&r, v);
}
