#include "data/common/cms_file_entry.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include <string.h>

int cms_file_entry_encode_stream(per_stream_t *s,
    const char *fileName, uint32_t fileSize,
    int has_lastModified, const uint8_t lastModified[8],
    int has_fileType, const char *fileType,
    int has_fileAttr, const char *fileAttr)
{
    per_stream_write_bits(s, has_lastModified ? 1 : 0, 1);
    per_stream_write_bits(s, has_fileType ? 1 : 0, 1);
    per_stream_write_bits(s, has_fileAttr ? 1 : 0, 1);
    per_encode_visible_string(s, fileName, 255);
    per_encode_constrained_int(s, fileSize, 0, 4294967295);
    if (has_lastModified) per_encode_octet_string_fixed(s, lastModified, 8);
    if (has_fileType) per_encode_visible_string(s, fileType, 64);
    if (has_fileAttr) per_encode_visible_string(s, fileAttr, 64);
    return CMS_OK;
}
int cms_file_entry_decode_stream(per_stream_t *s,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    int *has_lastModified, uint8_t lastModified[8],
    int *has_fileType, char *fileType, int *fileType_cap,
    int *has_fileAttr, char *fileAttr, int *fileAttr_cap)
{
    uint64_t bit;
    per_stream_read_bits(s, &bit, 1); *has_lastModified = (int)bit;
    per_stream_read_bits(s, &bit, 1); *has_fileType = (int)bit;
    per_stream_read_bits(s, &bit, 1); *has_fileAttr = (int)bit;
    per_decode_visible_string(s, fileName, (uint32_t)*fileName_cap);
    *fileName_cap = (int)strlen(fileName);
    int64_t tmp;
    per_decode_constrained_int(s, &tmp, 0, 4294967295);
    *fileSize = (uint32_t)tmp;
    if (*has_lastModified) per_decode_octet_string_fixed(s, lastModified, 8);
    if (*has_fileType) { per_decode_visible_string(s, fileType, (uint32_t)*fileType_cap); *fileType_cap = (int)strlen(fileType); }
    if (*has_fileAttr) { per_decode_visible_string(s, fileAttr, (uint32_t)*fileAttr_cap); *fileAttr_cap = (int)strlen(fileAttr); }
    return CMS_OK;
}

CMS_EXPORT int cms_file_entry_encode(const char *fn, uint32_t fs, int hlm, const uint8_t lm[8], int hft, const char *ft, int hfa, const char *fa, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_file_entry_encode_stream(&w, fn, fs, hlm, lm, hft, ft, hfa, fa); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_file_entry_decode(const uint8_t *b, int l, char *fn, int *fnc, uint32_t *fs, int *hlm, uint8_t lm[8], int *hft, char *ft, int *ftc, int *hfa, char *fa, int *fac)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); return cms_file_entry_decode_stream(&r, fn, fnc, fs, hlm, lm, hft, ft, ftc, hfa, fa, fac); }
