#include "svc/directory/cms_get_server_directory.h"

/* ==================== GetServerDirectory-RequestPDU ==================== */
CMS_EXPORT int cms_get_server_directory_request_encode(
    const cms_get_server_directory_request_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 128);
    if (err) return CMS_ERR;
    per_encode_constrained_int(&w, sdu->object_class, 0, 2);
    per_stream_write_bits(&w, sdu->has_ref_after ? 1 : 0, 1);
    if (sdu->has_ref_after) {
        cms_object_reference_encode_stream(&w, sdu->ref_after);
    }
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_get_server_directory_request_decode(
    const uint8_t *in_buf, int in_len,
    cms_get_server_directory_request_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 2);
    sdu->object_class = (int)tmp;
    uint64_t bit;
    per_stream_read_bits(&r, &bit, 1);
    sdu->has_ref_after = (int)bit;
    if (sdu->has_ref_after) {
        cms_object_reference_decode_stream(&r, sdu->ref_after);
    } else {
        sdu->ref_after[0] = '\0';
    }
    return CMS_OK;
}

/* ==================== GetServerDirectory-ResponsePDU ==================== */
CMS_EXPORT int cms_get_server_directory_response_encode(
    const cms_get_server_directory_response_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 512);
    if (err) return CMS_ERR;
    uint32_t count = (uint32_t)sdu->ref_count;
    per_encode_length(&w, count);
    int offset = 0;
    for (int i = 0; i < sdu->ref_count; i++) {
        cms_object_reference_encode_stream(&w, sdu->refs_flat + offset);
        offset += sdu->ref_lens[i] + 1;
    }
    per_stream_write_bits(&w, (sdu->more_follows == 0) ? 1 : 0, 1);
    if (sdu->more_follows == 0) {
        cms_boolean_encode_stream(&w, 0);
    }
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_get_server_directory_response_decode(
    const uint8_t *in_buf, int in_len,
    cms_get_server_directory_response_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint32_t count;
    per_decode_length(&r, &count);
    if ((int)count > CMS_MAX_REF_COUNT) count = (uint32_t)CMS_MAX_REF_COUNT;
    sdu->ref_count = (int)count;
    int offset = 0;
    for (uint32_t i = 0; i < count; i++) {
        cms_object_reference_decode_stream(&r, sdu->refs_flat + offset);
        int len = (int)strlen(sdu->refs_flat + offset);
        sdu->ref_lens[i] = len;
        offset += len + 1;
    }
    uint64_t bit;
    per_stream_read_bits(&r, &bit, 1);
    if (bit == 0) {
        sdu->more_follows = 1;
    } else {
        int val;
        cms_boolean_decode_stream(&r, &val);
        sdu->more_follows = val;
    }
    return CMS_OK;
}

/* ==================== GetServerDirectory-ErrorPDU ==================== */
CMS_EXPORT int cms_get_server_directory_error_encode(
    const cms_get_server_directory_error_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 64);
    if (err) return CMS_ERR;
    cms_service_error_encode_stream(&w, sdu->service_error);
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_get_server_directory_error_decode(
    const uint8_t *in_buf, int in_len,
    cms_get_server_directory_error_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_service_error_decode_stream(&r, &sdu->service_error);
    return CMS_OK;
}