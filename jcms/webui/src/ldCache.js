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
}

/** all-def 响应引用缓存（供 all-data / all-def 的 after 下拉），key 为 "ln|fc"。 */
export const allDefRefs = reactive([])
let allDefRefsKey = ''

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
    allDefRefs.splice(0, allDefRefs.length, ...(res.success && Array.isArray(res.data) ? res.data.map((d) => d.ref).filter(Boolean) : []))
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
    allCbRefs.splice(0, allCbRefs.length, ...(res.success && Array.isArray(res.data) ? res.data.map((d) => d.reference).filter(Boolean) : []))
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
    lnDirRefs.splice(0, lnDirRefs.length, ...(res.success && Array.isArray(res.data) ? res.data : []))
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
    if (res.success && Array.isArray(res.data)) {
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
    ldLns[ldName] = res.success && Array.isArray(res.data) ? res.data : []
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
