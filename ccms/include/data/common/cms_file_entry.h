#ifndef DATA_COMMON_CMS_FILE_ENTRY_H
#define DATA_COMMON_CMS_FILE_ENTRY_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/extended/cms_time.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CMS_FILE_NAME_MAX 129

/*
 * FileEntry ::= SEQUENCE {
 *     fileName       [0] IMPLICIT VisibleString129,
 *     fileSize       [1] IMPLICIT INT32U,
 *     lastModified   [2] IMPLICIT UtcTime,
 *     checkSum       [3] IMPLICIT INT32U
 * }
 */
typedef struct {
    cms_uint8_array_t            fileName;  /* VisibleString129 (SIZE(0..129)) */
    cms_int32u_t                 fileSize;
    cms_utc_time_t               lastModified;
    cms_int32u_t                 checkSum;
} cms_file_entry_t;

CMS_EXPORT int cms_file_entry_encode(const cms_file_entry_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_file_entry_decode(cms_file_entry_t *v, const uint8_t *in_buf, int in_len);
int cms_file_entry_encode_stream(per_stream_t *s, const cms_file_entry_t *v);
int cms_file_entry_decode_stream(per_stream_t *s, cms_file_entry_t *v);

#ifdef __cplusplus
}
#endif

#endif
