<template>
  <div class="ds-ref-row">
    <UiSelect
      :model-value="obj.ld"
      :options="ctx.ldCache"
      placeholder="LD"
      empty-label="（不选）"
      @update:model-value="onLd"
    />
    <UiSelect
      :model-value="obj.ln"
      :options="ctx.dsRefLns(obj)"
      placeholder="LN"
      empty-label="（不选）"
      @update:model-value="onLn"
    />
    <UiSelect
      v-if="info.selectOnly"
      :model-value="obj.name"
      :options="ctx.dsRefNameList(obj)"
      placeholder="数据集名称"
      empty-label="（不选）"
      @update:model-value="obj.name = $event"
    />
    <div v-else class="ds-ref-name-wrapper">
      <UiSelect
        :model-value="obj.name"
        :options="ctx.dsRefNameList(obj)"
        placeholder="数据集名称"
        empty-label="（不选）"
        editable
        :error="ctx.dsRefInvalid(obj)"
        @update:model-value="obj.name = $event"
      />
      <span v-if="ctx.dsRefExists(obj)" class="ds-ref-tag">已存在</span>
      <span v-else-if="obj.name" class="ds-ref-tag ds-ref-new">新数据集</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import UiSelect from '../../ui/UiSelect.vue'
import { useFormCtx } from '../formContext.js'

const props = defineProps({ param: { type: Object, required: true } })
const ctx = useFormCtx()
const info = computed(() => props.param)
const obj = computed(() => ctx.form[props.param.key])

function onLd(ld) {
  obj.value.ld = ld
  ctx.onDsRefLd(props.param.key)
}
function onLn(ln) {
  obj.value.ln = ln
  ctx.onDsRefLn(props.param.key)
}
</script>

<style scoped>
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

.ds-ref-tag {
  font-size: 11px;
  color: var(--red);
  font-weight: 600;
}

.ds-ref-new {
  color: var(--green);
}
</style>