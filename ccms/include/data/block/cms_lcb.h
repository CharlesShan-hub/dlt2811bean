#ifndef DATA_BLOCK_CMS_LCB_H
#define DATA_BLOCK_CMS_LCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/block/cms_opt_flds.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/common/cms_object_reference.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_boolean_t            logEna;
    cms_object_reference_t   datSet;
    cms_trigger_conditions_t trgOps;
    cms_int32u_t             intgPd;
    cms_object_reference_t   logRef;
    cms_lcb_opt_flds_t       optFlds;
    int                      optFlds_present;
    cms_int32u_t             bufTm;
    int                      bufTm_present;
} cms_lcb_t;

CMS_EXPORT int cms_lcb_encode(const cms_lcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_decode(const uint8_t *in_buf, int in_len, cms_lcb_t *value);
int cms_lcb_encode_stream(per_stream_t *s, const cms_lcb_t *value);
int cms_lcb_decode_stream(per_stream_t *s, cms_lcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
