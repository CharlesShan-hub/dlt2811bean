#ifndef DATA_COMMON_CMS_SUB_REFERENCE_H
#define DATA_COMMON_CMS_SUB_REFERENCE_H

#include "cms_core.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SubReference ::= VisibleString (SIZE(0..129))
 * ============================================================
 */
typedef struct {
    uint8_t value[130];
} cms_sub_reference_t;

CMS_EXPORT int cms_sub_reference_encode(const cms_sub_reference_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_sub_reference_decode(cms_sub_reference_t *v, const uint8_t *in_buf, int in_len);
int cms_sub_reference_encode_stream(per_stream_t *s, const cms_sub_reference_t *v);
int cms_sub_reference_decode_stream(per_stream_t *s, cms_sub_reference_t *v);

#ifdef __cplusplus
}
#endif

#endif
