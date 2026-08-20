<template>
  <div class="refs-entry">
    <!-- 第一行：序号 + LD → LN（SGCB 固定 LLN0） → DO -->
    <div class="refs-row">
      <span class="refs-label">{{ index + 1 }}</span>
      <UiSelect
        :model-value="row.ld"
        :options="ctx.ldCache"
        placeholder="LD"
        empty-label="（不选）"
        @update:model-value="onLd"
      />
      <template v-if="isSgcbCmd">
        <span class="sgcb-suffix sgcb-ln">{{ row.ln || 'LLN0' }}</span>
        <span class="sgcb-suffix sgcb-name">{{ row._sgcbName || '…' }}</span>
        <button v-if="!info.single" type="button" class="glass glass-danger refs-del" title="删除该引用" @click="ctx.removeRefs(index)">
          <X :size="14" />
        </button>
      </template>
      <template v-else>
        <UiSelect
          :model-value="row.ln"
          :options="ctx.rowLnOptions(row)"
          placeholder="LN"
          empty-label="（不选）"
          @update:model-value="onLn"
        />
        <UiSelect
          :model-value="row.do"
          :options="ctx.rowDoOptions(row)"
          placeholder="DO"
          empty-label="（不选）"
          @update:model-value="onDo"
        />
      </template>
    </div>

    <!-- 非 SGCB 命令：第二行 SDO → DA → FC -->
    <div v-if="!isSgcbCmd" class="refs-row">
      <button v-if="!info.single" type="button" class="glass glass-danger refs-del" title="删除该引用" @click="ctx.removeRefs(index)">
        <X :size="14" />
      </button>
      <span v-else class="refs-label-spacer"></span>
      <UiSelect
        :model-value="row.sdo"
        :options="ctx.rowSdoOptions(row)"
        placeholder="SDO"
        empty-label="（不选）"
        @update:model-value="onSdo"
      />
      <UiSelect
        :model-value="row.da"
        :options="ctx.rowDaOptions(row)"
        placeholder="DA"
        empty-label="（不选）"
        @update:model-value="onDa"
      />
      <UiSelect
        v-if="showFc"
        :model-value="row.fc"
        :options="ctx.fcRowOptions"
        placeholder="FC"
        empty-label="（不选）"
        @update:model-value="row.fc = $event"
      />
    </div>

    <!-- 设值命令：第三行 值 + 类型 -->
    <div v-if="showValue" class="refs-row">
      <span class="refs-label-spacer"></span>
      <button
        type="button"
        class="glass glass-accent refs-value-btn"
        :class="{ 'refs-value-btn--has': row.value }"
        :title="'点击编辑值' + (row._resolvedType ? ' (类型: ' + row._resolvedType + ')' : '')"
        @click="ctx.openValueEditor(row)"
      >
        {{ row.value || '点击输入值' }}
      </button>
      <span class="type-hint" :class="{ 'type-hint--unknown': !row._resolvedType }" :title="row._resolvedType ? '类型已自动解析' : '请先选择数据引用'">
        {{ row._resolvedType || '（类型）' }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import X from '@lucide/vue/dist/esm/icons/x.mjs'
import UiSelect from '../../ui/UiSelect.vue'
import { useFormCtx } from '../formContext.js'

const props = defineProps({
  /** 级联引用行对象（reactive） */
  row: { type: Object, required: true },
  /** 行号（用于删除） */
  index: { type: Number, required: true },
  /** refs-list 参数定义 */
  param: { type: Object, required: true },
})
const ctx = useFormCtx()

const info = computed(() => props.param)
const cmd = computed(() => ctx.cmd)
const isSgcbCmd = computed(() => ctx.isSgcbCmd)
const showFc = computed(() => !['data-dir', 'set-edit-sg'].includes(cmd.value))
const showValue = computed(() => ['set-data-values', 'set-edit-sg'].includes(cmd.value))

function onLd(ld) {
  props.row.ld = ld
  ctx.onRowLd(props.row)
}
function onLn(ln) {
  props.row.ln = ln
  ctx.onRowLn(props.row)
}
function onDo(doVal) {
  props.row.do = doVal
  ctx.onRowDo(props.row)
}
function onSdo(sdo) {
  props.row.sdo = sdo
  ctx.onRowSdo(props.row)
}
function onDa(da) {
  props.row.da = da
  ctx.onRowDa(props.row)
}
</script>

<style scoped>
.refs-entry {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 8px;
  border: 1px solid var(--border);
  border-radius: 6px;
  background: var(--bg-secondary, rgba(255, 255, 255, 0.02));
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
  background: var(--bg-tertiary, rgba(255, 255, 255, 0.05));
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

.refs-del {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.sgcb-suffix {
  flex-shrink: 0;
  padding: 0 10px;
  height: 32px;
  line-height: 32px;
  font-size: 13px;
  font-weight: 600;
  background: var(--bg-tertiary, rgba(255, 255, 255, 0.05));
  border: 1px solid var(--border);
  border-radius: 6px;
  white-space: nowrap;
  font-family: var(--font-mono, 'Fira Code', 'Cascadia Code', monospace);
}

.sgcb-ln {
  color: var(--text-primary, #e0e0e0);
}

.sgcb-name {
  color: var(--green, #4caf7d);
}

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
</style>