#ifndef DATA_COMMON_CMS_DBPOS_H
#define DATA_COMMON_CMS_DBPOS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Dbpos ::= BIT STRING (SIZE(2))
 * ============================================================
 */
#define CMS_DBPOS_INTERMEDIATE  0
#define CMS_DBPOS_OFF           1
#define CMS_DBPOS_ON            2
#define CMS_DBPOS_BAD_STATE     3

typedef struct { cms_int32_t value; } cms_dbpos_t;

CMS_EXPORT int cms_dbpos_encode(const cms_dbpos_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_dbpos_decode(cms_dbpos_t *v, const uint8_t *in_buf, int in_len);
int cms_dbpos_encode_stream(per_stream_t *s, const cms_dbpos_t *v);
int cms_dbpos_decode_stream(per_stream_t *s, cms_dbpos_t *v);

#ifdef __cplusplus
}
#endif

#endif
