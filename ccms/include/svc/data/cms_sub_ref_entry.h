#ifndef CMS_SUB_REF_ENTRY_H
#define CMS_SUB_REF_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_sub_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SubRefEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 * }
 *
 * Used by GetDataDirectory Response.
 * ============================================================
 */
typedef struct {
    cms_sub_reference_t       *reference;
    cms_boolean_t             *fc_present;
    cms_functional_constraint_t *fc;
} cms_sub_ref_entry_t;

int cms_sub_ref_entry_encode_stream(per_stream_t *s, const cms_sub_ref_entry_t *v);
int cms_sub_ref_entry_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
