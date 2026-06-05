#include <stdio.h>
#include <stdint.h>
#include "cms_core.h"

typedef struct {
    uint8_t *value;
    int len;
} cms_test_uint8_array_t;

typedef struct {
    int value;
} cms_test_boolean_t;

typedef struct {
    cms_test_uint8_array_t data;
    cms_test_boolean_t count;
} cms_test_mini_t;

CMS_EXPORT int cms_test_mini_encode(const cms_test_mini_t *v, uint8_t *out_buf, int *out_len) {
    printf("[C] data.value=%p data.len=%d count.value=%d\n",
           (void*)v->data.value, v->data.len, v->count.value);
    if (v->data.value) {
        printf("[C] data bytes: ");
        for (int i = 0; i < v->data.len; i++) {
            printf("%02x ", v->data.value[i]);
        }
        printf("\n");
    }
    *out_len = 0;
    return 0;
}

CMS_EXPORT int cms_test_mini_decode(cms_test_mini_t *v, const uint8_t *in_buf, int in_len) {
    (void)in_buf; (void)in_len;
    v->data.value = NULL;
    v->data.len = 0;
    v->count.value = 0;
    return 0;
}
