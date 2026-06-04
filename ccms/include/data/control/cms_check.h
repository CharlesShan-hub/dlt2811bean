#ifndef DATA_CONTROL_CMS_CHECK_H
#define DATA_CONTROL_CMS_CHECK_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Check
 * ============================================================
 */
CMS_EXPORT int cms_check_encode(const cms_bit_string_fixed_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_check_decode(const uint8_t *in_buf, int in_len, cms_bit_string_fixed_t *v);
int cms_check_encode_stream(per_stream_t *s, const cms_bit_string_fixed_t *v);
int cms_check_decode_stream(per_stream_t *s, cms_bit_string_fixed_t *v);

#ifdef __cplusplus
}
#endif

#endif
