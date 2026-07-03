#ifndef CMS_STRING_UINT8_ARRAY_H
#define CMS_STRING_UINT8_ARRAY_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * cms_uint8_array_t — 通用字节数组结构
 *
 * 用于所有 VisibleString / OctetString / UTF8String / BitString 等
 * pointer + len 模式的类型。
 *
 * sizeof = 16 (value* 8 + len 4 + padding 4)
 */
typedef struct {
    uint8_t *value; /* 8 bytes */
    int32_t len;    /* 4 bytes */
} cms_uint8_array_t;

#ifdef __cplusplus
}
#endif

#endif
