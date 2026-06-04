#ifndef DATA_BLOCK_CMS_GOCB_H
#define DATA_BLOCK_CMS_GOCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_phy_com_addr.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_boolean_t               goEna;
    cms_visible_string_fixed_t  goID;
    cms_object_reference_t      datSet;
    cms_int32u_t                confRev;
    cms_boolean_t               ndsCom;
    cms_phy_com_addr_t          dstAddress;
    int                         dstAddress_present;
} cms_gocb_t;

CMS_EXPORT int cms_gocb_encode(const cms_gocb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_gocb_decode(const uint8_t *in_buf, int in_len, cms_gocb_t *value);
int cms_gocb_encode_stream(per_stream_t *s, const cms_gocb_t *value);
int cms_gocb_decode_stream(per_stream_t *s, cms_gocb_t *value);

#ifdef __cplusplus
}
#endif

#endif
