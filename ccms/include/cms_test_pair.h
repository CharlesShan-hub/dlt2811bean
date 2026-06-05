#ifndef CMS_TEST_PAIR_H
#define CMS_TEST_PAIR_H

#include "cms_core.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_int32_t        a;
    cms_int32_t        b;
} cms_test_pair_t;

CMS_EXPORT int cms_test_pair_encode(const cms_test_pair_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_test_pair_decode(cms_test_pair_t *v, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
