#include "data/common/cms_phy_com_addr.h"

#define CMS_PHY_COM_ADDR_LEN 6

int cms_phy_com_addr_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_phy_com_addr_t *pdu = (const cms_phy_com_addr_t*)ptr;

    /* 1. addr — OCTET STRING (SIZE(6)), fixed */
    if (!pdu->addr || !pdu->addr->value) return CMS_ERR;
    int err = cms_octet_string_fixed_encode_stream(s, pdu->addr->value, CMS_PHY_COM_ADDR_LEN);
    if (err) return err;

    /* 2. priority — Int8U */
    if (!pdu->priority) return CMS_ERR;
    err = cms_int8u_encode_stream(s, pdu->priority);
    if (err) return err;

    /* 3. vid — Int16U */
    if (!pdu->vid) return CMS_ERR;
    err = cms_int16u_encode_stream(s, pdu->vid);
    if (err) return err;

    /* 4. appid — Int16U */
    if (!pdu->appid) return CMS_ERR;
    err = cms_int16u_encode_stream(s, pdu->appid);
    if (err) return err;

    return CMS_OK;
}

int cms_phy_com_addr_decode_stream(per_stream_t *s, void *ptr) {
    cms_phy_com_addr_t *pdu = (cms_phy_com_addr_t*)ptr;

    /* 1. addr */
    if (!pdu->addr || !pdu->addr->value) return CMS_ERR;
    int err = cms_octet_string_fixed_decode_stream(s, pdu->addr->value, CMS_PHY_COM_ADDR_LEN);
    if (err) return err;
    pdu->addr->len = CMS_PHY_COM_ADDR_LEN;

    /* 2. priority */
    if (!pdu->priority) return CMS_ERR;
    err = cms_int8u_decode_stream(s, pdu->priority);
    if (err) return err;

    /* 3. vid */
    if (!pdu->vid) return CMS_ERR;
    err = cms_int16u_decode_stream(s, pdu->vid);
    if (err) return err;

    /* 4. appid */
    if (!pdu->appid) return CMS_ERR;
    err = cms_int16u_decode_stream(s, pdu->appid);
    if (err) return err;

    return CMS_OK;
}

int cms_phy_com_addr_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_phy_com_addr_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_phy_com_addr_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_phy_com_addr_decode_stream(&s, ptr);
}
