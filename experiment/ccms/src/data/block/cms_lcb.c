#include "data/block/cms_lcb.h"

/* ---- internal stream version ---- */

int cms_lcb_encode_stream(per_stream_t *s, const cms_lcb_t *v)
{
    cms_boolean_encode_stream(s, v->logEna);
    cms_object_reference_encode_stream(s, v->datSet);
    cms_trigger_conditions_encode_stream(s, v->trgOps);
    cms_int32u_encode_stream(s, v->intgPd);
    cms_object_reference_encode_stream(s, v->logRef);
    /* optFlds OPTIONAL */
    per_encode_optional(s, v->optFlds_present);
    if (v->optFlds_present)
        cms_lcb_opt_flds_encode_stream(s, v->optFlds);
    /* bufTm OPTIONAL */
    per_encode_optional(s, v->bufTm_present);
    if (v->bufTm_present)
        cms_int32u_encode_stream(s, v->bufTm);
    return CMS_OK;
}

int cms_lcb_decode_stream(per_stream_t *s, cms_lcb_t *v)
{
    cms_boolean_decode_stream(s, &v->logEna);
    cms_object_reference_decode_stream(s, v->datSet);
    cms_trigger_conditions_decode_stream(s, v->trgOps);
    cms_int32u_decode_stream(s, &v->intgPd);
    cms_object_reference_decode_stream(s, v->logRef);
    /* optFlds OPTIONAL */
    v->optFlds_present = per_decode_optional(s);
    if (v->optFlds_present)
        cms_lcb_opt_flds_decode_stream(s, v->optFlds);
    /* bufTm OPTIONAL */
    v->bufTm_present = per_decode_optional(s);
    if (v->bufTm_present)
        cms_int32u_decode_stream(s, &v->bufTm);
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_lcb_encode(const cms_lcb_t *v, uint8_t *b, int *l)
{
    per_stream_t w;
    per_stream_init_write(&w, b, (size_t)*l);
    cms_lcb_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_lcb_decode(const uint8_t *b, int l, cms_lcb_t *v)
{
    per_stream_t r;
    per_stream_init_read(&r, b, (size_t)l);
    cms_lcb_decode_stream(&r, v);
    return CMS_OK;
}
