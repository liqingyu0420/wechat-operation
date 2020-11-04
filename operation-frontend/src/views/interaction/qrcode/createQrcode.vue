<template>
  <div>
    <template-grail >
      <template slot="left">
        <a-form :form="form" :label-col="{ span:3 }" :wrapper-col="{ span: 20 }" @submit="handleSubmit">
          <a-form-item label="当前公众号：">
            <account-view :accountId="accountId" @selectAccount="selectAccount"/>
          </a-form-item>
          <a-form-item label="二维码名称">
            <a-input
              :maxLength="20"
              placeholder="如: 扫码送使用装,限20字符"
              v-decorator="['label', { rules: [{ required: true, message: '请输入二维码名称' }] }]"
            />
          </a-form-item>
          <a-form-item label="二维码期限">
            <a-radio-group
              v-decorator="['type', { rules: [{ required: true, message: '请选择二维码期限' }] }]"
            >
              <a-radio :value="0">
                30天（临时二维码过期后不能再扫描）
              </a-radio>
              <a-radio :value="1">
                永久（永久二维码最多创建数量10万个）
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="推送方式" >
            <a-radio-group
              v-decorator="['pushType', { rules: [{ required: true, message: '请选择二维码推送方式' }] }]"
            >
              <a-radio :value="2">
                随机推送
              </a-radio>
              <a-radio :value="1">
                全部推送
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="推送内容" class="template-edit-style">
            <div class="template-edit">
              <template-edit :edit-list="content" @changeContent="changeContent"/>
            </div>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 12 }">
            <a-button type="primary" html-type="submit">
              {{ this.id ? '修改' : '新增' }}二维码
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template slot="right">
        <pre-view :list="content"/>
      </template>
    </template-grail>
    <account-list ref="account-list" @select="selectAccount"/>
  </div>

</template>

<script>
import { getQrcodeDetail, PostQrcode } from '@/api/interaction'
import RightPanel from '@/components/RightPanel'
import TemplateGrail from '@/components/TemplateGrail'
import TemplateEdit from '@/components/TemplateEdit'
import PreView from '@/components/PreView'
import { repalceTag } from '@/utils/global'
import AccountList from '@/components/AccountList'
import AccountView from '@/components/AccountView'
import { mapGetters } from 'vuex'
export default {
  name: 'CreateQrcode',
  components: {
      RightPanel,
      TemplateGrail,
      TemplateEdit,
      PreView,
      AccountList,
      AccountView
  },
  computed: {
    ...mapGetters(['account'])
  },
  created () {
    const { accountId, id } = this.$route.query
    this.accountId = accountId
    if (accountId && id) {
        this.id = id
        this.getQrcodeDetail()
    }
  },
  data () {
      return {
          form: this.$form.createForm(this, { name: 'coordinated' }),
          createQrcodeData: {},
          accountId: '',
          content: []
      }
  },
  methods: {
    selectAccount (account) {
      console.log(account)
      this.accountId = account[0].id
      // this.$set(this.createQrcodeData, 'account', account[0].id)
    },
    // 提交表单
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          const { label, pushType, type } = values
          this.PostQrcode(label, pushType, type)
        }
      })
    },
    // 新增 或者编辑 二维码
    async PostQrcode (label, pushType, type) {
      const resultContent = repalceTag.dealListPost(JSON.parse(JSON.stringify(this.content)))
      console.log(resultContent)
      const $par = {
        accountId: this.accountId,
        id: this.id,
        content: JSON.stringify(resultContent),
        label,
        pushType,
        type
      }

        console.log($par)
      if (this.content.length === 0) {
        this.$message.error('请填写推送内容')
        return
      }

      const { data, code } = await PostQrcode($par)

      if (code === 200) {
        console.log(data)
        this.$message.success(`${this.id ? '修改' : '新增'} 成功`)
         this.$router.replace('/information/qrcode')
      }
    },
    changeContent (item) {
      this.content = item
    },
    // 查询二维码详情
    async getQrcodeDetail () {
      const { data, code } = await getQrcodeDetail(this.id)
      if (code === 200) {
          console.log(data)
          this.createQrcodeData = data
          this.content = JSON.parse(data.content)
          this.fillForm()
      }
    },
    //   反填form表单
    fillForm () {
        const { label, type, pushType } = this.createQrcodeData
        this.form.setFieldsValue({ label, type, pushType })
    }
  }
}
</script>
<style scoped lang="less">
    .public-account {
        display: flex;
        .account {
            min-width: 240px;
            background: #fff;
            border: 1px solid #e9e9e9;
            overflow: hidden;
            height: 48px;
            padding: 8px;
            display: flex;
            justify-content: flex-start;
            align-items: center;
            margin-right: 20px;
            box-sizing: border-box;
            img {
                width: 30px;
                height: 30px;
                margin-right: 10px;
            }
        }
        .change-account {
            color: #1890ff;
            .anticon-swap {
                transform: rotate(90deg);
            }
        }
    }
</style>
