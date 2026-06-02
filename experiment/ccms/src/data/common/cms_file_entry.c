#include "data/common/cms_file_entry.h"
#include <string.h>

/* ---- internal stream version ---- */

int cms_file_entry_encode_stream(per_stream_t *s,
    const char *fileName,
    uint32_t fileSize,
    const uint8_t lastModified[8],
    uint32_t checkSum)
{
    per_encode_visible_string(s, fileName, 129);
    per_encode_constrained_int(s, fileSize, 0, 4294967295);
    per_encode_octet_string_fixed(s, lastModified, 8);
    per_encode_constrained_int(s, checkSum, 0, 4294967295);
    return CMS_OK;
}

int cms_file_entry_decode_stream(per_stream_t *s,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    uint8_t lastModified[8],
    uint32_t *checkSum)
{
    per_decode_visible_string(s, fileName, (uint32_t)*fileName_cap);
    *fileName_cap = (int)strlen(fileName);
    int64_t tmp;
    per_decode_constrained_int(s, &tmp, 0, 4294967295);
    *fileSize = (uint32_t)tmp;
    per_decode_octet_string_fixed(s, lastModified, 8);
    per_decode_constrained_int(s, &tmp, 0, 4294967295);
    *checkSum = (uint32_t)tmp;
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_file_entry_encode(const char *fn, uint32_t fs, const uint8_t lm[8], uint32_t cs, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_file_entry_encode_stream(&w, fn, fs, lm, cs); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_file_entry_decode(const uint8_t *b, int l, char *fn, int *fnc, uint32_t *fs, uint8_t lm[8], uint32_t *cs)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); return cms_file_entry_decode_stream(&r, fn, fnc, fs, lm, cs); }
