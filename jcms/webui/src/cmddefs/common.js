// 通用参数描述，供各命令定义复用（避免 key/label/type 重复书写）。

export const P_IP = { key: 'ip', label: 'IP 地址', type: 'text', placeholder: '127.0.0.1', default: '127.0.0.1' }

export const P_AP = { key: 'ap', label: '访问点', type: 'ap-select' }

export const P_APDU = { key: 'apdu', label: 'APDU 大小', type: 'number' }

export const P_ASDU = { key: 'asdu', label: 'ASDU 大小', type: 'number' }

export const P_VERSION = { key: 'version', label: '协议版本', type: 'number' }
