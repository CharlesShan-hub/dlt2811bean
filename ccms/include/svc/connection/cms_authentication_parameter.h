#ifndef CMS_AUTHENTICATION_PARAMETER_H
#define CMS_AUTHENTICATION_PARAMETER_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/extended/cms_time.h"

#ifdef __cplusplus
extern "C" {
#endif

#ifndef CMS_MAX_CERT_LEN
#define CMS_MAX_CERT_LEN 2048
#endif

/*
 * ============================================================
 * AuthenticationParameter — SEQUENCE used in Associate
 *
 *   signatureCertificate   OCTET STRING,
 *   signedTime             UtcTime,
 *   signedValue            OCTET STRING
 * ============================================================
 */
typedef struct {
    uint8_t  cert[CMS_MAX_CERT_LEN];
    int      cert_len;
    int64_t  signed_time_ms;    /* milliseconds since epoch */
    uint8_t  sig_val[CMS_MAX_CERT_LEN];
    int      sig_len;
} cms_authentication_parameter_t;

CMS_EXPORT int cms_authentication_parameter_encode(
    const cms_authentication_parameter_t *param,
    uint8_t *out_buf, int *out_len);

CMS_EXPORT int cms_authentication_parameter_decode(
    const uint8_t *in_buf, int in_len,
    cms_authentication_parameter_t *param);

int cms_authentication_parameter_encode_stream(
    per_stream_t *s, const cms_authentication_parameter_t *param);
int cms_authentication_parameter_decode_stream(
    per_stream_t *s, cms_authentication_parameter_t *param);

#ifdef __cplusplus
}
#endif

#endif
