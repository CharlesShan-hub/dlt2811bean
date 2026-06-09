#include "data/common/cms_dbpos.h"

int cms_dbpos_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_int32_encode_stream(s, ptr);
}
int cms_dbpos_decode_stream(per_stream_t *s, void *ptr) {
    return cms_int32_decode_stream(s, ptr);
}
int cms_dbpos_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    return cms_int32_encode(ptr, out_buf, out_len);
}
int cms_dbpos_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_int32_decode(ptr, in_buf, in_len);
}
