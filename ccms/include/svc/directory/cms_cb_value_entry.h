#ifndef CMS_CB_VALUE_ENTRY_H
#define CMS_CB_VALUE_ENTRY_H

#include "cms_types.h"
#include "svc/directory/cms_cb_value_choice.h"
#include "data/common/cms_sub_reference.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * CBValueEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     value         [1] IMPLICIT CBValue
 * }
 *
 * Used by GetAllCBValues response.
 * ============================================================
 */
typedef struct {
    cms_sub_reference_t   *reference;
    cms_cb_value_choice_t *value;
} cms_cb_value_entry_t;

#ifdef __cplusplus
}
#endif

#endif
