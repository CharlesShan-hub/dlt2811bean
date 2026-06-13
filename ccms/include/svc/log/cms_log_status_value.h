#ifndef CMS_LOG_STATUS_VALUE_H
#define CMS_LOG_STATUS_VALUE_H

#include "cms_types.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LogStatusValue ::= SEQUENCE {
 *     oldEntrTm   [0] IMPLICIT EntryTime,
 *     newEntrTm   [1] IMPLICIT EntryTime,
 *     oldEntr     [2] IMPLICIT EntryID,
 *     newEntr     [3] IMPLICIT EntryID
 * }
 *
 * Used by GetLogStatusValues response.
 * ============================================================
 */
typedef struct {
    cms_entry_time_t  *old_entr_tm;
    cms_entry_time_t  *new_entr_tm;
    cms_entry_id_t    *old_entr;
    cms_entry_id_t    *new_entr;
} cms_log_status_value_t;

#ifdef __cplusplus
}
#endif

#endif
