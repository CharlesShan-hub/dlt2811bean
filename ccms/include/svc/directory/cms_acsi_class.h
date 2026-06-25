#ifndef CMS_ACSI_CLASS_H
#define CMS_ACSI_CLASS_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_int32.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ACSIClass ::= INTEGER {
 *     reserved       (0),
 *     data-object    (1),
 *     data-set       (2),
 *     brcb           (3),
 *     urcb           (4),
 *     lcb            (5),
 *     log            (6),
 *     sgecb          (7),
 *     gocb           (8),
 *     msvcb          (10)
 * } (0..10)
 *
 * 编码为 constrained integer (0..10), 4 bits
 * ============================================================
 */
#define CMS_ACSI_CLASS_RESERVED       0
#define CMS_ACSI_CLASS_DATA_OBJECT    1
#define CMS_ACSI_CLASS_DATA_SET       2
#define CMS_ACSI_CLASS_BRCB           3
#define CMS_ACSI_CLASS_URCB           4
#define CMS_ACSI_CLASS_LCB            5
#define CMS_ACSI_CLASS_LOG            6
#define CMS_ACSI_CLASS_SGECB          7
#define CMS_ACSI_CLASS_GOCB           8
#define CMS_ACSI_CLASS_MSVCB         10

typedef cms_int32_t cms_acsi_class_t;

CMS_EXPORT int cms_acsi_class_encode(const cms_acsi_class_t *v, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_acsi_class_decode(cms_acsi_class_t *v, const uint8_t *in_buf, int in_len);
int cms_acsi_class_encode_stream(per_stream_t *s, const cms_acsi_class_t *v);
int cms_acsi_class_decode_stream(per_stream_t *s, cms_acsi_class_t *v);

#ifdef __cplusplus
}
#endif

#endif
