#include "data/block/cms_urcb.h"
#include "per/cms_sequence.h"

int cms_urcb_encode_stream(per_stream_t *s, const cms_urcb_t *v)
{
    cms_visible_string_fixed_encode_stream(s, &v->rptID);
    cms_boolean_encode_stream(s, &v->rptEna);
    cms_object_reference_encode_stream(s, &v->datSet);
    cms_int32u_encode_stream(s, &v->confRev);
    cms_rcb_opt_flds_encode_stream(s, v->optFlds);
    cms_int32u_encode_stream(s, &v->bufTm);
    cms_int16u_encode_stream(s, &v->sqNum);
    cms_trigger_conditions_encode_stream(s, v->trgOps);
    cms_int32u_encode_stream(s, &v->intgPd);
    cms_boolean_encode_stream(s, &v->gi);
    cms_boolean_encode_stream(s, &v->resv);
    per_encode_optional(s, v->owner_present);
    if (v->owner_present)
        cms_octet_string_var_encode_stream(s, &v->owner);
    return CMS_OK;
}

int cms_urcb_decode_stream(per_stream_t *s, cms_urcb_t *v)
{
    cms_visible_string_fixed_decode_stream(s, &v->rptID);
    cms_boolean_decode_stream(s, &v->rptEna);
    cms_object_reference_decode_stream(s, &v->datSet);
    cms_int32u_decode_stream(s, &v->confRev);
    cms_rcb_opt_flds_decode_stream(s, v->optFlds);
    cms_int32u_decode_stream(s, &v->bufTm);
    cms_int16u_decode_stream(s, &v->sqNum);
    cms_trigger_conditions_decode_stream(s, v->trgOps);
    cms_int32u_decode_stream(s, &v->intgPd);
    cms_boolean_decode_stream(s, &v->gi);
    cms_boolean_decode_stream(s, &v->resv);
    v->owner_present = per_decode_optional(s);
    if (v->owner_present)
        cms_octet_string_var_decode_stream(s, &v->owner);
    return CMS_OK;
}

CMS_EXPORT int cms_urcb_encode(const cms_urcb_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); cms_urcb_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_urcb_decode(const uint8_t *b, int l, cms_urcb_t *v)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_urcb_decode_stream(&r, v); }
