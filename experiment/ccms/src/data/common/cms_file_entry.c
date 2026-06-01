#include "data/common/cms_file_entry.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include <string.h>
#include <stdlib.h>

CMS_EXPORT int cms_file_entry_encode(
    const char *fileName, uint32_t fileSize,
    int has_lastModified, const uint8_t lastModified[8],
    int has_fileType, const char *fileType,
    int has_fileAttr, const char *fileAttr,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_stream_write_bits(&w, has_lastModified ? 1 : 0, 1);
    per_stream_write_bits(&w, has_fileType ? 1 : 0, 1);
    per_stream_write_bits(&w, has_fileAttr ? 1 : 0, 1);

    per_encode_visible_string(&w, fileName, 255);
    per_encode_constrained_int(&w, fileSize, 0, 4294967295);

    if (has_lastModified) {
        per_encode_octet_string_fixed(&w, lastModified, 8);
    }
    if (has_fileType) {
        per_encode_visible_string(&w, fileType, 64);
    }
    if (has_fileAttr) {
        per_encode_visible_string(&w, fileAttr, 64);
    }

    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_file_entry_decode(
    const uint8_t *in_buf, int in_len,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    int *has_lastModified, uint8_t lastModified[8],
    int *has_fileType, char *fileType, int *fileType_cap,
    int *has_fileAttr, char *fileAttr, int *fileAttr_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint64_t bit;
    per_stream_read_bits(&r, &bit, 1); *has_lastModified = (int)bit;
    per_stream_read_bits(&r, &bit, 1); *has_fileType = (int)bit;
    per_stream_read_bits(&r, &bit, 1); *has_fileAttr = (int)bit;

    per_decode_visible_string(&r, fileName, (uint32_t)*fileName_cap);
    *fileName_cap = (int)strlen(fileName);

    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 4294967295);
    *fileSize = (uint32_t)tmp;

    if (*has_lastModified) {
        per_decode_octet_string_fixed(&r, lastModified, 8);
    }
    if (*has_fileType) {
        per_decode_visible_string(&r, fileType, (uint32_t)*fileType_cap);
        *fileType_cap = (int)strlen(fileType);
    }
    if (*has_fileAttr) {
        per_decode_visible_string(&r, fileAttr, (uint32_t)*fileAttr_cap);
        *fileAttr_cap = (int)strlen(fileAttr);
    }

    return CMS_OK;
}