#ifndef DATA_BLOCK_CMS_BRCB_H
#define DATA_BLOCK_CMS_BRCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"
#include "data/block/cms_opt_flds.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/common/cms_time_stamp.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_object_reference.h"
#include "data/extended/cms_time.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_visible_string_fixed_t  rptID;
    cms_boolean_t               rptEna;
    cms_object_reference_t      datSet;
    cms_int32u_t                confRev;
    cms_rcb_opt_flds_t          optFlds;
    cms_int32u_t                bufTm;
    cms_int16u_t                sqNum;
    cms_trigger_conditions_t    trgOps;
    cms_int32u_t                intgPd;
    cms_boolean_t               gi;
    cms_boolean_t               purgeBuf;
    cms_entry_id_t              entryID;
    cms_binary_time_t           timeOfEntry;
    cms_int16_t                 resvTms;
    int                         resvTms_present;
    cms_octet_string_var_t      owner;
    int                         owner_present;
} cms_brcb_t;

CMS_EXPORT int cms_brcb_encode(const cms_brcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_brcb_decode(const uint8_t *in_buf, int in_len, cms_brcb_t *value);
int cms_brcb_encode_stream(per_stream_t *s, const cms_brcb_t *value);
int cms_brcb_decode_stream(per_stream_t *s, cms_brcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
