#include "data/scalar/cms_int32u.h"

int cms_int32u_encode_stream(per_stream_t *s, const void *ptr) {
    uint32_t val = *(const uint32_t*)ptr;
    return (int)per_encode_constrained_int(s, val, 0, 4294967295U);
}

int cms_int32u_decode_stream(per_stream_t *s, void *ptr) {
    int64_t val;
    per_error_t err = per_decode_constrained_int(s, &val, 0, 4294967295U);
    if (err) return CMS_ERR;
    *(uint32_t*)ptr = (uint32_t)val;
    return CMS_OK;
}

int cms_int32u_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_int32u_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_int32u_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_int32u_decode_stream(&s, ptr);
}
