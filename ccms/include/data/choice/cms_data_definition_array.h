#ifndef CMS_CHOICE_DATA_DEFINITION_ARRAY_H
#define CMS_CHOICE_DATA_DEFINITION_ARRAY_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_int32.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Array alternative inside DataDefinition ::= SEQUENCE {
 *     numberOfElement  [1] IMPLICIT Int32,
 *     elementType      [2] DataDefinition
 * }
 *
 * All-pointer layout (sizeof = 16):
 *   [0] numberOfElement  → cms_int32_t*
 *   [8] elementType      → cms_data_definition_t*
 */

/* Forward declaration */
typedef struct cms_data_definition_s cms_data_definition_t;

typedef struct {
    cms_int32_t *numberOfElement;
    cms_data_definition_t *elementType;
} cms_data_definition_array_t;

int cms_data_definition_array_encode_stream(per_stream_t *s, const void *ptr);
int cms_data_definition_array_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
