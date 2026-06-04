#ifndef DATA_COMMON_CMS_OBJECT_NAME_H
#define DATA_COMMON_CMS_OBJECT_NAME_H

#include "cms_core.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ObjectName ::= VisibleString (SIZE(0..64))
 * ============================================================
 */
typedef struct {
    uint8_t value[65];
} cms_object_name_t;

CMS_EXPORT int cms_object_name_encode(const cms_object_name_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_name_decode(cms_object_name_t *v, const uint8_t *in_buf, int in_len);
int cms_object_name_encode_stream(per_stream_t *s, const cms_object_name_t *v);
int cms_object_name_decode_stream(per_stream_t *s, cms_object_name_t *v);

#ifdef __cplusplus
}
#endif

#endif
