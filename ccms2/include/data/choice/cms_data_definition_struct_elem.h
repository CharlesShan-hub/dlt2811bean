#ifndef CMS_CHOICE_DATA_DEFINITION_STRUCT_ELEM_H
#define CMS_CHOICE_DATA_DEFINITION_STRUCT_ELEM_H

#include "cms_types.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_object_name.h"
#include "data/fc/cms_functional_constraint.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Element of Structure alternative inside DataDefinition ::= SEQUENCE {
 *     name   [0] IMPLICIT ObjectName,
 *     fc     [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     type   [2] DataDefinition
 * }
 *
 * All-pointer layout (sizeof = 32):
 *   [0] name    → cms_object_name_t*
 *   [8] fc_present → cms_boolean_t*
 *   [16] fc     → cms_functional_constraint_t*
 *   [24] type   → cms_data_definition_t*
 */

/* Forward declaration */
typedef struct cms_data_definition_s cms_data_definition_t;

typedef struct {
    cms_object_name_t            *name;           /* ObjectName */
    cms_boolean_t                *fc_present;
    cms_functional_constraint_t  *fc;             /* FunctionalConstraint OPTIONAL */
    cms_data_definition_t        *type;           /* DataDefinition */
} cms_data_definition_struct_elem_t;

#ifdef __cplusplus
}
#endif

#endif
