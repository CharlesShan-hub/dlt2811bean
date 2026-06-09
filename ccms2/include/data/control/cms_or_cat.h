#ifndef CMS_CONTROL_OR_CAT_H
#define CMS_CONTROL_OR_CAT_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * orCat ::= INTEGER (0..8)  —  7.5.2
 * PER: constrained integer (0..8), 4 bits
 */

#define CMS_OR_CAT_NOT_SUPPORTED      0
#define CMS_OR_CAT_BAY_CONTROL        1
#define CMS_OR_CAT_STATION_CONTROL    2
#define CMS_OR_CAT_REMOTE_CONTROL     3
#define CMS_OR_CAT_AUTOMATIC_BAY      4
#define CMS_OR_CAT_AUTOMATIC_STATION  5
#define CMS_OR_CAT_AUTOMATIC_REMOTE   6
#define CMS_OR_CAT_MAINTENANCE        7
#define CMS_OR_CAT_PROCESS            8

typedef cms_enumerated_t cms_or_cat_t;

int cms_or_cat_encode_stream(per_stream_t *s, const void *ptr);
int cms_or_cat_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_or_cat_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_or_cat_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
