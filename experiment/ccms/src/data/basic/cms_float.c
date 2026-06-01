#include "data/basic/cms_float.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include <string.h>

/* ---- stream versions ---- */

int cms_float32_encode_stream(per_stream_t *s, float value)
{
    uint32_t bits;
    memcpy(&bits, &value, sizeof(bits));
    uint8_t bytes[4];
    bytes[0] = (uint8_t)(bits >> 24);
    bytes[1] = (uint8_t)(bits >> 16);
    bytes[2] = (uint8_t)(bits >> 8);
    bytes[3] = (uint8_t)(bits);
    per_encode_octet_string_fixed(s, bytes, 4);
    return CMS_OK;
}

int cms_float32_decode_stream(per_stream_t *s, float *value)
{
    uint8_t bytes[4];
    per_decode_octet_string_fixed(s, bytes, 4);
    uint32_t bits = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
                  | ((uint32_t)bytes[2] << 8) | (uint32_t)bytes[3];
    memcpy(value, &bits, sizeof(bits));
    return CMS_OK;
}

int cms_float64_encode_stream(per_stream_t *s, double value)
{
    uint64_t bits;
    memcpy(&bits, &value, sizeof(bits));
    uint8_t bytes[8];
    bytes[0] = (uint8_t)(bits >> 56);
    bytes[1] = (uint8_t)(bits >> 48);
    bytes[2] = (uint8_t)(bits >> 40);
    bytes[3] = (uint8_t)(bits >> 32);
    bytes[4] = (uint8_t)(bits >> 24);
    bytes[5] = (uint8_t)(bits >> 16);
    bytes[6] = (uint8_t)(bits >> 8);
    bytes[7] = (uint8_t)(bits);
    per_encode_octet_string_fixed(s, bytes, 8);
    return CMS_OK;
}

int cms_float64_decode_stream(per_stream_t *s, double *value)
{
    uint8_t bytes[8];
    per_decode_octet_string_fixed(s, bytes, 8);
    uint64_t bits = ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48)
                  | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                  | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16)
                  | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7];
    memcpy(value, &bits, sizeof(bits));
    return CMS_OK;
}

/* ---- public buffer wrappers ---- */

CMS_EXPORT int cms_float32_encode(float value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_float32_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_float32_decode(const uint8_t *in_buf, int in_len, float *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_float32_decode_stream(&r, value); return CMS_OK; }
CMS_EXPORT int cms_float64_encode(double value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_float64_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_float64_decode(const uint8_t *in_buf, int in_len, double *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_float64_decode_stream(&r, value); return CMS_OK; }
