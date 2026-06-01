#include "data/basic/cms_boolean.h"
#include "per/cms_boolean.h"
#include "per/cms_stream.h"

/* ---- internal stream version ---- */
int cms_boolean_encode_stream(per_stream_t *s, int value)
{
    per_encode_boolean(s, value ? 1 : 0);
    return CMS_OK;
}

int cms_boolean_decode_stream(per_stream_t *s, int *value)
{
    bool b;
    per_decode_boolean(s, &b);
    *value = b ? 1 : 0;
    return CMS_OK;
}

/* ---- public buffer version ---- */
CMS_EXPORT int cms_boolean_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    cms_boolean_encode_stream(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_boolean_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_boolean_decode_stream(&r, value);
    return CMS_OK;
}
