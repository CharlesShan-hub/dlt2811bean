#include "data/choice/cms_data.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_float.h"
#include "data/extended/cms_time.h"
#include "data/control/cms_check.h"
#include "data/common/cms_quality.h"
#include "data/common/cms_dbpos.h"
#include "data/common/cms_tcmd.h"
#include "data/common/cms_service_error.h"
#include <string.h>
#include <stdlib.h>

static void write_utc_time(per_stream_t *s, int64_t t_ms)
{
    cms_utc_time_t utc;
    uint64_t _u = (uint64_t)t_ms;
    utc.seconds_since_epoch.value = (uint32_t)(_u / 1000);
    utc.fraction_of_second.value  = (uint32_t)(((_u % 1000) * 16777216) / 1000);
    utc.time_quality.tagf.value = 0;
    utc.time_quality.precision.value = 0;
    utc.time_quality.fraction.value = 0;
    cms_utc_time_encode_stream(s, &utc);
}

static int64_t read_utc_time(per_stream_t *s)
{
    cms_utc_time_t utc;
    cms_utc_time_decode_stream(s, &utc);
    return (int64_t)utc.seconds_since_epoch.value * 1000
        + (int64_t)(((uint64_t)utc.fraction_of_second.value * 1000) / 16777216);
}

int cms_data_encode_stream(per_stream_t *s, const cms_data_t *data)
{
    per_encode_small_non_negative(s, (uint32_t)data->choice);

    if (data->choice == 1 || data->choice == 2) {
        int count = (data->choice == 1) ? data->value.array.count : data->value.structure.count;
        const cms_data_t *elems = (data->choice == 1) ? data->value.array.elements : data->value.structure.elements;
        per_encode_length(s, (uint32_t)count);
        for (int i = 0; i < count; i++)
            cms_data_encode_stream(s, &elems[i]);
        return CMS_OK;
    }

    switch (data->choice) {
    case 0: { cms_service_error_t _v = { .value = data->value.error}; cms_service_error_encode_stream(s, &_v); } break;
    case 3: { cms_boolean_t _v = { .value = data->value.boolean_value }; cms_boolean_encode_stream(s, &_v); } break;
    case 4: { cms_int8_t _v = { .value = data->value.int8 }; cms_int8_encode_stream(s, &_v); } break;
    case 5: { cms_int16_t _v = { .value = data->value.int16 }; cms_int16_encode_stream(s, &_v); } break;
    case 6: { cms_int32_t _v = { .value = data->value.int32 }; cms_int32_encode_stream(s, &_v); } break;
    case 7: { cms_int64_t _v = { .value = data->value.int64 }; cms_int64_encode_stream(s, &_v); } break;
    case 8: { cms_int8u_t _v = { .value = data->value.int8u }; cms_int8u_encode_stream(s, &_v); } break;
    case 9: { cms_int16u_t _v = { .value = data->value.int16u }; cms_int16u_encode_stream(s, &_v); } break;
    case 10: { cms_int32u_t _v = { .value = data->value.int32u }; cms_int32u_encode_stream(s, &_v); } break;
    case 11: { cms_int64u_t _v = { .value = data->value.int64u }; cms_int64u_encode_stream(s, &_v); } break;
    case 12: { cms_float32_t _v = { .value = data->value.float32 }; cms_float32_encode_stream(s, &_v); } break;
    case 13: { cms_float64_t _v = { .value = data->value.float64 }; cms_float64_encode_stream(s, &_v); } break;
    case 14: per_encode_bit_string_unconstrained(s, data->value.bit_string.data, data->value.bit_string.nbits); break;
    case 15: per_encode_octet_string_unconstrained(s, data->value.octet_string.data, data->value.octet_string.len); break;
    case 16: per_encode_visible_string_unconstrained(s, data->value.visible_string); break;
    case 17: per_encode_utf8_string_unconstrained(s, data->value.utf8_string); break;
    case 18: write_utc_time(s, data->value.utc_time_ms); break;
    case 19: cms_binary_time_encode_stream(s, (const cms_binary_time_t *)&data->value.binary_time); break;
    case 20: { cms_quality_t _v = data->value.quality; cms_quality_encode_stream(s, &_v); } break;
    case 21: { cms_dbpos_t _v = { .value = { .value = data->value.dbpos } }; cms_dbpos_encode_stream(s, &_v); } break;
    case 22: { cms_tcmd_t _v = { .value = { .value = data->value.tcmd } }; cms_tcmd_encode_stream(s, &_v); } break;
    case 23: { cms_bit_string_fixed_t _v = { .value = (uint8_t *)data->value.check, .nbits = 2 }; cms_check_encode_stream(s, &_v); } break;
    }
    return CMS_OK;
}

int cms_data_decode_stream(per_stream_t *s, cms_data_t *data)
{
    uint32_t _idx;
    per_decode_small_non_negative(s, &_idx);
    data->choice = (int)_idx;

    if (data->choice == 1 || data->choice == 2) {
        uint32_t count;
        per_decode_length(s, &count);
        cms_data_t *elems = NULL;
        if (count) {
            elems = (cms_data_t *)calloc(count, sizeof(cms_data_t));
            if (!elems) return CMS_ERR;
            for (uint32_t i = 0; i < count; i++)
                cms_data_decode_stream(s, &elems[i]);
        }
        if (data->choice == 1) {
            data->value.array.count = (int)count;
            data->value.array.elements = elems;
        } else {
            data->value.structure.count = (int)count;
            data->value.structure.elements = elems;
        }
        return CMS_OK;
    }

    switch (data->choice) {
    case 0: { cms_service_error_t _v = { .value = { .value = 0 } }; cms_service_error_decode_stream(s, &_v); data->value.error = _v.value.value.value; } break;
    case 3: { cms_boolean_t _v = { .value = 0 }; cms_boolean_decode_stream(s, &_v); data->value.boolean_value = _v.value; } break;
    case 4: { cms_int8_t _v = { .value = 0 }; cms_int8_decode_stream(s, &_v); data->value.int8 = _v.value; } break;
    case 5: { cms_int16_t _v = { .value = 0 }; cms_int16_decode_stream(s, &_v); data->value.int16 = _v.value; } break;
    case 6: { cms_int32_t _v = { .value = 0 }; cms_int32_decode_stream(s, &_v); data->value.int32 = _v.value; } break;
    case 7: { cms_int64_t _v = { .value = 0 }; cms_int64_decode_stream(s, &_v); data->value.int64 = _v.value; } break;
    case 8: { cms_int8u_t _v = { .value = 0 }; cms_int8u_decode_stream(s, &_v); data->value.int8u = _v.value; } break;
    case 9: { cms_int16u_t _v = { .value = 0 }; cms_int16u_decode_stream(s, &_v); data->value.int16u = _v.value; } break;
    case 10: { cms_int32u_t _v = { .value = 0 }; cms_int32u_decode_stream(s, &_v); data->value.int32u = _v.value; } break;
    case 11: { cms_int64u_t _v = { .value = 0 }; cms_int64u_decode_stream(s, &_v); data->value.int64u = _v.value; } break;
    case 12: { cms_float32_t _v = { .value = 0 }; cms_float32_decode_stream(s, &_v); data->value.float32 = _v.value; } break;
    case 13: { cms_float64_t _v = { .value = 0 }; cms_float64_decode_stream(s, &_v); data->value.float64 = _v.value; } break;
    case 14: {
        int64_t len;
        per_decode_semi_constrained(s, &len, 0);
        data->value.bit_string.nbits = (int)len;
        int nbytes = ((int)len + 7) / 8;
        data->value.bit_string.data = (uint8_t *)malloc(nbytes ? (size_t)nbytes : 1);
        if (!data->value.bit_string.data) return CMS_ERR;
        per_stream_align(s);
        per_decode_bit_string_fixed(s, data->value.bit_string.data, (int)len);
        break;
    }
    case 15: {
        uint32_t len;
        per_decode_length(s, &len);
        data->value.octet_string.len = (int)len;
        data->value.octet_string.data = (uint8_t *)malloc(len ? (size_t)len : 1);
        if (!data->value.octet_string.data) return CMS_ERR;
        if (len) {
            per_stream_align(s);
            per_stream_read_bytes(s, data->value.octet_string.data, len);
        }
        break;
    }
    case 16: {
        uint32_t len;
        per_decode_length(s, &len);
        data->value.visible_string = (char *)malloc((size_t)(len + 1));
        if (!data->value.visible_string) return CMS_ERR;
        if (len) {
            per_stream_align(s);
            for (uint32_t i = 0; i < len; i++) {
                uint64_t ch;
                per_stream_read_bits(s, &ch, 8);
                data->value.visible_string[i] = (char)ch;
            }
        }
        data->value.visible_string[len] = '\0';
        break;
    }
    case 17: {
        uint32_t len;
        per_decode_length(s, &len);
        data->value.utf8_string = (char *)malloc((size_t)(len + 1));
        if (!data->value.utf8_string) return CMS_ERR;
        if (len) {
            per_stream_align(s);
            per_stream_read_bytes(s, (uint8_t *)data->value.utf8_string, len);
        }
        data->value.utf8_string[len] = '\0';
        break;
    }
    case 18: data->value.utc_time_ms = read_utc_time(s); break;
    case 19: cms_binary_time_decode_stream(s, (cms_binary_time_t *)&data->value.binary_time); break;
    case 20: cms_quality_decode_stream(s, &data->value.quality); break;
    case 21: { cms_dbpos_t _v = { .value = { .value = 0 } }; cms_dbpos_decode_stream(s, &_v); data->value.dbpos = _v.value.value.value; } break;
    case 22: { cms_tcmd_t _v = { .value = { .value = 0 } }; cms_tcmd_decode_stream(s, &_v); data->value.tcmd = _v.value.value.value; } break;
    case 23: { cms_bit_string_fixed_t _v = { .value = (uint8_t *)data->value.check, .nbits = 2 }; cms_check_decode_stream(s, &_v); } break;
    }
    return CMS_OK;
}

CMS_EXPORT int cms_data_encode(const cms_data_t *data, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    cms_data_encode_stream(&w, data);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_data_decode(const uint8_t *in_buf, int in_len, cms_data_t *data)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    return cms_data_decode_stream(&r, data);
}

CMS_EXPORT void cms_data_free(cms_data_t *data)
{
    if (!data) return;
    if (data->choice == 1 || data->choice == 2) {
        int count = (data->choice == 1) ? data->value.array.count : data->value.structure.count;
        cms_data_t *elems = (data->choice == 1) ? data->value.array.elements : data->value.structure.elements;
        for (int i = 0; i < count; i++)
            cms_data_free(&elems[i]);
        free(elems);
        return;
    }
    if (data->choice == 14) { free(data->value.bit_string.data); data->value.bit_string.data = NULL; }
    else if (data->choice == 15) { free(data->value.octet_string.data); data->value.octet_string.data = NULL; }
    else if (data->choice == 16) { free(data->value.visible_string); data->value.visible_string = NULL; }
    else if (data->choice == 17) { free(data->value.utf8_string); data->value.utf8_string = NULL; }
}

CMS_EXPORT int cms_data_choice_encode(int choice, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_small_non_negative(&w, (uint32_t)choice);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_data_count_encode(int count, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_length(&w, (uint32_t)count);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}
