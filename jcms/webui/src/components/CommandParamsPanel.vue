<template>
  <UiCard :title="isConnect ? '连接设置' : '参数'" icon="⚙" fill>
    <!-- connect 专属：使用共享连接表单 + 断开按钮 + 结果消息 -->
    <ConnectForm v-if="isConnect" :busy="busy" submit-label="连接" @submit="emit('run-cmd', $event)" @update:cmd="emit('update:connectCmd', $event)">
      <template #extra>
        <UiButton v-if="tcpConnected || connected" variant="danger" @click="emit('disconnect-tcp')">断开 TCP</UiButton>
        <UiButton v-if="connected" variant="danger" @click="emit('release-ap')">断开 AP</UiButton>
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
            :class="{ 'switch-field': p.type === 'switch' || p.type === 'auto-pull-switch' }"
          >
            <label class="field-label">{{ p.label }}</label>
            <UiInput
              v-if="p.type === 'text'"
              v-model="form[p.key]"
              :placeholder="p.placeholder"
              :readonly="p.readonly"
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
            <!-- 数据集选择：依赖 ln-cascade 选中的 LN，动态加载数据集名称 -->
            <UiSelect
              v-else-if="p.type === 'dataset-select'"
              v-model="form[p.key]"
              :options="datasetOptions"
              placeholder="请先选择逻辑节点"
              empty-label="（不选）"
            />
            <!-- 定值组控制块选择：依赖 ln-cascade 选中的 LN，选择 SGCB 名 -->
            <UiSelect
              v-else-if="p.type === 'sgcb-select'"
              v-model="form[p.key]"
              :options="sgcbOptions"
              placeholder="请先选择逻辑节点"
              empty-label="（不选）"
            />
            <!-- 数据集引用三选：LD → LN → 数据集名称（create-dataset 可输入；selectOnly 时仅下拉选择） -->
            <div v-else-if="p.type === 'ds-ref-input'" class="ds-ref-row">
              <UiSelect
                v-model="form[p.key].ld"
                :options="ldCache"
                placeholder="LD"
                empty-label="（不选）"
                @update:modelValue="onDsRefLd(p.key)"
              />
              <UiSelect
                v-model="form[p.key].ln"
                :options="dsRefLns(form[p.key])"
                placeholder="LN"
                empty-label="（不选）"
                @update:modelValue="onDsRefLn(p.key)"
              />
              <UiSelect
                v-if="p.selectOnly"
                v-model="form[p.key].name"
                :options="dsRefNameList(form[p.key])"
                placeholder="数据集名称"
                empty-label="（不选）"
              />
              <div v-else class="ds-ref-name-wrapper">
                <UiSelect
                  v-model="form[p.key].name"
                  :options="dsRefNameList(form[p.key])"
                  placeholder="数据集名称，如 dsMySet"
                  empty-label="（不选）"
                  editable
                  :error="dsRefInvalid(form[p.key])"
                />
                <span v-if="!form.after && dsRefExists(form[p.key])" class="ds-ref-exists-tag">已存在</span>
                <span v-else-if="form.after && !dsRefExists(form[p.key])" class="ds-ref-invalid-tag">不存在</span>
              </div>
            </div>
            <!-- 数据集成员引用 after 下拉（get-dataset-dir / get-dataset-values 用） -->
            <UiSelect
              v-else-if="p.type === 'ds-member-after'"
              v-model="form[p.key]"
              :options="['', ...dsMemberAfterOptions]"
              :placeholder="p.placeholder"
              empty-label="（不选）"
            />
            <!-- 数据集成员值列表编辑（set-dataset-values 用） -->
            <ValuesListEditor
              v-else-if="p.type === 'values-list'"
              v-model="form[p.key]"
              :members="filteredDsMemberOptions"
            />
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
                    <button v-if="!p.single" type="button" class="glass glass-danger refs-del" title="删除该引用" @click="removeRefs(i)"><X :size="14" /></button>
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
                      v-if="cmd !== 'data-dir'"
                      v-model="r.fc"
                      :options="fcRowOptions"
                      :disabled="!!r.da"
                      placeholder="FC"
                      empty-label="（不选）"
                    />
                  </div>
                  <!-- 第三行：值 + 类型（仅 set-data-values） -->
                  <div v-if="cmd === 'set-data-values'" class="refs-row">
                    <span class="refs-label-spacer"></span>
                    <button
                      type="button"
                      class="glass glass-accent refs-value-btn"
                      :class="{ 'refs-value-btn--has': r.value }"
                      :title="'点击编辑值' + (r._resolvedType ? ' (类型: ' + r._resolvedType + ')' : '')"
                      @click="emit('open-value-editor', r)"
                    >
                      {{ r.value || '点击输入值' }}
                    </button>
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
                  <button v-if="!p.single" type="button" class="glass glass-danger refs-del" title="删除该引用" @click="removeRefs(i)"><X :size="14" /></button>
                </div>
              </template>
              <button v-if="!p.single" type="button" class="glass glass-accent refs-add" @click="addRefs">＋ 添加引用</button>
            </div>
            <UiSwitch v-else-if="p.type === 'switch'" v-model="form[p.key]" />
            <UiSwitch v-else-if="p.type === 'auto-pull-switch'" v-model="form[p.key]" />
          </div>
        </div>
      </template>
    </template>
    <p v-if="!isConnect && simpleParams.length === 0" class="empty">该命令无需参数，直接执行。</p>

    <div v-if="!isConnect" class="actions">
      <UiButton :variant="formValid ? 'primary' : 'danger'" :loading="busy" @click="emit('run')">执行 {{ cmd }}</UiButton>
    </div>
  </UiCard>
</template>

<script setup>
import { computed } from 'vue'
import X from '@lucide/vue/dist/esm/icons/x.mjs'
import ConnectForm from './ConnectForm.vue'
import ApPicker from './ApPicker.vue'
import UiCard from './ui/UiCard.vue'
import UiButton from './ui/UiButton.vue'
import UiInput from './ui/UiInput.vue'
import UiSelect from './ui/UiSelect.vue'
import UiSwitch from './ui/UiSwitch.vue'
import ValuesListEditor from './ValuesListEditor.vue'

const props = defineProps({
  def: Object,
  form: Object,
  busy: Boolean,
  isConnect: Boolean,
  cmd: String,
  connected: Boolean,
  tcpConnected: Boolean,
  ldCache: Array,
  ldLns: Object,
  refOptions: Array,
  fcRowOptions: Array,
  refsListOptions: Array,
  datasetOptions: Array,
  sgcbOptions: Array,
  dsMemberAfterOptions: Array,
  dsMemberOptions: Array,
  lnRef: String,
  connMsg: String,
  connMsgOk: Boolean,
  simpleParams: Array,
  paramRows: Array,
  formValid: Boolean,
  datasetRefs: Object,
  // 以下函数由父级通过 useCommandForm 提供
  cascadeLns: Function,
  onCascadeLd: Function,
  cascadeLdDisabled: Function,
  addRefs: Function,
  removeRefs: Function,
  rowDoOptions: Function,
  onRowLd: Function,
  onRowLn: Function,
  onRowDo: Function,
  onRowSdo: Function,
  rowSdoOptions: Function,
  rowDaOptions: Function,
  onRowDa: Function,
  // ds-ref-input 专用
  onDsRefLd: Function,
  onDsRefLn: Function,
  dsRefLns: Function,
  dsRefExists: Function,
  dsRefNameList: Function,
  dsRefInvalid: Function,
})

const emit = defineEmits([
  'run',
  'run-cmd',
  'disconnect-tcp',
  'release-ap',
  'open-value-editor',
  'add-refs',
  'remove-refs',
])

const connectCmd = defineModel('connectCmd', { default: '' })

/** 根据 after 选择过滤成员列表：只显示从 after 位置开始的成员 */
const filteredDsMemberOptions = computed(() => {
  const members = props.dsMemberOptions
  if (!Array.isArray(members) || members.length === 0) return []
  const after = props.form?.after
  if (!after) return members
  const idx = members.findIndex(m => m.reference === after)
  return idx >= 0 ? members.slice(idx) : members
})
</script>

<style scoped>
.field {
  margin-bottom: 20px;
}

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

.refs-value-btn {
  flex: 1;
  min-width: 0;
}

.refs-add {
  align-self: flex-start;
}

.refs-del {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* ── 级联 ── */
.cascade-pair {
  display: flex;
  gap: 8px;
}

.cascade-pair .ui-select {
  flex: 1;
  min-width: 0;
}

/* ── 数据集引用三选（ds-ref-input） ── */
.ds-ref-row {
  display: flex;
  gap: 8px;
  align-items: flex-start;
}

.ds-ref-row .ui-select {
  flex: 1;
  min-width: 0;
}

.ds-ref-name-wrapper {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.ds-ref-exists-tag {
  font-size: 11px;
  color: var(--red);
  font-weight: 600;
}
.ds-ref-invalid-tag {
  font-size: 11px;
  color: var(--red);
  font-weight: 600;
}

/* ── 类型提示 ── */
.type-hint {
  flex-shrink: 0;
  font-size: 11px;
  color: var(--text-muted);
  padding: 2px 6px;
  background: var(--bg-tertiary);
  border-radius: 4px;
  white-space: nowrap;
}

.type-hint--unknown {
  color: var(--red);
  font-style: italic;
}

/* ── 操作按钮 ── */
.actions {
  margin-top: 24px;
}

.empty {
  font-size: 13px;
  color: var(--text-muted);
  text-align: center;
  padding: 24px 0;
}

/* ── 连接消息 ── */
.msg {
  margin-top: 12px;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  line-height: 1.4;
}

.msg.ok {
  background: rgba(76, 175, 125, 0.1);
  border: 1px solid rgba(76, 175, 125, 0.3);
  color: var(--green);
}

.msg.err {
  background: rgba(229, 85, 90, 0.1);
  border: 1px solid rgba(229, 85, 90, 0.3);
  color: var(--red);
}

/* ── 提示 ── */
.tip {
  font-size: 13px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 12px 0;
}

.svc-note {
  font-size: 13px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 12px 0;
}
</style>