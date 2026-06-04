#include "data/block/cms_sgcb.h"
#include "per/cms_sequence.h"

int cms_sgcb_encode_stream(per_stream_t *s, const cms_sgcb_t *v)
{
    cms_int8u_encode_stream(s, &v->numOfSG);
    cms_int8u_encode_stream(s, &v->actSG);
    cms_int8u_encode_stream(s, &v->editSG);
    cms_time_stamp_encode_stream(s, &v->tActEdt);
    per_encode_optional(s, v->resvTms_present);
    if (v->resvTms_present)
        cms_int16u_encode_stream(s, &v->resvTms);
    return CMS_OK;
}

int cms_sgcb_decode_stream(per_stream_t *s, cms_sgcb_t *v)
{
    cms_int8u_decode_stream(s, &v->numOfSG);
    cms_int8u_decode_stream(s, &v->actSG);
    cms_int8u_decode_stream(s, &v->editSG);
    cms_time_stamp_decode_stream(s, &v->tActEdt);
    v->resvTms_present = per_decode_optional(s);
    if (v->resvTms_present)
        cms_int16u_decode_stream(s, &v->resvTms);
    return CMS_OK;
}

CMS_EXPORT int cms_sgcb_encode(const cms_sgcb_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); cms_sgcb_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_sgcb_decode(const uint8_t *b, int l, cms_sgcb_t *v)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_sgcb_decode_stream(&r, v); }
