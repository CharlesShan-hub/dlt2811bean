#include "data/scalar/cms_int16.h"
#include "per/cms_integer.h"

int cms_int16_encode_stream(per_stream_t *s, const void *ptr) {
    int16_t val = *(const int16_t*)ptr;
    return (int)per_encode_constrained_int(s, val, -32768, 32767);
}

int cms_int16_decode_stream(per_stream_t *s, void *ptr) {
    int64_t val;
    per_error_t err = per_decode_constrained_int(s, &val, -32768, 32767);
    if (err) return CMS_ERR;
    *(int16_t*)ptr = (int16_t)val;
    return CMS_OK;
}

int cms_int16_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_int16_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_int16_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_int16_decode_stream(&s, ptr);
}
