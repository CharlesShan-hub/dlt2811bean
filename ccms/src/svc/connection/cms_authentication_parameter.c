#include "svc/connection/cms_authentication_parameter.h"
#include "data/string/cms_octet_string.h"
#include "data/time/cms_utc_time.h"

int cms_authentication_parameter_encode_stream(
    per_stream_t *s, const cms_authentication_parameter_t *param)
{
    int err;

    /* 1. cert — OCTET STRING */
    err = cms_octet_string_encode_stream(s, param->cert, UINT32_MAX);
    if (err) return CMS_ERR;

    /* 2. signedTime — UTCTime */
    err = cms_utc_time_encode_stream(s, param->signed_time);
    if (err) return CMS_ERR;

    /* 3. sigVal — OCTET STRING */
    err = cms_octet_string_encode_stream(s, param->sig_val, UINT32_MAX);
    if (err) return CMS_ERR;

    return CMS_OK;
}

int cms_authentication_parameter_decode_stream(
    per_stream_t *s, cms_authentication_parameter_t *param)
{
    int err;

    /* 1. cert */
    err = cms_octet_string_decode_stream(s, param->cert, UINT32_MAX);
    if (err) return CMS_ERR;

    /* 2. signedTime */
    err = cms_utc_time_decode_stream(s, param->signed_time);
    if (err) return CMS_ERR;

    /* 3. sigVal */
    err = cms_octet_string_decode_stream(s, param->sig_val, UINT32_MAX);
    if (err) return CMS_ERR;

    return CMS_OK;
}

int cms_authentication_parameter_encode(
    const cms_authentication_parameter_t *param,
    uint8_t **out_buf, size_t *out_len)
{
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_authentication_parameter_encode_stream(&s, param);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_authentication_parameter_decode(
    cms_authentication_parameter_t *param,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_authentication_parameter_decode_stream(&s, param);
}
