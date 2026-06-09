#ifndef CMS2_DATA_H
#define CMS2_DATA_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * cms2_boolean_t — sizeof=4, inline int
 * ============================================================
 */
CMS2_EXPORT int cms2_boolean_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS2_EXPORT int cms2_boolean_decode(void *ptr, const uint8_t *in_buf, int in_len);

/*
 * ============================================================
 * cms2_int8_t — sizeof=1, inline int8_t
 * ============================================================
 */
CMS2_EXPORT int cms2_int8_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS2_EXPORT int cms2_int8_decode(void *ptr, const uint8_t *in_buf, int in_len);

/*
 * ============================================================
 * cms2_int8u_t — sizeof=1, inline uint8_t
 * ============================================================
 */
CMS2_EXPORT int cms2_int8u_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS2_EXPORT int cms2_int8u_decode(void *ptr, const uint8_t *in_buf, int in_len);

/*
 * ============================================================
 * cms2_uint8_array_t — sizeof=16, pointer + len
 * ============================================================
 */
CMS2_EXPORT int cms2_uint8_array_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS2_EXPORT int cms2_uint8_array_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
