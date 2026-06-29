#include "util/cms_log.h"
#include <stdio.h>
#include <stdarg.h>

void cms_log(const char *format, ...) {
    va_list ap;
    va_start(ap, format);
    vfprintf(stderr, format, ap);
    va_end(ap);
    fprintf(stderr, "\n");
    fflush(stderr);
}

void cms_log_hex(const char *tag, const uint8_t *buf, int len) {
    fprintf(stderr, "%s len=%d\n", tag, len);
    int max_print = len < 256 ? len : 256;
    for (int i = 0; i < max_print; i++) {
        fprintf(stderr, "%02X ", buf[i]);
        if ((i + 1) % 32 == 0) fprintf(stderr, "\n");
    }
    if (max_print < len) fprintf(stderr, "... (%d more bytes)", len - max_print);
    fprintf(stderr, "\n");
    fflush(stderr);
}
