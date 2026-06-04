#ifndef DATA_COMMON_CMS_QUALITY_H
#define DATA_COMMON_CMS_QUALITY_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Quality (BIT STRING, 13 bits)
 * ============================================================
 */
CMS_EXPORT int cms_quality_encode(const cms_bit_string_fixed_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_quality_decode(const uint8_t *in_buf, int in_len, cms_bit_string_fixed_t *v);
int cms_quality_encode_stream(per_stream_t *s, const cms_bit_string_fixed_t *v);
int cms_quality_decode_stream(per_stream_t *s, cms_bit_string_fixed_t *v);

/*
 * ============================================================
 * DBPos
 * ============================================================
 */
 typedef enum {
    CMS_DBPOS_INTERMEDIATE = 0,
    CMS_DBPOS_OFF          = 1,
    CMS_DBPOS_ON           = 2,
    CMS_DBPOS_BAD_STATE   = 3
} cms_dbpos_t;

CMS_EXPORT int cms_dbpos_encode(cms_dbpos_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_dbpos_decode(const uint8_t *in_buf, int in_len, cms_dbpos_t *value);
int cms_dbpos_encode_stream(per_stream_t *s, cms_dbpos_t value);
int cms_dbpos_decode_stream(per_stream_t *s, cms_dbpos_t *value);

/*
 * ============================================================
 * TCMD
 * ============================================================
 */
 typedef enum {
    CMS_TCMD_RESERVED = 0,
    CMS_TCMD_SELECT   = 1,
    CMS_TCMD_OPERATE  = 2,
    CMS_TCMD_CANCEL   = 3
} cms_tcmd_t;

CMS_EXPORT int cms_tcmd_encode(cms_tcmd_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_tcmd_decode(const uint8_t *in_buf, int in_len, cms_tcmd_t *value);
int cms_tcmd_encode_stream(per_stream_t *s, cms_tcmd_t value);
int cms_tcmd_decode_stream(per_stream_t *s, cms_tcmd_t *value);

/*
 * ============================================================
 * ServiceError
 * ============================================================
 */
 typedef enum {
    CMS_SERVICE_ERROR_NO_ERROR                         = 0,
    CMS_SERVICE_ERROR_INSTANCE_NOT_AVAILABLE           = 1,
    CMS_SERVICE_ERROR_INSTANCE_IN_USE                  = 2,
    CMS_SERVICE_ERROR_ACCESS_VIOLATION                 = 3,
    CMS_SERVICE_ERROR_ACCESS_NOT_ALLOWED_IN_CURRENT_STATE = 4,
    CMS_SERVICE_ERROR_PARAMETER_VALUE_INAPPROPRIATE    = 5,
    CMS_SERVICE_ERROR_PARAMETER_VALUE_INCONSISTENT     = 6,
    CMS_SERVICE_ERROR_CLASS_NOT_SUPPORTED              = 7,
    CMS_SERVICE_ERROR_INSTANCE_LOCKED_BY_OTHER_CLIENT  = 8,
    CMS_SERVICE_ERROR_CONTROL_MUST_BE_SELECTED         = 9,
    CMS_SERVICE_ERROR_TYPE_CONFLICT                    = 10,
    CMS_SERVICE_ERROR_FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT = 11,
    CMS_SERVICE_ERROR_FAILED_DUE_TO_SERVER_CONSTRAINT  = 12
} cms_service_error_t;

CMS_EXPORT int cms_service_error_encode(cms_service_error_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_service_error_decode(const uint8_t *in_buf, int in_len, cms_service_error_t *value);
int cms_service_error_encode_stream(per_stream_t *s, cms_service_error_t value);
int cms_service_error_decode_stream(per_stream_t *s, cms_service_error_t *value);

#ifdef __cplusplus
}
#endif

#endif
