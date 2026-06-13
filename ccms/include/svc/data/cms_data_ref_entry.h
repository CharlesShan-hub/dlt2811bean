#ifndef CMS_DATA_REF_ENTRY_H
#define CMS_DATA_REF_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DataRefEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 * }
 *
 * Used by GetDataValues Request, GetDataDefinition Request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t    *reference;
    cms_boolean_t             *fc_present;
    cms_functional_constraint_t *fc;
} cms_data_ref_entry_t;

#ifdef __cplusplus
}
#endif

#endif
