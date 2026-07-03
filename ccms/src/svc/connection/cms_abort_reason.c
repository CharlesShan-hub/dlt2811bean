#include "svc/connection/cms_abort_reason.h"
#include "data/scalar/cms_int32.h"

int cms_abort_reason_encode_stream(per_stream_t *s, const cms_abort_reason_t *v) {
    return cms_int32_encode_stream(s, v);
}

int cms_abort_reason_decode_stream(per_stream_t *s, void *ptr) {
    return cms_int32_decode_stream(s, ptr);
}

int cms_abort_reason_encode(const cms_abort_reason_t *v, uint8_t **out_buf, size_t *out_len) {
    return cms_int32_encode(v, out_buf, out_len);
}

int cms_abort_reason_decode(cms_abort_reason_t *v, const uint8_t *in_buf, int in_len) {
    return cms_int32_decode(v, in_buf, in_len);
}
