// 命令定义注册表：新增命令 = 新建一个文件并在此登记（对象顺序即侧边栏/列表顺序）。
// connect / disconnect 由连接管理主界面（Dashboard）承担，不在此注册。
import negotiate from './negotiate.js'
import associate from './associate.js'
import release from './release.js'
import abort from './abort.js'
import test from './test.js'
import serverDir from './server-dir.js'
import ldDir from './ld-dir.js'
import lnDir from './ln-dir.js'
import allData from './all-data.js'
import allDef from './all-def.js'
import allCb from './all-cb.js'

export const CMD_DEFS = {
  negotiate,
  associate,
  release,
  abort,
  test,
  'server-dir': serverDir,
  'ld-dir': ldDir,
  'ln-dir': lnDir,
  'all-data': allData,
  'all-def': allDef,
  'all-cb': allCb,
}

/** 命令 id 列表（保持注册表顺序）。 */
export const CMD_IDS = Object.keys(CMD_DEFS)
