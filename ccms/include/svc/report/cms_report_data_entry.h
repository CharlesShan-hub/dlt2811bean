#ifndef CMS_REPORT_DATA_ENTRY_H
#define CMS_REPORT_DATA_ENTRY_H

#include "cms_types.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/block/cms_reason_code.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReportDataEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference OPTIONAL,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     id            [2] IMPLICIT INT16U,
 *     value         [3] IMPLICIT Data,
 *     reason        [4] IMPLICIT ReasonCode OPTIONAL
 * }
 *
 * Used by ReportPDU entryData.
 * ============================================================
 */
typedef struct {
    cms_boolean_t *ref_present;
    cms_object_reference_t *reference;
    cms_boolean_t *fc_present;
    cms_functional_constraint_t *fc;
    cms_int16u_t *id;
    cms_data_t *value;
    cms_boolean_t *reason_present;
    cms_reason_code_t *reason;
} cms_report_data_entry_t;

int cms_report_data_entry_encode_stream(per_stream_t *s, const cms_report_data_entry_t *v);
int cms_report_data_entry_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
