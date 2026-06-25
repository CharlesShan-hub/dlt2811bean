#ifndef CMS_OBJECT_CLASS_H
#define CMS_OBJECT_CLASS_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_int32.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ObjectClass ::= INTEGER {
 *     reserved        (0),
 *     logical-device  (1),
 *     file-system     (2)
 * } (0..2)
 *
 * 编码为 constrained integer (0..2), 2 bits
 * ============================================================
 */
#define CMS_OBJECT_CLASS_RESERVED        0
#define CMS_OBJECT_CLASS_LOGICAL_DEVICE  1
#define CMS_OBJECT_CLASS_FILE_SYSTEM     2

typedef cms_int32_t cms_object_class_t;

CMS_EXPORT int cms_object_class_encode(const cms_object_class_t *v, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_object_class_decode(cms_object_class_t *v, const uint8_t *in_buf, int in_len);
int cms_object_class_encode_stream(per_stream_t *s, const cms_object_class_t *v);
int cms_object_class_decode_stream(per_stream_t *s, cms_object_class_t *v);

#ifdef __cplusplus
}
#endif

#endif
