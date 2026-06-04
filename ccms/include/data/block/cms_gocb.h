#ifndef DATA_BLOCK_CMS_GOCB_H
#define DATA_BLOCK_CMS_GOCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_sequence.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_phy_com_addr.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GoCB (GOOSE Control Block)
 *
 * ASN.1 definition:
 * GoCB ::= SEQUENCE {
 *     goEna           [1] IMPLICIT BOOLEAN,
 *     goID            [2] IMPLICIT VisibleString129,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     ndsCom          [5] IMPLICIT BOOLEAN,
 *     dstAddress      [6] IMPLICIT PHYCOMADDR OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    int                         goEna;                /* BOOLEAN */
    cms_visible_string_fixed_t  goID;                 /* VisibleString129 */
    char                        datSet[256];          /* ObjectReference */
    uint32_t                    confRev;              /* INT32U */
    int                         ndsCom;               /* BOOLEAN */
    /* dstAddress OPTIONAL */
    uint8_t     dstAddr[6];            /* PHYCOMADDR addr */
    uint8_t     dstPriority;           /* priority */
    uint16_t    dstVid;                /* VID */
    uint16_t    dstAppId;              /* APPID */
    int         dstAddress_present;    /* 1 if present */
} cms_gocb_t;

CMS_EXPORT int cms_gocb_encode(const cms_gocb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_gocb_decode(const uint8_t *in_buf, int in_len, cms_gocb_t *value);
int cms_gocb_encode_stream(per_stream_t *s, const cms_gocb_t *value);
int cms_gocb_decode_stream(per_stream_t *s, cms_gocb_t *value);

#ifdef __cplusplus
}
#endif

#endif
