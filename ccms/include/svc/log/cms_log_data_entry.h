#ifndef CMS_LOG_DATA_ENTRY_H
#define CMS_LOG_DATA_ENTRY_H

#include "cms_types.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/block/cms_reason_code.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LogDataEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     fc          [1] IMPLICIT FunctionalConstraint,
 *     value       [2] IMPLICIT Data,
 *     reason      [3] IMPLICIT ReasonCode
 * }
 *
 * Used by LogEntry entryData.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t    *reference;
    cms_functional_constraint_t *fc;
    cms_data_t                *value;
    cms_reason_code_t         *reason;
} cms_log_data_entry_t;

int cms_log_data_entry_encode_stream(per_stream_t *s, const cms_log_data_entry_t *v);
int cms_log_data_entry_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
