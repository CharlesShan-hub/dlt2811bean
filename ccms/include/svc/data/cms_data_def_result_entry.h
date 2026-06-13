#ifndef CMS_DATA_DEF_RESULT_ENTRY_H
#define CMS_DATA_DEF_RESULT_ENTRY_H

#include "cms_types.h"
#include "data/choice/cms_data_definition.h"
#include "data/string/cms_uint8_array.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DataDefResultEntry ::= SEQUENCE {
 *     cdcType       [0] IMPLICIT VisibleString OPTIONAL,
 *     definition    [1] IMPLICIT DataDefinition
 * }
 *
 * Used by GetDataDefinition Response.
 * ============================================================
 */
typedef struct {
    cms_boolean_t        *cdc_type_present;
    cms_uint8_array_t    *cdc_type;
    cms_data_definition_t *definition;
} cms_data_def_result_entry_t;

#ifdef __cplusplus
}
#endif

#endif
