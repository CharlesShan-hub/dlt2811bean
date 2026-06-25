#include "data/common/cms_entry_time.h"

int cms_entry_time_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_binary_time_encode_stream(s, ptr);
}
int cms_entry_time_decode_stream(per_stream_t *s, void *ptr) {
    return cms_binary_time_decode_stream(s, ptr);
}
int cms_entry_time_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    return cms_binary_time_encode(ptr, out_buf, out_len);
}
int cms_entry_time_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_binary_time_decode(ptr, in_buf, in_len);
}
