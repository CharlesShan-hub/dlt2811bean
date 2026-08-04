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
    const res = await executeJson('server-dir --json')
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
    const res = await executeJson(`ld-dir --ld ${ldName} --json`)
    ldLns[ldName] = res.success && Array.isArray(res.data) ? res.data : []
  } catch {
    ldLns[ldName] = []
  }
  return ldLns[ldName]
}

/** 全量 LN 完整引用：由 ldCache + ldLns 拼接（LD/LN），无需额外请求裸 ld-dir。 */
export async function ensureAllLnRefs() {
  if (allLnRefs.length > 0) return allLnRefs
  const refs = []
  for (const ld of ldCache) {
    if (!ldLns[ld]) {
      await ensureLdLns(ld) // 未缓存的 LD 顺带补齐
    }
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
}
