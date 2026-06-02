#include "data/choice/cms_data.h"
#include <string.h>
#include <stdlib.h>

static void encode_data_value(per_stream_t *w, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len);
static void decode_data_value(per_stream_t *r, int choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap);

/* ---- internal stream version ---- */

int cms_data_encode_stream(per_stream_t *s, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len)
{
    per_encode_small_non_negative(s, choice);
    encode_data_value(s, choice, int_val, float_val, str_val, bytes_val, bytes_len);
    return CMS_OK;
}

int cms_data_decode_stream(per_stream_t *s, int *choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    uint32_t _idx;
    per_decode_small_non_negative(s, &_idx);
    *choice = (int)_idx;
    decode_data_value(s, *choice, int_val, float_val, str_val, str_cap, bytes_val, bytes_cap);
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_data_encode(int choice, int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    cms_data_encode_stream(&w, choice, int_val, float_val, str_val, bytes_val, bytes_len);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_data_decode(const uint8_t *in_buf, int in_len,
    int *choice, int64_t *int_val, double *float_val,
    char *str_val, int *str_cap, uint8_t *bytes_val, int *bytes_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_data_decode_stream(&r, choice, int_val, float_val, str_val, str_cap, bytes_val, bytes_cap);
    return CMS_OK;
}

/* ---- internal value encoders ---- */

static void encode_data_value(per_stream_t *w, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len)
{
    switch (choice) {
    case 0: per_encode_constrained_int(w, int_val, 0, 12); break;
    case 1:
    case 2: { per_encode_length(w, (uint32_t)int_val); break; }
    case 3: per_stream_write_bit(w, (int)int_val); break;
    case 4: per_encode_constrained_int(w, int_val, -128, 127); break;
    case 5: per_encode_constrained_int(w, int_val, -32768, 32767); break;
    case 6: per_encode_constrained_int(w, int_val, -2147483648, 2147483647); break;
    case 7: per_encode_unconstrained_int(w, int_val); break;
    case 8: per_encode_constrained_int(w, int_val, 0, 255); break;
    case 9: per_encode_constrained_int(w, int_val, 0, 65535); break;
    case 10: per_encode_constrained_int(w, int_val, 0, 4294967295); break;
    case 11: per_encode_unconstrained_int(w, int_val); break;
    case 12: {
        uint32_t bits;
        float fv = (float)float_val;
        memcpy(&bits, &fv, sizeof(bits));
        uint8_t fbytes[4] = {(uint8_t)(bits >> 24), (uint8_t)(bits >> 16), (uint8_t)(bits >> 8), (uint8_t)(bits)};
        per_encode_octet_string_fixed(w, fbytes, 4);
        break;
    }
    case 13: {
        uint64_t bits;
        memcpy(&bits, &float_val, sizeof(bits));
        uint8_t dbytes[8] = {(uint8_t)(bits >> 56), (uint8_t)(bits >> 48), (uint8_t)(bits >> 40), (uint8_t)(bits >> 32),
                            (uint8_t)(bits >> 24), (uint8_t)(bits >> 16), (uint8_t)(bits >> 8), (uint8_t)(bits)};
        per_encode_octet_string_fixed(w, dbytes, 8);
        break;
    }
    case 14: per_encode_bit_string_unconstrained(w, bytes_val, bytes_len * 8); break;
    case 15: per_encode_octet_string_unconstrained(w, bytes_val, bytes_len); break;
    case 16: per_encode_visible_string_unconstrained(w, str_val); break;
    case 17: per_encode_utf8_string_unconstrained(w, str_val); break;
    case 18: {
        uint8_t bytes[8];
        uint64_t ms = (uint64_t)int_val;
        bytes[0] = (uint8_t)(ms >> 56); bytes[1] = (uint8_t)(ms >> 48);
        bytes[2] = (uint8_t)(ms >> 40); bytes[3] = (uint8_t)(ms >> 32);
        bytes[4] = (uint8_t)(ms >> 24); bytes[5] = (uint8_t)(ms >> 16);
        bytes[6] = (uint8_t)(ms >> 8);  bytes[7] = (uint8_t)(ms);
        per_encode_octet_string_fixed(w, bytes, 8);
        break;
    }
    case 19: {
        uint8_t b6[6];
        uint64_t ms = (uint64_t)int_val;
        uint32_t msOfDay = (uint32_t)(ms % 86400000);
        uint16_t days = (uint16_t)(ms / 86400000);
        b6[0] = (uint8_t)(msOfDay >> 24); b6[1] = (uint8_t)(msOfDay >> 16);
        b6[2] = (uint8_t)(msOfDay >> 8);  b6[3] = (uint8_t)(msOfDay);
        b6[4] = (uint8_t)(days >> 8);     b6[5] = (uint8_t)(days);
        per_encode_octet_string_fixed(w, b6, 6);
        break;
    }
    case 20: per_encode_bit_string_fixed(w, bytes_val, 13); break;
    case 21: per_encode_constrained_int(w, int_val, 0, 3); break;
    case 22: per_encode_constrained_int(w, int_val, 0, 3); break;
    case 23: per_encode_bit_string_fixed(w, bytes_val, 16); break;
    }
}

static void decode_data_value(per_stream_t *r, int choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    switch (choice) {
    case 0: { int64_t t; per_decode_constrained_int(r, &t, 0, 12); *int_val = t; break; }
    case 1:
    case 2: { uint32_t count; per_decode_length(r, &count); *int_val = count; break; }
    case 3: { int _b; per_stream_read_bit(r, &_b); *int_val = _b; break; }
    case 4: { int64_t t; per_decode_constrained_int(r, &t, -128, 127); *int_val = t; break; }
    case 5: { int64_t t; per_decode_constrained_int(r, &t, -32768, 32767); *int_val = t; break; }
    case 6: { int64_t t; per_decode_constrained_int(r, &t, -2147483648, 2147483647); *int_val = t; break; }
    case 7: per_decode_unconstrained_int(r, int_val); break;
    case 8: { int64_t t; per_decode_constrained_int(r, &t, 0, 255); *int_val = t; break; }
    case 9: { int64_t t; per_decode_constrained_int(r, &t, 0, 65535); *int_val = t; break; }
    case 10: { int64_t t; per_decode_constrained_int(r, &t, 0, 4294967295); *int_val = t; break; }
    case 11: per_decode_unconstrained_int(r, int_val); break;
    case 12: {
        uint8_t fbytes[4];
        per_decode_octet_string_fixed(r, fbytes, 4);
        uint32_t bits = ((uint32_t)fbytes[0] << 24) | ((uint32_t)fbytes[1] << 16) | ((uint32_t)fbytes[2] << 8) | (uint32_t)fbytes[3];
        float fv; memcpy(&fv, &bits, sizeof(fv)); *float_val = fv;
        break;
    }
    case 13: {
        uint8_t dbytes[8];
        per_decode_octet_string_fixed(r, dbytes, 8);
        uint64_t bits = ((uint64_t)dbytes[0] << 56) | ((uint64_t)dbytes[1] << 48) | ((uint64_t)dbytes[2] << 40) | ((uint64_t)dbytes[3] << 32)
                      | ((uint64_t)dbytes[4] << 24) | ((uint64_t)dbytes[5] << 16) | ((uint64_t)dbytes[6] << 8)  | (uint64_t)dbytes[7];
        double dv; memcpy(&dv, &bits, sizeof(dv)); *float_val = dv;
        break;
    }
    case 14: { int n = *bytes_cap * 8; per_decode_bit_string_unconstrained(r, bytes_val, &n); *bytes_cap = (n + 7) / 8; break; }
    case 15: { size_t ol = (size_t)*bytes_cap; per_decode_octet_string_unconstrained(r, bytes_val, &ol); *bytes_cap = (int)ol; break; }
    case 16: { uint32_t _l; per_decode_visible_string_unconstrained(r, str_val, &_l); *str_cap = (int)_l; break; }
    case 17: { uint32_t _l; per_decode_utf8_string_unconstrained(r, str_val, &_l); *str_cap = (int)_l; break; }
    case 18: {
        uint8_t bytes[8];
        per_decode_octet_string_fixed(r, bytes, 8);
        *int_val = (int64_t)(((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48) | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                           | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16) | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7]);
        break;
    }
    case 19: {
        uint8_t b6[6];
        per_decode_octet_string_fixed(r, b6, 6);
        uint32_t msOfDay = ((uint32_t)b6[0] << 24) | ((uint32_t)b6[1] << 16)
                         | ((uint32_t)b6[2] << 8)  | (uint32_t)b6[3];
        uint16_t days = (uint16_t)(((uint16_t)b6[4] << 8) | (uint16_t)b6[5]);
        *int_val = (int64_t)days * 86400000 + msOfDay;
        break;
    }
    case 20: per_decode_bit_string_fixed(r, bytes_val, 13); break;
    case 21: { int64_t t; per_decode_constrained_int(r, &t, 0, 3); *int_val = t; break; }
    case 22: { int64_t t; per_decode_constrained_int(r, &t, 0, 3); *int_val = t; break; }
    case 23: per_decode_bit_string_fixed(r, bytes_val, 16); break;
    }
}
