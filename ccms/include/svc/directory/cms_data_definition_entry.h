#ifndef CMS_DATA_DEFINITION_ENTRY_H
#define CMS_DATA_DEFINITION_ENTRY_H

#include "cms_types.h"
#include "data/choice/cms_data_definition.h"
#include "data/common/cms_sub_reference.h"
#include "data/string/cms_uint8_array.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DataDefinitionEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     cdcType       [1] IMPLICIT VisibleString OPTIONAL,
 *     definition    [2] IMPLICIT DataDefinition
 * }
 *
 * Used by GetAllDataDefinition response.
 * ============================================================
 */
typedef struct {
    cms_sub_reference_t        *reference;
    cms_boolean_t              *cdc_type_present;
    cms_uint8_array_t          *cdc_type;
    cms_data_definition_t      *definition;
} cms_data_definition_entry_t;

int cms_data_definition_entry_encode_stream(per_stream_t *s, const cms_data_definition_entry_t *v);
int cms_data_definition_entry_decode_stream(per_stream_t *s, cms_data_definition_entry_t *v);

#ifdef __cplusplus
}
#endif

#endif
