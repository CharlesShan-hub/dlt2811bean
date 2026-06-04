#include "svc/connection/cms_authentication_parameter.h"
#include "data/basic/cms_string.h"
#include "data/extended/cms_time.h"
#include <string.h>

/* ---- internal stream version ---- */

int cms_authentication_parameter_encode_stream(
    per_stream_t *s, const cms_authentication_parameter_t *param)
{
    cms_utc_time_t utc;
    uint64_t _u = (uint64_t)param->signed_time_ms;
    utc.seconds_since_epoch.value  = (uint32_t)(_u / 1000);
    utc.fraction_of_second.value   = (uint32_t)(((_u % 1000) * 16777216) / 1000);
    utc.time_quality.tagf.value    = 0;
    utc.time_quality.precision.value = 0;
    utc.time_quality.fraction.value = 0;
    { cms_octet_string_var_t _v = { .value = (uint8_t *)param->cert, .len = param->cert_len, .max_len = CMS_MAX_CERT_LEN };
      cms_octet_string_var_encode_stream(s, &_v); }
    cms_utc_time_encode_stream(s, &utc);
    { cms_octet_string_var_t _v = { .value = (uint8_t *)param->sig_val, .len = param->sig_len, .max_len = CMS_MAX_CERT_LEN };
      cms_octet_string_var_encode_stream(s, &_v); }
    return CMS_OK;
}

int cms_authentication_parameter_decode_stream(
    per_stream_t *s, cms_authentication_parameter_t *param)
{
    cms_utc_time_t utc;
    { cms_octet_string_var_t _v = { .value = param->cert, .len = param->cert_len, .max_len = CMS_MAX_CERT_LEN };
      cms_octet_string_var_decode_stream(s, &_v);
      param->cert_len = _v.len; }
    cms_utc_time_decode_stream(s, &utc);
    param->signed_time_ms = (int64_t)utc.seconds_since_epoch.value * 1000
        + (int64_t)(((uint64_t)utc.fraction_of_second.value * 1000) / 16777216);
    { cms_octet_string_var_t _v = { .value = param->sig_val, .len = param->sig_len, .max_len = CMS_MAX_CERT_LEN };
      cms_octet_string_var_decode_stream(s, &_v);
      param->sig_len = _v.len; }
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
