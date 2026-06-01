#ifndef DATA_COMMON_CMS_OBJECT_NAME_H
#define DATA_COMMON_CMS_OBJECT_NAME_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_object_name_encode(const char *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_name_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap);

CMS_EXPORT int cms_object_reference_encode(const char *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_reference_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap);

CMS_EXPORT int cms_sub_reference_encode(const char *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_sub_reference_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap);

#ifdef __cplusplus
}
#endif

#endif