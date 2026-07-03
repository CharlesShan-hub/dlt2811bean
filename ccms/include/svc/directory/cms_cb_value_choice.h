#ifndef CMS_CB_VALUE_CHOICE_H
#define CMS_CB_VALUE_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/block/cms_brcb.h"
#include "data/block/cms_urcb.h"
#include "data/block/cms_lcb.h"
#include "data/block/cms_sgcb.h"
#include "data/block/cms_go_cb.h"
#include "data/block/cms_msvcb.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * CBValue ::= CHOICE {
 *     brcb          [0] IMPLICIT BRCB,
 *     urcb          [1] IMPLICIT URCB,
 *     lcb           [2] IMPLICIT LCB,
 *     sgecb         [3] IMPLICIT SGECB,
 *     gocb          [4] IMPLICIT GOCB,
 *     msvcb         [5] IMPLICIT MSVCB
 * }
 * ============================================================
 */

#define CMS_CB_VALUE_CHOICE_BRCB   0
#define CMS_CB_VALUE_CHOICE_URCB   1
#define CMS_CB_VALUE_CHOICE_LCB    2
#define CMS_CB_VALUE_CHOICE_SGECB  3
#define CMS_CB_VALUE_CHOICE_GOCB   4
#define CMS_CB_VALUE_CHOICE_MSVCB  5

typedef struct {
    cms_enumerated_t   *choice;
    cms_brcb_t         *alt_brcb;
    cms_urcb_t         *alt_urcb;
    cms_lcb_t          *alt_lcb;
    cms_sgcb_t         *alt_sgecb;
    cms_go_cb_t        *alt_gocb;
    cms_msvcb_t        *alt_msvcb;
} cms_cb_value_choice_t;

int cms_cb_value_choice_encode_stream(per_stream_t *s, const cms_cb_value_choice_t *v);
int cms_cb_value_choice_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
