#include "data/string/cms_utf8_string.h"
#include <string.h>

#define ARRAY_PTR(v)      (*(const uint8_t *const*)(v))
#define ARRAY_LEN(v)      (*((const int32_t*)((const uint8_t*)(v) + 8)))
#define ARRAY_PTR_MUT(v)  (*(uint8_t**)(v))
#define ARRAY_LEN_PTR(v)  ((int32_t*)((uint8_t*)(v) + 8))

int cms_utf8_string_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_len) {
    const uint8_t *vptr = ptr ? ARRAY_PTR(ptr) : NULL;
    if (!vptr) return CMS_ERR;
    return (int)per_encode_utf8_string(s, vptr, max_len);
}

int cms_utf8_string_decode_stream(per_stream_t *s, void *ptr, uint32_t max_len) {
    uint8_t tmp[256];
    uint8_t *target = ptr ? ARRAY_PTR_MUT(ptr) : tmp;
    per_error_t err = per_decode_utf8_string(s, target, max_len);
    if (err) return CMS_ERR;
    if (ptr) *(ARRAY_LEN_PTR(ptr)) = (int32_t)strlen((const char*)target);
    return CMS_OK;
}

int cms_utf8_string_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_utf8_string_encode_stream(&s, ptr, 1024);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_utf8_string_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_utf8_string_decode_stream(&s, ptr, 65535);
}
