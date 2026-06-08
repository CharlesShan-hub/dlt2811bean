#ifndef DATA_COMMON_CMS_OBJECT_REFERENCE_H
#define DATA_COMMON_CMS_OBJECT_REFERENCE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ObjectReference ::= VisibleString (SIZE(0..129))
 * ============================================================
 */
typedef cms_uint8_array_t cms_object_reference_t;
#define CMS_OBJECT_REFERENCE_MAX_LEN 129

CMS_EXPORT int cms_object_reference_encode(const cms_object_reference_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_reference_decode(cms_object_reference_t *v, const uint8_t *in_buf, int in_len);
int cms_object_reference_encode_stream(per_stream_t *s, const cms_object_reference_t *v);
int cms_object_reference_decode_stream(per_stream_t *s, cms_object_reference_t *v);

/*
 * ============================================================
 * SEQUENCE OF ObjectReference 
 * ============================================================
 */
typedef struct {
    cms_object_reference_t *elements;
    int32_t                 count;
} cms_object_reference_array_t;

int cms_object_reference_array_encode_stream(per_stream_t *s, const cms_object_reference_array_t *arr);
int cms_object_reference_array_decode_stream(per_stream_t *s, cms_object_reference_array_t *arr);
void cms_object_reference_array_free(cms_object_reference_array_t *arr);

#ifdef __cplusplus
}
#endif

#endif
