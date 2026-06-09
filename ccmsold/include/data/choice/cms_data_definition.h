#ifndef DATA_CHOICE_CMS_DATA_DEFINITION_H
#define DATA_CHOICE_CMS_DATA_DEFINITION_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_boolean.h"
#include "data/common/cms_object_name.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/common/cms_service_error.h"
#include <stdlib.h>
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DataDefinition ::= CHOICE { ... (24 alternatives) }
 * ============================================================
 */
typedef struct cms_data_definition cms_data_definition_t;

typedef struct {
    cms_object_name_t            name;       /* VisibleString (0..64) */
    cms_functional_constraint_t  fc;         /* FunctionalConstraint (2 chars) */
    cms_boolean_t                has_fc;
    cms_data_definition_t       *type;       /* DataDefinition */
} cms_data_definition_member_t;

typedef struct {
    cms_int32_t                  numberOfElement;
    cms_data_definition_t       *elementType;
} cms_data_definition_array_t;

typedef struct {
    cms_data_definition_member_t *elements;
    cms_int32_t                   count;
} cms_data_definition_structure_t;

struct cms_data_definition {
    int32_t  choice;       /* 0 .. 23 */
    union {
        cms_service_error_t           error;            /*  0 */
        cms_data_definition_array_t   array;            /*  1 */
        cms_data_definition_structure_t structure;      /*  2 */
        cms_int32_t                   string_length;    /* 14-17 */
    } value;
};

CMS_EXPORT int cms_data_definition_encode(const cms_data_definition_t *def, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_data_definition_decode(cms_data_definition_t *def, const uint8_t *in_buf, int in_len);
int cms_data_definition_encode_stream(per_stream_t *s, const cms_data_definition_t *def);
int cms_data_definition_decode_stream(per_stream_t *s, cms_data_definition_t *def);
CMS_EXPORT void cms_data_definition_free(cms_data_definition_t *def);

#ifdef __cplusplus
}
#endif

#endif
