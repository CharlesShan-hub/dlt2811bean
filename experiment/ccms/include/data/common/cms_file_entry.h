#ifndef DATA_COMMON_CMS_FILE_ENTRY_H
#define DATA_COMMON_CMS_FILE_ENTRY_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_file_entry_encode(
    const char *fileName, uint32_t fileSize,
    int has_lastModified, const uint8_t lastModified[8],
    int has_fileType, const char *fileType,
    int has_fileAttr, const char *fileAttr,
    uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_file_entry_decode(
    const uint8_t *in_buf, int in_len,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    int *has_lastModified, uint8_t lastModified[8],
    int *has_fileType, char *fileType, int *fileType_cap,
    int *has_fileAttr, char *fileAttr, int *fileAttr_cap);
int cms_file_entry_encode_stream(per_stream_t *s,
    const char *fileName, uint32_t fileSize,
    int has_lastModified, const uint8_t lastModified[8],
    int has_fileType, const char *fileType,
    int has_fileAttr, const char *fileAttr);
int cms_file_entry_decode_stream(per_stream_t *s,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    int *has_lastModified, uint8_t lastModified[8],
    int *has_fileType, char *fileType, int *fileType_cap,
    int *has_fileAttr, char *fileAttr, int *fileAttr_cap);

#ifdef __cplusplus
}
#endif

#endif
