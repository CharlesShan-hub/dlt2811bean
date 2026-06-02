#ifndef DATA_COMMON_CMS_FILE_ENTRY_H
#define DATA_COMMON_CMS_FILE_ENTRY_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * FileEntry ::= SEQUENCE {
 *     fileName       [0] IMPLICIT VisibleString129,    -- VisibleString (SIZE(0..129))
 *     fileSize       [1] IMPLICIT INT32U,               -- INTEGER (0..4294967295)
 *     lastModified   [2] IMPLICIT UtcTime,              -- OCTET STRING (SIZE(8))
 *     checkSum       [3] IMPLICIT INT32U                -- INTEGER (0..4294967295)
 * }
 */

CMS_EXPORT int cms_file_entry_encode(
    const char *fileName,
    uint32_t fileSize,
    const uint8_t lastModified[8],
    uint32_t checkSum,
    uint8_t *out_buf, int *out_len);

CMS_EXPORT int cms_file_entry_decode(
    const uint8_t *in_buf, int in_len,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    uint8_t lastModified[8],
    uint32_t *checkSum);

int cms_file_entry_encode_stream(per_stream_t *s,
    const char *fileName,
    uint32_t fileSize,
    const uint8_t lastModified[8],
    uint32_t checkSum);

int cms_file_entry_decode_stream(per_stream_t *s,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    uint8_t lastModified[8],
    uint32_t *checkSum);

#ifdef __cplusplus
}
#endif

#endif
