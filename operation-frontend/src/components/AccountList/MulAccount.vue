<template>
  <div class="AccountList">
    <a-modal
      width="800px"
      title="渠道二维码详情"
      v-model="visable"
    >
      <a-row :gutter="[16,16]">
        <a-col
          class="account-item textOverflow"
          :span="7"
          v-for="item in account"
          :key="item.id"
          @click="selectAccountId = item.id"
        >
          <img class="Qrcode-left-list-item-headImg img" :src="item.headImage" alt="">

          <p class="textOverflow"> {{ item.nickName }}</p>
          <div class="active-flag" v-if="selectAccountId === item.id">
            <a-icon type="check" class="active-flag-icon"/>
          </div>
        </a-col>
      </a-row>
      <template slot="footer">
        <a-button key="submit" type="primary" @click="handleOk">
          确定
        </a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import { getAccount } from '@/api/publicAccount'
import { mapGetters } from 'vuex'
export default {
  name: 'AccountList',
  computed: {
    ...mapGetters(['account'])
  },
  props: {
    maskClosable: {
      type: Boolean,
      default: false
    }
  },
  data () {
      return {
          visable: false,
          publicAccountList: [],
          selectAccountId: ''
      }
  },
  methods: {
    openModal () {
        this.visable = true
    },
    handleOk () {
        this.$emit('select', this.account.filter(item => {
            return item.id === this.selectAccountId
        }))
        this.visable = false
    },
    // 查询公众号列表
    async getAccount () {
      const { data, code } = await getAccount()
      if (code === 200) {
        this.publicAccountList = Object.freeze(data)
      }
    }
  }
}
</script>
<style scoped lang="less">
@import '../../utils/utils.less';

.left-button{
    width: 219px;
}

.account-item {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 60px;
    margin-bottom: 8px;
    background: #f5f5f5;
    margin-right: 10px;
    border: 1px solid #f5f5f5;
    color: #000;

    cursor: pointer;
    .img {
      width: 30px;
      height: 30px;
      object-fit: cover;
      margin-right: 10px;
    }
    p {
      width: 220px;
      margin-bottom: 0;
    }
}

</style>
