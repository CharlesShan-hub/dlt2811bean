#include "svc/connection/cms_release.h"

/* ==================== Release-RequestPDU ==================== */
CMS_EXPORT int cms_release_request_encode(
    const cms_release_request_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 128);
    if (err) return CMS_ERR;
    cms_association_id_encode_stream(&w, &sdu->assoc_id);
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_release_request_decode(
    cms_release_request_t *sdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_association_id_decode_stream(&r, &sdu->assoc_id);
    return CMS_OK;
}

/* ==================== Release-ResponsePDU ==================== */
CMS_EXPORT int cms_release_response_encode(
    const cms_release_response_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 128);
    if (err) return CMS_ERR;
    cms_association_id_encode_stream(&w, &sdu->assoc_id);
    cms_service_error_encode_stream(&w, &sdu->service_error);
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_release_response_decode(
    cms_release_response_t *sdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_association_id_decode_stream(&r, &sdu->assoc_id);
    cms_service_error_decode_stream(&r, &sdu->service_error);
    return CMS_OK;
}

/* ==================== Release-ErrorPDU ==================== */
CMS_EXPORT int cms_release_error_encode(
    const cms_release_error_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 64);
    if (err) return CMS_ERR;
    cms_service_error_encode_stream(&w, &sdu->service_error);
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_release_error_decode(
    cms_release_error_t *sdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_service_error_decode_stream(&r, &sdu->service_error);
    return CMS_OK;
}
