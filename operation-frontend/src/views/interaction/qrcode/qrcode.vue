<template>
  <div class="Qrcode">
    <div class="Qrcode-left">
      <a-button icon="appstore" class="Qrcode-left-label left-button">全部公众号二维码</a-button>
      <div class="Qrcode-left-list">
        <div
          class="Qrcode-left-list-item"
          v-for="(item, index) in publicAccountList"
          :key="item.id"
          @click="handlerAccount(item, index)"
        >
          <a-button :type="leftCurrentAccountIndex === index ? 'primary' : ''" class="Qrcode-left-label left-button">
            <img class="Qrcode-left-list-item-headImg" :src="item.headImage" alt="">
            {{ item.nickName }}
          </a-button>
        </div>
      </div>
    </div>
    <div class="Qrcode-right">

      <div class="current-code-desc"><a-icon type="exclamation-circle" />设置带有参数的二维码，获取扫码关注的粉丝数量，渠道来源等信息，并自动为粉丝贴上标签。可用于比较各种渠道的增粉效果，为不同渠道来的粉丝回复不同消息。
      </div>
      <a-card>
        <div class="new-add-qrcode">
          <a-button type="primary" icon="plus" @click="newAddQrcode">新增二维码</a-button>
        </div>
        <a-table :columns="columns" :data-source="qrcodeData">
          <span slot="account" slot-scope="text, record">
            <img class="img" :src="record.headImage" alt="">
            {{ record.nikeName }}
          </span>
          <span slot="action" slot-scope="text, record">
            <a @click="headerQueryDetail(record)">详情</a>
            <a-divider type="vertical" />
            <a @click="editQrcode(record)">修改</a>
          </span>
        </a-table>
      </a-card>
    </div>

    <!--详情弹窗  -->
    <a-modal width="800px" v-model="detailModel" title="渠道二维码详情" >
      <code-detail :curLookCodeItem="curLookCodeItem"/>
    </a-modal>
    <!--新增二维码  -->
    <account-list ref="account-list" @select="jumpCreateQrcode"/>
  </div>
</template>

<script>
import { getQrcode } from '@/api/interaction'
import AccountList from '@/components/AccountList'
import CodeDetail from './detail'
import { mapGetters } from 'vuex'
const columns = [
  {
    title: '二维码名称',
    dataIndex: 'label',
    key: 'label'
  },
  {
    title: '公众号',
    dataIndex: 'account',
    key: 'account',
    scopedSlots: { customRender: 'account' }
  },
  {
    title: '总扫码次数',
    dataIndex: 'totalNum',
    key: 'totalNum'
  },
  {
    title: '新扫码且关注',
    dataIndex: 'newNum',
    key: 'newNum'
  },
  {
    title: '已关注扫码',
    dataIndex: 'followNum',
    key: 'followNum'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime'
  },
  {
    title: '到期时间',
    dataIndex: 'expireTime',
    key: 'expireTime'
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  name: 'Qrcode',
  components: {
    CodeDetail,
    AccountList
  },
  computed: {
    accountId () {
      return this.publicAccountList[this.leftCurrentAccountIndex].id
    },
    ...mapGetters({
      publicAccountList: 'account'
    })
  },
  async created () {
    await this.getQrcode()
  },
  data () {
      return {
          detailModel: false,
          qrcodeModel: false,
          columns,
          leftCurrentAccountIndex: 0, // 左侧下标
          chooseNewAddAccountId: '',
          params: {
            page: 1,
            accountId: ''
          },
          qrcodeData: [],
          curLookCodeItem: {} // 当前选择的公众号详情
      }
  },
  methods: {
      // 修改二维码
      editQrcode (record) {
        this.$router.push({
          path: '/interaction/createQrcode',
          query: {
            accountId: record.accountId,
            id: record.id
          }
        })
      },
      // 新增二维码
      newAddQrcode () {
        this.$refs['account-list'].openModal()
      },
      // 跳转到新建二维码页面
      jumpCreateQrcode (account) {
        console.log(account, 'accountaccount')
        this.$router.push({
          path: '/interaction/createQrcode',
           query: {
            accountId: account[0].id
          }
        })
      },
      // 查看详情
      headerQueryDetail (record) {
        this.detailModel = true
        this.curLookCodeItem = record
      },
      // 选择左侧公众号列表
      handlerAccount (item, index) {
        this.leftCurrentAccountIndex = index
        this.getQrcode()
      },
      // 查询二维码列表
      async getQrcode () {
        this.params.accountId = this.publicAccountList[this.leftCurrentAccountIndex].id
        const $par = Object.assign({}, { ...this.params })
        const { data, code } = await getQrcode($par)
        if (code === 200) {
          this.qrcodeData = data.records
        }
      }
  }
}
</script>
<style scoped lang="less">
@import '~ant-design-vue/lib/style/themes/default.less';
// @primary-color
.Qrcode {
    margin-top: -20px;
    &-left {
        position: fixed;
        top: 64px;
        margin-left: -22px;
        padding-top: 20px;
        padding-left: 10px;
        width: 240px;
        background: #fcfcfc;
        border-right: 1px solid #e9e9e9;
        height: calc(100vh - 64px);
        overflow-x: hidden;
        overflow-y: auto;
        &-label {
            width: 100%;
            display: flex;
            justify-content: flex-start;
            align-items: center;
        }
        .Qrcode-left-list {

            &-item {
                margin-top: 10px;
                display: flex;
                justify-content: flex-start;
                &-headImg {
                    width: 24px;
                    height: 24px;
                    object-fit: cover;
                    margin-right: 10px;
                }
            }
        }
    }
    &-right {
        width: 100%;
        padding-left: 260px;
        // height: calc(100vh - 85px);
        // background-color: red;
       .current-code-desc {
            background: #e6f7ff;
            border: 1px solid #91d5ff;
            padding: 10px 0 10px 16px;
            margin-bottom: 12px;
            margin-top: 5px;

            i {
                margin-right: 10px;
                color: #0091fa;
            }
        }
    }
}

.left-button{
    width: 219px;
}

.img {
  width: 24px;
  height: 24px;
  object-fit: cover;
}
/deep/ .anticon-appstore {
  margin-top: 4px;
}
.new-add-qrcode {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-bottom: 10px;
}
</style>
