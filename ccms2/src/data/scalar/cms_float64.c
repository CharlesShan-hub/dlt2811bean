#include "data/scalar/cms_float64.h"

int cms_float64_encode_stream(per_stream_t *s, const void *ptr) {
    per_stream_align(s);
    return (int)per_stream_write_bytes(s, (const uint8_t*)ptr, 8);
}

int cms_float64_decode_stream(per_stream_t *s, void *ptr) {
    per_stream_align(s);
    return (int)per_stream_read_bytes(s, (uint8_t*)ptr, 8);
}

int cms_float64_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_float64_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_float64_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_float64_decode_stream(&s, ptr);
}
