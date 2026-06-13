#ifndef CMS_SG_REF_VALUE_ENTRY_H
#define CMS_SG_REF_VALUE_ENTRY_H

#include "cms_types.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SGRefValueEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     value         [2] IMPLICIT Data
 * }
 *
 * Used by SetEditSGValue Request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t *reference;
    cms_data_t             *value;
} cms_sg_ref_value_entry_t;

#ifdef __cplusplus
}
#endif

#endif
