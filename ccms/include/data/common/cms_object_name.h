#ifndef DATA_COMMON_CMS_OBJECT_NAME_H
#define DATA_COMMON_CMS_OBJECT_NAME_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ObjectName
 * ============================================================
 */
CMS_EXPORT int cms_object_name_encode(const cms_visible_string_var_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_name_decode(const uint8_t *in_buf, int in_len, cms_visible_string_var_t *v);
int cms_object_name_encode_stream(per_stream_t *s, const cms_visible_string_var_t *v);
int cms_object_name_decode_stream(per_stream_t *s, cms_visible_string_var_t *v);

/*
 * ============================================================
 * ObjectReference
 * ============================================================
 */
CMS_EXPORT int cms_object_reference_encode(const cms_visible_string_var_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_reference_decode(const uint8_t *in_buf, int in_len, cms_visible_string_var_t *v);
int cms_object_reference_encode_stream(per_stream_t *s, const cms_visible_string_var_t *v);
int cms_object_reference_decode_stream(per_stream_t *s, cms_visible_string_var_t *v);

/*
 * ============================================================
 * SubReference
 * ============================================================
 */
CMS_EXPORT int cms_sub_reference_encode(const cms_visible_string_var_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_sub_reference_decode(const uint8_t *in_buf, int in_len, cms_visible_string_var_t *v);
int cms_sub_reference_encode_stream(per_stream_t *s, const cms_visible_string_var_t *v);
int cms_sub_reference_decode_stream(per_stream_t *s, cms_visible_string_var_t *v);

#ifdef __cplusplus
}
#endif

#endif
