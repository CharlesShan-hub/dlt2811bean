#include "data/choice/cms_data.h"
#include "per/cms_stream.h"
#include "per/cms_boolean.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_bit_string.h"
#include <string.h>
#include <stdlib.h>

static void encode_data_value(per_stream_t *w, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len);
static void decode_data_value(per_stream_t *r, int choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap);

CMS_EXPORT int cms_data_encode(
    int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_small_non_negative(&w, choice);
    encode_data_value(&w, choice, int_val, float_val,
                      str_val, bytes_val, bytes_len);

    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_data_decode(
    const uint8_t *in_buf, int in_len,
    int *choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint32_t _idx;
    per_decode_small_non_negative(&r, &_idx);
    *choice = (int)_idx;

    decode_data_value(&r, *choice, int_val, float_val,
                      str_val, str_cap, bytes_val, bytes_cap);

    return CMS_OK;
}

static void encode_data_value(per_stream_t *w, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len)
{
    switch (choice) {
    case 0: per_encode_constrained_int(w, int_val, 0, 12); break;
    case 1:
    case 2: {
        per_encode_length(w, (uint32_t)int_val);
        break;
    }
    case 3: per_encode_boolean(w, (int)int_val); break;
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
        uint8_t fbytes[4];
        fbytes[0] = (uint8_t)(bits >> 24);
        fbytes[1] = (uint8_t)(bits >> 16);
        fbytes[2] = (uint8_t)(bits >> 8);
        fbytes[3] = (uint8_t)(bits);
        per_encode_octet_string_fixed(w, fbytes, 4);
        break;
    }
    case 13: {
        uint64_t bits;
        memcpy(&bits, &float_val, sizeof(bits));
        uint8_t dbytes[8];
        dbytes[0] = (uint8_t)(bits >> 56);
        dbytes[1] = (uint8_t)(bits >> 48);
        dbytes[2] = (uint8_t)(bits >> 40);
        dbytes[3] = (uint8_t)(bits >> 32);
        dbytes[4] = (uint8_t)(bits >> 24);
        dbytes[5] = (uint8_t)(bits >> 16);
        dbytes[6] = (uint8_t)(bits >> 8);
        dbytes[7] = (uint8_t)(bits);
        per_encode_octet_string_fixed(w, dbytes, 8);
        break;
    }
    case 14: per_encode_bit_string(w, bytes_val, bytes_len * 8, 65535); break;
    case 15: per_encode_octet_string(w, bytes_val, bytes_len, 65535); break;
    case 16: per_encode_visible_string(w, str_val, 255); break;
    case 17: per_encode_utf8_string(w, str_val, 255); break;
    case 18: {
        uint8_t bytes[8];
        uint64_t ms = (uint64_t)int_val;
        bytes[0] = (uint8_t)(ms >> 56);
        bytes[1] = (uint8_t)(ms >> 48);
        bytes[2] = (uint8_t)(ms >> 40);
        bytes[3] = (uint8_t)(ms >> 32);
        bytes[4] = (uint8_t)(ms >> 24);
        bytes[5] = (uint8_t)(ms >> 16);
        bytes[6] = (uint8_t)(ms >> 8);
        bytes[7] = (uint8_t)(ms);
        per_encode_octet_string_fixed(w, bytes, 8);
        break;
    }
    case 19: {
        per_encode_constrained_int(w, int_val, 0, 86400000);
        per_encode_constrained_int(w, 0, 0, 65535);
        break;
    }
    case 20: per_encode_bit_string_fixed(w, bytes_val, 13); break;
    case 21: per_encode_small_non_negative(w, (uint32_t)int_val); break;
    case 22: per_encode_small_non_negative(w, (uint32_t)int_val); break;
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
    case 3: { bool b; per_decode_boolean(r, &b); *int_val = b ? 1 : 0; break; }
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
        uint32_t bits = ((uint32_t)fbytes[0] << 24) | ((uint32_t)fbytes[1] << 16)
                       | ((uint32_t)fbytes[2] << 8) | (uint32_t)fbytes[3];
        float fv;
        memcpy(&fv, &bits, sizeof(fv));
        *float_val = fv;
        break;
    }
    case 13: {
        uint8_t dbytes[8];
        per_decode_octet_string_fixed(r, dbytes, 8);
        uint64_t bits = ((uint64_t)dbytes[0] << 56) | ((uint64_t)dbytes[1] << 48)
                       | ((uint64_t)dbytes[2] << 40) | ((uint64_t)dbytes[3] << 32)
                       | ((uint64_t)dbytes[4] << 24) | ((uint64_t)dbytes[5] << 16)
                       | ((uint64_t)dbytes[6] << 8)  | (uint64_t)dbytes[7];
        double dv;
        memcpy(&dv, &bits, sizeof(dv));
        *float_val = dv;
        break;
    }
    case 14: {
        int out_nbits = *bytes_cap * 8;
        per_decode_bit_string(r, bytes_val, &out_nbits, 65535);
        *bytes_cap = (out_nbits + 7) / 8;
        break;
    }
    case 15: { size_t ol = (size_t)*bytes_cap; per_decode_octet_string(r, bytes_val, &ol, 65535); *bytes_cap = (int)ol; break; }
    case 16: per_decode_visible_string(r, str_val, (uint32_t)*str_cap); *str_cap = (int)strlen(str_val); break;
    case 17: per_decode_utf8_string(r, str_val, (uint32_t)*str_cap); *str_cap = (int)strlen(str_val); break;
    case 18: {
        uint8_t bytes[8];
        per_decode_octet_string_fixed(r, bytes, 8);
        uint64_t ms = ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48)
                    | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                    | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16)
                    | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7];
        *int_val = (int64_t)ms;
        break;
    }
    case 19: {
        int64_t t;
        per_decode_constrained_int(r, &t, 0, 86400000);
        *int_val = t;
        int64_t _dummy;
        per_decode_constrained_int(r, &_dummy, 0, 65535);
        break;
    }
    case 20: per_decode_bit_string_fixed(r, bytes_val, 13); break;
    case 21: { uint32_t u; per_decode_small_non_negative(r, &u); *int_val = u; break; }
    case 22: { uint32_t u; per_decode_small_non_negative(r, &u); *int_val = u; break; }
    case 23: per_decode_bit_string_fixed(r, bytes_val, 16); break;
    }
}