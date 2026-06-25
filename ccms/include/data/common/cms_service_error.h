#ifndef CMS_COMMON_SERVICE_ERROR_H
#define CMS_COMMON_SERVICE_ERROR_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ServiceError ::= INTEGER (0..12)  —  7.3.11
 * PER: constrained integer (0..12), 4 bits
 */

#define CMS_SERVICE_ERROR_NO_ERROR                                  0
#define CMS_SERVICE_ERROR_INSTANCE_NOT_AVAILABLE                    1
#define CMS_SERVICE_ERROR_INSTANCE_IN_USE                           2
#define CMS_SERVICE_ERROR_ACCESS_VIOLATION                          3
#define CMS_SERVICE_ERROR_ACCESS_NOT_ALLOWED_IN_CURRENT_STATE       4
#define CMS_SERVICE_ERROR_PARAMETER_VALUE_INAPPROPRIATE             5
#define CMS_SERVICE_ERROR_PARAMETER_VALUE_INCONSISTENT              6
#define CMS_SERVICE_ERROR_CLASS_NOT_SUPPORTED                       7
#define CMS_SERVICE_ERROR_INSTANCE_LOCKED_BY_OTHER_CLIENT           8
#define CMS_SERVICE_ERROR_CONTROL_MUST_BE_SELECTED                  9
#define CMS_SERVICE_ERROR_TYPE_CONFLICT                            10
#define CMS_SERVICE_ERROR_FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT  11
#define CMS_SERVICE_ERROR_FAILED_DUE_TO_SERVER_CONSTRAINT          12

typedef cms_enumerated_t cms_service_error_t;

int cms_service_error_encode_stream(per_stream_t *s, const void *ptr);
int cms_service_error_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_service_error_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_service_error_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
