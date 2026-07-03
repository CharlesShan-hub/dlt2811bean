#include "data/string/cms_visible_string.h"
#include <string.h>

/* Extract uint8_t* value and int32_t len from a cms_uint8_array_t* */
#define ARRAY_PTR(v)    (*(const uint8_t *const*)(v))
#define ARRAY_LEN(v)    (*((const int32_t*)((const uint8_t*)(v) + 8)))
#define ARRAY_PTR_MUT(v)  (*(uint8_t**)(v))

int cms_visible_string_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_len) {
    const uint8_t *vptr = ptr ? ARRAY_PTR(ptr) : NULL;
    if (!vptr) return CMS_ERR;
    return (int)per_encode_visible_string(s, vptr, max_len);
}

int cms_visible_string_decode_stream(per_stream_t *s, void *ptr, uint32_t max_len) {
    uint8_t tmp[256];
    uint8_t *target = ptr ? ARRAY_PTR_MUT(ptr) : tmp;
    per_error_t err = per_decode_visible_string(s, target, max_len);
    if (err) return CMS_ERR;
    if (ptr) *(int32_t*)((uint8_t*)ptr + 8) = (int32_t)strlen((const char*)target);
    return CMS_OK;
}

/* Fixed-size: SIZE(N), no length prefix */
int cms_visible_string_encode_stream_fixed(per_stream_t *s, const void *ptr, uint32_t fixed_len) {
    const uint8_t *vptr = ptr ? ARRAY_PTR(ptr) : NULL;
    if (!vptr) return CMS_ERR;
    return (int)per_encode_visible_string_fixed(s, vptr, fixed_len);
}

int cms_visible_string_decode_stream_fixed(per_stream_t *s, void *ptr, uint32_t fixed_len) {
    uint8_t tmp[256];
    uint8_t *target = ptr ? ARRAY_PTR_MUT(ptr) : tmp;
    per_error_t err = per_decode_visible_string_fixed(s, target, fixed_len);
    if (err) return CMS_ERR;
    if (ptr) *(int32_t*)((uint8_t*)ptr + 8) = (int32_t)fixed_len;
    return CMS_OK;
}

int cms_visible_string_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_visible_string_encode_stream(&s, ptr, 129);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_visible_string_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_visible_string_decode_stream(&s, ptr, 129);
}
