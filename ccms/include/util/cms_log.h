#ifndef CMS_LOG_H
#define CMS_LOG_H

#include <stdio.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/** Log a formatted message to stderr (visible in Java console via JNA). */
void cms_log(const char *format, ...);

/** Hex dump a buffer to stderr. */
void cms_log_hex(const char *tag, const uint8_t *buf, int len);

#ifdef __cplusplus
}
#endif

#endif /* CMS_LOG_H */
