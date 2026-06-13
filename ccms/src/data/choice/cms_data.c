#include "data/choice/cms_data.h"
#include "data/string/cms_visible_string.h"
#include "data/string/cms_octet_string.h"
#include "data/string/cms_bit_string.h"
#include "data/string/cms_utf8_string.h"
#include "per/cms_integer.h"

int cms_data_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_data_t *d = (const cms_data_t*)ptr;
    if (!d || !d->choice) return CMS_ERR;
    int sel = d->choice->value;
    if (sel < 0 || sel > 23) return CMS_ERR;

    int err;

    /* 1. Encode CHOICE index — 24 alts → small non-negative */
    err = (int)per_encode_small_non_negative(s, (uint32_t)sel);
    if (err) return err;

    /* 2. Encode the selected alternative */
    switch (sel) {

    case CMS_DATA_CHOICE_ERROR:
        if (!d->alt_error) return CMS_ERR;
        return cms_service_error_encode_stream(s, d->alt_error);

    case CMS_DATA_CHOICE_ARRAY:
    case CMS_DATA_CHOICE_STRUCTURE: {
        /* SEQUENCE OF Data via cms_array_t */
        cms_array_t *arr = (cms_array_t*)d->alt_sequence;
        int32_t count = arr ? arr->count : 0;
        err = (int)per_encode_length(s, (uint32_t)count);
        if (err) return err;
        for (int32_t i = 0; i < count; i++) {
            if (!arr->elements || !arr->elements[i]) return CMS_ERR;
            err = cms_data_encode_stream(s, (const cms_data_t*)arr->elements[i]);
            if (err) return err;
        }
        return CMS_OK;
    }

    case CMS_DATA_CHOICE_BOOLEAN:
        if (!d->alt_boolean) return CMS_ERR;
        return cms_boolean_encode_stream(s, d->alt_boolean);

    case CMS_DATA_CHOICE_INT8:
        if (!d->alt_int8) return CMS_ERR;
        return cms_int8_encode_stream(s, d->alt_int8);

    case CMS_DATA_CHOICE_INT16:
        if (!d->alt_int16) return CMS_ERR;
        return cms_int16_encode_stream(s, d->alt_int16);

    case CMS_DATA_CHOICE_INT32:
        if (!d->alt_int32) return CMS_ERR;
        return cms_int32_encode_stream(s, d->alt_int32);

    case CMS_DATA_CHOICE_INT64:
        if (!d->alt_int64) return CMS_ERR;
        return cms_int64_encode_stream(s, d->alt_int64);

    case CMS_DATA_CHOICE_INT8U:
        if (!d->alt_int8u) return CMS_ERR;
        return cms_int8u_encode_stream(s, d->alt_int8u);

    case CMS_DATA_CHOICE_INT16U:
        if (!d->alt_int16u) return CMS_ERR;
        return cms_int16u_encode_stream(s, d->alt_int16u);

    case CMS_DATA_CHOICE_INT32U:
        if (!d->alt_int32u) return CMS_ERR;
        return cms_int32u_encode_stream(s, d->alt_int32u);

    case CMS_DATA_CHOICE_INT64U:
        if (!d->alt_int64u) return CMS_ERR;
        return cms_int64u_encode_stream(s, d->alt_int64u);

    case CMS_DATA_CHOICE_FLOAT32:
        if (!d->alt_float32) return CMS_ERR;
        return cms_float32_encode_stream(s, d->alt_float32);

    case CMS_DATA_CHOICE_FLOAT64:
        if (!d->alt_float64) return CMS_ERR;
        return cms_float64_encode_stream(s, d->alt_float64);

    case CMS_DATA_CHOICE_BIT_STRING: {
        if (!d->alt_bit_string) return CMS_ERR;
        /* len is in bits */
        return (int)per_encode_bit_string(s, d->alt_bit_string->value, d->alt_bit_string->len, INT32_MAX);
    }

    case CMS_DATA_CHOICE_OCTET_STRING:
        if (!d->alt_octet_string) return CMS_ERR;
        return cms_octet_string_encode_stream(s, d->alt_octet_string, INT32_MAX);

    case CMS_DATA_CHOICE_VISIBLE_STRING:
        if (!d->alt_visible_string) return CMS_ERR;
        return cms_visible_string_encode_stream(s, d->alt_visible_string, INT32_MAX);

    case CMS_DATA_CHOICE_UNICODE_STRING:
        if (!d->alt_unicode_string) return CMS_ERR;
        return cms_utf8_string_encode_stream(s, d->alt_unicode_string, INT32_MAX);

    case CMS_DATA_CHOICE_UTC_TIME:
        if (!d->alt_utc_time) return CMS_ERR;
        return cms_utc_time_encode_stream(s, d->alt_utc_time);

    case CMS_DATA_CHOICE_BINARY_TIME:
        if (!d->alt_binary_time) return CMS_ERR;
        return cms_binary_time_encode_stream(s, d->alt_binary_time);

    case CMS_DATA_CHOICE_QUALITY:
        if (!d->alt_quality) return CMS_ERR;
        return cms_quality_encode_stream(s, d->alt_quality);

    case CMS_DATA_CHOICE_DBPOS:
        if (!d->alt_dbpos) return CMS_ERR;
        return cms_dbpos_encode_stream(s, d->alt_dbpos);

    case CMS_DATA_CHOICE_TCMD:
        if (!d->alt_tcmd) return CMS_ERR;
        return cms_tcmd_encode_stream(s, d->alt_tcmd);

    case CMS_DATA_CHOICE_CHECK:
        if (!d->alt_check) return CMS_ERR;
        return cms_check_encode_stream(s, d->alt_check);

    default:
        return CMS_ERR;
    }
}

int cms_data_decode_stream(per_stream_t *s, void *ptr) {
    cms_data_t *d = (cms_data_t*)ptr;
    if (!d || !d->choice) return CMS_ERR;

    /* 1. Decode CHOICE index — small non-negative */
    uint32_t sel32;
    per_error_t perr = per_decode_small_non_negative(s, &sel32);
    if (perr) return CMS_ERR;
    if (sel32 > 23) return CMS_ERR;
    d->choice->value = (int)sel32;

    /* 2. Decode the selected alternative */
    int err = 0;
    switch (d->choice->value) {

    case CMS_DATA_CHOICE_ERROR:
        if (!d->alt_error) return CMS_ERR;
        return cms_service_error_decode_stream(s, d->alt_error);

    case CMS_DATA_CHOICE_ARRAY:
    case CMS_DATA_CHOICE_STRUCTURE: {
        uint32_t count;
        perr = per_decode_length(s, &count);
        if (perr) return CMS_ERR;
        cms_array_t *arr = (cms_array_t*)d->alt_sequence;
        if (arr) arr->count = (int32_t)count;
        for (uint32_t i = 0; i < count; i++) {
            if (!arr || !arr->elements || !arr->elements[i]) return CMS_ERR;
            err = cms_data_decode_stream(s, arr->elements[i]);
            if (err) return err;
        }
        return CMS_OK;
    }

    case CMS_DATA_CHOICE_BOOLEAN:
        if (!d->alt_boolean) return CMS_ERR;
        return cms_boolean_decode_stream(s, d->alt_boolean);

    case CMS_DATA_CHOICE_INT8:
        if (!d->alt_int8) return CMS_ERR;
        return cms_int8_decode_stream(s, d->alt_int8);

    case CMS_DATA_CHOICE_INT16:
        if (!d->alt_int16) return CMS_ERR;
        return cms_int16_decode_stream(s, d->alt_int16);

    case CMS_DATA_CHOICE_INT32:
        if (!d->alt_int32) return CMS_ERR;
        return cms_int32_decode_stream(s, d->alt_int32);

    case CMS_DATA_CHOICE_INT64:
        if (!d->alt_int64) return CMS_ERR;
        return cms_int64_decode_stream(s, d->alt_int64);

    case CMS_DATA_CHOICE_INT8U:
        if (!d->alt_int8u) return CMS_ERR;
        return cms_int8u_decode_stream(s, d->alt_int8u);

    case CMS_DATA_CHOICE_INT16U:
        if (!d->alt_int16u) return CMS_ERR;
        return cms_int16u_decode_stream(s, d->alt_int16u);

    case CMS_DATA_CHOICE_INT32U:
        if (!d->alt_int32u) return CMS_ERR;
        return cms_int32u_decode_stream(s, d->alt_int32u);

    case CMS_DATA_CHOICE_INT64U:
        if (!d->alt_int64u) return CMS_ERR;
        return cms_int64u_decode_stream(s, d->alt_int64u);

    case CMS_DATA_CHOICE_FLOAT32:
        if (!d->alt_float32) return CMS_ERR;
        return cms_float32_decode_stream(s, d->alt_float32);

    case CMS_DATA_CHOICE_FLOAT64:
        if (!d->alt_float64) return CMS_ERR;
        return cms_float64_decode_stream(s, d->alt_float64);

    case CMS_DATA_CHOICE_BIT_STRING: {
        if (!d->alt_bit_string) return CMS_ERR;
        uint8_t *data = d->alt_bit_string->value;
        int out_nbits;
        per_error_t perr = per_decode_bit_string(s, data, &out_nbits, INT32_MAX);
        if (perr) return CMS_ERR;
        d->alt_bit_string->len = out_nbits;  /* len is in bits */
        return CMS_OK;
    }

    case CMS_DATA_CHOICE_OCTET_STRING:
        if (!d->alt_octet_string) return CMS_ERR;
        return cms_octet_string_decode_stream(s, d->alt_octet_string, INT32_MAX);

    case CMS_DATA_CHOICE_VISIBLE_STRING:
        if (!d->alt_visible_string) return CMS_ERR;
        return cms_visible_string_decode_stream(s, d->alt_visible_string, INT32_MAX);

    case CMS_DATA_CHOICE_UNICODE_STRING:
        if (!d->alt_unicode_string) return CMS_ERR;
        return cms_utf8_string_decode_stream(s, d->alt_unicode_string, INT32_MAX);

    case CMS_DATA_CHOICE_UTC_TIME:
        if (!d->alt_utc_time) return CMS_ERR;
        return cms_utc_time_decode_stream(s, d->alt_utc_time);

    case CMS_DATA_CHOICE_BINARY_TIME:
        if (!d->alt_binary_time) return CMS_ERR;
        return cms_binary_time_decode_stream(s, d->alt_binary_time);

    case CMS_DATA_CHOICE_QUALITY:
        if (!d->alt_quality) return CMS_ERR;
        return cms_quality_decode_stream(s, d->alt_quality);

    case CMS_DATA_CHOICE_DBPOS:
        if (!d->alt_dbpos) return CMS_ERR;
        return cms_dbpos_decode_stream(s, d->alt_dbpos);

    case CMS_DATA_CHOICE_TCMD:
        if (!d->alt_tcmd) return CMS_ERR;
        return cms_tcmd_decode_stream(s, d->alt_tcmd);

    case CMS_DATA_CHOICE_CHECK:
        if (!d->alt_check) return CMS_ERR;
        return cms_check_decode_stream(s, d->alt_check);

    default:
        return CMS_ERR;
    }
}

int cms_data_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_data_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_data_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_data_decode_stream(&s, ptr);
}
