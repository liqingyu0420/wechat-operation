<template>
  <div class="QueryType">
    <a-select
      style="width: 120px"
      @change="handleChange"
      default-value=""
    >
      <a-select-option :value="item.type" v-for="item in status" :key="item.type">
        {{ item.label }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
const status = [
    {
        type: '',
        label: '全部'
    },
    {
        type: 0,
        label: '已发送'
    },
    {
        type: 1,
        label: '未发生'
    },
    {
        type: 2,
        label: '发送中'
    },
    {
        type: 3,
        label: '发送终止'
    },
    {
        type: 4,
        label: '发送失败'
    }
]
export default {
  name: 'QueryType',
  data () {
      return {
          status
      }
  },
  methods: {
      handleChange (item) {
          console.log(item)
          this.$emit('queryType', item)
      }
  }
}
</script>
<style scoped lang="less">

</style>
