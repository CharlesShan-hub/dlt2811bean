#include "data/scalar/cms_float32.h"
#include "data/string/cms_octet_string.h"

int cms_float32_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_octet_string_fixed_encode_stream(s, (const uint8_t*)ptr, 4);
}

int cms_float32_decode_stream(per_stream_t *s, void *ptr) {
    return cms_octet_string_fixed_decode_stream(s, (uint8_t*)ptr, 4);
}

int cms_float32_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_float32_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_float32_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_float32_decode_stream(&s, ptr);
}
