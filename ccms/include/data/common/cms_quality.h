#ifndef DATA_COMMON_CMS_QUALITY_H
#define DATA_COMMON_CMS_QUALITY_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Quality ::= BIT STRING (SIZE(13))
 * ============================================================
 */

#define CMS_QUALITY_VALIDITY_GOOD  0
#define CMS_QUALITY_VALIDITY_INVALID 1
#define CMS_QUALITY_VALIDITY_RESERVED 2
#define CMS_QUALITY_VALIDITY_QUESTIONABLE 3

typedef struct {
    cms_int32_t validity;            /* 0=good, 1=invalid, 2=reserved, 3=questionable */
    cms_boolean_t overflow;          /* boolean */
    cms_boolean_t outOfRange;        /* boolean */
    cms_boolean_t badReference;      /* boolean */
    cms_boolean_t oscillatory;       /* boolean */
    cms_boolean_t failure;           /* boolean */
    cms_boolean_t oldData;           /* boolean */
    cms_boolean_t inconsistent;      /* boolean */
    cms_boolean_t inaccurate;        /* boolean */
    cms_boolean_t substituted;       /* boolean */
    cms_boolean_t test;              /* boolean */
    cms_boolean_t operatorBlocked;   /* boolean */
} cms_quality_t;

CMS_EXPORT int cms_quality_encode(const cms_quality_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_quality_decode(cms_quality_t *v, const uint8_t *in_buf, int in_len);
int cms_quality_encode_stream(per_stream_t *s, const cms_quality_t *v);
int cms_quality_decode_stream(per_stream_t *s, cms_quality_t *v);

#ifdef __cplusplus
}
#endif

#endif
