#include "data/common/cms_object_name.h"

/* ---- internal stream version ---- */

int cms_object_name_encode_stream(per_stream_t *s, const cms_visible_string_var_t *v)
    { per_encode_visible_string(s, v->value, 64); return CMS_OK; }
int cms_object_name_decode_stream(per_stream_t *s, cms_visible_string_var_t *v)
    { per_decode_visible_string(s, v->value, 64); return CMS_OK; }

int cms_object_reference_encode_stream(per_stream_t *s, const cms_visible_string_var_t *v)
    { per_encode_visible_string(s, v->value, 129); return CMS_OK; }
int cms_object_reference_decode_stream(per_stream_t *s, cms_visible_string_var_t *v)
    { per_decode_visible_string(s, v->value, 129); return CMS_OK; }

int cms_sub_reference_encode_stream(per_stream_t *s, const cms_visible_string_var_t *v)
    { per_encode_visible_string(s, v->value, 129); return CMS_OK; }
int cms_sub_reference_decode_stream(per_stream_t *s, cms_visible_string_var_t *v)
    { per_decode_visible_string(s, v->value, 129); return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_object_name_encode(const cms_visible_string_var_t *v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_object_name_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_object_name_decode(const uint8_t *b, int l, cms_visible_string_var_t *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_object_name_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_object_reference_encode(const cms_visible_string_var_t *v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_object_reference_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_object_reference_decode(const uint8_t *b, int l, cms_visible_string_var_t *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_object_reference_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_sub_reference_encode(const cms_visible_string_var_t *v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_sub_reference_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_sub_reference_decode(const uint8_t *b, int l, cms_visible_string_var_t *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_sub_reference_decode_stream(&r, v); return CMS_OK; }
