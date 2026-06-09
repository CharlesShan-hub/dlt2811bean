#ifndef CMS_COMMON_DBPOS_H
#define CMS_COMMON_DBPOS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_int32.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Dbpos ::= BIT STRING (SIZE(2))  —  7.3.5
 * PER: constrained integer (0..3), 2 bits
 */

#define CMS_DBPOS_INTERMEDIATE  0
#define CMS_DBPOS_OFF           1
#define CMS_DBPOS_ON            2
#define CMS_DBPOS_BAD_STATE     3

typedef struct { cms_int32_t value; } cms_dbpos_t;

int cms_dbpos_encode_stream(per_stream_t *s, const void *ptr);
int cms_dbpos_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_dbpos_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_dbpos_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
