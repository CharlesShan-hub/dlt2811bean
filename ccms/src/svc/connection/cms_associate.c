#include "svc/connection/cms_associate.h"
#include "svc/connection/cms_authentication_parameter.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_boolean.h"
#include <string.h>

/*
 * ============================================================
 * Associate-RequestPDU ::= SEQUENCE {
 *     serverAccessPointReference  [0] IMPLICIT VisibleString129 OPTIONAL,
 *     authenticationParameter     [1] IMPLICIT SEQUENCE { ... } OPTIONAL
 * }
 *
 * PER encoding (non-extensible SEQUENCE with OPTIONAL fields):
 *   1. bit — serverAccessPointReference present?
 *   2. bit — authenticationParameter present?
 *   3. if present: VisibleString129 (IMPLICIT [0] — no tag, raw value)
 *   4. if present: AuthenticationParameter (IMPLICIT [1] — no tag, raw SEQUENCE)
 * ============================================================
 */
CMS_EXPORT int cms_associate_request_encode(
    const cms_associate_request_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 512);
    if (err) return CMS_ERR;
    int rc;
    rc = cms_boolean_encode_stream(&w, &sdu->sap_ref_present);
    if (rc) return rc;
    rc = cms_boolean_encode_stream(&w, &sdu->auth_param_present);
    if (rc) return rc;
    if (sdu->sap_ref_present.value) {
        cms_visible_string_var_t _sap = { sdu->sap_ref.value, CMS_SAP_REF_MAX_LEN };
        rc = cms_visible_string_var_encode_stream(&w, &_sap);
        if (rc) return rc;
    }
    if (sdu->auth_param_present.value) {
        rc = cms_authentication_parameter_encode_stream(&w, &sdu->auth_param);
        if (rc) return rc;
    }
    return cms_write_out(&w, out_buf, out_len);
}

CMS_EXPORT int cms_associate_request_decode(
    cms_associate_request_t *sdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int rc;
    cms_boolean_t has_sap = {0};
    /* presence bits */
    rc = cms_boolean_decode_stream(&r, &has_sap);
    if (rc) return rc;
    rc = cms_boolean_decode_stream(&r, &sdu->auth_param_present);
    if (rc) return rc;
    sdu->sap_ref_present.value = has_sap.value;
    if (sdu->sap_ref_present.value) {
        cms_visible_string_var_t _sap = { sdu->sap_ref.value, CMS_SAP_REF_MAX_LEN };
        rc = cms_visible_string_var_decode_stream(&r, &_sap);
        if (rc) return rc;
        sdu->sap_ref.len = (int32_t)strlen((const char *)sdu->sap_ref.value);
    } else {
        sdu->sap_ref.value = NULL;
        sdu->sap_ref.len = 0;
    }
    if (sdu->auth_param_present.value) {
        rc = cms_authentication_parameter_decode_stream(&r, &sdu->auth_param);
        if (rc) return rc;
    }
    return CMS_OK;
}

/*
 * ============================================================
 * Associate-ResponsePDU ::= SEQUENCE {
 *     associationId               [0] IMPLICIT OCTET STRING (SIZE(0..64)),
 *     serviceError                [1] IMPLICIT ServiceError,
 *     authenticationParameter     [2] IMPLICIT SEQUENCE { ... } OPTIONAL
 * }
 * ============================================================
 */
CMS_EXPORT int cms_associate_response_encode(
    const cms_associate_response_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 512);
    if (err) return CMS_ERR;
    int rc;
    /* presence bit for OPTIONAL auth_param */
    rc = cms_boolean_encode_stream(&w, &sdu->auth_param_present);
    if (rc) return rc;
    /* mandatory fields */
    rc = cms_association_id_encode_stream(&w, &sdu->assoc_id);
    if (rc) return rc;
    rc = cms_service_error_encode_stream(&w, &sdu->service_error);
    if (rc) return rc;
    /* optional auth_param */
    if (sdu->auth_param_present.value) {
        rc = cms_authentication_parameter_encode_stream(&w, &sdu->auth_param);
        if (rc) return rc;
    }
    return cms_write_out(&w, out_buf, out_len);
}

CMS_EXPORT int cms_associate_response_decode(
    cms_associate_response_t *sdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int rc;
    /* presence bit for OPTIONAL auth_param */
    rc = cms_boolean_decode_stream(&r, &sdu->auth_param_present);
    if (rc) return rc;
    /* mandatory fields */
    rc = cms_association_id_decode_stream(&r, &sdu->assoc_id);
    if (rc) return rc;
    rc = cms_service_error_decode_stream(&r, &sdu->service_error);
    if (rc) return rc;
    /* optional auth_param */
    if (sdu->auth_param_present.value) {
        rc = cms_authentication_parameter_decode_stream(&r, &sdu->auth_param);
        if (rc) return rc;
    }
    return CMS_OK;
}

/*
 * ============================================================
 * Associate-ErrorPDU ::= ServiceError
 * ============================================================
 */
CMS_EXPORT int cms_associate_error_encode(
    const cms_associate_error_t *sdu,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    int err = per_stream_init_dynamic(&w, 64);
    if (err) return CMS_ERR;
    int rc = cms_service_error_encode_stream(&w, &sdu->service_error);
    if (rc) return rc;
    return cms_write_out(&w, out_buf, out_len);
}

CMS_EXPORT int cms_associate_error_decode(
    cms_associate_error_t *sdu,
    const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int rc = cms_service_error_decode_stream(&r, &sdu->service_error);
    if (rc) return rc;
    return CMS_OK;
}
