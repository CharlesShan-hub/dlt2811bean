<template>
  <UiCard :title="isConnect ? '连接设置' : '参数'" icon="⚙" fill>
    <!-- connect 专属：共享连接表单 + 断开按钮 + 结果消息 -->
    <template v-if="isConnect">
      <ConnectForm
        :busy="busy"
        submit-label="连接"
        @submit="emit('run-cmd', $event)"
        @update:cmd="emit('update:connectCmd', $event)"
      >
        <template #extra>
          <UiButton v-if="connected || tcpConnected" variant="danger" @click="emit('release-ap')">断开 AP</UiButton>
        </template>
      </ConnectForm>
      <transition name="fade">
        <div v-if="connMsg" class="msg" :class="connMsgOk ? 'ok' : 'err'">{{ connMsg }}</div>
      </transition>
    </template>

    <!-- 通用参数渲染：由 params 定义驱动，FormField 按 type 分派 -->
    <template v-else>
      <div class="empty" v-if="!params.length">该命令无需参数，直接执行。</div>
      <template v-else>
        <FormRow v-for="row in paramRows" :key="row.inline || 'r' + row.items[0].key" :inline="row.inline">
          <FormField v-for="p in row.items" :key="p.key" :param="p" />
        </FormRow>
        <div class="actions">
          <UiButton :variant="formValid ? 'primary' : 'danger'" :loading="busy" @click="emit('run')">
            执行 {{ cmd }}
          </UiButton>
        </div>
      </template>
    </template>
  </UiCard>
</template>

<script setup>
import { computed, reactive, provide, watch } from 'vue'
import { FORM_CTX } from './formContext.js'
import FormRow from './FormRow.vue'
import FormField from './FormField.vue'
import ConnectForm from '../ConnectForm.vue'
import UiCard from '../ui/UiCard.vue'
import UiButton from '../ui/UiButton.vue'
import { useFormOptions } from '../../composables/form/useFormOptions.js'
import { useRefsRows } from '../../composables/form/useRefsRows.js'
import { useFormValidation } from '../../composables/form/useFormValidation.js'
import { useFormWatchers } from '../../composables/form/useFormWatchers.js'

const props = defineProps({
  /** 命令定义（CMD_DEFS 项） */
  def: { type: Object, default: () => ({}) },
  /** 响应式表单对象（父持有，随 cmd 切换重置） */
  form: { type: Object, required: true },
  cmd: String,
  busy: Boolean,
  isConnect: Boolean,
  connected: Boolean,
  tcpConnected: Boolean,
  connMsg: String,
  connMsgOk: Boolean,
})

const emit = defineEmits([
  'run',
  'run-cmd',
  'disconnect-tcp',
  'release-ap',
  'open-value-editor',
  'update:connectCmd',
])

const opts = useFormOptions(props.form, { getDef: () => props.def, getCmd: () => props.cmd })
const refs = useRefsRows(props.form, { getDef: () => props.def, getCmd: () => props.cmd })
const valid = useFormValidation(props.form, {
  getDef: () => props.def,
  getCmd: () => props.cmd,
  dsRefExists: opts.dsRefExists,
  dsRefInvalid: opts.dsRefInvalid,
})
const watchers = useFormWatchers(props.form, {
  getDef: () => props.def,
  getCmd: () => props.cmd,
  getLnRef: () => opts.lnRef.value,
  lnRequiredCmds: ['ln-dir', 'all-data', 'all-def', 'all-cb', 'get-dataset-values', 'get-dataset-dir'],
})

const p = props

/* ====== 注入上下文：字段组件据此取 options / handlers ====== */
const ctx = reactive({
  form: p.form,
  cmd: computed(() => p.cmd),
  def: computed(() => p.def),
  busy: computed(() => p.busy),
  isConnect: computed(() => p.isConnect),
  connected: computed(() => p.connected),
  tcpConnected: computed(() => p.tcpConnected),
  connMsg: computed(() => p.connMsg),
  connMsgOk: computed(() => p.connMsgOk),
  formValid: valid.formValid,
  openValueEditor: (row) => emit('open-value-editor', row),
  ...opts,
  ...refs,
})
provide(FORM_CTX, ctx)

const isConnect = computed(() => props.isConnect)
const params = computed(() => props.def.params || [])

/** 按 inline 分组：inline 名相同的参数并排一行。 */
const paramRows = computed(() => {
  const rows = []
  let cur = null
  for (const p0 of params.value) {
    if (p0.inline) {
      if (!cur || cur.inline !== p0.inline) {
        cur = { inline: p0.inline, items: [] }
        rows.push(cur)
      }
      cur.items.push(p0)
    } else {
      rows.push({ inline: null, items: [p0] })
    }
  }
  return rows
})

watch(
  () => props.cmd,
  async () => {
    watchers.initForm()
    await watchers.applyCmdDefaults()
  },
  { immediate: true }
)

watchers.setup()
</script>

<style scoped>
.actions {
  margin-top: 24px;
}

.empty {
  font-size: 13px;
  color: var(--text-muted);
  text-align: center;
  padding: 24px 0;
}

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
</style>