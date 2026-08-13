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
  { value: '', label: '（不选）', desc: '' },
  { value: 'XX', label: 'XX', desc: '通配（任意）' },
  { value: 'ST', label: 'ST', desc: '状态值 Status' },
  { value: 'MX', label: 'MX', desc: '测量值 Measurand' },
  { value: 'SP', label: 'SP', desc: '设定值 Setpoint' },
  { value: 'SV', label: 'SV', desc: '取代值 Substitution' },
  { value: 'CF', label: 'CF', desc: '配置 Configuration' },
  { value: 'DC', label: 'DC', desc: '描述 Description' },
  { value: 'SG', label: 'SG', desc: '定值组 Setting group' },
  { value: 'SE', label: 'SE', desc: '定值组可编辑 Setting group editable' },
  { value: 'SR', label: 'SR', desc: '服务响应 Service response' },
  { value: 'OR', label: 'OR', desc: '运行报告 Operating report' },
  { value: 'BL', label: 'BL', desc: '联锁 Block' },
  { value: 'EX', label: 'EX', desc: '扩展 Extension' },
]

/** FC 代码 → 描述映射 */
export const FC_DESC_MAP = Object.fromEntries(FC_OPTIONS.filter(o => o.value).map(o => [o.value, o.desc]))

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
