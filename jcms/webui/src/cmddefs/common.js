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
  { value: '', label: '（不选）' },
  { value: 'XX', label: '全部（all，不过滤）' },
  { value: 'ST', label: '状态（status）' },
  { value: 'MX', label: '测量（measurand）' },
  { value: 'SP', label: '定值（setpoint）' },
  { value: 'SV', label: '替代值（substituted）' },
  { value: 'CF', label: '配置（configuration）' },
  { value: 'DC', label: '描述（description）' },
  { value: 'SG', label: '定值组（setting group）' },
  { value: 'SE', label: '定值组编辑（setting group editable）' },
  { value: 'SR', label: '服务响应（service response）' },
  { value: 'OR', label: '操作（operational）' },
  { value: 'BL', label: '闭锁（blocked）' },
  { value: 'EX', label: '扩展（extended）' },
]

/**
 * 数据类型选项（用于设置数据值时的类型选择）。
 */
export const TYPE_OPTIONS = [
  'visible-string',
  'int32',
  'float32',
  'boolean',
  'int8',
  'int16',
  'int8u',
  'int16u',
  'int32u',
  'int64',
  'int64u',
  'float64',
  'octet-string',
]
