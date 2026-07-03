#include "data/time/cms_binary_time.h"
#include "data/string/cms_octet_string.h"
#include <string.h>

static void pack_binary_time(const cms_binary_time_t *t, uint8_t out[6]) {
    uint32_t ms = t->msOfDay ? t->msOfDay->value : 0;
    uint16_t days = t->daysSince1984 ? t->daysSince1984->value : 0;
    out[0] = (uint8_t)(ms >> 24);
    out[1] = (uint8_t)(ms >> 16);
    out[2] = (uint8_t)(ms >> 8);
    out[3] = (uint8_t)(ms);
    out[4] = (uint8_t)(days >> 8);
    out[5] = (uint8_t)(days);
}

static void unpack_binary_time(const uint8_t in[6], cms_binary_time_t *t) {
    if (t->msOfDay)
        t->msOfDay->value       = ((uint32_t)in[0] << 24) | ((uint32_t)in[1] << 16) |
                                  ((uint32_t)in[2] << 8)  | (uint32_t)in[3];
    if (t->daysSince1984)
        t->daysSince1984->value = ((uint16_t)in[4] << 8) | (uint16_t)in[5];
}

int cms_binary_time_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t buf[6];
    pack_binary_time((const cms_binary_time_t*)ptr, buf);
    return cms_octet_string_fixed_encode_stream(s, buf, 6);
}

int cms_binary_time_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t buf[6];
    int err = cms_octet_string_fixed_decode_stream(s, buf, 6);
    if (err) return CMS_ERR;
    if (ptr) unpack_binary_time(buf, (cms_binary_time_t*)ptr);
    return CMS_OK;
}

int cms_binary_time_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_binary_time_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_binary_time_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_binary_time_decode_stream(&s, ptr);
}
