#include "data/common/cms_object_name.h"

int cms_object_name_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_visible_string_encode_stream(s, ptr, CMS_OBJECT_NAME_MAX_LEN);
}
int cms_object_name_decode_stream(per_stream_t *s, void *ptr) {
    return cms_visible_string_decode_stream(s, ptr, CMS_OBJECT_NAME_MAX_LEN);
}
int cms_object_name_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    return cms_visible_string_encode(ptr, out_buf, out_len);
}
int cms_object_name_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_visible_string_decode(ptr, in_buf, in_len);
}
