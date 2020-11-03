<template>
  <a-modal
    title="title"
    :visible="visible"
    :confirm-loading="confirmLoading"
    :footer="footer"
    @ok="handleOk"
    @cancel="handleCancel"
    class="model"
    :destroyOnClose="true"
  >
    <slot></slot>
  </a-modal>
</template>

<script>
export default {
  name: 'Dialog',
  props: {
      title: {
          type: String,
          default: 'title'
      },
      visible: {
          type: Boolean,
          default: true
      },
      footer: {
        type: null,
        default: !null
      }
  },
  data () {
    return {
      confirmLoading: false
    }
  },
  methods: {
    handleOk () {

    },
    handleCancel () {
      this.$emit('cancle')
    }
  }
}
</script>
<style scoped lang="less">
/deep/ .ant-modal-body {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
