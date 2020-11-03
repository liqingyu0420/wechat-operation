<template>
  <div class="MsgType">
    <div class="label">
      消息类型：
    </div>
    <div>
      <a-radio-group v-model="value" @change="onChange">
        <a-radio :value="item.type" v-for="item in msgType" :key="item.type">
          {{ item.label }}
        </a-radio>
      </a-radio-group>
    </div>
  </div>
</template>

<script>
// const msgType = [
//     {
//         type: 1,
//         label: '图文'
//     },
//     {
//         type: 2,
//         label: '图片'
//     },
//     {
//         type: 3,
//         label: '文字'
//     },
//     {
//         type: 4,
//         label: '音频'
//     },
//     {
//         type: 5,
//         label: '视频'
//     }
// ]
export default {
  name: 'MsgType',
  props: {
    currentEditFormType: {
      type: [Number, String],
      required: true
    },
    len: {
      type: Number,
      default: 5
    }
  },
  watch: {
    currentEditFormType () {
      console.log(this.currentEditFormType, ' this.currentEditFormType')
        this.value = this.currentEditFormType
    },
    lenL: {
      handler () {
        this.msgType.splice(this.len)
      },
      immediate: true
    }
  },
  data () {
      return {
          value: 1,
          msgType: [
              {
                  type: 1,
                  label: '图文消息'
              },
              {
                  type: 2,
                  label: '图片消息'
              },
              {
                  type: 3,
                  label: '文字消息'
              },
              {
                  type: 4,
                  label: '音频消息'
              },
              {
                  type: 5,
                  label: '视频消息'
              }
          ]
        }
  },
  methods: {
      onChange (e) {
          const result = this.msgType.filter(item => {
              return item.type === e.target.value
          })
          this.$emit('chooseType', ...result)
      }
  }
}
</script>
<style scoped lang="less">
.MsgType {
    display: flex;
    justify-content: flex-start;
    padding-left: 60px;
}
</style>
