<template>
  <div class="Push">
    <!-- :gutter="[18, 16]" -->
    <a-row >
      <a-col
        v-for="item in pushList"
        :key="item.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="8"
        :xl="6"
      >
        <action-card
          :item-data="item"
          @jumpEditReply="jumpEditReply"
          @enableInteraction="enableInteraction"
        />
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { getPush, enableInteraction } from '@/api/interaction'
import ActionCard from '../components/ActionCard'
export default {
  name: 'Push',
  components: {
    ActionCard
  },
  created () {
    this.getPush()
  },
  data () {
    return {
      pushList: []
    }
  },
  methods: {
    // 开启或者关闭被关注回复
    async enableInteraction ($par) {
      const { enableVal, id } = $par
      const { data, code } = await enableInteraction({ enableVal }, id)
      if (code === 200) {
        console.log(data)
        this.pushList = this.pushList.map(item => {
          if (item.id === id) {
            item.enable = enableVal
          }
          return item
        })
      }
    },
    // 跳转编辑回复页
    jumpEditReply (record) {
      const { accountId, id } = record
      console.log(record)
      this.$router.push({
        path: '/interaction/editPush',
        query: { accountId, id }
      })
    },
    // 获取被关注回复列表
    async getPush () {
      const { data, code } = await getPush()
      if (code === 200) {
        console.log(data)
        this.pushList = data
      }
    }
  }
}
</script>
<style scoped lang="less">
.Push {
  // display: grid;
  // grid-template-columns: auto auto auto auto;
  // grid-template-rows: auto auto auto auto;
}
</style>
