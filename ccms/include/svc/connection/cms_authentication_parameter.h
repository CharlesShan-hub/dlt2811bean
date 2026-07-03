#ifndef CMS_AUTHENTICATION_PARAMETER_H
#define CMS_AUTHENTICATION_PARAMETER_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/string/cms_uint8_array.h"
#include "data/time/cms_utc_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * AuthenticationParameter ::= SEQUENCE {
 *     signatureCertificate  [0] IMPLICIT OCTET STRING,
 *     signedTime            [1] IMPLICIT UtcTime,
 *     signedValue           [2] IMPLICIT OCTET STRING
 * }
 * ============================================================
 */
typedef struct {
    cms_uint8_array_t *cert;
    cms_utc_time_t    *signed_time;
    cms_uint8_array_t *sig_val;
} cms_authentication_parameter_t;

CMS_EXPORT int cms_authentication_parameter_encode(
    const cms_authentication_parameter_t *param,
    uint8_t *out_buf, size_t *out_len);

CMS_EXPORT int cms_authentication_parameter_decode(
    cms_authentication_parameter_t *param,
    const uint8_t *in_buf, int in_len);

int cms_authentication_parameter_encode_stream(
    per_stream_t *s, const cms_authentication_parameter_t *param);
int cms_authentication_parameter_decode_stream(
    per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
