#ifndef CMS_REFERENCE_CHOICE_H
#define CMS_REFERENCE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_object_reference.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReferenceChoice ::= CHOICE {
 *     ldName        [0] IMPLICIT ObjectName,
 *     lnReference   [1] IMPLICIT ObjectReference
 * }
 *
 * Used by GetLogicalNodeDirectory, GetAllDataValues,
 * GetAllDataDefinition, GetAllCBValues.
 *
 * Flat all-pointer layout:
 * [choice* (8B)] [alt* (8B)]
 * ============================================================
 */

#define CMS_REFERENCE_CHOICE_LD_NAME      0
#define CMS_REFERENCE_CHOICE_LN_REFERENCE 1

typedef struct {
    cms_enumerated_t        *choice;       /* 0=ldName, 1=lnReference */
    cms_object_name_t       *alt_ld_name;
    cms_object_reference_t  *alt_ln_reference;
} cms_reference_choice_t;

int cms_reference_choice_encode_stream(
    per_stream_t *s, const cms_reference_choice_t *v);
int cms_reference_choice_decode_stream(
    per_stream_t *s, cms_reference_choice_t *v);

CMS_EXPORT int cms_reference_choice_encode(
    const cms_reference_choice_t *v,
    uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_reference_choice_decode(
    cms_reference_choice_t *v,
    const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
