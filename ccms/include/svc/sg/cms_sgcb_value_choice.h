#ifndef CMS_SGCB_VALUE_CHOICE_H
#define CMS_SGCB_VALUE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_service_error.h"
#include "data/block/cms_sgcb.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SGCBValue ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT SGCB
 * }
 *
 * Used by GetSGCBValues response.
 * ============================================================
 */

#define CMS_SGCB_VALUE_CHOICE_ERROR  0
#define CMS_SGCB_VALUE_CHOICE_VALUE  1

typedef struct {
    cms_enumerated_t   *choice;
    cms_service_error_t *alt_error;
    cms_sgcb_t          *alt_value;
} cms_sgcb_value_choice_t;

int cms_sgcb_value_choice_encode_stream(per_stream_t *s, const cms_sgcb_value_choice_t *v);
int cms_sgcb_value_choice_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
