<template>
  <div class="FansTag">

    <div class="FansTag-left">
      <a-button icon="appstore" class="FansList-left-label left-button">全部公众号二维码</a-button>
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
    <div class="FansTag-right">

      <div class="current-code-desc"><a-icon type="exclamation-circle" />增加标签，来为粉丝贴上不同的专属分组吧。
      </div>
      <a-card>
        <div class="new-add-qrcode">
          <div class="tag-count">
            共有20 个 标签
          </div>
          <div class="tag-opration">
            <a-button type="primary" icon="plus" @click="() => {modalVisable = true, chooseNewAddAccountId = ''}">新增标签</a-button>
            <a-button type="primary" class="reload-btn" icon="reload" @click="reloadTag">同步标签</a-button>
          </div>
        </div>
        <a-table :loading="loading" :columns="columns" :data-source="tagList">
          <span slot="name" slot-scope="text, record">
            <a-tag color="pink">{{ record.name }}</a-tag>
          </span>
          <span slot="action" slot-scope="text, record">

            <a @click="editTag(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定要删除这个标签吗?"
              ok-text="确定"
              cancel-text="取消"
              @confirm="deleteTag(record)"
            >
              <a >删除</a>
            </a-popconfirm>

          </span>
        </a-table>
      </a-card>

    </div>

    <a-modal v-model="modalVisable" :title="name? '编辑标签': '新增标签'">
      <a-input v-model="name" :maxLength="6" placeholder="请输入标签，不能超过六个字符"></a-input>
      <template slot="footer">
        <a-button key="submit" type="primary" :loading="loading" @click="editAddTag">
          确定
        </a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import { editAddTag, reloadTag, deleteTag } from '@/api/fans'
import { getAccount } from '@/api/publicAccount'
/*eslint-disable */
const columns = [
  {
    title: '标签名称',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' }
  },
  {
    title: '粉丝数',
    dataIndex: 'fansCount',
    key: 'fansCount'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime'
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  name: 'FansTag',
  computed: {
    accountId () {
      return this.publicAccountList[this.leftCurrentAccountIndex].id
    }
  },
  async created () {
    await this.getAccount()
    await this.getTagList()
  },
  data () {
    return {
      loading: false,
      columns,
      modalVisable: false,
      leftCurrentAccountIndex: 0, // 左侧下标
      publicAccountList: [],
      tagList: [],
      params: {
        page: 1,
        accountId: ''
      },
      name: '',
      aa: 0
    }
  },
  methods: {
    // 删除标签
    async deleteTag (record) {
      const { data, code }  = await deleteTag(record.id)
      if (code === 200) {
        if (!data) {
          this.$message.error('当前标签下人数超过10万,请解除他们的关联')
         
        }
        this.$message.success('删除成功')
        this.getTagList()

      }
    },
    // 同步标签
    async reloadTag () {
      const { data, code } = await reloadTag({ accountId: this.accountId })
      if (code === 200) {
        this.$message.success('同步成功,标签正在从后台获取')
      }
    },
    editTag (record) {
      console.log(record)
      this.modalVisable = true
      this.name = record.name
      this.id = record.id
    },
    // 新增标签
    async editAddTag () {
      const $par = {
        accountId: this.accountId,
        name: this.name,
        id: this.id
      }
      this.loading = true
      const { data, code } = await editAddTag($par)
      this.loading = false
      if (code === 200) {
        console.log(data)
        this.modalVisable = false
        this.getTagList()
      }
        this.name = ''
    },
    // 选择左侧公众号列表
    handlerAccount (item, index) {
      this.leftCurrentAccountIndex = index
      this.getTagList()
    },
    // 查询列表
    async getTagList () {
      this.params.accountId = this.publicAccountList[this.leftCurrentAccountIndex].id
      const $par = Object.assign({}, { ...this.params })
      this.loading = true
      this.tagList = await this.$store.dispatch('tag/getTagList', $par)
      this.loading = false
    },
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
.FansTag {
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
                .Qrcode-left-list-item-headImg {
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
.img {
  width: 24px;
  height: 24px;
  object-fit: cover;
}
  .Qrcode-left-list-item-headImg {
                    width: 24px;
                    height: 24px;
                    object-fit: cover;
                    margin-right: 10px;
                }
.left-button{
    width: 219px;
}
.new-add-qrcode {
  display: flex;
  align-items: center;

  justify-content: space-between;
  margin-bottom: 10px;
  .tag-opration {
    .reload-btn {
      margin-left: 10px;
    }
  }
}
</style>
