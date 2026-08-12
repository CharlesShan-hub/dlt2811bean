<template>
  <div class="cmd-debug">
    <header class="page-head">
      <div class="title-row">
        <div class="title-left">
          <span v-if="def.doc" class="sec-badge doc-badge" title="协议说明" @click="docOpen = true">✦ {{ section }}</span>
          <span v-else-if="section" class="sec-badge">✦ {{ section }}</span>
          <h1 class="page-title">{{ isConnect ? '连接管理' : shortTitle }}</h1>
        </div>
        <div class="title-right">
          <button type="button" class="glass asn1-toggle-header-btn" :title="showAsn1 ? '隐藏 ASN.1' : '显示 ASN.1'" @click="showAsn1 = !showAsn1">𝔄</button>
          <template v-if="isConnect">
            <code class="glass cmd-chip">connect</code>
            <span class="desc-text">TCP → 协商 → 关联</span>
            <span class="sep">·</span>
            <code class="glass cmd-chip">disconnect</code>
            <span class="desc-text">断开 TCP</span>
            <span class="sep">·</span>
            <code class="glass cmd-chip">release</code>
            <span class="desc-text">断开 AP</span>
          </template>
          <template v-else>
            <code class="glass cmd-chip">{{ props.cmd }}</code>
            <span class="sep">·</span>
            <span class="desc-text">{{ def.desc }}</span>
          </template>
        </div>
      </div>
    </header>

    <UiModal v-model="docOpen" wide title="协议说明">
      <div v-if="def.doc" class="doc-md" v-html="docHtml"></div>
      <template v-else>
        <pre v-if="def.asn1" class="doc-asn1">{{ def.asn1 }}</pre>
        <p v-else class="doc-desc muted">该命令没有独立的协议说明。</p>
      </template>
    </UiModal>

    <div class="debug-grid" ref="gridRef" :style="{ gridTemplateColumns: leftColWidth + 'px 6px 1fr' }">
      <!-- ── 左栏：参数 ── -->
      <CommandParamsPanel
        :def="def"
        :form="form"
        :busy="busy"
        :is-connect="isConnect"
        :cmd="props.cmd"
        :connected="props.connected"
        :tcp-connected="props.tcpConnected"
        :ld-cache="ldCache"
        :ld-lns="ldLns"
        :ref-options="refOptions"
        :fc-row-options="fcRowOptions"
        :refs-list-options="refsListOptions"
        :ln-ref="lnRef"
        :conn-msg="connMsg"
        :conn-msg-ok="connMsgOk"
        :simple-params="simpleParams"
        :param-rows="paramRows"
        :form-valid="formValid"
        :cascade-lns="cascadeLns"
        :on-cascade-ld="onCascadeLd"
        :cascade-ld-disabled="cascadeLdDisabled"
        :add-refs="addRefs"
        :remove-refs="removeRefs"
        :row-do-options="rowDoOptions"
        :on-row-ld="onRowLd"
        :on-row-ln="onRowLn"
        :on-row-do="onRowDo"
        :on-row-sdo="onRowSdo"
        :row-sdo-options="rowSdoOptions"
        :row-da-options="rowDaOptions"
        :on-row-da="onRowDa"
        @run="run"
        @run-cmd="runCmd"
        @disconnect-tcp="disconnectTcp"
        @release-ap="releaseAp"
        @open-value-editor="openValueEditor"
      />

      <!-- 垂直拖拽手柄 -->
      <div class="drag-v" @mousedown.prevent="startVDrag"></div>

      <!-- ── 右栏：流程 + 命令与返回 ── -->
      <CommandResultPanel
        :def="def"
        :result="result"
        :show-asn1="showAsn1"
        :is-connect="isConnect"
        :active-state="activeState"
        :right-title="rightTitle"
        :connect-flow="connectFlow"
        :top-height="topHeight"
        :json-format="jsonFormat"
        :formatted-json="formattedJson"
        :output-lines="outputLines"
        :highlighted-cmd="highlightedCmd"
        :highlighted-result-cmd="highlightedResultCmd"
        :preview-cmd="previewCmd"
        @update:show-asn1="showAsn1 = $event"
        @update:json-format="jsonFormat = $event"
        @edit="onCmdEdit"
        @start-h-drag="startHDrag"
      />
    </div>
  </div>
  <ComplexValueEditor
    :model-value="editorRow?.value ?? ''"
    :visible="editorVisible"
    :type="editorType"
    @update:visible="editorVisible = $event"
    @confirm="onEditorConfirm"
  />
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { debugShared } from '../stores/debugShared.js'
import CommandParamsPanel from '../components/CommandParamsPanel.vue'
import CommandResultPanel from '../components/CommandResultPanel.vue'
import ComplexValueEditor from '../components/ComplexValueEditor.vue'
import UiModal from '../components/ui/UiModal.vue'
import { executeCommand, executeJson } from '../api/cms.js'
import { marked } from 'marked'
import { CMD_DEFS } from '../cmddefs/index.js'
import { CONNECT_FLOW } from '../cmddefs/connect.js'
import { pushTerminal } from '../terminalLog.js'
import { ldCache, ldLns, allLnRefs, lnDirRefs, allDefRefs, allCbRefs, ensureLdLns, ensureAllLnRefs, ensureLnDirRefs, ensureAllDefRefs, ensureAllCbRefs } from '../ldCache.js'
import { buildCmd, highlightCmdStr, syntaxHighlightJson, parseResult, parseCmd } from '../utils/cmdFormat.js'
import { FC_OPTIONS } from '../cmddefs/common.js'
import { useSplitPane } from '../composables/useSplitPane.js'
import { useCommandForm } from '../composables/useCommandForm.js'

const props = defineProps({
  cmd: String,
  connected: { type: Boolean, default: false },
  tcpConnected: { type: Boolean, default: false },
})

const connectFlow = CONNECT_FLOW

const def = computed(() => CMD_DEFS[props.cmd] || { title: props.cmd, desc: '', params: [] })

const activeState = computed(() => {
  if (props.connected) return 'assoc'
  if (props.tcpConnected) return 'tcp'
  return 'init'
})

const titleParts = computed(() => {
  const m = (def.value.title || '').match(/^(.*?)\s+\S+\s*\(([\d.]+)\)$/)
  return m ? { name: m[1], section: m[2] } : { name: def.value.title, section: '' }
})
const shortTitle = computed(() => titleParts.value.name)
const section = computed(() => titleParts.value.section)
const isConnect = computed(() => props.cmd === 'connect')
const simpleParams = computed(() => def.value.params)

const paramRows = computed(() => {
  const rows = []
  let cur = null
  for (const p of def.value.params || []) {
    if (p.inline) {
      if (!cur || cur.inline !== p.inline) {
        cur = { inline: p.inline, items: [] }
        rows.push(cur)
      }
      cur.items.push(p)
    } else {
      rows.push({ inline: null, items: [p] })
    }
  }
  return rows
})

const lnRef = computed(() => {
  const p = def.value.params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
  const o = p ? form[p.key] : null
  return o && o.ld ? (o.ln ? `${o.ld}/${o.ln}` : o.ld) : ''
})

function cascadeLns(o) {
  return o && o.ld ? ldLns[o.ld] || [] : []
}

function onCascadeLd(key) {
  const o = form[key]
  if (!o) return
  o.ln = ''
  if (o.ld) ensureLdLns(o.ld)
}

function cascadeLdDisabled(p) {
  return props.cmd === 'ld-dir' && p.key === 'after' && !!form.ld
}

const refOptions = computed(() => {
  if (props.cmd === 'ln-dir') return lnDirRefs
  if (props.cmd === 'all-cb') return allCbRefs
  return allDefRefs
})

const refsListOptions = computed(() => allLnRefs)

const fcRowOptions = computed(() => FC_OPTIONS)

const rightTitle = computed(() => {
  if (isConnect.value) return '连接流程'
  return def.value.asn1 ? 'ASN.1' : '服务说明'
})

const form = reactive({})
const busy = ref(false)
const result = ref(null)
const connectCmd = ref('')
const docOpen = ref(false)

const editorVisible = ref(false)
const editorRow = ref(null)
const editorType = ref('')

function openValueEditor(row) {
  editorRow.value = row
  editorType.value = row._resolvedType || ''
  editorVisible.value = true
}

function onEditorConfirm(val) {
  if (editorRow.value) {
    editorRow.value.value = val
  }
}

const {
  gridRef,
  leftColWidth,
  topHeight,
  showAsn1,
  startVDrag,
  startHDrag,
} = useSplitPane()

const {
  initForm,
  addRefs,
  removeRefs,
  rowDoOptions,
  onRowLd,
  onRowLn,
  onRowDo,
  onRowSdo,
  rowSdoOptions,
  rowDaOptions,
  loadNegotiateDefaults,
  setupFcWatch,
  setupLnRequiredWatch,
  setupLdDirWatch,
  setupLazyLnWatch,
  setupRefsWatch,
  setupAllDataRefsWatch,
} = useCommandForm(form, {
  getDef: () => def.value,
  getCmd: () => props.cmd,
  getLnRef: () => lnRef.value,
  lnRequiredCmds: ['ln-dir', 'all-data', 'all-def', 'all-cb'],
})

const formValid = computed(() => {
  if (isConnect.value) return true
  const params = def.value.params || []
  for (const p of params) {
    const v = form[p.key]
    if (p.type === 'ln-cascade') {
      if (v && v.ln && !v.ld) return false
      if (props.cmd === 'ld-dir' && p.key === 'after' && !form.ld) {
        if (v && v.ld && !v.ln) return false
      }
    }
    if (p.type === 'refs-list' && p.cascade) {
      const rows = v || []
      for (const r of rows) {
        if (r && r.ln && !r.ld) return false
      }
    }
    if (!p.required) continue
    if (p.type === 'ln-cascade') {
      if (!v || !v.ld) return false
    } else if (p.type === 'ld-select') {
      if (!v) return false
    } else if (p.type === 'refs-list') {
      const rows = v || []
      const hasValid = p.cascade
        ? rows.some(r => r && r.ld && r.ln)
        : rows.some(r => r && typeof r === 'string' && r)
      if (!hasValid) return false
    } else if (v === '' || v === null || v === undefined) {
      return false
    }
  }
  return true
})

const connMsg = ref('')
const connMsgOk = ref(true)

const jsonFormat = ref(localStorage.getItem('cms-json-format') !== '0')
watch(jsonFormat, (v) => localStorage.setItem('cms-json-format', v ? '1' : '0'))

const formattedJson = computed(() => {
  if (!result.value) return ''
  const raw = result.value.output.replace(/\x1b\[\d+m/g, '').trim()
  const jsonStart = raw.indexOf('{')
  if (jsonStart < 0) return ''
  try {
    const parsed = JSON.parse(raw.slice(jsonStart))
    return syntaxHighlightJson(JSON.stringify(parsed, null, 2))
  } catch {
    return ''
  }
})

const outputLines = computed(() => {
  if (!result.value) return []
  return result.value.output.split('\n')
})

const docHtml = computed(() => marked.parse(def.value.doc || ''))

const previewCmd = computed(() => {
  if (isConnect.value) {
    return connectCmd.value
  }
  return buildCmd(props.cmd, def.value.params, form, { cmdProp: props.cmd })
})

const highlightedCmd = computed(() => highlightCmdStr(previewCmd.value))

const highlightedResultCmd = computed(() => {
  return result.value ? highlightCmdStr(result.value.cmd) : ''
})

// 设置 watcher
setupFcWatch()
setupLnRequiredWatch()
setupLdDirWatch()
setupLazyLnWatch()
setupRefsWatch()
setupAllDataRefsWatch()

// cmd 切换时重置表单 + 加载 negotiate 默认值
watch(() => props.cmd, async () => {
  result.value = null
  connectCmd.value = ''
  initForm()
  if (props.cmd === 'negotiate') {
    await loadNegotiateDefaults()
  }
}, { immediate: true })

async function run() {
  await runCmd(buildCmd(props.cmd, def.value.params, form, { cmdProp: props.cmd }))
}

function onCmdEdit(cmdStr) {
  const cmdName = (cmdStr.match(/^\S+/) || [''])[0]
  const curCmd = props.cmd
  if (cmdName !== curCmd) {
    pushTerminal(['⚠ 双击编辑未同步：命令名不匹配，当前页面为 ' + curCmd])
    return
  }
  const parsed = parseCmd(cmdStr, def.value.params, { cmdName: curCmd })
  if (!parsed.valid) {
    for (const err of parsed.errors) {
      pushTerminal(['⚠ 双击编辑未同步：' + err])
    }
    return
  }
  for (const key of Object.keys(parsed.form)) {
    if (key in form) {
      const val = parsed.form[key]
      if (val && typeof val === 'object' && val.ld === '' && val.ln && form.ld) {
        form[key] = { ld: form.ld, ln: val.ln }
      } else {
        form[key] = val
      }
      if (typeof val === 'string' && key === 'ld' && val) {
        ensureLdLns(val)
      } else if (val && typeof val === 'object' && val.ld) {
        ensureLdLns(val.ld)
      }
    }
  }
}

async function runCmd(cmdLine) {
  busy.value = true
  try {
    const final = cmdLine
    const text = await executeCommand(final)
    result.value = {
      cmd: final,
      output: text.trim(),
      time: new Date().toLocaleTimeString(),
    }
    pushTerminal([`$ ${final}`, text.trim()])
    if (isConnect.value) {
      const res = parseResult(text)
      if (res) {
        connMsg.value = res.success ? (res.message || '操作成功') : (res.error || '操作失败')
        connMsgOk.value = !!res.success
      } else {
        connMsg.value = '命令已执行'
        connMsgOk.value = true
      }
    }
  } catch (e) {
    result.value = {
      cmd: cmdLine,
      output: String(e),
      time: new Date().toLocaleTimeString(),
    }
    pushTerminal([`$ ${cmdLine}`, 'ERR ' + e])
    if (isConnect.value) {
      connMsg.value = String(e)
      connMsgOk.value = false
    }
  } finally {
    busy.value = false
  }
}

async function disconnectTcp() {
  await runCmd('disconnect')
}

async function releaseAp() {
  await runCmd('release')
}
</script>

<style scoped>
.cmd-debug {
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding: 24px 32px;
  overflow: hidden;
}

.page-head {
  margin-bottom: 20px;
  flex-shrink: 0;
}

.page-title {
  font-size: 22px;
  font-weight: 700;
}

.title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 6px;
}

.title-left {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.title-left .sec-badge {
  padding: 3px 12px;
  font-size: 13px;
}

.title-right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  flex-wrap: wrap;
  text-align: right;
  color: var(--text-secondary);
  font-size: 13px;
  min-width: 0;
}

.sec-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: rgba(91, 141, 239, 0.15);
  color: var(--accent);
  border-radius: 999px;
  padding: 2px 10px;
  font-size: 12px;
  font-weight: 600;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(91, 141, 239, 0.2);
}

.cmd-chip {
  font-family: var(--font-mono);
  font-size: 12px;
  color: var(--text-primary);
  border-radius: 5px;
  padding: 1px 8px;
}

.sep {
  color: var(--text-muted);
}

.desc-text {
  color: var(--text-secondary);
}

.doc-badge {
  cursor: pointer;
  transition: background 0.15s, transform 0.12s;
}
.doc-badge:hover {
  background: var(--accent);
  color: #fff;
  transform: scale(1.05);
}

.asn1-toggle-header-btn {
  flex-shrink: 0;
  width: 30px;
  height: 30px;
  border-radius: 8px;
  font-size: 16px;
  font-style: italic;
  font-weight: bold;
  line-height: 1;
  cursor: pointer;
  color: var(--text-secondary);
}
.asn1-toggle-header-btn:hover {
  color: var(--text-primary);
  transform: scale(1.08);
}

.doc-desc {
  margin: 0 0 12px;
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-secondary);
}

.doc-desc.muted {
  color: var(--text-muted);
}

.doc-asn1 {
  margin: 0;
  padding: 12px 14px;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  font-size: 12px;
  line-height: 1.6;
  color: var(--text-secondary);
  overflow-x: auto;
  white-space: pre;
}

.doc-md {
  font-size: 13px;
  line-height: 1.8;
  color: var(--text-secondary);
}

.doc-md :deep(h2) {
  --doc-color: var(--accent);
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 34px 0 14px;
  padding: 11px 16px;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.5px;
  color: var(--doc-color);
  border-radius: 10px;
  border: 1px solid color-mix(in srgb, var(--doc-color) 35%, transparent);
  background: color-mix(in srgb, var(--doc-color) 9%, transparent);
}

.doc-md :deep(h2:nth-of-type(1)) { --doc-color: #7c8cf8; }
.doc-md :deep(h2:nth-of-type(2)) { --doc-color: #4caf7d; }
.doc-md :deep(h2:nth-of-type(3)) { --doc-color: #e5b955; }
.doc-md :deep(h2:nth-of-type(4)) { --doc-color: #e56a7f; }
.doc-md :deep(h2:nth-of-type(5)) { --doc-color: #5b8def; }
.doc-md :deep(h2:nth-of-type(6)) { --doc-color: #8a5ce0; }

.doc-md :deep(h2::before) {
  content: '❖';
  flex-shrink: 0;
  font-size: 12px;
  color: var(--doc-color);
  text-shadow: 0 0 8px var(--doc-color);
}

.doc-md :deep(h3) {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 20px 0 8px;
  font-size: 13.5px;
  font-weight: 700;
  color: var(--text-primary);
}

.doc-md :deep(h3::before) {
  content: '◆';
  flex-shrink: 0;
  font-size: 9px;
  color: var(--green);
  text-shadow: 0 0 6px var(--green);
}

.doc-md :deep(p) {
  margin: 8px 0;
}

.doc-md :deep(ul),
.doc-md :deep(ol) {
  margin: 8px 0;
  padding-left: 24px;
}

.doc-md :deep(li) {
  margin: 4px 0;
}

.doc-md :deep(ul li::marker) {
  color: var(--accent);
}

.doc-md :deep(ol li::marker) {
  color: var(--green);
  font-weight: 700;
}

.doc-md :deep(strong) {
  color: #ffc163;
  font-weight: 600;
}

.doc-md :deep(code) {
  font-family: var(--font-mono);
  font-size: 12px;
  background: var(--accent-muted);
  border: 1px solid color-mix(in srgb, var(--accent) 35%, transparent);
  border-radius: 5px;
  padding: 1px 6px;
  color: var(--accent-hover);
}

.doc-md :deep(pre) {
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 12px 14px;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.6;
}

.doc-md :deep(pre code) {
  background: none;
  border: none;
  padding: 0;
}

.doc-md :deep(table) {
  width: fit-content;
  max-width: 100%;
  margin: 14px auto;
  border-collapse: separate;
  border-spacing: 0;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.35);
}

.doc-md :deep(th) {
  padding: 9px 16px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-align: left;
  color: #fff;
  background: linear-gradient(135deg, #5b8def, #7a5ce0);
  border: none;
}

.doc-md :deep(td) {
  padding: 8px 16px;
  font-size: 12px;
  border: none;
  border-bottom: 1px solid var(--border);
  color: var(--text-secondary);
}

.doc-md :deep(tbody tr:nth-child(even)) {
  background: rgba(255, 255, 255, 0.025);
}

.doc-md :deep(tbody tr:hover) {
  background: var(--bg-hover);
}

.doc-md :deep(td:first-child) {
  color: var(--text-primary);
  font-weight: 600;
}

.doc-md :deep(tr:last-child td) {
  border-bottom: none;
}

.doc-md :deep(blockquote) {
  margin: 10px 0;
  padding: 8px 14px;
  border-radius: 0 8px 8px 0;
  background: var(--green-bg);
  border-left: 3px solid var(--green);
  color: var(--text-secondary);
}

.doc-md :deep(hr) {
  border: none;
  height: 2px;
  margin: 18px 0;
  border-radius: 2px;
  background: linear-gradient(90deg, transparent, var(--accent), transparent);
}

.doc-md :deep(a) {
  color: var(--accent-hover);
}

.debug-grid {
  flex: 1;
  min-height: 0;
  display: grid;
  grid-template-columns: 380px 6px 1fr;
  grid-auto-rows: minmax(0, 1fr);
  gap: 12px;
}

.drag-v {
  width: 6px;
  cursor: col-resize;
  background: transparent;
  position: relative;
  z-index: 5;
  transition: background 0.15s;
}
.drag-v:hover,
.drag-v:active {
  background: var(--accent);
}
</style>