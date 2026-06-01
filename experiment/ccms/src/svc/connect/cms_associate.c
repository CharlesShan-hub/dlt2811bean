#include "svc/connect/cms_associate.h"
#include "data/basic/cms_string.h"
#include "data/extended/cms_time.h"
#include "per/cms_stream.h"
#include <string.h>
#include <stdlib.h>

static void cms_server_access_point_encode(per_stream_t *w, const char *sap_ref)
{
    cms_visible_string_encode_stream(w, sap_ref);
}

static void cms_authentication_parameter_encode(
    per_stream_t *w,
    const uint8_t *cert, int cert_len,
    int64_t signed_time,
    const uint8_t *sig_val, int sig_len)
{
    cms_octet_string_encode_stream(w, cert, cert_len);
    cms_utc_time_encode_stream(w, signed_time);
    cms_octet_string_encode_stream(w, sig_val, sig_len);
}

static void cms_server_access_point_decode(per_stream_t *r, char *sap_ref)
{
    cms_visible_string_decode_stream(r, sap_ref);
}

static void cms_authentication_parameter_decode(
    per_stream_t *r,
    uint8_t *cert, int *cert_cap,
    int64_t *signed_time,
    uint8_t *sig_val, int *sig_val_cap)
{
    cms_octet_string_decode_stream(r, cert, cert_cap);
    cms_utc_time_decode_stream(r, signed_time);
    cms_octet_string_decode_stream(r, sig_val, sig_val_cap);
}

CMS_EXPORT int cms_associate_request_encode(
    const char *sap_ref,
    int has_auth,
    const uint8_t *cert, int cert_len,
    int64_t signed_time,
    const uint8_t *sig_val, int sig_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 512);
    if (err) return CMS_ERR;

    int has_sap = (sap_ref != NULL && sap_ref[0] != '\0');
    per_stream_write_bits(&w, has_sap ? 1 : 0, 1);
    per_stream_write_bits(&w, has_auth ? 1 : 0, 1);

    if (has_sap) {
        cms_server_access_point_encode(&w, sap_ref);
    }
    if (has_auth) {
        cms_authentication_parameter_encode(&w, cert, cert_len, signed_time, sig_val, sig_len);
    }

    size_t asdu_len;
    uint8_t *data = per_stream_detach(&w, &asdu_len);
    if ((size_t)*out_len < asdu_len) {
        free(data);
        return CMS_ERR_BUF_TOO_SMALL;
    }
    memcpy(out_buf, data, asdu_len);
    *out_len = (int)asdu_len;
    free(data);
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
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint64_t bit;
    per_stream_read_bits(&r, &bit, 1);
    int has_sap = (int)bit;
    per_stream_read_bits(&r, &bit, 1);
    *has_auth = (int)bit;

    if (has_sap) {
        cms_server_access_point_decode(&r, sap_ref);
        *sap_ref_cap = (int)strlen(sap_ref);
    } else {
        sap_ref[0] = '\0';
        *sap_ref_cap = 0;
    }

    if (*has_auth) {
        cms_authentication_parameter_decode(&r, cert, cert_cap, signed_time, sig_val, sig_val_cap);
    } else {
        *cert_cap = 0;
        *signed_time = 0;
        *sig_val_cap = 0;
    }

    return CMS_OK;
}
