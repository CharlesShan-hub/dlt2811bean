#ifndef CMS_RCB_VALUE_CHOICE_H
#define CMS_RCB_VALUE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_service_error.h"
#include "data/block/cms_brcb.h"
#include "data/block/cms_urcb.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RCBValueChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT BRCB
 * }
 *
 * Used by GetBRCBValues/GetURCBValues response.
 * (URCB uses the same structure with urcb field.)
 * ============================================================
 */

#define CMS_RCB_VALUE_CHOICE_ERROR  0
#define CMS_RCB_VALUE_CHOICE_VALUE  1

typedef struct {
    cms_enumerated_t    *choice;
    cms_service_error_t *alt_error;
    cms_brcb_t          *alt_value;      /* BRCB or URCB in same slot */
} cms_rcb_value_choice_t;

#ifdef __cplusplus
}
#endif

#endif
