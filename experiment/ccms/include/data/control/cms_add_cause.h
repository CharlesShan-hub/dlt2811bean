#ifndef DATA_CONTROL_CMS_ADD_CAUSE_H
#define DATA_CONTROL_CMS_ADD_CAUSE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * AddCause
 * ============================================================
 */
typedef enum {
    CMS_ADD_CAUSE_UNKNOWN                   = 0,
    CMS_ADD_CAUSE_PROCESS_ERROR             = 1,
    CMS_ADD_CAUSE_PROTOCOL_ERROR            = 2,
    CMS_ADD_CAUSE_APPLICATION_ERROR         = 3,
    CMS_ADD_CAUSE_PERFORMANCE_LIMITATION    = 4,
    CMS_ADD_CAUSE_RESOURCE_LIMITATION       = 5,
    CMS_ADD_CAUSE_AUTHENTICATION_FAILURE    = 6,
    CMS_ADD_CAUSE_SECURITY_VIOLATION        = 7,
    CMS_ADD_CAUSE_COMMUNICATION_FAILURE     = 8,
    CMS_ADD_CAUSE_SYSTEM_FAILURE            = 9,
    CMS_ADD_CAUSE_HARDWARE_FAILURE          = 10,
    CMS_ADD_CAUSE_SOFTWARE_FAILURE          = 11,
    CMS_ADD_CAUSE_CONFIGURATION_ERROR       = 12,
    CMS_ADD_CAUSE_OPERATION_NOT_SUPPORTED   = 13,
    CMS_ADD_CAUSE_OPERATION_BLOCKED         = 14,
    CMS_ADD_CAUSE_TEMPORARY_FAILURE         = 15,
    CMS_ADD_CAUSE_PERMANENT_FAILURE         = 16
} cms_add_cause_t;

CMS_EXPORT int cms_add_cause_encode(cms_add_cause_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_add_cause_decode(const uint8_t *in_buf, int in_len, cms_add_cause_t *value);
int cms_add_cause_encode_stream(per_stream_t *s, cms_add_cause_t value);
int cms_add_cause_decode_stream(per_stream_t *s, cms_add_cause_t *value);

#ifdef __cplusplus
}
#endif

#endif
