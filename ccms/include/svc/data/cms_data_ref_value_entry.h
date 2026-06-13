#ifndef CMS_DATA_REF_VALUE_ENTRY_H
#define CMS_DATA_REF_VALUE_ENTRY_H

#include "cms_types.h"
#include "svc/data/cms_data_ref_entry.h"
#include "data/choice/cms_data.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DataRefValueEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     value         [2] IMPLICIT Data
 * }
 *
 * Used by SetDataValues Request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t    *reference;
    cms_boolean_t             *fc_present;
    cms_functional_constraint_t *fc;
    cms_data_t                *value;
} cms_data_ref_value_entry_t;

#ifdef __cplusplus
}
#endif

#endif
