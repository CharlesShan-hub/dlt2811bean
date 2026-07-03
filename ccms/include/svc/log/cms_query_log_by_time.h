#ifndef CMS_QUERY_LOG_BY_TIME_H
#define CMS_QUERY_LOG_BY_TIME_H

#include "svc/cms_svc.h"
#include "svc/log/cms_log_entry.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * QueryLogByTime-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     stopTime        [2] IMPLICIT EntryTime OPTIONAL,
 *     entryAfter      [3] IMPLICIT EntryID OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_object_reference_t *log_reference;
    cms_boolean_t *start_time_present;
    cms_entry_time_t *start_time;
    cms_boolean_t *stop_time_present;
    cms_entry_time_t *stop_time;
    cms_boolean_t *entry_after_present;
    cms_entry_id_t *entry_after;
} cms_query_log_by_time_request_t;

/*
 * ============================================================
 * QueryLogByTime-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *log_entry;      /* SEQUENCE OF LogEntry */
    cms_boolean_t *more_follows; /* DEFAULT TRUE */
} cms_query_log_by_time_response_t;

/*
 * ============================================================
 * QueryLogByTime-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_query_log_by_time_error_t;

CMS_EXPORT int cms_query_log_by_time_request_encode(const cms_query_log_by_time_request_t *pdu, uint8_t **out_buf,
                                                    size_t *out_len);

CMS_EXPORT int cms_query_log_by_time_request_decode(cms_query_log_by_time_request_t *pdu, const uint8_t *in_buf,
                                                    int in_len);

CMS_EXPORT int cms_query_log_by_time_response_encode(const cms_query_log_by_time_response_t *pdu, uint8_t **out_buf,
                                                     size_t *out_len);

CMS_EXPORT int cms_query_log_by_time_response_decode(cms_query_log_by_time_response_t *pdu, const uint8_t *in_buf,
                                                     int in_len);

CMS_EXPORT int cms_query_log_by_time_error_encode(const cms_query_log_by_time_error_t *pdu, uint8_t **out_buf,
                                                  size_t *out_len);

CMS_EXPORT int cms_query_log_by_time_error_decode(cms_query_log_by_time_error_t *pdu, const uint8_t *in_buf,
                                                  int in_len);

#ifdef __cplusplus
}
#endif

#endif
