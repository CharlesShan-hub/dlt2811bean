#include "cms_apdu.h"
#include <string.h>

/* 7.0 APCH / APDU frame */
CMS_EXPORT void cms_apch_encode(uint8_t *buf, const cms_apch_t *apch) {
    buf[0] = apch->cc;
    buf[1] = apch->sc;
    buf[2] = (uint8_t)((apch->fl >> 8) & 0xFF);
    buf[3] = (uint8_t)(apch->fl & 0xFF);
}

CMS_EXPORT void cms_apch_decode(cms_apch_t *apch, const uint8_t *buf) {
    apch->cc = buf[0];
    apch->sc = buf[1];
    apch->fl = (uint16_t)(((uint16_t)buf[2] << 8) | buf[3]);
}

CMS_EXPORT int cms_apdu_encode(uint8_t *buf, size_t cap, size_t *out_len,
                    bool is_resp, bool is_err, uint8_t svc_code,
                    const uint8_t *asdu, size_t asdu_len) {
    size_t total = 4 + asdu_len;
    if (cap < total) return -1;

    cms_apch_t apch;
    apch.cc = cms_cc_make(false, is_resp, is_err);
    apch.sc = svc_code;
    apch.fl = (uint16_t)asdu_len;

    cms_apch_encode(buf, &apch);
    if (asdu_len > 0) {
        memcpy(buf + 4, asdu, asdu_len);
    }

    *out_len = total;
    return 0;
}

CMS_EXPORT int cms_apdu_decode(const uint8_t *buf, size_t len,
                    cms_apch_t *apch,
                    const uint8_t **asdu, size_t *asdu_len) {
    if (len < 4) return -1;

    cms_apch_decode(apch, buf);

    if (len < (size_t)(4 + apch->fl)) return -1;

    *asdu = buf + 4;
    *asdu_len = apch->fl;
    return 0;
}
