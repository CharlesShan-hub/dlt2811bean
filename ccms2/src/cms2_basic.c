#include "data/basic/cms2_basic.h"
#include <string.h>

int cms2_boolean_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int val = *(const int*)ptr;  /* cms2_boolean_t { int value; } */
    /* Boolean is constrained int (0..1), 1 bit */
    per_error_t err = per_encode_constrained_int(&s, val, 0, 1);
    if (err) return CMS2_ERR;
    *out_len = (int)per_stream_tell(&s);
    return CMS2_OK;
}

int cms2_boolean_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int64_t val;
    per_error_t err = per_decode_constrained_int(&s, &val, 0, 1);
    if (err) return CMS2_ERR;
    *(int*)ptr = (int)val;
    return CMS2_OK;
}

int cms2_int8_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int8_t val = *(const int8_t*)ptr;
    per_error_t err = per_encode_constrained_int(&s, val, -128, 127);
    if (err) return CMS2_ERR;
    *out_len = (int)per_stream_tell(&s);
    return CMS2_OK;
}

int cms2_int8_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int64_t val;
    per_error_t err = per_decode_constrained_int(&s, &val, -128, 127);
    if (err) return CMS2_ERR;
    *(int8_t*)ptr = (int8_t)val;
    return CMS2_OK;
}

int cms2_int8u_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    uint8_t val = *(const uint8_t*)ptr;
    per_error_t err = per_encode_constrained_int(&s, val, 0, 255);
    if (err) return CMS2_ERR;
    *out_len = (int)per_stream_tell(&s);
    return CMS2_OK;
}

int cms2_int8u_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int64_t val;
    per_error_t err = per_decode_constrained_int(&s, &val, 0, 255);
    if (err) return CMS2_ERR;
    *(uint8_t*)ptr = (uint8_t)val;
    return CMS2_OK;
}

int cms2_uint8_array_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    const uint8_t *vptr = *(const uint8_t *const*)ptr;         /* value */
    int32_t len = *(const int32_t*)((const uint8_t*)ptr + 8);  /* len */
    if (len < 0) len = 0;
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    per_error_t err = per_encode_visible_string(&s, vptr, 129);
    if (err) return CMS2_ERR;
    *out_len = (int)per_stream_tell(&s);
    return CMS2_OK;
}

int cms2_uint8_array_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    uint8_t *vptr = *(uint8_t **)ptr;
    int32_t max_len = *(const int32_t*)((const uint8_t*)ptr + 8);
    if (!vptr || max_len <= 0) return CMS2_ERR;
    per_error_t err = per_decode_visible_string(&s, vptr, (uint32_t)max_len);
    if (err) return CMS2_ERR;
    *(int32_t*)((uint8_t*)ptr + 8) = (int32_t)strlen((const char*)vptr);
    return CMS2_OK;
}
