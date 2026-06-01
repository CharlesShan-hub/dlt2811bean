#ifndef CMS_TYPES3_H
#define CMS_TYPES3_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_object_name_encode(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_object_name_decode(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_EXPORT int cms_object_reference_encode(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_object_reference_decode(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_EXPORT int cms_sub_reference_encode(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_sub_reference_decode(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_EXPORT int cms_entry_id_encode(
    const uint8_t value[8],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_entry_id_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[8]
);

CMS_EXPORT int cms_time_stamp_encode(
    int64_t seconds_since_epoch, int64_t fractional,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_time_stamp_decode(
    const uint8_t *in_buf, int in_len,
    int64_t *seconds_since_epoch, int64_t *fractional
);

CMS_EXPORT int cms_phy_com_addr_encode(
    const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_phy_com_addr_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid
);

CMS_EXPORT int cms_quality_encode(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_quality_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

CMS_EXPORT int cms_dbpos_encode(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_dbpos_decode(
    const uint8_t *in_buf, int in_len,
    int *value
);

CMS_EXPORT int cms_tcmd_encode(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_tcmd_decode(
    const uint8_t *in_buf, int in_len,
    int *value
);

CMS_EXPORT int cms_service_error_encode(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_service_error_decode(
    const uint8_t *in_buf, int in_len,
    int *value
);

/* ==================== 7.3.10 FileEntry ==================== */
CMS_EXPORT int cms_file_entry_encode(
    const char *fileName,
    uint32_t fileSize,
    int has_lastModified, const uint8_t lastModified[8],
    int has_fileType, const char *fileType,
    int has_fileAttr, const char *fileAttr,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_file_entry_decode(
    const uint8_t *in_buf, int in_len,
    char *fileName, int *fileName_cap,
    uint32_t *fileSize,
    int *has_lastModified, uint8_t lastModified[8],
    int *has_fileType, char *fileType, int *fileType_cap,
    int *has_fileAttr, char *fileAttr, int *fileAttr_cap
);

#ifdef __cplusplus
}
#endif

#endif
