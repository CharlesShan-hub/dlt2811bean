#include "svc/connect/cms_authentication_parameter.h"
#include "data/basic/cms_string.h"
#include "data/extended/cms_time.h"

/* ---- internal stream version ---- */

int cms_authentication_parameter_encode_stream(
    per_stream_t *s, const cms_authentication_parameter_t *param)
{
    cms_utc_time_t utc;
    cms_utc_time_from_ms(&utc, param->signed_time_ms);
    cms_octet_string_encode_stream(s, param->cert, param->cert_len, CMS_MAX_CERT_LEN);
    cms_utc_time_encode_stream(s, &utc);
    cms_octet_string_encode_stream(s, param->sig_val, param->sig_len, CMS_MAX_CERT_LEN);
    return CMS_OK;
}

int cms_authentication_parameter_decode_stream(
    per_stream_t *s, cms_authentication_parameter_t *param)
{
    cms_utc_time_t utc;
    cms_octet_string_decode_stream(s, param->cert, &param->cert_len, CMS_MAX_CERT_LEN);
    cms_utc_time_decode_stream(s, &utc);
    param->signed_time_ms = cms_utc_time_to_ms(&utc);
    cms_octet_string_decode_stream(s, param->sig_val, &param->sig_len, CMS_MAX_CERT_LEN);
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_authentication_parameter_encode(
    const cms_authentication_parameter_t *param,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    cms_authentication_parameter_encode_stream(&w, param);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_authentication_parameter_decode(
    const uint8_t *in_buf, int in_len,
    cms_authentication_parameter_t *param)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    return cms_authentication_parameter_decode_stream(&r, param);
}
