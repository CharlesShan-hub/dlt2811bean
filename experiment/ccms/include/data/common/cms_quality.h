#ifndef DATA_COMMON_CMS_QUALITY_H
#define DATA_COMMON_CMS_QUALITY_H

#include "cms_core.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_quality_encode(const uint8_t value[2], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_quality_decode(const uint8_t *in_buf, int in_len, uint8_t value[2]);

CMS_EXPORT int cms_dbpos_encode(int value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_dbpos_decode(const uint8_t *in_buf, int in_len, int *value);

CMS_EXPORT int cms_tcmd_encode(int value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_tcmd_decode(const uint8_t *in_buf, int in_len, int *value);

CMS_EXPORT int cms_service_error_encode(int value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_service_error_decode(const uint8_t *in_buf, int in_len, int *value);

#ifdef __cplusplus
}
#endif

#endif