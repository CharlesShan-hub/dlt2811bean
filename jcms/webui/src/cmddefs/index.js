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
import getDataValues from './get-data-values.js'
import setDataValues from './set-data-values.js'
import dataDir from './data-dir.js'
import getDataDef from './get-data-def.js'
import getDatasetValues from './get-dataset-values.js'
import setDatasetValues from './set-dataset-values.js'
import createDataset from './create-dataset.js'
import deleteDataset from './delete-dataset.js'
import getDatasetDir from './get-dataset-dir.js'
import selectActiveSg from './select-active-sg.js'
import selectEditSg from './select-edit-sg.js'
import setEditSg from './set-edit-sg.js'
import confirmEditSg from './confirm-edit-sg.js'
import getEditSg from './get-edit-sg.js'
import sgcbVals from './sgcb-vals.js'
import getBrcbVals from './get-brcb-vals.js'
import setBrcbVals from './set-brcb-vals.js'
import getUrcbVals from './get-urcb-vals.js'
import setUrcbVals from './set-urcb-vals.js'
import getLcbVals from './get-lcb-vals.js'
import setLcbVals from './set-lcb-vals.js'
import queryLogByTime from './query-log-by-time.js'
import queryLogAfter from './query-log-after.js'
import getLogStatus from './get-log-status.js'
import getGoRef from './get-go-ref.js'
import getGooseElem from './get-goose-elem.js'
import getGocbVals from './get-gocb-vals.js'
import setGocbVals from './set-gocb-vals.js'
import getMsvcbVals from './get-msvcb-vals.js'
import setMsvcbVals from './set-msvcb-vals.js'
import getFile from './get-file.js'
import setFile from './set-file.js'
import deleteFile from './delete-file.js'
import getFileAttrs from './get-file-attrs.js'
import getFileDir from './get-file-dir.js'
import rpcIfaceDir from './rpc-iface-dir.js'
import rpcMethodDir from './rpc-method-dir.js'
import rpcIfaceDef from './rpc-iface-def.js'
import rpcMethodDef from './rpc-method-def.js'
import rpcCall from './rpc-call.js'

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
  'get-data-values': getDataValues,
  'set-data-values': setDataValues,
  'data-dir': dataDir,
  'get-data-def': getDataDef,
  'get-dataset-values': getDatasetValues,
  'set-dataset-values': setDatasetValues,
  'create-dataset': createDataset,
  'delete-dataset': deleteDataset,
  'get-dataset-dir': getDatasetDir,
  'select-active-sg': selectActiveSg,
  'select-edit-sg': selectEditSg,
  'set-edit-sg': setEditSg,
  'confirm-edit-sg': confirmEditSg,
  'get-edit-sg': getEditSg,
  'sgcb-vals': sgcbVals,
  'get-brcb-vals': getBrcbVals,
  'set-brcb-vals': setBrcbVals,
  'get-urcb-vals': getUrcbVals,
  'set-urcb-vals': setUrcbVals,
  'get-lcb-vals': getLcbVals,
  'set-lcb-vals': setLcbVals,
  'query-log-by-time': queryLogByTime,
  'query-log-after': queryLogAfter,
  'get-log-status': getLogStatus,
  'get-go-ref': getGoRef,
  'get-goose-elem': getGooseElem,
  'get-gocb-vals': getGocbVals,
  'set-gocb-vals': setGocbVals,
  'get-msvcb-vals': getMsvcbVals,
  'set-msvcb-vals': setMsvcbVals,
  'get-file': getFile,
  'set-file': setFile,
  'delete-file': deleteFile,
  'get-file-attrs': getFileAttrs,
  'get-file-dir': getFileDir,
  'rpc-iface-dir': rpcIfaceDir,
  'rpc-method-dir': rpcMethodDir,
  'rpc-iface-def': rpcIfaceDef,
  'rpc-method-def': rpcMethodDef,
  'rpc-call': rpcCall,
}

/** 命令 id 列表（保持注册表顺序）。 */
export const CMD_IDS = Object.keys(CMD_DEFS)
