// 8.5.4 删除数据集（DeleteDataSet）

export default {
  title: '删除数据集 delete-dataset (8.5.4)',
  desc: '删除动态创建的数据集',
  params: [
    { key: 'ds', label: '数据集引用', type: 'text', required: true, placeholder: 'LD/LN.dsName，如 PROT/LLN0.dsNewDs' },
  ],
}