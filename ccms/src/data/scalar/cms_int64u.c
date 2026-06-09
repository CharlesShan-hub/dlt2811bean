#include "data/scalar/cms_int64u.h"
#include "per/cms_integer.h"

int cms_int64u_encode_stream(per_stream_t *s, const void *ptr) {
    uint64_t val = *(const uint64_t*)ptr;
    uint8_t content[8];
    int len = per_unsigned_to_bytes(val, content, 8);
    per_error_t err = per_encode_length(s, (uint32_t)len);
    if (err) return CMS_ERR;
    return (int)per_stream_write_bytes(s, content, len);
}

int cms_int64u_decode_stream(per_stream_t *s, void *ptr) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err) return CMS_ERR;
    if (len > 8) return CMS_ERR;
    uint8_t content[8];
    err = per_stream_read_bytes(s, content, len);
    if (err) return CMS_ERR;
    uint64_t val = 0;
    for (uint32_t i = 0; i < len; i++) val = (val << 8) | content[i];
    *(uint64_t*)ptr = val;
    return CMS_OK;
}

int cms_int64u_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_int64u_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_int64u_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_int64u_decode_stream(&s, ptr);
}
