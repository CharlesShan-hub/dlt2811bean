#ifndef CMS_MSVCB_VALUE_CHOICE_H
#define CMS_MSVCB_VALUE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_service_error.h"
#include "data/block/cms_msvcb.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * MSVCBValueChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT MSVCB
 * }
 *
 * Used by GetMSVCBValues response.
 * ============================================================
 */

#define CMS_MSVCB_VALUE_CHOICE_ERROR  0
#define CMS_MSVCB_VALUE_CHOICE_VALUE  1

typedef struct {
    cms_enumerated_t    *choice;
    cms_service_error_t *alt_error;
    cms_msvcb_t         *alt_value;
} cms_msvcb_value_choice_t;

int cms_msvcb_value_choice_encode_stream(per_stream_t *s, const cms_msvcb_value_choice_t *v);
int cms_msvcb_value_choice_decode_stream(per_stream_t *s, cms_msvcb_value_choice_t *v);

#ifdef __cplusplus
}
#endif

#endif
