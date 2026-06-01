#ifndef DATA_COMMON_CMS_TIME_STAMP_H
#define DATA_COMMON_CMS_TIME_STAMP_H

#include "cms_core.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_time_stamp_encode(int64_t seconds_since_epoch, int64_t fractional, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_time_stamp_decode(const uint8_t *in_buf, int in_len, int64_t *seconds_since_epoch, int64_t *fractional);

CMS_EXPORT int cms_entry_id_encode(const uint8_t value[8], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_entry_id_decode(const uint8_t *in_buf, int in_len, uint8_t value[8]);

#ifdef __cplusplus
}
#endif

#endif