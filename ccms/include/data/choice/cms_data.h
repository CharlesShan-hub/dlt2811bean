#ifndef CMS_CHOICE_DATA_H
#define CMS_CHOICE_DATA_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int8.h"
#include "data/scalar/cms_int16.h"
#include "data/scalar/cms_int32.h"
#include "data/scalar/cms_int64.h"
#include "data/scalar/cms_int8u.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int64u.h"
#include "data/scalar/cms_float32.h"
#include "data/scalar/cms_float64.h"
#include "data/string/cms_uint8_array.h"
#include "data/string/cms_utf8_string.h"
#include "data/time/cms_utc_time.h"
#include "data/time/cms_binary_time.h"
#include "data/common/cms_quality.h"
#include "data/common/cms_dbpos.h"
#include "data/common/cms_tcmd.h"
#include "data/control/cms_check.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Data ::= CHOICE { ... }  —  7.7
 *
 * Flat all-pointer layout; the active alternative is selected by the
 * 'choice' selector (cms_enumerated_t*).
 *
 * For array and structure (SEQUENCE OF Data), use cms_array_t
 *   { void** elements; int32_t count; }.
 *
 * All other alternatives use their natural typed pointer.
 */

/* ── selector values ── */
#define CMS_DATA_CHOICE_ERROR 0
#define CMS_DATA_CHOICE_ARRAY 1
#define CMS_DATA_CHOICE_STRUCTURE 2
#define CMS_DATA_CHOICE_BOOLEAN 3
#define CMS_DATA_CHOICE_INT8 4
#define CMS_DATA_CHOICE_INT16 5
#define CMS_DATA_CHOICE_INT32 6
#define CMS_DATA_CHOICE_INT64 7
#define CMS_DATA_CHOICE_INT8U 8
#define CMS_DATA_CHOICE_INT16U 9
#define CMS_DATA_CHOICE_INT32U 10
#define CMS_DATA_CHOICE_INT64U 11
#define CMS_DATA_CHOICE_FLOAT32 12
#define CMS_DATA_CHOICE_FLOAT64 13
#define CMS_DATA_CHOICE_BIT_STRING 14
#define CMS_DATA_CHOICE_OCTET_STRING 15
#define CMS_DATA_CHOICE_VISIBLE_STRING 16
#define CMS_DATA_CHOICE_UNICODE_STRING 17
#define CMS_DATA_CHOICE_UTC_TIME 18
#define CMS_DATA_CHOICE_BINARY_TIME 19
#define CMS_DATA_CHOICE_QUALITY 20
#define CMS_DATA_CHOICE_DBPOS 21
#define CMS_DATA_CHOICE_TCMD 22
#define CMS_DATA_CHOICE_CHECK 23

/* Forward declaration for SEQUENCE OF Data (array / structure) */
typedef struct cms_data_s cms_data_t;

typedef struct cms_data_s {
    cms_enumerated_t *choice; /* selector, 0..23 */

    /* ARRAY / STRUCTURE — SEQUENCE OF Data via cms_array_t */
    cms_array_t *alt_sequence; /* { void** elements; int32_t count; } */

    /* scalar / string / time / quality alternatives */
    cms_boolean_t *alt_boolean;
    cms_int8_t *alt_int8;
    cms_int16_t *alt_int16;
    cms_int32_t *alt_int32;
    cms_int64_t *alt_int64;
    cms_int8u_t *alt_int8u;
    cms_int16u_t *alt_int16u;
    cms_int32u_t *alt_int32u;
    cms_int64u_t *alt_int64u;
    cms_float32_t *alt_float32;
    cms_float64_t *alt_float64;
    cms_uint8_array_t *alt_bit_string;     /* BIT STRING (variable) */
    cms_uint8_array_t *alt_octet_string;   /* OCTET STRING (variable) */
    cms_uint8_array_t *alt_visible_string; /* VisibleString (variable) */
    cms_uint8_array_t *alt_unicode_string; /* UTF8String (variable) */
    cms_utc_time_t *alt_utc_time;
    cms_binary_time_t *alt_binary_time;
    cms_quality_t *alt_quality;
    cms_dbpos_t *alt_dbpos;         /* Dbpos — BIT STRING SIZE(2) */
    cms_tcmd_t *alt_tcmd;           /* Tcmd — BIT STRING SIZE(2) */
    cms_check_t *alt_check;         /* Check — BIT STRING SIZE(2) */
    cms_service_error_t *alt_error; /* ServiceError — integer 0..12 */
} cms_data_t;

int cms_data_encode_stream(per_stream_t *s, const void *ptr);
int cms_data_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_data_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_data_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
