#include "data/block/cms_lcb_opt_flds.h"

int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = ((const cms_lcb_opt_flds_t*)ptr)->value ? ((const cms_lcb_opt_flds_t*)ptr)->value->value : 0;
    return cms_bit_string_fixed_encode_stream(s, &byte, 1);
}

int cms_lcb_opt_flds_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 1);
    if (err) return CMS_ERR;
    cms_lcb_opt_flds_t *q = (cms_lcb_opt_flds_t*)ptr;
    if (q->value) q->value->value = byte & 1;
    return CMS_OK;
}

int cms_lcb_opt_flds_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_lcb_opt_flds_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_lcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_lcb_opt_flds_decode_stream(&s, ptr);
}
