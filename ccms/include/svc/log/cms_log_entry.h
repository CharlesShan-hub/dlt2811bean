#ifndef CMS_LOG_ENTRY_H
#define CMS_LOG_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "svc/log/cms_log_data_entry.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LogEntry ::= SEQUENCE {
 *     timeOfEntry     [0] IMPLICIT EntryTime,
 *     entryID         [1] IMPLICIT EntryID,
 *     entryData       [2] IMPLICIT SEQUENCE OF LogDataEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_entry_time_t  *time_of_entry;
    cms_entry_id_t    *entry_id;
    cms_array_t       *entry_data;    /* SEQUENCE OF LogDataEntry */
} cms_log_entry_t;

#ifdef __cplusplus
}
#endif

#endif
