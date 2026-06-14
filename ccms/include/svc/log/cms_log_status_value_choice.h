#ifndef CMS_LOG_STATUS_VALUE_CHOICE_H
#define CMS_LOG_STATUS_VALUE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_service_error.h"
#include "svc/log/cms_log_status_value.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LogStatusValueChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT LogStatusValue
 * }
 *
 * Used by GetLogStatusValues response.
 * ============================================================
 */

#define CMS_LOG_STATUS_VALUE_CHOICE_ERROR  0
#define CMS_LOG_STATUS_VALUE_CHOICE_VALUE  1

typedef struct {
    cms_enumerated_t     *choice;
    cms_service_error_t  *alt_error;
    cms_log_status_value_t *alt_value;
} cms_log_status_value_choice_t;

int cms_log_status_value_choice_encode_stream(per_stream_t *s, const cms_log_status_value_choice_t *v);
int cms_log_status_value_choice_decode_stream(per_stream_t *s, cms_log_status_value_choice_t *v);

#ifdef __cplusplus
}
#endif

#endif
