#include "data/block/cms_lcb_opt_flds.h"
#include "data/string/cms_bitutil.h"

int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr) {
    int val = ((const cms_lcb_opt_flds_t *) ptr)->value ? ((const cms_lcb_opt_flds_t *) ptr)->value->value : 0;
    uint8_t byte = 0;
    pack_bit(&byte, 0, val);
    return cms_bit_string_fixed_encode_stream(s, &byte, 1);
}

int cms_lcb_opt_flds_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 1);
    if (err)
        return CMS_ERR;
    if (ptr && ((cms_lcb_opt_flds_t *) ptr)->value)
        ((cms_lcb_opt_flds_t *) ptr)->value->value = unpack_bit(byte, 0);
    return CMS_OK;
}

int cms_lcb_opt_flds_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err)
        return (int) err;
    int rc = cms_lcb_opt_flds_encode_stream(&s, ptr);
    if (rc) {
        per_stream_free(&s);
        return rc;
    }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_lcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    return cms_lcb_opt_flds_decode_stream(&s, ptr);
}
