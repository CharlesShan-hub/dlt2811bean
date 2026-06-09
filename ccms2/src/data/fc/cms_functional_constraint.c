#include "data/fc/cms_functional_constraint.h"

int cms_functional_constraint_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_visible_string_encode_stream(s, ptr, CMS_FUNCTIONAL_CONSTRAINT_LEN);
}

int cms_functional_constraint_decode_stream(per_stream_t *s, void *ptr) {
    return cms_visible_string_decode_stream(s, ptr, CMS_FUNCTIONAL_CONSTRAINT_LEN);
}

int cms_functional_constraint_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    return cms_visible_string_encode(ptr, out_buf, out_len);
}

int cms_functional_constraint_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_visible_string_decode(ptr, in_buf, in_len);
}
