<template>
  <div class="Pagination">
    <pagination show-quick-jumper :default-current="1" :defaultPageSize="15" :total="total" @change="onChange" />
  </div>
</template>

<script>
import { pagination } from 'ant-design-vue'
export default {
  name: 'Pagination',
  components: {
      pagination
  },
  props: {
      total: {
          type: Number,
          default: 0
      }
  },
  methods: {
      onChange (item) {
          console.log(item)
          this.$emit('change', item)
      }
  }
}
</script>
<style scoped lang="less">
.Pagination {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 10px;
}
</style>
