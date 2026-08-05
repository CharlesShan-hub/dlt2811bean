// 通用参数描述，供各命令定义复用（避免 key/label/type 重复书写）。

export const P_IP = { key: 'ip', label: 'IP 地址 ip', type: 'text', placeholder: '127.0.0.1', default: '127.0.0.1' }

export const P_AP = { key: 'ap', label: '访问点 ap', type: 'ap-select' }

export const P_APDU = { key: 'apdu', label: 'APDU 大小 apdu', type: 'number', inline: 'size' }

export const P_ASDU = { key: 'asdu', label: 'ASDU 大小 asdu', type: 'number', inline: 'size' }

export const P_VERSION = { key: 'version', label: '协议版本 version', type: 'number' }

/**
 * 功能约束 FC 全量取值（对应后端 CmsFC：ST..XX，共 13 个）。
 * XX 放首位作为默认（select 默认取 options[0]），即不过滤。
 */
export const FC_OPTIONS = [
  '',      // 空 = 不选
  'XX: 全部（all，不过滤）',
  'ST: 状态（status）',
  'MX: 测量（measurand）',
  'SP: 定值（setpoint）',
  'SV: 替代值（substituted）',
  'CF: 配置（configuration）',
  'DC: 描述（description）',
  'SG: 定值组（setting group）',
  'SE: 定值组编辑（setting group editable）',
  'SR: 服务响应（service response）',
  'OR: 操作（operational）',
  'BL: 闭锁（blocked）',
  'EX: 扩展（extended）',
]
