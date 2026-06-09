#include "data/block/cms_smp_mod.h"
#include "data/scalar/cms_int8.h"

int cms_smp_mod_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_int8_encode_stream(s, ptr);
}

int cms_smp_mod_decode_stream(per_stream_t *s, void *ptr) {
    return cms_int8_decode_stream(s, ptr);
}

int cms_smp_mod_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    return cms_int8_encode(ptr, out_buf, out_len);
}

int cms_smp_mod_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_int8_decode(ptr, in_buf, in_len);
}
