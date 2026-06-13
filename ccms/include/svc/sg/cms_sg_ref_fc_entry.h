#ifndef CMS_SG_REF_FC_ENTRY_H
#define CMS_SG_REF_FC_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SGRefFcEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint
 * }
 *
 * Used by GetEditSGValue Request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t    *reference;
    cms_functional_constraint_t *fc;
} cms_sg_ref_fc_entry_t;

#ifdef __cplusplus
}
#endif

#endif
