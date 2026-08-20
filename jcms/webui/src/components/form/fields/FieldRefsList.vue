<template>
  <div class="refs-list">
    <!-- 级联模式：每行由 RefRow 渲染（LD/LN/DO/SDO/DA/FC + 值/类型） -->
    <template v-if="info.cascade">
      <RefRow
        v-for="(r, i) in rows"
        :key="i"
        :row="r"
        :index="i"
        :param="info"
      />
      <button v-if="!info.single" type="button" class="glass glass-accent refs-add" @click="ctx.addRefs">＋ 添加引用</button>
    </template>

    <!-- 普通模式：每行一个字符串引用下拉 -->
    <template v-else>
      <div v-for="(r, i) in rows" :key="i" class="refs-row">
        <UiSelect
          :model-value="rows[i]"
          :options="['', ...ctx.refsListOptions]"
          :placeholder="info.placeholder"
          empty-label="（不选）"
          @update:model-value="rows[i] = $event"
        />
        <button v-if="!info.single" type="button" class="glass glass-danger refs-del" title="删除该引用" @click="ctx.removeRefs(i)"><X :size="14" /></button>
      </div>
      <button v-if="!info.single" type="button" class="glass glass-accent refs-add" @click="ctx.addRefs">＋ 添加引用</button>
    </template>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import X from '@lucide/vue/dist/esm/icons/x.mjs'
import UiSelect from '../../ui/UiSelect.vue'
import RefRow from './RefRow.vue'
import { useFormCtx } from '../formContext.js'

const props = defineProps({ param: { type: Object, required: true } })
const ctx = useFormCtx()

const info = computed(() => props.param)
/** form[key] 是引用数组（reactive），直接引用操控增删。 */
const rows = computed(() => ctx.form[props.param.key])
</script>

<style scoped>
.refs-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
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

.refs-del {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.refs-add {
  align-self: flex-start;
}
</style>