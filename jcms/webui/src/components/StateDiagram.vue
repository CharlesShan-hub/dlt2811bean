<template>
  <div class="state-diagram">
    <svg :viewBox="`0 0 ${width} ${height}`" role="img" aria-label="状态图">
      <!-- 返回边（虚线）：先画避免盖住节点 -->
      <g v-for="(e, i) in backEdges" :key="'b' + i">
        <path :d="backPath(e)" class="edge back" />
        <polygon :points="backArrow(e)" class="edge back" />
        <text :x="backLabelX(e)" :y="backLabelY(e)" text-anchor="middle" class="edge-label">{{ e.label }}</text>
      </g>

      <!-- 正向边 -->
      <g v-for="(e, i) in forwardEdges" :key="'f' + i">
        <line :x1="fwdX" :y1="fwdY1(e)" :x2="fwdX" :y2="fwdY2(e)" class="edge" />
        <polygon :points="fwdArrow(e)" class="edge" />
        <text :x="fwdX + 10" :y="(fwdY1(e) + fwdY2(e)) / 2 + 4" class="edge-label">{{ e.label }}</text>
      </g>

      <!-- 状态节点：彩色圆角块，中间只有文字 -->
      <g v-for="(s, i) in states" :key="s.id">
        <rect
          :x="X0 + 1"
          :y="stateY(i)"
          :width="STATE_W - 2"
          :height="STATE_H"
          rx="14"
          :fill="fill(i)"
          :stroke="stroke(i)"
          stroke-width="1.6"
          class="state-box"
          :class="{ active: s.id === active }"
        />
        <text :x="X0 + STATE_W / 2" :y="stateY(i) + STATE_H / 2 + 5" text-anchor="middle" :fill="textFill(i)" class="state-label">
          {{ s.label }}
        </text>
      </g>
    </svg>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  /** 状态节点：[{ id, label }]，自上而下排列 */
  states: { type: Array, required: true },
  /**
   * 转换边：[{ from, to, label, back }]
   * back=true 为返回边：默认走右侧车道，side='left' 走左侧车道；
   * 同一侧多条时用 lane 错开（0 靠内，越大越靠外）。
   */
  edges: { type: Array, default: () => [] },
  /** 当前激活状态 id（高亮） */
  active: String,
})

const STATE_W = 230
const STATE_H = 46
const GAP = 62
const BACK_W = 130
const LEFT_W = 110
const PAD_TOP = 8

/** 每个状态的颜色 [描边色, 文字色]，按顺序循环使用 */
const PALETTE = [
  ['#5b8def', '#cfe0ff'],
  ['#34c3a0', '#d2f5ea'],
  ['#e08b4d', '#fbe2cd'],
  ['#a06ee0', '#ecddfa'],
  ['#e05d7e', '#fbdce5'],
  ['#3fb4d9', '#d8f0f9'],
]

const idx = computed(() => Object.fromEntries(props.states.map((s, i) => [s.id, i])))
const forwardEdges = computed(() => props.edges.filter((e) => !e.back))
const backEdges = computed(() => props.edges.filter((e) => e.back))
const leftEdges = computed(() => backEdges.value.filter((e) => e.side === 'left'))
const rightEdges = computed(() => backEdges.value.filter((e) => e.side !== 'left'))

/** 是否有左侧返回边；没有则不占左车道 */
const X0 = computed(() => (leftEdges.value.length ? LEFT_W : 0))
const fwdX = computed(() => X0.value + STATE_W / 2)

const rightLaneCount = computed(() => {
  const lanes = rightEdges.value.map((e) => e.lane || 0)
  return lanes.length ? Math.max(...lanes) + 1 : 1
})

const width = computed(() => X0.value + STATE_W + BACK_W + 12)
const height = computed(() => PAD_TOP + props.states.length * STATE_H + (props.states.length - 1) * GAP + 8)

function color(i) {
  return PALETTE[i % PALETTE.length]
}
/** 节点底色：主题色 16% 透明 */
function fill(i) {
  return color(i)[0] + '29'
}
function stroke(i) {
  return color(i)[0]
}
function textFill(i) {
  return color(i)[1]
}

function stateY(i) {
  return PAD_TOP + i * (STATE_H + GAP)
}

function fwdY1(e) {
  return stateY(idx.value[e.from]) + STATE_H
}
function fwdY2(e) {
  return stateY(idx.value[e.to])
}

/** 正向边箭头（朝下） */
function fwdArrow(e) {
  const y = fwdY2(e)
  return `${fwdX.value - 5},${y - 9} ${fwdX.value + 5},${y - 9} ${fwdX.value},${y}`
}

/** 返回边竖直车道的 x 坐标 */
function laneX(e) {
  if (e.side === 'left') {
    return 0
  }
  const f = ((e.lane || 0) + 1) / rightLaneCount.value
  return X0.value + STATE_W + BACK_W * f
}

/** 返回边路径：源状态侧边 → 横走 → 车道竖走 → 回到目标状态侧边 */
function backPath(e) {
  const x1 = e.side === 'left' ? X0.value : X0.value + STATE_W
  const x2 = laneX(e)
  const y1 = stateY(idx.value[e.from]) + STATE_H / 2
  const y2 = stateY(idx.value[e.to]) + STATE_H / 2
  return `M ${x1} ${y1} L ${x2} ${y1} L ${x2} ${y2} L ${x1} ${y2}`
}

/** 返回边箭头：左侧朝右、右侧朝左，落在目标状态侧边 */
function backArrow(e) {
  const x1 = e.side === 'left' ? X0.value : X0.value + STATE_W
  const y2 = stateY(idx.value[e.to]) + STATE_H / 2
  if (e.side === 'left') {
    return `${x1 - 9},${y2 - 5} ${x1 - 9},${y2 + 5} ${x1},${y2}`
  }
  return `${x1 + 9},${y2 - 5} ${x1 + 9},${y2 + 5} ${x1},${y2}`
}

/** 返回边标签：放在底部横线上方，避免与节点/箭头重叠 */
function backLabelX(e) {
  const x1 = e.side === 'left' ? X0.value : X0.value + STATE_W
  return (x1 + laneX(e)) / 2
}
function backLabelY(e) {
  const y2 = stateY(idx.value[e.to]) + STATE_H / 2
  return y2 - 7
}
</script>

<style scoped>
.state-diagram {
  overflow-x: auto;
}

.state-diagram svg {
  display: block;
  width: 100%;
  height: auto;
  max-width: 480px;
}

.state-box.active {
  stroke-width: 2.5;
  filter: drop-shadow(0 0 8px rgba(91, 141, 239, 0.55));
}

.state-label {
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 1px;
}

.edge {
  stroke: var(--accent);
  stroke-width: 1.6;
  fill: none;
}

.edge.back {
  stroke: var(--text-muted);
  stroke-dasharray: 5 4;
}

.edge-label {
  font-size: 11px;
  fill: var(--text-secondary);
}
</style>
