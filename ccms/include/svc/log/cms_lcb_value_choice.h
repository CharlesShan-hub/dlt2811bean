#ifndef CMS_LCB_VALUE_CHOICE_H
#define CMS_LCB_VALUE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_service_error.h"
#include "data/block/cms_lcb.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LCBValueChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT LCB
 * }
 *
 * Used by GetLCBValues response.
 * ============================================================
 */

#define CMS_LCB_VALUE_CHOICE_ERROR  0
#define CMS_LCB_VALUE_CHOICE_VALUE  1

typedef struct {
    cms_enumerated_t    *choice;
    cms_service_error_t *alt_error;
    cms_lcb_t           *alt_value;
} cms_lcb_value_choice_t;

int cms_lcb_value_choice_encode_stream(per_stream_t *s, const cms_lcb_value_choice_t *v);
int cms_lcb_value_choice_decode_stream(per_stream_t *s, cms_lcb_value_choice_t *v);

#ifdef __cplusplus
}
#endif

#endif
