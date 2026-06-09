#include "data/choice/cms_data.h"
#include "per/cms_choice.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_float.h"
#include "data/extended/cms_time.h"
#include <string.h>
#include <stdlib.h>

int cms_data_encode_stream(per_stream_t *s, const cms_data_t *data)
{
    int rc;
    rc = per_encode_choice(s, (uint32_t)data->choice);
    if (rc) return rc;

    if (data->choice == 1 || data->choice == 2) {
        int32_t count = (data->choice == 1) ? data->value.array.count.value : data->value.structure.count.value;
        cms_data_t *elems = (data->choice == 1) ? data->value.array.elements : data->value.structure.elements;
        per_encode_length(s, (uint32_t)count);
        for (int32_t i = 0; i < count; i++){
            rc = cms_data_encode_stream(s, &elems[i]);
            if (rc) return rc;
        }
        return CMS_OK;
    }

    switch (data->choice) {
    case 0:  rc = cms_service_error_encode_stream(s, &data->value.error); break;
    case 3:  rc = cms_boolean_encode_stream(s, &data->value.boolean_value); break;
    case 4:  rc = cms_int8_encode_stream(s, &data->value.int8); break;
    case 5:  rc = cms_int16_encode_stream(s, &data->value.int16); break;
    case 6:  rc = cms_int32_encode_stream(s, &data->value.int32); break;
    case 7:  rc = cms_int64_encode_stream(s, &data->value.int64); break;
    case 8:  rc = cms_int8u_encode_stream(s, &data->value.int8u); break;
    case 9:  rc = cms_int16u_encode_stream(s, &data->value.int16u); break;
    case 10: rc = cms_int32u_encode_stream(s, &data->value.int32u); break;
    case 11: rc = cms_int64u_encode_stream(s, &data->value.int64u); break;
    case 12: rc = cms_float32_encode_stream(s, &data->value.float32); break;
    case 13: rc = cms_float64_encode_stream(s, &data->value.float64); break;
    case 14: per_encode_bit_string_unconstrained(s, data->value.bit_string.value, data->value.bit_string.len); rc = CMS_OK; break;
    case 15: per_encode_octet_string_unconstrained(s, data->value.octet_string.value, data->value.octet_string.len); rc = CMS_OK; break;
    case 16: per_encode_visible_string_unconstrained(s, data->value.visible_string.value); rc = CMS_OK; break;
    case 17: per_encode_utf8_string_unconstrained(s, data->value.utf8_string.value); rc = CMS_OK; break;
    case 18: rc = cms_utc_time_encode_stream(s, &data->value.utc_time); break;
    case 19: rc = cms_binary_time_encode_stream(s, &data->value.binary_time); break;
    case 20: rc = cms_quality_encode_stream(s, &data->value.quality); break;
    case 21: rc = cms_dbpos_encode_stream(s, &data->value.dbpos); break;
    case 22: rc = cms_tcmd_encode_stream(s, &data->value.tcmd); break;
    case 23: rc = cms_check_encode_stream(s, &data->value.check); break;
    }
    return rc;
}

int cms_data_decode_stream(per_stream_t *s, cms_data_t *data)
{
    int rc;
    uint32_t idx;
    rc = per_decode_choice(s, &idx);
    if (rc) return rc;
    data->choice = (int32_t)idx;

    if (data->choice == 1 || data->choice == 2) {
        uint32_t count;
        per_decode_length(s, &count);
        cms_data_t *elems = NULL;
        if (count) {
            elems = (cms_data_t *)calloc(count, sizeof(cms_data_t));
            if (!elems) return CMS_ERR;
            for (uint32_t i = 0; i < count; i++){
                rc = cms_data_decode_stream(s, &elems[i]);
                if (rc) return rc;
            }
        }
        if (data->choice == 1) {
            data->value.array.count.value = (int32_t)count;
            data->value.array.elements = elems;
        } else {
            data->value.structure.count.value = (int32_t)count;
            data->value.structure.elements = elems;
        }
        return CMS_OK;
    }

    switch (data->choice) {
    case 0:  rc = cms_service_error_decode_stream(s, &data->value.error); break;
    case 3:  rc = cms_boolean_decode_stream(s, &data->value.boolean_value); break;
    case 4:  rc = cms_int8_decode_stream(s, &data->value.int8); break;
    case 5:  rc = cms_int16_decode_stream(s, &data->value.int16); break;
    case 6:  rc = cms_int32_decode_stream(s, &data->value.int32); break;
    case 7:  rc = cms_int64_decode_stream(s, &data->value.int64); break;
    case 8:  rc = cms_int8u_decode_stream(s, &data->value.int8u); break;
    case 9:  rc = cms_int16u_decode_stream(s, &data->value.int16u); break;
    case 10: rc = cms_int32u_decode_stream(s, &data->value.int32u); break;
    case 11: rc = cms_int64u_decode_stream(s, &data->value.int64u); break;
    case 12: rc = cms_float32_decode_stream(s, &data->value.float32); break;
    case 13: rc = cms_float64_decode_stream(s, &data->value.float64); break;
    case 14: {
        int64_t len;
        rc = per_decode_semi_constrained(s, &len, 0);
        if (rc) return rc;
        data->value.bit_string.len = (int32_t)len;
        int nbytes = ((int32_t)len + 7) / 8;
        data->value.bit_string.value = (uint8_t *)malloc(nbytes ? (size_t)nbytes : 1);
        if (!data->value.bit_string.value) return CMS_ERR;
        per_stream_align(s);
        per_decode_bit_string_fixed(s, data->value.bit_string.value, (int32_t)len);
        rc = CMS_OK;
        break;
    }
    case 15: {
        uint32_t len;
        per_decode_length(s, &len);
        data->value.octet_string.len = (int32_t)len;
        data->value.octet_string.value = (uint8_t *)malloc(len ? (size_t)len : 1);
        if (!data->value.octet_string.value) return CMS_ERR;
        if (len) {
            per_stream_align(s);
            per_stream_read_bytes(s, data->value.octet_string.value, len);
        }
        rc = CMS_OK;
        break;
    }
    case 16: {
        uint32_t len;
        per_decode_length(s, &len);
        data->value.visible_string.value = (uint8_t *)malloc((size_t)(len + 1));
        if (!data->value.visible_string.value) return CMS_ERR;
        if (len) {
            per_stream_align(s);
            for (uint32_t i = 0; i < len; i++) {
                uint64_t ch;
                per_stream_read_bits(s, &ch, 8);
                data->value.visible_string.value[i] = (uint8_t)ch;
            }
        }
        data->value.visible_string.value[len] = '\0';
        data->value.visible_string.len = (int32_t)len;
        rc = CMS_OK;
        break;
    }
    case 17: {
        uint32_t len;
        per_decode_length(s, &len);
        data->value.utf8_string.value = (uint8_t *)malloc((size_t)(len + 1));
        if (!data->value.utf8_string.value) return CMS_ERR;
        if (len) {
            per_stream_align(s);
            per_stream_read_bytes(s, data->value.utf8_string.value, len);
        }
        data->value.utf8_string.value[len] = '\0';
        data->value.utf8_string.len = (int32_t)len;
        rc = CMS_OK;
        break;
    }
    case 18: rc = cms_utc_time_decode_stream(s, &data->value.utc_time); break;
    case 19: rc = cms_binary_time_decode_stream(s, &data->value.binary_time); break;
    case 20: rc = cms_quality_decode_stream(s, &data->value.quality); break;
    case 21: rc = cms_dbpos_decode_stream(s, &data->value.dbpos); break;
    case 22: rc = cms_tcmd_decode_stream(s, &data->value.tcmd); break;
    case 23: rc = cms_check_decode_stream(s, &data->value.check); break;
    }
    return rc;
}

CMS_EXPORT int cms_data_encode(const cms_data_t *data, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    int rc = cms_data_encode_stream(&w, data);
    *out_len = (int)per_stream_bytes_written(&w);
    return rc;
}

CMS_EXPORT int cms_data_decode(cms_data_t *data, const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    return cms_data_decode_stream(&r, data);
}

CMS_EXPORT void cms_data_free(cms_data_t *data)
{
    if (!data) return;
    if (data->choice == 1 || data->choice == 2) {
        int32_t count = (data->choice == 1) ? data->value.array.count.value : data->value.structure.count.value;
        cms_data_t *elems = (data->choice == 1) ? data->value.array.elements : data->value.structure.elements;
        for (int32_t i = 0; i < count; i++)
            cms_data_free(&elems[i]);
        free(elems);
        return;
    }
    if (data->choice == 14) { free(data->value.bit_string.value); data->value.bit_string.value = NULL; }
    else if (data->choice == 15) { free(data->value.octet_string.value); data->value.octet_string.value = NULL; }
    else if (data->choice == 16) { free(data->value.visible_string.value); data->value.visible_string.value = NULL; }
    else if (data->choice == 17) { free(data->value.utf8_string.value); data->value.utf8_string.value = NULL; }
}

CMS_EXPORT int cms_data_choice_encode(int32_t choice, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    int rc = per_encode_choice(&w, (uint32_t)choice);
    *out_len = (int)per_stream_bytes_written(&w);
    return rc;
}

CMS_EXPORT int cms_data_count_encode(int32_t count, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    int rc = per_encode_length(&w, (uint32_t)count);
    *out_len = (int)per_stream_bytes_written(&w);
    return rc;
}
