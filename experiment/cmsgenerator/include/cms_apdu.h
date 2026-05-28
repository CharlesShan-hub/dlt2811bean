#ifndef CMS_APDU_H
#define CMS_APDU_H

#include <stdint.h>
#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== Service Code 枚举 ==================== */
/* 与 Java ServiceName 保持一致 */
#define CMS_SVC_ASSOCIATE                   0x01
#define CMS_SVC_ABORT                       0x02
#define CMS_SVC_RELEASE                     0x03
#define CMS_SVC_GET_SERVER_DIRECTORY        0x50
#define CMS_SVC_GET_LOGIC_DEVICE_DIRECTORY  0x51
#define CMS_SVC_GET_LOGIC_NODE_DIRECTORY    0x52
#define CMS_SVC_GET_ALL_DATA_VALUES         0x53
#define CMS_SVC_GET_ALL_DATA_DEFINITION     0x9B
#define CMS_SVC_GET_ALL_CB_VALUES           0x9C
#define CMS_SVC_GET_DATA_VALUES             0x30
#define CMS_SVC_SET_DATA_VALUES             0x31
#define CMS_SVC_GET_DATA_DIRECTORY          0x32
#define CMS_SVC_GET_DATA_DEFINITION         0x33
#define CMS_SVC_GET_DATA_SET_VALUES         0x3A
#define CMS_SVC_SET_DATA_SET_VALUES         0x3B
#define CMS_SVC_CREATE_DATA_SET             0x36
#define CMS_SVC_DELETE_DATA_SET             0x37
#define CMS_SVC_GET_DATA_SET_DIRECTORY      0x39
#define CMS_SVC_SELECT_ACTIVE_SG            0x54
#define CMS_SVC_SELECT_EDIT_SG              0x55
#define CMS_SVC_SET_EDIT_SG_VALUE           0x56
#define CMS_SVC_CONFIRM_EDIT_SG_VALUES      0x57
#define CMS_SVC_GET_EDIT_SG_VALUE           0x58
#define CMS_SVC_GET_SGCB_VALUES             0x59
#define CMS_SVC_REPORT                      0x5A
#define CMS_SVC_GET_BRCB_VALUES             0x5B
#define CMS_SVC_SET_BRCB_VALUES             0x5C
#define CMS_SVC_GET_URCB_VALUES             0x5D
#define CMS_SVC_SET_URCB_VALUES             0x5E
#define CMS_SVC_GET_LCB_VALUES              0x5F
#define CMS_SVC_SET_LCB_VALUES              0x60
#define CMS_SVC_QUERY_LOG_BY_TIME           0x61
#define CMS_SVC_QUERY_LOG_AFTER             0x62
#define CMS_SVC_GET_LOG_STATUS_VALUES       0x63
#define CMS_SVC_GET_GOCB_VALUES             0x66
#define CMS_SVC_SET_GOCB_VALUES             0x67
#define CMS_SVC_GET_MSVCB_VALUES            0x69
#define CMS_SVC_SET_MSVCB_VALUES            0x6A
#define CMS_SVC_SELECT                      0x44
#define CMS_SVC_SELECT_WITH_VALUE           0x45
#define CMS_SVC_OPERATE                     0x47
#define CMS_SVC_CANCEL                      0x46
#define CMS_SVC_COMMAND_TERMINATION         0x48
#define CMS_SVC_TIME_ACTIVATED_OPERATE      0x49
#define CMS_SVC_TIME_ACTIVATED_OPERATE_TERM 0x4A
#define CMS_SVC_GET_FILE                    0x80
#define CMS_SVC_SET_FILE                    0x81
#define CMS_SVC_DELETE_FILE                 0x82
#define CMS_SVC_GET_FILE_ATTRIBUTE_VALUES   0x83
#define CMS_SVC_GET_FILE_DIRECTORY          0x84
#define CMS_SVC_GET_RPC_INTERFACE_DIR       0x6E
#define CMS_SVC_GET_RPC_METHOD_DIR          0x6F
#define CMS_SVC_GET_RPC_INTERFACE_DEF       0x70
#define CMS_SVC_GET_RPC_METHOD_DEF          0x71
#define CMS_SVC_RPC_CALL                    0x72
#define CMS_SVC_TEST                        0x99
#define CMS_SVC_ASSOCIATE_NEGOTIATE         0x9A

/* ==================== APCH (4-byte frame header) ==================== */
/*
 * APCH 帧头格式 (与 Java CmsApch 一致):
 * ┌──────────┬────────┬──────────┐
 * │ CC (1B)  │ SC(1B) │ FL (2B)  │
 * │ Control  │ SvcCode│ FrameLen │
 * │ Code     │        │ (大端)   │
 * └──────────┴────────┴──────────┘
 *
 * Control Code 字节布局:
 * bit7: Next (0=last, 1=more fragments)
 * bit6: Resp (0=request, 1=response)
 * bit5: Err  (0=positive, 1=negative)
 * bit4: bak (reserved, shall be 0)
 * bit3~0: PI (protocol identifier, default 0x01)
 */
typedef struct {
    uint8_t cc;   /* Control Code */
    uint8_t sc;   /* Service Code */
    uint16_t fl;  /* Frame Length (大端) */
} cms_apch_t;

/* 构建 Control Code */
static inline uint8_t cms_cc_make(bool next, bool resp, bool err) {
    return (uint8_t)((next ? 0x80 : 0) | (resp ? 0x40 : 0) | (err ? 0x20 : 0) | 0x01);
}

static inline bool cms_cc_is_next(uint8_t cc) { return (cc & 0x80) != 0; }
static inline bool cms_cc_is_resp(uint8_t cc) { return (cc & 0x40) != 0; }
static inline bool cms_cc_is_err(uint8_t cc)  { return (cc & 0x20) != 0; }

/* ==================== APDU 编解码 ==================== */

/* 编码 APCH 头 (4 字节) */
void cms_apch_encode(uint8_t *buf, const cms_apch_t *apch);

/* 解码 APCH 头 (4 字节) */
void cms_apch_decode(cms_apch_t *apch, const uint8_t *buf);

/* 编码完整 APDU: APCH(4B) + ASDU */
int cms_apdu_encode(uint8_t *buf, size_t cap, size_t *out_len,
                    bool is_resp, bool is_err, uint8_t svc_code,
                    const uint8_t *asdu, size_t asdu_len);

/* 解码完整 APDU: APCH(4B) + ASDU */
int cms_apdu_decode(const uint8_t *buf, size_t len,
                    cms_apch_t *apch,
                    const uint8_t **asdu, size_t *asdu_len);

#ifdef __cplusplus
}
#endif

#endif /* CMS_APDU_H */
