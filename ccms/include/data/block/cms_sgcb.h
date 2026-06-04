#ifndef DATA_BLOCK_CMS_SGCB_H
#define DATA_BLOCK_CMS_SGCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_integer.h"
#include "data/common/cms_time_stamp.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_int8u_t      numOfSG;
    cms_int8u_t      actSG;
    cms_int8u_t      editSG;
    cms_utc_time_t   tActEdt;
    cms_int16u_t     resvTms;
    int              resvTms_present;
} cms_sgcb_t;

CMS_EXPORT int cms_sgcb_encode(const cms_sgcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_sgcb_decode(const uint8_t *in_buf, int in_len, cms_sgcb_t *value);
int cms_sgcb_encode_stream(per_stream_t *s, const cms_sgcb_t *value);
int cms_sgcb_decode_stream(per_stream_t *s, cms_sgcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
