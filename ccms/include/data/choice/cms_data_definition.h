#ifndef CMS_CHOICE_DATA_DEFINITION_H
#define CMS_CHOICE_DATA_DEFINITION_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/scalar/cms_int32.h"
#include "data/common/cms_service_error.h"
#include "data/choice/cms_data_definition_array.h"
#include "data/choice/cms_data_definition_struct_elem.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * DataDefinition ::= CHOICE { ... }  —  7.7
 *
 * Flat all-pointer layout.  Alternatives marked IMPLICIT NULL in ASN.1
 * carry no payload (the choice selector alone is sufficient).
 *
 * Only these alternatives have data:
 *   [0]  error       → ServiceError
 *   [1]  array       → DataDefinitionArray
 *   [2]  structure   → SEQUENCE OF DataDefinitionStructElem
 *   [14] bit-string  → INTEGER (max bit length)
 *   [15] octet-string → INTEGER (max byte length)
 *   [16] visible-string → INTEGER (max char length)
 *   [17] unicode-string → INTEGER (max char length)
 */

/* ── selector values ── */
#define CMS_DATA_DEFINITION_CHOICE_ERROR            0
#define CMS_DATA_DEFINITION_CHOICE_ARRAY            1
#define CMS_DATA_DEFINITION_CHOICE_STRUCTURE        2
#define CMS_DATA_DEFINITION_CHOICE_BOOLEAN          3
#define CMS_DATA_DEFINITION_CHOICE_INT8             4
#define CMS_DATA_DEFINITION_CHOICE_INT16            5
#define CMS_DATA_DEFINITION_CHOICE_INT32            6
#define CMS_DATA_DEFINITION_CHOICE_INT64            7
#define CMS_DATA_DEFINITION_CHOICE_INT8U            8
#define CMS_DATA_DEFINITION_CHOICE_INT16U           9
#define CMS_DATA_DEFINITION_CHOICE_INT32U          10
#define CMS_DATA_DEFINITION_CHOICE_INT64U          11
#define CMS_DATA_DEFINITION_CHOICE_FLOAT32         12
#define CMS_DATA_DEFINITION_CHOICE_FLOAT64         13
#define CMS_DATA_DEFINITION_CHOICE_BIT_STRING      14
#define CMS_DATA_DEFINITION_CHOICE_OCTET_STRING    15
#define CMS_DATA_DEFINITION_CHOICE_VISIBLE_STRING  16
#define CMS_DATA_DEFINITION_CHOICE_UNICODE_STRING  17
#define CMS_DATA_DEFINITION_CHOICE_UTC_TIME        18
#define CMS_DATA_DEFINITION_CHOICE_BINARY_TIME     19
#define CMS_DATA_DEFINITION_CHOICE_QUALITY         20
#define CMS_DATA_DEFINITION_CHOICE_DBPOS           21
#define CMS_DATA_DEFINITION_CHOICE_TCMD            22
#define CMS_DATA_DEFINITION_CHOICE_CHECK           23

typedef struct cms_data_definition_s {
    cms_enumerated_t                   *choice;         /* selector, 0..23 */

    /* [0] error */
    cms_service_error_t                *alt_error;

    /* [1] array */
    cms_data_definition_array_t        *alt_array;

    /* [2] structure — SEQUENCE OF struct_elem via cms_array_t */
    cms_array_t                        *alt_structure;  /* { void** elements; int32_t count; } */

    /* [14..17] bit-string / octet-string / visible-string / unicode-string — INTEGER max length */
    cms_int32_t                        *alt_bit_string_len;
    cms_int32_t                        *alt_octet_string_len;
    cms_int32_t                        *alt_visible_string_len;
    cms_int32_t                        *alt_unicode_string_len;
} cms_data_definition_t;

int cms_data_definition_encode_stream(per_stream_t *s, const void *ptr);
int cms_data_definition_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_data_definition_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_data_definition_decode(void *ptr, const uint8_t *in_buf, int in_len);

/** Initialise a cms_data_definition_t (zero/NULL all fields). */
void cms_data_definition_init(cms_data_definition_t *d);

#ifdef __cplusplus
}
#endif

#endif
