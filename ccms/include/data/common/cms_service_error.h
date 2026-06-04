#ifndef DATA_COMMON_CMS_SERVICE_ERROR_H
#define DATA_COMMON_CMS_SERVICE_ERROR_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ServiceError ::= INTEGER { ... (0..12) }
 * ============================================================
 */
#define CMS_SERVICE_ERROR_NO_ERROR                         0
#define CMS_SERVICE_ERROR_INSTANCE_NOT_AVAILABLE           1
#define CMS_SERVICE_ERROR_INSTANCE_IN_USE                  2
#define CMS_SERVICE_ERROR_ACCESS_VIOLATION                 3
#define CMS_SERVICE_ERROR_ACCESS_NOT_ALLOWED_IN_CURRENT_STATE  4
#define CMS_SERVICE_ERROR_PARAMETER_VALUE_INAPPROPRIATE    5
#define CMS_SERVICE_ERROR_PARAMETER_VALUE_INCONSISTENT     6
#define CMS_SERVICE_ERROR_CLASS_NOT_SUPPORTED              7
#define CMS_SERVICE_ERROR_INSTANCE_LOCKED_BY_OTHER_CLIENT  8
#define CMS_SERVICE_ERROR_CONTROL_MUST_BE_SELECTED         9
#define CMS_SERVICE_ERROR_TYPE_CONFLICT                   10
#define CMS_SERVICE_ERROR_FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT 11
#define CMS_SERVICE_ERROR_FAILED_DUE_TO_SERVER_CONSTRAINT  12

typedef struct { cms_int32_t value; } cms_service_error_t;

CMS_EXPORT int cms_service_error_encode(const cms_service_error_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_service_error_decode(cms_service_error_t *v, const uint8_t *in_buf, int in_len);
int cms_service_error_encode_stream(per_stream_t *s, const cms_service_error_t *v);
int cms_service_error_decode_stream(per_stream_t *s, cms_service_error_t *v);

#ifdef __cplusplus
}
#endif

#endif
