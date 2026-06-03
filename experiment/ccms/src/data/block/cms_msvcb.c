#include "data/block/cms_msvcb.h"

/* ---- internal stream version ---- */

int cms_msvcb_encode_stream(per_stream_t *s, const cms_msvcb_t *v)
{
    cms_boolean_encode_stream(s, v->svEna);
    cms_visible_string_encode_stream(s, v->msvID, 129);
    cms_object_reference_encode_stream(s, v->datSet);
    cms_int32u_encode_stream(s, v->confRev);
    /* smpMod OPTIONAL */
    per_encode_optional(s, v->smpMod_present);
    if (v->smpMod_present)
        cms_smp_mod_encode_stream(s, v->smpMod);
    cms_int16u_encode_stream(s, v->smpRate);
    cms_msvcb_opt_flds_encode_stream(s, v->optFlds);
    /* dstAddress OPTIONAL */
    per_encode_optional(s, v->dstAddress_present);
    if (v->dstAddress_present)
        cms_phy_com_addr_encode_stream(s, v->dstAddr, v->dstPriority, v->dstVid, v->dstAppId);
    return CMS_OK;
}

int cms_msvcb_decode_stream(per_stream_t *s, cms_msvcb_t *v)
{
    cms_boolean_decode_stream(s, &v->svEna);
    cms_visible_string_decode_stream(s, v->msvID, 129);
    cms_object_reference_decode_stream(s, v->datSet);
    cms_int32u_decode_stream(s, &v->confRev);
    /* smpMod OPTIONAL */
    v->smpMod_present = per_decode_optional(s);
    if (v->smpMod_present)
        cms_smp_mod_decode_stream(s, &v->smpMod);
    cms_int16u_decode_stream(s, &v->smpRate);
    cms_msvcb_opt_flds_decode_stream(s, v->optFlds);
    /* dstAddress OPTIONAL */
    v->dstAddress_present = per_decode_optional(s);
    if (v->dstAddress_present)
        cms_phy_com_addr_decode_stream(s, v->dstAddr, &v->dstPriority, &v->dstVid, &v->dstAppId);
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_msvcb_encode(const cms_msvcb_t *v, uint8_t *b, int *l)
{
    per_stream_t w;
    per_stream_init_write(&w, b, (size_t)*l);
    cms_msvcb_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_msvcb_decode(const uint8_t *b, int l, cms_msvcb_t *v)
{
    per_stream_t r;
    per_stream_init_read(&r, b, (size_t)l);
    cms_msvcb_decode_stream(&r, v);
    return CMS_OK;
}
