#ifndef CMS_COMMON_QUALITY_H
#define CMS_COMMON_QUALITY_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "data/scalar/cms_int32.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Quality ::= BIT STRING (SIZE(13))  —  7.3.6
 * PER: align + 2 bytes (13 bits)
 *
 * Bit layout:
 *   bit 0-1:   validity (0= good, 1=invalid, 2=reserved, 3=questionable)
 *   bit 2:     overflow
 *   bit 3:     outOfRange
 *   bit 4:     badReference
 *   bit 5:     oscillatory
 *   bit 6:     failure
 *   bit 7:     oldData
 *   bit 8:     inconsistent
 *   bit 9:     inaccurate
 *   bit 10:    substituted
 *   bit 11:    test
 *   bit 12:    operatorBlocked
 */

#define CMS_QUALITY_GOOD          0
#define CMS_QUALITY_INVALID       1
#define CMS_QUALITY_RESERVED      2
#define CMS_QUALITY_QUESTIONABLE  3

typedef struct {
    cms_int32_t   *validity;
    cms_boolean_t *overflow;
    cms_boolean_t *outOfRange;
    cms_boolean_t *badReference;
    cms_boolean_t *oscillatory;
    cms_boolean_t *failure;
    cms_boolean_t *oldData;
    cms_boolean_t *inconsistent;
    cms_boolean_t *inaccurate;
    cms_boolean_t *substituted;
    cms_boolean_t *test;
    cms_boolean_t *operatorBlocked;
} cms_quality_t;

int cms_quality_encode_stream(per_stream_t *s, const void *ptr);
int cms_quality_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_quality_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_quality_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
