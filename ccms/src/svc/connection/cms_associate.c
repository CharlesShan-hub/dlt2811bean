#include "svc/connection/cms_associate.h"
#include "data/basic/cms_string.h"

static void cms_server_access_point_encode(per_stream_t *w, const char *sap_ref)
{
    cms_visible_string_encode_stream(w, sap_ref, 64);
}
static void cms_authentication_parameter_encode(
    per_stream_t *w,
    const uint8_t *cert, int cert_len,
    int64_t signed_time_ms,
    const uint8_t *sig_val, int sig_len)
{
    cms_utc_time_t utc;
    cms_utc_time_from_ms(&utc, signed_time_ms);
    cms_octet_string_encode_stream(w, cert, cert_len, CMS_MAX_CERT_LEN);
    cms_utc_time_encode_stream(w, &utc);
    cms_octet_string_encode_stream(w, sig_val, sig_len, CMS_MAX_CERT_LEN);
}
static void cms_server_access_point_decode(per_stream_t *r, char *sap_ref)
{
    cms_visible_string_decode_stream(r, sap_ref, 64);
}
static void cms_authentication_parameter_decode(
    per_stream_t *r,
    uint8_t *cert, int *cert_cap,
    int64_t *signed_time_ms,
    uint8_t *sig_val, int *sig_val_cap)
{
    cms_utc_time_t utc;
    cms_octet_string_decode_stream(r, cert, cert_cap, CMS_MAX_CERT_LEN);
    cms_utc_time_decode_stream(r, &utc);
    *signed_time_ms = cms_utc_time_to_ms(&utc);
    cms_octet_string_decode_stream(r, sig_val, sig_val_cap, CMS_MAX_CERT_LEN);
}

/* ==================== Associate-RequestPDU ==================== */
CMS_EXPORT int cms_associate_request_encode(
    const cms_associate_request_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 512);
    if (err) return CMS_ERR;
    int has_sap = (sdu->sap_ref[0] != '\0');
    per_encode_optional(&w, has_sap);
    per_encode_optional(&w, sdu->has_auth);
    if (has_sap) {
        cms_server_access_point_encode(&w, sdu->sap_ref);
    }
    if (sdu->has_auth) {
        cms_authentication_parameter_encode(&w,
            sdu->cert, sdu->cert_len,
            sdu->signed_time,
            sdu->sig_val, sdu->sig_len);
    }
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_associate_request_decode(
    const uint8_t *in_buf, int in_len,
    cms_associate_request_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int has_sap = per_decode_optional(&r);
    sdu->has_auth = per_decode_optional(&r);
    if (has_sap) {
        cms_server_access_point_decode(&r, sdu->sap_ref);
    } else {
        sdu->sap_ref[0] = '\0';
    }
    if (sdu->has_auth) {
        cms_authentication_parameter_decode(&r,
            sdu->cert, &sdu->cert_len,
            &sdu->signed_time,
            sdu->sig_val, &sdu->sig_len);
    } else {
        sdu->cert_len = 0;
        sdu->signed_time = 0;
        sdu->sig_len = 0;
    }
    return CMS_OK;
}

/* ==================== Associate-ResponsePDU ==================== */
CMS_EXPORT int cms_associate_response_encode(
    const cms_associate_response_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 512);
    if (err) return CMS_ERR;
    per_encode_optional(&w, sdu->has_auth);
    cms_association_id_encode_stream(&w, &sdu->assoc_id);
    cms_service_error_encode_stream(&w, sdu->service_error);
    if (sdu->has_auth) {
        cms_authentication_parameter_encode(&w,
            sdu->cert, sdu->cert_len,
            sdu->signed_time,
            sdu->sig_val, sdu->sig_len);
    }
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_associate_response_decode(
    const uint8_t *in_buf, int in_len,
    cms_associate_response_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    sdu->has_auth = per_decode_optional(&r);
    cms_association_id_decode_stream(&r, &sdu->assoc_id);
    cms_service_error_decode_stream(&r, &sdu->service_error);
    if (sdu->has_auth) {
        cms_authentication_parameter_decode(&r,
            sdu->cert, &sdu->cert_len,
            &sdu->signed_time,
            sdu->sig_val, &sdu->sig_len);
    } else {
        sdu->cert_len = 0;
        sdu->signed_time = 0;
        sdu->sig_len = 0;
    }
    return CMS_OK;
}

/* ==================== Associate-ErrorPDU ==================== */
CMS_EXPORT int cms_associate_error_encode(
    const cms_associate_error_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 64);
    if (err) return CMS_ERR;
    cms_service_error_encode_stream(&w, sdu->service_error);
    return cms_write_out(&w, out_buf, out_len);
}
CMS_EXPORT int cms_associate_error_decode(
    const uint8_t *in_buf, int in_len,
    cms_associate_error_t *sdu)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    cms_service_error_decode_stream(&r, &sdu->service_error);
    return CMS_OK;
}