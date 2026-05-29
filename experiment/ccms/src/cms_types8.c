#include "cms_types8.h"
#include "per_stream.h"
#include "per_integer.h"
#include <string.h>
#include <stdlib.h>

/* 7.8 DataDefinition */
int cms_encode_DataDefinition(
    int choice,
    int64_t int_val,
    const char *str_val,
    const uint8_t *bytes_val, int bytes_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_small_non_negative(&w, choice);

    switch (choice) {
    case 0:
        per_encode_constrained_int(&w, (int)int_val, 0, 12);
        break;
    case 1:
        per_encode_constrained_int(&w, int_val, -2147483648, 2147483647);
        break;
    case 2:
        {
            uint32_t count = (uint32_t)int_val;
            per_encode_length(&w, count);
        }
        break;
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
        break;
    case 14:
    case 15:
    case 16:
    case 17:
        per_encode_constrained_int(&w, int_val, 0, 65535);
        break;
    }

    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_DataDefinition(
    const uint8_t *in_buf, int in_len,
    int *choice,
    int64_t *int_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint32_t _idx;
    per_decode_small_non_negative(&r, &_idx);
    *choice = (int)_idx;

    switch (*choice) {
    case 0: { int64_t t; per_decode_constrained_int(&r, &t, 0, 12); *int_val = t; break; }
    case 1: { int64_t t; per_decode_constrained_int(&r, &t, -2147483648, 2147483647); *int_val = t; break; }
    case 2: { uint32_t count; per_decode_length(&r, &count); *int_val = count; break; }
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
        break;
    case 14:
    case 15:
    case 16:
    case 17: { int64_t t; per_decode_constrained_int(&r, &t, 0, 65535); *int_val = t; break; }
    }

    return CMS_OK;
}
