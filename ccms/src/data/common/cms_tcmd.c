#include "data/common/cms_tcmd.h"
#include "data/string/cms_bit_string.h"

int cms_tcmd_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = (uint8_t)(((const cms_tcmd_t*)ptr)->value);
    return cms_bit_string_fixed_encode_stream(s, &byte, 2);
}

int cms_tcmd_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 2);
    if (err) return CMS_ERR;
    ((cms_tcmd_t*)ptr)->value = (int)byte;
    return CMS_OK;
}

int cms_tcmd_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_tcmd_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_tcmd_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_tcmd_decode_stream(&s, ptr);
}
