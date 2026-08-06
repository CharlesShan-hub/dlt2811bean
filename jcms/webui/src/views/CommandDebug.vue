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
            <span class="desc-text">断开 TCP 连接</span>
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
      <UiCard :title="isConnect ? '连接设置' : '参数'" icon="⚙" fill>
        <!-- connect 专属：使用共享连接表单 + 断开按钮 + 结果消息 -->
        <ConnectForm v-if="isConnect" :busy="busy" submit-label="连接" @submit="runCmd" @update:cmd="connectCmd = $event">
          <template #extra>
            <UiButton v-if="connected" @click="disconnect">断开</UiButton>
          </template>
        </ConnectForm>
        <transition name="fade">
          <div v-if="isConnect && connMsg" class="msg" :class="connMsgOk ? 'ok' : 'err'">{{ connMsg }}</div>
        </transition>

        <!-- 通用参数渲染（connect 由上方专属模板渲染，跳过） -->
        <template v-if="!isConnect && simpleParams.length">
          <template v-for="p in simpleParams" :key="p.key">
            <ApPicker v-if="p.type === 'ap-select'" v-model="form[p.key]" />
          </template>

          <!-- 其余参数按行渲染：inline 同组的参数并排一行（如 APDU / ASDU） -->
          <template v-for="row in paramRows" :key="row.inline || row.items[0].key">
            <div class="field-row" :class="{ single: !row.inline }">
              <div
                v-for="p in row.items"
                :key="p.key"
                class="field"
                :class="{ 'switch-field': p.type === 'switch' }"
              >
                <label class="field-label">{{ p.label }}</label>
                <UiInput
                  v-if="p.type === 'text'"
                  v-model="form[p.key]"
                  :placeholder="p.placeholder"
                />
                <UiInput
                  v-else-if="p.type === 'number'"
                  v-model.number="form[p.key]"
                  type="number"
                  :readonly="p.readonly"
                />
                <UiSelect
                  v-else-if="p.type === 'select'"
                  v-model="form[p.key]"
                  :options="p.options"
                  :disabled="p.disabled"
                  empty-label="（不选）"
                />
                <UiSelect
                  v-else-if="p.type === 'ld-select'"
                  v-model="form[p.key]"
                  :options="p.required ? ldCache : ['', ...ldCache]"
                  :placeholder="p.placeholder"
                  empty-label="（不选）"
                />
                <!-- 级联二选：LD → LN，生成 --ln/--after "LD/LN"（ld-dir 的 after 与上面 ld 联动） -->
                <div v-else-if="p.type === 'ln-cascade'" class="cascade-pair">
                  <UiSelect
                    v-model="form[p.key].ld"
                    :options="ldCache"
                    :disabled="cascadeLdDisabled(p)"
                    placeholder="LD"
                    empty-label="（不选）"
                    @update:modelValue="onCascadeLd(p.key)"
                  />
                  <UiSelect
                    v-model="form[p.key].ln"
                    :options="['', ...cascadeLns(form[p.key])]"
                    placeholder="LN"
                    empty-label="（不选）"
                  />
                </div>
                <UiSelect
                  v-else-if="p.type === 'ln-ref-select'"
                  v-model="form[p.key]"
                  :options="['', ...refOptions]"
                  :placeholder="p.placeholder"
                  empty-label="（不选）"
                />
                <!-- 动态引用列表：加号增行、叉号删行，命令拼接为 --refs "r1 r2 ..." -->
                <div v-else-if="p.type === 'refs-list'" class="refs-list">
                  <!-- 级联三选：LD → LN → DO，逐层下钻引用 -->
                  <template v-if="p.cascade">
                    <div v-for="(r, i) in form[p.key]" :key="i" class="refs-entry">
                      <div class="refs-row">
                        <span class="refs-label">{{ i + 1 }}</span>
                        <UiSelect
                          v-model="r.ld"
                          :options="ldCache"
                          placeholder="LD"
                          empty-label="（不选）"
                          @update:modelValue="onRowLd(r)"
                        />
                        <UiSelect
                          v-model="r.ln"
                          :options="r.ld ? ldLns[r.ld] || [] : []"
                          placeholder="LN"
                          empty-label="（不选）"
                          @update:modelValue="onRowLn(r)"
                        />
                        <UiSelect
                          v-model="r.do"
                          :options="rowDoOptions(r)"
                          placeholder="DO"
                          empty-label="（不选）"
                          @update:modelValue="onRowDo(r)"
                        />
                      </div>
                      <div class="refs-row">
                        <button type="button" class="glass glass-danger refs-del" title="删除该引用" @click="removeRefs(i)">✕</button>
                        <UiSelect
                          v-model="r.sdo"
                          :options="rowSdoOptions(r)"
                          placeholder="SDO"
                          empty-label="（不选）"
                          @update:modelValue="onRowSdo(r)"
                        />
                        <UiSelect
                          v-model="r.da"
                          :options="rowDaOptions(r)"
                          placeholder="DA"
                          empty-label="（不选）"
                          @update:modelValue="onRowDa(r)"
                        />
                        <UiSelect
                          v-model="r.fc"
                          :options="fcRowOptions"
                          :disabled="!!r.da"
                          placeholder="FC"
                          empty-label="（不选）"
                        />
                      </div>
                      <!-- 第三行：值 + 类型（仅 set-data-values） -->
                      <div v-if="props.cmd === 'set-data-values'" class="refs-row">
                        <span class="refs-label-spacer"></span>
                        <UiInput
                          v-model="r.value"
                          placeholder="值 value"
                          class="refs-value-input"
                        />
                        <span class="type-hint" :class="{ 'type-hint--unknown': !r._resolvedType }" :title="r._resolvedType ? '类型已自动解析' : '请先选择数据引用'">
                          {{ r._resolvedType || '（类型）' }}
                        </span>
                      </div>
                    </div>
                  </template>
                  <template v-else>
                    <div v-for="(r, i) in form[p.key]" :key="i" class="refs-row">
                      <UiSelect
                        v-model="form[p.key][i]"
                        :options="['', ...refsListOptions]"
                        :placeholder="p.placeholder"
                        empty-label="（不选）"
                      />
                      <button type="button" class="glass glass-danger refs-del" title="删除该引用" @click="removeRefs(i)">✕</button>
                    </div>
                  </template>
                  <button type="button" class="glass glass-accent refs-add" @click="addRefs">＋ 添加引用</button>
                </div>
                <UiSwitch v-else-if="p.type === 'switch'" v-model="form[p.key]" />
              </div>
            </div>
          </template>
        </template>
        <p v-if="!isConnect && simpleParams.length === 0" class="empty">该命令无需参数，直接执行。</p>

        <!-- JSON 格式化开关：仅在 --json 模式开启时可用 -->
        <div v-if="!isConnect && jsonMode" class="json-format-opt">
          <span class="json-opt-label prettify-label">JSON 格式化</span>
          <UiSwitch v-model="jsonFormat" />
        </div>

        <div v-if="!isConnect" class="actions">
          <UiButton variant="primary" :loading="busy" @click="run">执行 {{ cmd }}</UiButton>
        </div>
      </UiCard>

      <!-- 垂直拖拽手柄 -->
      <div class="drag-v" @mousedown.prevent="startVDrag"></div>

      <!-- ── 右栏：流程 + 命令与返回（含实时预览） ── -->
      <div class="col-right">
        <div v-if="showAsn1" class="split-top" :style="{ height: topHeight + 'px' }">
          <UiCard :title="rightTitle" icon="⛓" fill>
            <template #header>
              <span class="glass ui-card__toggle" title="隐藏 {{ rightTitle }}" @click="showAsn1 = false">𝔄</span>
            </template>
            <!-- connect 是便捷封装命令：显示状态图而非 ASN.1 -->
            <StateDiagram v-if="isConnect" :states="connectFlow.states" :edges="connectFlow.edges" :active="activeState" />
            <p v-if="isConnect" class="tip">💡 <code>connect --ap</code> 自动完成上述三步；关联建立后即可使用各服务页面。</p>
            <template v-else>
              <Asn1Code v-if="def.asn1" :code="def.asn1" />
              <p v-else-if="def.desc" class="svc-note">{{ def.desc }}</p>
              <p v-if="def.note" class="svc-note">{{ def.note }}</p>
            </template>
          </UiCard>
        </div>

        <!-- 水平拖拽手柄：仅在 ASN.1 可见时显示 -->
        <div v-if="showAsn1" class="drag-h" @mousedown.prevent="startHDrag"></div>

        <CmdResultCard
          :result="result"
          :json-mode="jsonMode"
          :json-format="jsonFormat"
          :highlighted-cmd="highlightedCmd"
          :highlighted-result-cmd="highlightedResultCmd"
          :formatted-json="formattedJson"
          :output-lines="outputLines"
          :preview-cmd="previewCmd"
          :result-cmd="result?.cmd ?? ''"
          @update:json-mode="jsonMode = $event"
          @update:json-format="jsonFormat = $event"
          @edit="onCmdEdit"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { debugShared } from '../stores/debugShared.js'
import ConnectForm from '../components/ConnectForm.vue'
import ApPicker from '../components/ApPicker.vue'
import StateDiagram from '../components/StateDiagram.vue'
import Asn1Code from '../components/Asn1Code.vue'
import CmdResultCard from '../components/CmdResultCard.vue'
import UiCard from '../components/ui/UiCard.vue'
import UiButton from '../components/ui/UiButton.vue'
import UiInput from '../components/ui/UiInput.vue'
import UiSelect from '../components/ui/UiSelect.vue'
import UiSwitch from '../components/ui/UiSwitch.vue'
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
  /** 连接管理页：当前是否已关联 / 已 TCP 连接（用于状态图高亮） */
  connected: { type: Boolean, default: false },
  tcpConnected: { type: Boolean, default: false },
})

const connectFlow = CONNECT_FLOW

const def = computed(() => CMD_DEFS[props.cmd] || { title: props.cmd, desc: '', params: [] })

/** connect 状态图高亮：关联 > TCP 连接 > 未连接。 */
const activeState = computed(() => {
  if (props.connected) return 'assoc'
  if (props.tcpConnected) return 'tcp'
  return 'init'
})

/** 页头拆解："关联 associate (8.2.1)" → 大标题"关联" + 章节徽章"8.2.1"。 */
const titleParts = computed(() => {
  const m = (def.value.title || '').match(/^(.*?)\s+\S+\s*\(([\d.]+)\)$/)
  return m ? { name: m[1], section: m[2] } : { name: def.value.title, section: '' }
})
const shortTitle = computed(() => titleParts.value.name)
const section = computed(() => titleParts.value.section)
const isConnect = computed(() => props.cmd === 'connect')
const simpleParams = computed(() => def.value.params)

/** 参数按行分组：inline 值相同的参数并排一行（如 APDU / ASDU）。 */
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

/** ln-cascade 参数（key=ln）当前拼出的 LD/LN 引用（未选时为空），驱动 after 下拉懒加载。 */
const lnRef = computed(() => {
  const p = def.value.params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
  const o = p ? form[p.key] : null
  return o && o.ld ? (o.ln ? `${o.ld}/${o.ln}` : o.ld) : ''
})

/** ln-cascade 的 LN 选项：选中 LD 后取该 LD 的 LN 列表。 */
function cascadeLns(o) {
  return o && o.ld ? ldLns[o.ld] || [] : []
}

/** ln-cascade 的 LD 变化：清空 LN 并懒加载该 LD 的 LN 列表。 */
function onCascadeLd(key) {
  const o = form[key]
  if (!o) return
  o.ln = ''
  if (o.ld) ensureLdLns(o.ld)
}

/** ld-dir 的 after 级联：上面已选 ld 时，after 的 LD 跟随上面并禁用（只选 LN，单设备模式不带前缀）。 */
function cascadeLdDisabled(p) {
  return props.cmd === 'ld-dir' && p.key === 'after' && !!form.ld
}

/** after 下拉选项：ln-dir 用该 LN 的子引用；all-data/all-def 用 all-def 的 DO 引用；all-cb 用 CB 引用。 */
const refOptions = computed(() => {
  if (props.cmd === 'ln-dir') return lnDirRefs
  if (props.cmd === 'all-cb') return allCbRefs
  return allDefRefs
})

/** 动态引用列表（refs-list）选项：全量 LD/LN 完整引用。 */
const refsListOptions = computed(() => allLnRefs)

/** 每行引用的 FC 选项 */
const fcRowOptions = computed(() => FC_OPTIONS)

/** 右侧卡片标题：connect 是流程状态图；有 ASN.1 报文则标 ASN.1，否则为服务说明。 */
const rightTitle = computed(() => {
  if (isConnect.value) return '连接流程'
  return def.value.asn1 ? 'ASN.1' : '服务说明'
})

const form = reactive({})
const busy = ref(false)
const result = ref(null)
const connectCmd = ref('')
const docOpen = ref(false)

/** 可拖拽分割面板（跨标签页共享） */
const {
  gridRef,
  leftColWidth,
  topHeight,
  showAsn1,
  startVDrag,
  startHDrag,
} = useSplitPane()

/** 命令表单逻辑 */
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

/** 连接管理页：执行 connect/disconnect 的结果消息。 */
const connMsg = ref('')
const connMsgOk = ref(true)

/** 全局 JSON 输出开关：命令自动追加 --json（localStorage 持久化，各子页面共享）。 */
const jsonMode = ref(localStorage.getItem('cms-json-mode') === '1')
watch(jsonMode, (v) => localStorage.setItem('cms-json-mode', v ? '1' : '0'))

/** JSON 格式化显示开关：将输出中的 JSON 部分漂亮打印并高亮。 */
const jsonFormat = ref(localStorage.getItem('cms-json-format') === '1')
watch(jsonFormat, (v) => localStorage.setItem('cms-json-format', v ? '1' : '0'))

/** 将结果输出中的 JSON 提取并格式化为语法高亮的 HTML。 */
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

/** 将结果输出按行拆分，用于终端风格渲染。 */
const outputLines = computed(() => {
  if (!result.value) return []
  return result.value.output.split('\n')
})

/** 协议说明 doc 的 markdown 渲染结果。 */
const docHtml = computed(() => marked.parse(def.value.doc || ''))

/** 实时生成的命令：connect 由 ConnectForm 输出，其余由表单状态实时拼接 */
const previewCmd = computed(() => {
  if (isConnect.value) {
    return connectCmd.value
  }
  return buildCmd(props.cmd, def.value.params, form, jsonMode.value, { cmdProp: props.cmd })
})

const highlightedCmd = computed(() => highlightCmdStr(previewCmd.value))

/** 已执行命令的高亮，基于 result.cmd（不会随当前参数变化） */
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
  await runCmd(buildCmd(props.cmd, def.value.params, form, jsonMode.value, { cmdProp: props.cmd }))
}

/** @type {function} 双击命令预览编辑后，尝试同步回左侧表单 */
function onCmdEdit(cmdStr) {
  const cmdName = (cmdStr.match(/^\S+/) || [''])[0]
  const curCmd = props.cmd
  // 命令名不一致：不生效
  if (cmdName !== curCmd) {
    pushTerminal(['⚠ 双击编辑未同步：命令名不匹配，当前页面为 ' + curCmd])
    return
  }
  const parsed = parseCmd(cmdStr, def.value.params, { cmdName: curCmd })
  // 参数有误：不生效，错误信息在终端提示
  if (!parsed.valid) {
    for (const err of parsed.errors) {
      pushTerminal(['⚠ 双击编辑未同步：' + err])
    }
    return
  }
  if (parsed.jsonMode !== undefined) {
    jsonMode.value = parsed.jsonMode
  }
  // 逐字段更新表单
  for (const key of Object.keys(parsed.form)) {
    if (key in form) {
      const val = parsed.form[key]
      // ln-cascade 类型：若只有 ln 没有 ld，从 form.ld 补上
      if (val && typeof val === 'object' && val.ld === '' && val.ln && form.ld) {
        form[key] = { ld: form.ld, ln: val.ln }
      } else {
        form[key] = val
      }
      // 同步后加载级联所需数据
      if (typeof val === 'string' && key === 'ld' && val) {
        ensureLdLns(val)
      } else if (val && typeof val === 'object' && val.ld) {
        ensureLdLns(val.ld)
      }
    }
  }
}

/** 执行命令并写入结果区（只保留最新一条），同时回显到共享终端。 */
async function runCmd(cmdLine) {
  busy.value = true
  try {
    // 连接管理页：按 --json 开关决定是否补 --json（开关开才解析结构化消息）
    const final = isConnect.value && jsonMode.value && !cmdLine.includes('--json') ? `${cmdLine} --json` : cmdLine
    const text = await executeCommand(final)
    result.value = {
      cmd: final,
      output: text.trim(),
      time: new Date().toLocaleTimeString(),
    }
    // 终端里保留 ANSI 颜色（由 Terminal.vue 的 parseAnsi 渲染）
    pushTerminal([`$ ${final}`, text.trim()])
    if (isConnect.value) {
      const res = parseResult(text)
      if (res) {
        connMsg.value = res.success ? (res.message || '操作成功') : (res.error || '操作失败')
        connMsgOk.value = !!res.success
      } else {
        connMsg.value = '命令已执行（未启用 --json）'
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

/** 连接管理页：断开 TCP 连接。 */
async function disconnect() {
  await runCmd('disconnect')
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

/* ── 页头标题行：左（章节徽章 + 大标题）右（命令名 + 简介）两端对齐 ── */
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

/* ── 页头副标题：章节徽章 + 命令名 + 简介 ── */
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

/* 可点击的章节徽章（打开协议说明） */
.doc-badge {
  cursor: pointer;
  transition: background 0.15s, transform 0.12s;
}
.doc-badge:hover {
  background: var(--accent);
  color: #fff;
  transform: scale(1.05);
}

/* ASN.1 切换按钮（标题栏右侧） */
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

/* ── 协议说明：markdown 渲染（v-html 内容需 :deep 才生效） ── */
.doc-md {
  font-size: 13px;
  line-height: 1.8;
  color: var(--text-secondary);
}

/* 章节花纹：每个二级标题一种主题色，顶部有彩色渐变花纹线 + 花纹字符 */
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

/* 花纹字符（章节前缀） */
.doc-md :deep(h2::before) {
  content: '❖';
  flex-shrink: 0;
  font-size: 12px;
  color: var(--doc-color);
  text-shadow: 0 0 8px var(--doc-color);
}

/* 小节标题：绿色菱形点缀 */
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

/* 列表：彩色项目符号 / 编号 */
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

/* 加粗：暖金高亮 */
.doc-md :deep(strong) {
  color: #ffc163;
  font-weight: 600;
}

/* 行内代码 */
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

/* 表格：居中 + 圆角 + 渐变彩色表头 + 斑马纹 */
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

/* 引用块：绿色侧边 */
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

.col-right {
  min-height: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-right: 6px;
}

/* 垂直拖拽手柄 */
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

/* 右列上半部分（ASN.1） */
.split-top {
  flex-shrink: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* 水平拖拽手柄 */
.drag-h {
  height: 6px;
  cursor: row-resize;
  background: transparent;
  flex-shrink: 0;
  position: relative;
  z-index: 5;
  transition: background 0.15s;
}
.drag-h:hover,
.drag-h:active {
  background: var(--accent);
}

/* ASN.1 卡片头部：隐藏按钮 */
.ui-card__toggle {
  margin-left: auto;
  cursor: pointer;
  font-size: 14px;
  color: var(--text-secondary);
  transition: all 0.2s;
  user-select: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  border-radius: 6px;
}
.ui-card__toggle:hover {
  color: var(--text-primary);
}

.field {
  margin-bottom: 20px;
}

/* 参数按行分组：同组并排一行（如 APDU / ASDU） */
.field-row {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.field-row .field {
  flex: 1;
  min-width: 0;
  margin-bottom: 0;
}

.field-label {
  display: block;
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.field-label::before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--red);
  margin-right: 7px;
  vertical-align: middle;
  box-shadow: 0 0 4px rgba(229, 85, 90, 0.6);
}

.switch-field {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.switch-field .field-label {
  margin-bottom: 0;
}

/* ── 动态引用列表（refs-list） ── */
.refs-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.refs-entry {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 8px;
  border: 1px solid var(--border);
  border-radius: 6px;
  background: var(--bg-secondary, rgba(255,255,255,0.02));
}

.refs-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.refs-row .ui-select {
  flex: 1;
  min-width: 0;
}

.refs-label {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 600;
  color: var(--text-muted);
  background: var(--bg-tertiary, rgba(255,255,255,0.05));
  border-radius: 4px;
  border: 1px solid var(--border);
}

.refs-label-spacer {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
}

.refs-value-input {
  flex: 1;
  min-width: 0;
}

.type-hint {
  display: inline-flex;
  align-items: center;
  padding: 0 10px;
  height: 32px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
  color: var(--text-secondary, #8b949e);
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.1);
  white-space: nowrap;
  user-select: none;
  cursor: default;
  transition: all 0.2s;
  letter-spacing: 0.5px;
}
.type-hint--unknown {
  color: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.05);
  background: transparent;
  font-weight: 400;
}

/* ── 级联二选（ln-cascade：LD → LN） ── */
.cascade-pair {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cascade-pair .ui-select {
  flex: 1;
  min-width: 0;
}

.refs-del {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  border-radius: 4px;
  color: var(--text-muted);
  font-size: 11px;
  cursor: pointer;
}

.refs-add {
  align-self: flex-start;
  border-style: dashed;
  border-radius: 8px;
  padding: 6px 12px;
  font-size: 12px;
  cursor: pointer;
  color: var(--text-secondary);
}

.divider {
  height: 1px;
  background: var(--border);
  margin: 4px 0 20px;
}

.actions {
  margin-top: 20px;
}

/* ── JSON 格式化选项（左栏） ── */
.json-format-opt {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16px;
  padding: 10px 14px;
  background: rgba(229, 185, 85, 0.08);
  border: 1px solid rgba(229, 185, 85, 0.2);
  border-radius: 8px;
}

.json-format-opt .json-opt-label {
  font-size: 13px;
  font-weight: 500;
}

.empty {
  color: var(--text-muted);
  font-size: 13px;
}

/* ── 连接管理页：结果消息 ── */
.msg {
  margin-top: 16px;
  padding: 10px 14px;
  border-radius: 8px;
  font-size: 13px;
}

.msg.ok {
  color: var(--green);
  background: var(--green-bg);
  border: 1px solid rgba(76, 175, 125, 0.3);
}

.msg.err {
  color: var(--red);
  background: var(--red-bg);
  border: 1px solid rgba(229, 85, 90, 0.3);
}

/* ── 连接流程提示 ── */
.tip {
  margin: 12px 0 0;
  font-size: 12px;
  color: var(--text-muted);
}

.tip code {
  color: var(--accent);
}

/* ── JSON 格式化开关 ── */
.json-opt-sep {
  width: 1px;
  height: 16px;
  background: var(--border);
  margin: 0 6px;
}

.prettify-label {
  color: var(--yellow) !important;
}
</style>
