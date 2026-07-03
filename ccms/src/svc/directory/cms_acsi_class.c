#include "svc/directory/cms_acsi_class.h"
#include "data/scalar/cms_int32.h"

int cms_acsi_class_encode_stream(per_stream_t *s, const cms_acsi_class_t *v) {
    return cms_int32_encode_stream(s, v);
}

int cms_acsi_class_decode_stream(per_stream_t *s, void *ptr) {
    return cms_int32_decode_stream(s, ptr);
}

int cms_acsi_class_encode(const cms_acsi_class_t *v, uint8_t **out_buf, size_t *out_len) {
    return cms_int32_encode(v, out_buf, out_len);
}

int cms_acsi_class_decode(cms_acsi_class_t *v, const uint8_t *in_buf, int in_len) {
    return cms_int32_decode(v, in_buf, in_len);
}
