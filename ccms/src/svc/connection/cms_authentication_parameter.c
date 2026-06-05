#include "svc/connection/cms_authentication_parameter.h"
#include "data/basic/cms_string.h"
#include "data/extended/cms_time.h"
#include <string.h>

int cms_authentication_parameter_encode_stream(
    per_stream_t *s, const cms_authentication_parameter_t *param)
{
    int rc;
    cms_octet_string_var_t _cert = { param->cert.value, param->cert.len, CMS_MAX_CERT_LEN };
    rc = cms_octet_string_var_encode_stream(s, &_cert);
    if (rc) return rc;
    cms_utc_time_t utc;
    uint64_t _u = (uint64_t)param->signed_time_ms.value;
    utc.seconds_since_epoch.value  = (uint32_t)(_u / 1000);
    utc.fraction_of_second.value   = (uint32_t)(((_u % 1000) * 16777216) / 1000);
    utc.time_quality.tagf.value    = 0;
    utc.time_quality.precision.value = 0;
    utc.time_quality.fraction.value = 0;
    rc = cms_utc_time_encode_stream(s, &utc);
    if (rc) return rc;
    cms_octet_string_var_t _sig = { param->sig_val.value, param->sig_val.len, CMS_MAX_CERT_LEN };
    rc = cms_octet_string_var_encode_stream(s, &_sig);
    if (rc) return rc;
    return CMS_OK;
}

int cms_authentication_parameter_decode_stream(
    per_stream_t *s, cms_authentication_parameter_t *param)
{
    int rc;
    cms_octet_string_var_t _cert = { param->cert.value, param->cert.len, CMS_MAX_CERT_LEN };
    rc = cms_octet_string_var_decode_stream(s, &_cert);
    if (rc) return rc;
    param->cert.len = _cert.len;
    cms_utc_time_t utc;
    rc = cms_utc_time_decode_stream(s, &utc);
    if (rc) return rc;
    param->signed_time_ms.value = (int64_t)utc.seconds_since_epoch.value * 1000
        + (int64_t)(((uint64_t)utc.fraction_of_second.value * 1000) / 16777216);
    cms_octet_string_var_t _sig = { param->sig_val.value, param->sig_val.len, CMS_MAX_CERT_LEN };
    rc = cms_octet_string_var_decode_stream(s, &_sig);
    if (rc) return rc;
    param->sig_val.len = _sig.len;
    return CMS_OK;
}

CMS_EXPORT int cms_authentication_parameter_encode(
    const cms_authentication_parameter_t *param,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len);
    int rc = cms_authentication_parameter_encode_stream(&w, param);
    *out_len = (int)per_stream_bytes_written(&w);
    return rc;
}

CMS_EXPORT int cms_authentication_parameter_decode(
    cms_authentication_parameter_t *param,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len);
    return cms_authentication_parameter_decode_stream(&r, param);
}
