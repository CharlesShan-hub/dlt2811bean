import { reactive } from 'vue'
import { executeJson } from './api/cms.js'

/**
 * 逻辑设备（LD）共享缓存：连接建立后由 server-dir 填充。
 * 目录树 / 服务器目录页面的引用选择都从这取，避免重复请求。
 */
export const ldCache = reactive([])

/** LD → LN 引用列表（由 ld-dir 懒加载缓存，供 ld-dir 的 after 引用选择）。 */
export const ldLns = reactive({})

/** 全量 LN 完整引用列表（省略 ldName 的 ld-dir 结果，如 "LD0/GGIO2"）。 */
export const allLnRefs = reactive([])

/** LN 目录子引用缓存（ln-dir 响应，供 ln-dir 的 after 下拉选择），key 为 "ln|acsi"。 */
export const lnDirRefs = reactive([])
let lnDirRefsKey = ''

/** 清空 ln-dir 子引用缓存（断开连接或 LN/ACSI 切换时）。 */
export function clearLnDirRefs() {
  lnDirRefs.splice(0)
  lnDirRefsKey = ''
  allDefRefs.splice(0)
  allDefRefsKey = ''
  allCbRefs.splice(0)
  allCbRefsKey = ''
  clearAllDefData()
  clearDatasetRefs()
}

/** all-def 响应引用缓存（供 all-data / all-def 的 after 下拉），key 为 "ln|fc"。 */
export const allDefRefs = reactive([])
let allDefRefsKey = ''

/**
 * all-def 完整数据结构缓存（含 DO 的 structure 及 DA 类型），key 为 lnRef。
 * 值: { "DO名称": { cdcType, structure: [{name, fc, type}] } }
 */
const allDefData = {}

/**
 * 获取某 LN 的 all-def 完整数据（含 structure 类型信息），幂等。
 * @param {string} lnRef 如 "LD0/LLN0"
 * @returns {Promise<object>} DO 名 → 结构信息的 Map
 */
export async function ensureAllDefData(lnRef) {
  if (!lnRef) return {}
  if (allDefData[lnRef]) return allDefData[lnRef]
  try {
    const res = await executeJson(`all-def --ln ${lnRef} --fc XX --auto-pull true --json`)
    const map = {}
    if (res && Array.isArray(res.data)) {
      for (const entry of res.data) {
        if (entry.reference) {
          map[entry.reference] = {
            cdcType: entry.cdcType || '',
            structure: (entry.definition?.structure || []).map(s => ({
              name: s.name,
              fc: s.fc,
              type: s.type ? Object.keys(s.type)[0] || '' : '',
            })),
          }
        }
      }
    }
    allDefData[lnRef] = map
  } catch {
    allDefData[lnRef] = {}
  }
  return allDefData[lnRef]
}

/** 数据集引用缓存（get-dataset-values 用），key 为 lnRef（如 "LD0/LLN0"）。 */
export const datasetRefs = reactive({})

/**
 * 拉取某 LN 下的数据集名称列表（经 ln-dir --acsi data-set 查询，幂等）。
 * @param {string} lnRef 如 "LD0/LLN0"
 * @returns {Promise<string[]>} 数据集名称列表，如 ["dsRelayEna", "dsGridEna"]
 */
export async function ensureDatasetRefs(lnRef) {
  if (!lnRef) return []
  if (datasetRefs[lnRef]) return datasetRefs[lnRef]
  try {
    const res = await executeJson(`ln-dir --ln ${lnRef} --acsi data-set --auto-pull true --json`)
    datasetRefs[lnRef] = res && Array.isArray(res.reference) ? res.reference : []
  } catch {
    datasetRefs[lnRef] = []
  }
  return datasetRefs[lnRef]
}

/** 清空数据集缓存（断开连接时）。 */
export function clearDatasetRefs() {
  Object.keys(datasetRefs).forEach(k => delete datasetRefs[k])
  clearDatasetMemberRefs()
}

/** 数据集成员引用缓存（get-dataset-dir 用），key 为 "lnRef.dsName"（如 "LD0/LLN0.dsAlarm"）。 */
export const datasetMemberRefs = reactive({})

/**
 * 拉取某数据集的所有成员引用（经 get-dataset-dir 查询，幂等）。
 * @param {string} lnRef 如 "LD0/LLN0"
 * @param {string} dsName 数据集名称，如 "dsAlarm"
 * @returns {Promise<string[]>} 成员引用列表，如 ["LD0/GGIO1.Alm1", "LD0/GGIO1.Alm2"]
 */
export async function ensureDatasetMemberRefs(lnRef, dsName) {
  if (!lnRef || !dsName) return []
  const key = `${lnRef}.${dsName}`
  if (datasetMemberRefs[key]) return datasetMemberRefs[key]
  try {
    const res = await executeJson(`get-dataset-dir --ds ${lnRef}.${dsName} --auto-pull true --json`)
    datasetMemberRefs[key] = res && Array.isArray(res.memberData) ? res.memberData.map(m => m.reference).filter(Boolean) : []
  } catch {
    datasetMemberRefs[key] = []
  }
  return datasetMemberRefs[key]
}

/** 数据集成员完整数据缓存（含 reference + fc），key 为 "lnRef.dsName"（如 "LD0/LLN0.dsAlarm"）。 */
export const datasetMembers = reactive({})

/**
 * 拉取某数据集的所有成员完整数据（含 reference + fc，经 get-dataset-dir 查询，幂等）。
 * @param {string} lnRef 如 "LD0/LLN0"
 * @param {string} dsName 数据集名称，如 "dsAlarm"
 * @returns {Promise<Array<{reference: string, fc: string}>>} 成员完整数据列表
 */
export async function ensureDatasetMembers(lnRef, dsName) {
  if (!lnRef || !dsName) return []
  const key = `${lnRef}.${dsName}`
  if (datasetMembers[key]) return datasetMembers[key]
  try {
    const res = await executeJson(`get-dataset-dir --ds ${lnRef}.${dsName} --auto-pull true --json`)
    datasetMembers[key] = res && Array.isArray(res.memberData) ? res.memberData.filter(m => m.reference) : []
  } catch {
    datasetMembers[key] = []
  }
  return datasetMembers[key]
}

/** 清空数据集成员引用缓存（断开连接时）。 */
function clearDatasetMemberRefs() {
  Object.keys(datasetMemberRefs).forEach(k => delete datasetMemberRefs[k])
  Object.keys(datasetMembers).forEach(k => delete datasetMembers[k])
}

/** 清空 all-def 数据缓存（断开连接时）。 */
export function clearAllDefData() {
  Object.keys(allDefData).forEach(k => delete allDefData[k])
}

/**
 * 拉取某 LN（或 LD）在指定 fc 过滤下的数据对象引用（经轻量的 all-def 查询，
 * 供 all-data / all-def 的 after 下拉；值域与 all-data 返回的 DO 引用一致）。
 * @param {string} lnRef ln-select 值，如 "LD0" 或 "LD0/LLN0"
 * @param {string} fc 功能约束码（如 "XX"、"ST"）
 */
export async function ensureAllDefRefs(lnRef, fc) {
  if (!lnRef) {
    allDefRefs.splice(0)
    return allDefRefs
  }
  const key = `${lnRef}|${fc}`
  if (allDefRefsKey === key && allDefRefs.length > 0) return allDefRefs
  try {
    const res = await executeJson(`all-def --ln ${lnRef} --fc ${fc} --auto-pull true --json`)
    allDefRefs.splice(0, allDefRefs.length, ...(res && Array.isArray(res.data) ? res.data.map((d) => d.reference).filter(Boolean) : []))
    allDefRefsKey = key
  } catch {
    allDefRefs.splice(0)
    allDefRefsKey = key
  }
  return allDefRefs
}

/** all-cb 响应引用缓存（供 all-cb 的 after 下拉），key 为 "ln|acsi"。 */
export const allCbRefs = reactive([])
let allCbRefsKey = ''

/**
 * 拉取某 LN（或 LD）在指定 ACSI 控制块类下的引用列表（经 all-cb 查询，
 * 供 all-cb 的 after 下拉）。
 * @param {string} lnRef ln-select 值，如 "LD0" 或 "LD0/LLN0"
 * @param {string} acsi ACSI 控制块类（如 "brcb"、"sgcb"）
 */
export async function ensureAllCbRefs(lnRef, acsi) {
  if (!lnRef) {
    allCbRefs.splice(0)
    return allCbRefs
  }
  const key = `${lnRef}|${acsi}`
  if (allCbRefsKey === key && allCbRefs.length > 0) return allCbRefs
  try {
    const res = await executeJson(`all-cb --ln ${lnRef} --acsi ${acsi} --auto-pull true --json`)
    allCbRefs.splice(0, allCbRefs.length, ...(res && Array.isArray(res.cbValue) ? res.cbValue.map((d) => d.reference).filter(Boolean) : []))
    allCbRefsKey = key
  } catch {
    allCbRefs.splice(0)
    allCbRefsKey = key
  }
  return allCbRefs
}

/**
 * 拉取某 LN（或 LD）在指定 ACSI 类下的子引用列表（幂等，供 ln-dir 的 after 下拉）。
 * @param {string} lnRef ln-select 值，如 "LD0" 或 "LD0/LLN0"
 * @param {number|string} acsi ACSI 数字（如 1）
 */
export async function ensureLnDirRefs(lnRef, acsi) {
  if (!lnRef) {
    lnDirRefs.splice(0)
    return lnDirRefs
  }
  const key = `${lnRef}|${acsi}`
  if (lnDirRefsKey === key && lnDirRefs.length > 0) return lnDirRefs
  try {
    const res = await executeJson(`ln-dir --ln ${lnRef} --acsi ${acsi} --auto-pull true --json`)
    lnDirRefs.splice(0, lnDirRefs.length, ...(res && Array.isArray(res.reference) ? res.reference : []))
    lnDirRefsKey = key
  } catch {
    lnDirRefs.splice(0)
    lnDirRefsKey = key
  }
  return lnDirRefs
}

export function setLds(list) {
  ldCache.splice(0, ldCache.length, ...(list || []))
  if (!list || list.length === 0) {
    clearLdLns()
    allLnRefs.splice(0)
  }
}

/** 从服务器拉取逻辑设备列表并写入缓存。 */
export async function refreshLds() {
  try {
    const res = await executeJson('server-dir --auto-pull true --json')
    // 新格式：直接返回 { reference: [...], moreFollows }
    if (res.reference) {
      setLds(res.reference)
    } else if (res.success && res.data) {
      // 旧格式：data 直接是数组
      setLds(res.data)
    } else {
      setLds([])
    }
  } catch {
    setLds([])
  }
}

/** 拉取并缓存某 LD 下的 LN 列表（幂等）。 */
export async function ensureLdLns(ldName) {
  if (!ldName) return []
  if (ldLns[ldName]) return ldLns[ldName]
  try {
    const res = await executeJson(`ld-dir --ld ${ldName} --auto-pull true --json`)
    ldLns[ldName] = res && Array.isArray(res.lnReference) ? res.lnReference : []
  } catch {
    ldLns[ldName] = []
  }
  return ldLns[ldName]
}

/** 全量 LN 完整引用：由 ldCache + ldLns 拼接（LD/LN），无需额外请求裸 ld-dir。 */
export async function ensureAllLnRefs() {
  if (allLnRefs.length > 0) return allLnRefs
  // 并发拉取所有未缓存的 LD 的 LN 列表
  await Promise.all(ldCache.filter(ld => !ldLns[ld]).map(ld => ensureLdLns(ld)))
  const refs = []
  for (const ld of ldCache) {
    for (const ln of ldLns[ld] || []) {
      refs.push(`${ld}/${ln}`)
    }
  }
  allLnRefs.splice(0, allLnRefs.length, ...refs)
  return allLnRefs
}

/** 清空 LN 缓存（断开连接时）。 */
export function clearLdLns() {
  for (const k of Object.keys(ldLns)) {
    delete ldLns[k]
  }
  clearLnDirRefs()
}
