#ifndef CMS_REPORT_ENTRY_H
#define CMS_REPORT_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "data/scalar/cms_boolean.h"
#include "svc/report/cms_report_data_entry.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReportEntry ::= SEQUENCE {
 *     timeOfEntry   [0] IMPLICIT EntryTime OPTIONAL,
 *     entryID       [1] IMPLICIT EntryID OPTIONAL,
 *     entryData     [2] IMPLICIT SEQUENCE OF ReportDataEntry
 * }
 *
 * Used by ReportPDU.
 * ============================================================
 */
typedef struct {
    cms_boolean_t      *time_of_entry_present;
    cms_entry_time_t   *time_of_entry;
    cms_boolean_t      *entry_id_present;
    cms_entry_id_t     *entry_id;
    cms_array_t        *entry_data;     /* SEQUENCE OF ReportDataEntry */
} cms_report_entry_t;

int cms_report_entry_encode_stream(per_stream_t *s, const cms_report_entry_t *v);
int cms_report_entry_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
