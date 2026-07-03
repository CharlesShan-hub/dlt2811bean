#ifndef CMS_DATA_VALUE_ENTRY_H
#define CMS_DATA_VALUE_ENTRY_H

#include "cms_types.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_sub_reference.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DataValueEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     value         [1] IMPLICIT Data
 * }
 *
 * Used by GetAllDataValues response.
 * ============================================================
 */
typedef struct {
    cms_sub_reference_t *reference;
    cms_data_t *value;
} cms_data_value_entry_t;

int cms_data_value_entry_encode_stream(per_stream_t *s, const cms_data_value_entry_t *v);
int cms_data_value_entry_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
