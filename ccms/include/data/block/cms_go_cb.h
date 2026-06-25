#ifndef CMS_BLOCK_GO_CB_H
#define CMS_BLOCK_GO_CB_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_uint8_array.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_phy_com_addr.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * GoCB ::= SEQUENCE {
 *     goEna           [1] IMPLICIT BOOLEAN,
 *     goID            [2] IMPLICIT VisibleString129,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     ndsCom          [5] IMPLICIT BOOLEAN,
 *     dstAddress      [6] IMPLICIT PHYCOMADDR OPTIONAL
 * }  —  8.9.4
 */

#define CMS_GO_CB_GO_ID_MAX_LEN 129

typedef struct {
    cms_boolean_t          *goEna;         /* BOOLEAN */
    cms_uint8_array_t      *goID;          /* VisibleString129 */
    cms_object_reference_t *datSet;        /* ObjectReference */
    cms_int32u_t           *confRev;       /* INT32U */
    cms_boolean_t          *ndsCom;        /* BOOLEAN */
    cms_boolean_t          *dstAddress_present;
    cms_phy_com_addr_t     *dstAddress;    /* PHYCOMADDR OPTIONAL */
} cms_go_cb_t;

int cms_go_cb_encode_stream(per_stream_t *s, const void *ptr);
int cms_go_cb_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_go_cb_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_go_cb_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
