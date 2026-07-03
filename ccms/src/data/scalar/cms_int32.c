#include "data/scalar/cms_int32.h"
#include "per/cms_integer.h"

int cms_int32_encode_stream(per_stream_t *s, const void *ptr) {
    int32_t val = *(const int32_t*)ptr;
    return (int)per_encode_constrained_int(s, val, -2147483648LL, 2147483647LL);
}

int cms_int32_decode_stream(per_stream_t *s, void *ptr) {
    int64_t val;
    per_error_t err = per_decode_constrained_int(s, &val, -2147483648LL, 2147483647LL);
    if (err) return CMS_ERR;
    if (ptr) *(int32_t*)ptr = (int32_t)val;
    return CMS_OK;
}

int cms_int32_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_int32_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_int32_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_int32_decode_stream(&s, ptr);
}
