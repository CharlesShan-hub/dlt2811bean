#ifndef CMS_COMMON_FILE_ENTRY_H
#define CMS_COMMON_FILE_ENTRY_H

#include "cms_types.h"
#include "data/string/cms_visible_string.h"
#include "data/scalar/cms_int32u.h"
#include "data/time/cms_utc_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * FileEntry ::= SEQUENCE {
 *     fileName       [0] IMPLICIT VisibleString129,
 *     fileSize       [1] IMPLICIT INT32U,
 *     lastModified   [2] IMPLICIT UtcTime,
 *     checkSum       [3] IMPLICIT INT32U
 * }  —  7.3.10
 *
 * All-pointer layout (sizeof = 4 * 8 = 32):
 *   [0] fileName      → cms_uint8_array_t* (VisibleString, max 129)
 *   [8] fileSize      → cms_int32u_t*
 *   [16] lastModified → cms_utc_time_t*
 *   [24] checkSum     → cms_int32u_t*
 */
typedef struct {
    void *fileName;     /* cms_uint8_array_t* */
    void *fileSize;     /* cms_int32u_t* */
    void *lastModified; /* cms_utc_time_t* */
    void *checkSum;     /* cms_int32u_t* */
} cms_file_entry_t;

int cms_file_entry_encode_stream(per_stream_t *s, const void *ptr);
int cms_file_entry_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_file_entry_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_file_entry_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
