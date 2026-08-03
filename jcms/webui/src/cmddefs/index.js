// 命令定义注册表：新增命令 = 新建一个文件并在此登记（对象顺序即侧边栏/列表顺序）。
import connect from './connect.js'
import disconnect from './disconnect.js'
import negotiate from './negotiate.js'
import associate from './associate.js'
import release from './release.js'
import abort from './abort.js'
import test from './test.js'
import serverDir from './server-dir.js'

export const CMD_DEFS = {
  connect,
  disconnect,
  negotiate,
  associate,
  release,
  abort,
  test,
  'server-dir': serverDir,
}

/** 命令 id 列表（保持注册表顺序）。 */
export const CMD_IDS = Object.keys(CMD_DEFS)
