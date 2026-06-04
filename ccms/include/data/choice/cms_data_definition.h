#ifndef DATA_CHOICE_CMS_DATA_DEFINITION_H
#define DATA_CHOICE_CMS_DATA_DEFINITION_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/common/cms_object_name.h"
#include "data/fc/cms_fc.h"
#include "data/common/cms_quality.h"
#include <stdlib.h>
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * cms_data_definition_member_t — a field in a structure definition
 * ============================================================
 */
typedef struct cms_data_definition_member {
    cms_visible_string_var_t     name;                          /* ObjectName (0..64) */
    uint8_t                      fc[3];                         /* FunctionalConstraint (2 chars) */
    int                          has_fc;                        /* 1 = fc present */
    struct cms_data_definition  *type;                          /* DataDefinition */
} cms_data_definition_member_t;

/*
 * ============================================================
 * cms_data_definition_t — tagged union for DataDefinition CHOICE
 *
 *   error (0)       → value.error
 *   array (1)       → value.array
 *   structure (2)   → value.structure
 *   boolean~check   → value is NULL (just choice tag, no payload)
 *   bit-string (14) → value.string_length (max bits)
 *   octet-string(15) → value.string_length (max bytes)
 *   visible-str (16) → value.string_length (max chars)
 *   unicode-str (17) → value.string_length (max chars)
 * ============================================================
 */
typedef struct cms_data_definition {
    int      choice;       /* 0 .. 23 */
    union {
        cms_service_error_t error;                        /*  0 */
        struct {                                           /*  1 */
            int32_t                      numberOfElement;
            struct cms_data_definition  *elementType;
        } array;
        struct {                                           /*  2 */
            cms_data_definition_member_t *elements;
            int                          count;
        } structure;
        int              string_length;                   /* 14-17 */
    } value;
} cms_data_definition_t;

/*
 * ============================================================
 * Struct-based encode / decode / free
 * ============================================================
 */
CMS_EXPORT int cms_data_definition_encode(const cms_data_definition_t *def, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_data_definition_decode(const uint8_t *in_buf, int in_len, cms_data_definition_t *def);

int cms_data_definition_encode_stream(per_stream_t *s, const cms_data_definition_t *def);
int cms_data_definition_decode_stream(per_stream_t *s, cms_data_definition_t *def);

CMS_EXPORT void cms_data_definition_free(cms_data_definition_t *def);

#ifdef __cplusplus
}
#endif

#endif
