import { reactive } from 'vue'
import { executeJson } from './api/cms.js'

/**
 * 逻辑设备（LD）共享缓存：连接建立后由 server-dir 填充。
 * 目录树 / 服务器目录页面的引用选择都从这取，避免重复请求。
 */
export const ldCache = reactive([])

export function setLds(list) {
  ldCache.splice(0, ldCache.length, ...(list || []))
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
