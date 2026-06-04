#include "data/block/cms_msvcb.h"
#include "per/cms_sequence.h"

int cms_msvcb_encode_stream(per_stream_t *s, const cms_msvcb_t *v)
{
    cms_boolean_encode_stream(s, &v->svEna);
    cms_visible_string_fixed_encode_stream(s, &v->msvID);
    cms_object_reference_encode_stream(s, &v->datSet);
    cms_int32u_encode_stream(s, &v->confRev);
    per_encode_optional(s, v->smpMod_present);
    if (v->smpMod_present)
        cms_smp_mod_encode_stream(s, v->smpMod);
    cms_int16u_encode_stream(s, &v->smpRate);
    cms_msvcb_opt_flds_encode_stream(s, v->optFlds);
    per_encode_optional(s, v->dstAddress_present);
    if (v->dstAddress_present)
        cms_phy_com_addr_encode_stream(s, &v->dstAddress);
    return CMS_OK;
}

int cms_msvcb_decode_stream(per_stream_t *s, cms_msvcb_t *v)
{
    cms_boolean_decode_stream(s, &v->svEna);
    cms_visible_string_fixed_decode_stream(s, &v->msvID);
    cms_object_reference_decode_stream(s, &v->datSet);
    cms_int32u_decode_stream(s, &v->confRev);
    v->smpMod_present = per_decode_optional(s);
    if (v->smpMod_present)
        cms_smp_mod_decode_stream(s, &v->smpMod);
    cms_int16u_decode_stream(s, &v->smpRate);
    cms_msvcb_opt_flds_decode_stream(s, v->optFlds);
    v->dstAddress_present = per_decode_optional(s);
    if (v->dstAddress_present)
        cms_phy_com_addr_decode_stream(s, &v->dstAddress);
    return CMS_OK;
}

CMS_EXPORT int cms_msvcb_encode(const cms_msvcb_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); cms_msvcb_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_msvcb_decode(const uint8_t *b, int l, cms_msvcb_t *v)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_msvcb_decode_stream(&r, v); }
