import { reactive } from 'vue'
import { executeJson } from './api/cms.js'

/**
 * 逻辑设备（LD）共享缓存：连接建立后由 server-dir 填充。
 * 目录树 / 服务器目录页面的引用选择都从这取，避免重复请求。
 */
export const ldCache = reactive([])

/** LD → LN 引用列表（由 ld-dir 懒加载缓存，供 ld-dir 的 after 引用选择）。 */
export const ldLns = reactive({})

export function setLds(list) {
  ldCache.splice(0, ldCache.length, ...(list || []))
  if (!list || list.length === 0) {
    clearLdLns()
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

/** 清空 LN 缓存（断开连接时）。 */
export function clearLdLns() {
  for (const k of Object.keys(ldLns)) {
    delete ldLns[k]
  }
}
