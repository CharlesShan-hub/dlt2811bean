#ifndef CMS_APDU_HEADER_H
#define CMS_APDU_HEADER_H

#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_boolean_t next;
    cms_boolean_t resp;
    cms_boolean_t err;
    cms_int8u_t sc;
    cms_int8u_t fl;
    cms_int16u_t req_id;
} cms_apdu_header_t;

#ifdef __cplusplus
}
#endif

#endif
