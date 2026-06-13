#ifndef CMS_GOCB_VALUE_CHOICE_H
#define CMS_GOCB_VALUE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_service_error.h"
#include "data/block/cms_go_cb.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GoCBValueChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT GoCB
 * }
 *
 * Used by GetGoCBValues response.
 * ============================================================
 */

#define CMS_GOCB_VALUE_CHOICE_ERROR  0
#define CMS_GOCB_VALUE_CHOICE_VALUE  1

typedef struct {
    cms_enumerated_t    *choice;
    cms_service_error_t *alt_error;
    cms_go_cb_t         *alt_value;
} cms_gocb_value_choice_t;

#ifdef __cplusplus
}
#endif

#endif
