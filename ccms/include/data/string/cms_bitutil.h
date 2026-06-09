#ifndef CMS_BITUTIL_H
#define CMS_BITUTIL_H

#include <stdint.h>

/**
 * BIT STRING 打包工具：PER 编码按 MSB-first 读 bit，即 bit[0] 在字节的 bit 7 位置。
 *
 * pack_bit(buf, nth, val) — 将 val 设为 PER 第 nth 个 bit
 * unpack_bit(byte, nth)    — 读取 PER 第 nth 个 bit
 */

static inline void pack_bit(uint8_t *buf, int nth, int val) {
    if (val) *buf |= (uint8_t)(0x80 >> nth);
}

static inline int unpack_bit(uint8_t byte, int nth) {
    return (byte >> (7 - nth)) & 1;
}

/* 对于 >8 位的 BIT STRING，用 pack_bit16 / unpack_bit16 */
static inline void pack_bit16(uint16_t *buf, int nth, int val) {
    if (val) *buf |= (uint16_t)(0x8000 >> nth);
}

static inline int unpack_bit16(uint16_t bits, int nth) {
    return (bits >> (15 - nth)) & 1;
}

#endif
