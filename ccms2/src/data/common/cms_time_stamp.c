#include "data/common/cms_time_stamp.h"

int cms_time_stamp_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_utc_time_encode_stream(s, ptr);
}
int cms_time_stamp_decode_stream(per_stream_t *s, void *ptr) {
    return cms_utc_time_decode_stream(s, ptr);
}
int cms_time_stamp_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    return cms_utc_time_encode(ptr, out_buf, out_len);
}
int cms_time_stamp_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_utc_time_decode(ptr, in_buf, in_len);
}
