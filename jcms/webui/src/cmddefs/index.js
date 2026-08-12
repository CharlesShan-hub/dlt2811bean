// 命令定义注册表：新增命令 = 新建一个文件并在此登记（对象顺序即侧边栏/列表顺序）。
// connect / disconnect 由连接管理主界面（Dashboard）承担，不在此注册。
import negotiate from './connection/negotiate.js'
import associate from './connection/associate.js'
import release from './connection/release.js'
import abort from './connection/abort.js'
import test from './connection/test.js'
import serverDir from './directory/server-dir.js'
import ldDir from './directory/ld-dir.js'
import lnDir from './directory/ln-dir.js'
import allData from './directory/all-data.js'
import allDef from './directory/all-def.js'
import allCb from './directory/all-cb.js'
import getDataValues from './data/get-data-values.js'
import setDataValues from './data/set-data-values.js'
import dataDir from './data/data-dir.js'
import getDataDef from './data/get-data-def.js'
import getDatasetValues from './dataset/get-dataset-values.js'
import setDatasetValues from './dataset/set-dataset-values.js'
import createDataset from './dataset/create-dataset.js'
import deleteDataset from './dataset/delete-dataset.js'
import getDatasetDir from './dataset/get-dataset-dir.js'
import selectActiveSg from './sg/select-active-sg.js'
import selectEditSg from './sg/select-edit-sg.js'
import setEditSg from './sg/set-edit-sg.js'
import confirmEditSg from './sg/confirm-edit-sg.js'
import getEditSg from './sg/get-edit-sg.js'
import sgcbVals from './sg/sgcb-vals.js'
import getBrcbVals from './report/get-brcb-vals.js'
import setBrcbVals from './report/set-brcb-vals.js'
import getUrcbVals from './report/get-urcb-vals.js'
import setUrcbVals from './report/set-urcb-vals.js'
import getLcbVals from './log/get-lcb-vals.js'
import setLcbVals from './log/set-lcb-vals.js'
import queryLogByTime from './log/query-log-by-time.js'
import queryLogAfter from './log/query-log-after.js'
import getLogStatus from './log/get-log-status.js'
import getGoRef from './goose/get-go-ref.js'
import getGooseElem from './goose/get-goose-elem.js'
import getGocbVals from './goose/get-gocb-vals.js'
import setGocbVals from './goose/set-gocb-vals.js'
import getMsvcbVals from './msv/get-msvcb-vals.js'
import setMsvcbVals from './msv/set-msvcb-vals.js'
import getFile from './file/get-file.js'
import setFile from './file/set-file.js'
import deleteFile from './file/delete-file.js'
import getFileAttrs from './file/get-file-attrs.js'
import getFileDir from './file/get-file-dir.js'
import rpcIfaceDir from './rpc/rpc-iface-dir.js'
import rpcMethodDir from './rpc/rpc-method-dir.js'
import rpcIfaceDef from './rpc/rpc-iface-def.js'
import rpcMethodDef from './rpc/rpc-method-def.js'
import rpcCall from './rpc/rpc-call.js'

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