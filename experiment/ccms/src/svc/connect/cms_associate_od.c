#include "svc/connect/cms_associate.h"
#include "svc/other/cms_apdu.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_boolean.h"
#include <string.h>
#include <stdlib.h>

static void utc_time_to_bytes(int64_t timestamp_ms, uint8_t bytes[8])
{
    uint64_t ms = (uint64_t)timestamp_ms;
    bytes[0] = (uint8_t)(ms >> 56);
    bytes[1] = (uint8_t)(ms >> 48);
    bytes[2] = (uint8_t)(ms >> 40);
    bytes[3] = (uint8_t)(ms >> 32);
    bytes[4] = (uint8_t)(ms >> 24);
    bytes[5] = (uint8_t)(ms >> 16);
    bytes[6] = (uint8_t)(ms >> 8);
    bytes[7] = (uint8_t)(ms);
}

static int64_t bytes_to_utc_time(const uint8_t bytes[8])
{
    return (int64_t)(
        ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48) |
        ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32) |
        ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16) |
        ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7]
    );
}

static void auth_param_encode(per_stream_t *w,
    const uint8_t *cert, int cert_len,
    int64_t signed_time,
    const uint8_t *sig_val, int sig_len)
{
    uint8_t utc_bytes[8];
    utc_time_to_bytes(signed_time, utc_bytes);
    per_encode_octet_string_unconstrained(w, cert, cert_len);
    per_encode_octet_string_fixed(w, utc_bytes, 8);
    per_encode_octet_string_unconstrained(w, sig_val, sig_len);
}

static void auth_param_decode(per_stream_t *r,
    uint8_t *cert, int *cert_cap,
    int64_t *signed_time,
    uint8_t *sig_val, int *sig_val_cap)
{
    size_t len;

    len = (size_t)*cert_cap;
    per_decode_octet_string_unconstrained(r, cert, &len);
    *cert_cap = (int)len;

    uint8_t utc_bytes[8];
    per_decode_octet_string_fixed(r, utc_bytes, 8);
    *signed_time = bytes_to_utc_time(utc_bytes);

    len = (size_t)*sig_val_cap;
    per_decode_octet_string_unconstrained(r, sig_val, &len);
    *sig_val_cap = (int)len;
}

/* ==================== Associate-RequestPDU ==================== */

CMS_EXPORT int cms_associate_request_encode(
    const char *sap_ref,
    int has_auth,
    const uint8_t *cert, int cert_len,
    int64_t signed_time,
    const uint8_t *sig_val, int sig_len,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[4096];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    int has_sap = (sap_ref != NULL && sap_ref[0] != '\0');

    per_stream_write_bits(&w, has_sap ? 1 : 0, 1);     // [0] serverAccessPointReference
    per_stream_write_bits(&w, has_auth ? 1 : 0, 1);     // [1] authenticationParameter

    if (has_sap) {
        per_encode_visible_string(&w, sap_ref, 129);
    }
    if (has_auth) {
        auth_param_encode(&w, cert, cert_len, signed_time, sig_val, sig_len);
    }

    size_t asdu_len = per_stream_bytes_written(&w);
    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, false, CMS_SVC_ASSOCIATE,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;
    *out_len = (int)apdu_len;
    return CMS_OK;
}

CMS_EXPORT int cms_associate_request_decode(
    const uint8_t *in_buf, int in_len,
    char *sap_ref, int *sap_ref_cap,
    int *has_auth,
    uint8_t *cert, int *cert_cap,
    int64_t *signed_time,
    uint8_t *sig_val, int *sig_val_cap)
{
    cms_apch_t apch;
    const uint8_t *asdu;
    size_t asdu_len;

    int ret = cms_apdu_decode(in_buf, (size_t)in_len, &apch, &asdu, &asdu_len);
    if (ret != 0) return CMS_ERR;

    per_stream_t r;
    per_stream_init_read(&r, asdu, asdu_len);

    uint64_t bit;
    per_stream_read_bits(&r, &bit, 1);
    int has_sap = (int)bit;
    per_stream_read_bits(&r, &bit, 1);
    *has_auth = (int)bit;

    if (has_sap) {
        per_decode_visible_string(&r, sap_ref, (uint32_t)*sap_ref_cap);
        *sap_ref_cap = (int)strlen(sap_ref);
    } else {
        sap_ref[0] = '\0';
        *sap_ref_cap = 0;
    }

    if (*has_auth) {
        auth_param_decode(&r, cert, cert_cap, signed_time, sig_val, sig_val_cap);
    } else {
        *cert_cap = 0;
        *sig_val_cap = 0;
        *signed_time = 0;
    }

    return CMS_OK;
}

/* ==================== Associate-ResponsePDU ==================== */

CMS_EXPORT int cms_associate_response_encode(
    const uint8_t *assoc_id, int assoc_id_len,
    int service_error,
    int has_auth,
    const uint8_t *cert, int cert_len,
    int64_t signed_time,
    const uint8_t *sig_val, int sig_len,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[4096];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    per_stream_write_bits(&w, has_auth ? 1 : 0, 1);     // [2] authenticationParameter OPTIONAL

    per_encode_octet_string(&w, assoc_id, assoc_id_len, 64);  // [0] associationId
    per_encode_constrained_int(&w, service_error, 0, 12);     // [1] serviceError

    if (has_auth) {
        auth_param_encode(&w, cert, cert_len, signed_time, sig_val, sig_len);
    }

    size_t asdu_len = per_stream_bytes_written(&w);
    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, true, CMS_SVC_ASSOCIATE,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;
    *out_len = (int)apdu_len;
    return CMS_OK;
}

CMS_EXPORT int cms_associate_response_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t *assoc_id, int *assoc_id_cap,
    int *service_error,
    int *has_auth,
    uint8_t *cert, int *cert_cap,
    int64_t *signed_time,
    uint8_t *sig_val, int *sig_val_cap)
{
    cms_apch_t apch;
    const uint8_t *asdu;
    size_t asdu_len;

    int ret = cms_apdu_decode(in_buf, (size_t)in_len, &apch, &asdu, &asdu_len);
    if (ret != 0) return CMS_ERR;

    per_stream_t r;
    per_stream_init_read(&r, asdu, asdu_len);

    uint64_t bit;
    per_stream_read_bits(&r, &bit, 1);
    *has_auth = (int)bit;

    size_t len = (size_t)*assoc_id_cap;
    per_decode_octet_string(&r, assoc_id, &len, 64);
    *assoc_id_cap = (int)len;

    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 12);
    *service_error = (int)tmp;

    if (*has_auth) {
        auth_param_decode(&r, cert, cert_cap, signed_time, sig_val, sig_val_cap);
    } else {
        *cert_cap = 0;
        *sig_val_cap = 0;
        *signed_time = 0;
    }

    return CMS_OK;
}

/* ==================== Associate-ErrorPDU ==================== */

CMS_EXPORT int cms_associate_error_encode(
    int service_error,
    uint8_t *out_buf, int *out_len)
{
    uint8_t asdu_buf[64];
    per_stream_t w;
    per_stream_init_write(&w, asdu_buf, sizeof(asdu_buf));

    per_encode_constrained_int(&w, service_error, 0, 12);

    size_t asdu_len = per_stream_bytes_written(&w);
    size_t apdu_len = 0;
    int ret = cms_apdu_encode(out_buf, (size_t)*out_len, &apdu_len,
                              false, true, CMS_SVC_ASSOCIATE,
                              asdu_buf, asdu_len);
    if (ret != 0) return CMS_ERR_BUF_TOO_SMALL;
    *out_len = (int)apdu_len;
    return CMS_OK;
}

CMS_EXPORT int cms_associate_error_decode(
    const uint8_t *in_buf, int in_len,
    int *service_error)
{
    cms_apch_t apch;
    const uint8_t *asdu;
    size_t asdu_len;

    int ret = cms_apdu_decode(in_buf, (size_t)in_len, &apch, &asdu, &asdu_len);
    if (ret != 0) return CMS_ERR;

    per_stream_t r;
    per_stream_init_read(&r, asdu, asdu_len);

    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 12);
    *service_error = (int)tmp;

    return CMS_OK;
}
