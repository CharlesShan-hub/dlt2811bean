<template>
  <div class="cascade-pair">
    <UiSelect
      :model-value="obj.ld"
      :options="ctx.ldCache"
      :disabled="ctx.cascadeLdDisabled(info)"
      placeholder="LD"
      empty-label="（不选）"
      @update:model-value="onLd($event)"
    />
    <UiSelect
      :model-value="obj.ln"
      :options="['', ...ctx.cascadeLns(obj)]"
      :placeholder="info.placeholder"
      empty-label="（不选）"
      @update:model-value="obj.ln = $event"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import UiSelect from '../../ui/UiSelect.vue'
import { useFormCtx } from '../formContext.js'
const info = computed(() => props.param)
const props = defineProps({ param: { type: Object, required: true } })
const ctx = useFormCtx()

/** form[key] 始终是 { ld, ln } 对象。 */
const obj = computed(() => ctx.form[props.param.key])

function onLd(ld) {
  obj.value.ld = ld
  ctx.onCascadeLd(props.param.key)
}
</script>

<style scoped>
.cascade-pair {
  display: flex;
  gap: 8px;
}
</style>