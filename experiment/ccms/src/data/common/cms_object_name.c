#include "data/common/cms_object_name.h"
#include "per/cms_string.h"
#include <string.h>

int cms_object_name_encode_stream(per_stream_t *s, const char *value)
    { per_encode_visible_string(s, value, 64); return CMS_OK; }
int cms_object_name_decode_stream(per_stream_t *s, char *value)
    { per_decode_visible_string(s, value, 64); return CMS_OK; }

int cms_object_reference_encode_stream(per_stream_t *s, const char *value)
    { per_encode_visible_string(s, value, 129); return CMS_OK; }
int cms_object_reference_decode_stream(per_stream_t *s, char *value)
    { per_decode_visible_string(s, value, 129); return CMS_OK; }

int cms_sub_reference_encode_stream(per_stream_t *s, const char *value)
    { per_encode_visible_string(s, value, 129); return CMS_OK; }
int cms_sub_reference_decode_stream(per_stream_t *s, char *value)
    { per_decode_visible_string(s, value, 129); return CMS_OK; }

CMS_EXPORT int cms_object_name_encode(const char *v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_object_name_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_object_name_decode(const uint8_t *b, int l, char *v, int *c)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_object_name_decode_stream(&r, v); *c = (int)strlen(v); return CMS_OK; }
CMS_EXPORT int cms_object_reference_encode(const char *v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_object_reference_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_object_reference_decode(const uint8_t *b, int l, char *v, int *c)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_object_reference_decode_stream(&r, v); *c = (int)strlen(v); return CMS_OK; }
CMS_EXPORT int cms_sub_reference_encode(const char *v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_sub_reference_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_sub_reference_decode(const uint8_t *b, int l, char *v, int *c)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_sub_reference_decode_stream(&r, v); *c = (int)strlen(v); return CMS_OK; }
