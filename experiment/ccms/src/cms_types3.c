#include "cms_types3.h"
#include "per_stream.h"
#include "per_integer.h"
#include "per_string.h"
#include "per_bit_string.h"
#include <string.h>
#include <stdlib.h>

/* 7.3.1 ObjectName */
int cms_encode_ObjectName(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 64);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_ObjectName(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}

int cms_encode_ObjectReference(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 129);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_ObjectReference(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}

/* 7.3.3 SubReference */
int cms_encode_SubReference(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 129);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_SubReference(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}

int cms_encode_EntryID(const uint8_t value[8], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string_fixed(&w, value, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_EntryID(const uint8_t *in_buf, int in_len, uint8_t value[8])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_octet_string_fixed(&r, value, 8);
    return CMS_OK;
}

int cms_encode_TimeStamp(
    int64_t seconds_since_epoch, int64_t fractional,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_constrained_int(&w, seconds_since_epoch, -2147483648, 2147483647);
    per_encode_constrained_int(&w, fractional, 0, 16777215);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_TimeStamp(
    const uint8_t *in_buf, int in_len,
    int64_t *seconds_since_epoch, int64_t *fractional)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    per_decode_constrained_int(&r, seconds_since_epoch, -2147483648, 2147483647);
    per_decode_constrained_int(&r, fractional, 0, 16777215);
    return CMS_OK;
}

/* 7.3.12 PhyComAddr */
int cms_encode_PhyComAddr(
    const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string_fixed(&w, addr, 6);
    per_encode_constrained_int(&w, priority, 0, 255);
    per_encode_constrained_int(&w, vid, 0, 65535);
    per_encode_constrained_int(&w, appid, 0, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_PhyComAddr(
    const uint8_t *in_buf, int in_len,
    uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_octet_string_fixed(&r, addr, 6);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 255);
    *priority = (uint8_t)tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *vid = (uint16_t)tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *appid = (uint16_t)tmp;
    return CMS_OK;
}

/* 7.3.6 Quality */
int cms_encode_Quality(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 13);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_Quality(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 13);
    return CMS_OK;
}

/* 7.3.5 Dbpos */
int cms_encode_Dbpos(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_small_non_negative(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_Dbpos(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint32_t tmp;
    per_decode_small_non_negative(&r, &tmp);
    *value = (int)tmp;
    return CMS_OK;
}

/* 7.3.10 FileEntry */
int cms_encode_FileEntry(
    const char *fileName,
    uint32_t fileSize,
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

int cms_decode_FileEntry(
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

/* 7.3.7 Tcmd */
int cms_encode_Tcmd(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_small_non_negative(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_Tcmd(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint32_t tmp;
    per_decode_small_non_negative(&r, &tmp);
    *value = (int)tmp;
    return CMS_OK;
}

int cms_encode_ServiceError(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 12);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_ServiceError(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 12);
    *value = (int)tmp;
    return CMS_OK;
}
