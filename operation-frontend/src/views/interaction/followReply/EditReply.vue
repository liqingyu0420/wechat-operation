<template>
  <div class="EditReply">
    <template-grail>
      <template slot="left">
        <a-form :form="form" :label-col="{ span:3 }" :wrapper-col="{ span: 20 }" @submit="handleSubmit">
          <a-form-item label="当前公众号">
            <div class="public-account">
              <div class="account">
                <img class="img" :src="editReplyData.headImage" alt="">
                {{ editReplyData.nikeName }}
              </div>
            </div>
          </a-form-item>
          <a-form-item label="推送内容">
            <div class="current-code-desc"><a-icon type="exclamation-circle" /> 推送方式为“全部推送”、“按顺序推送”时，仅第一条消息可设置成多图文。
            </div>
          </a-form-item>
          <a-form-item label="推送方式">
            <a-radio-group
              v-decorator="['pushType', { rules: [{ required: true, message: '请选择二维码期限' }] }]"
            >
              <a-radio :value="0">
                全部推送
              </a-radio>
              <a-radio :value="1">
                按顺序推送
              </a-radio>
              <a-radio :value="2">
                随机推送一条
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="推送内容" class="template-edit-style">
            <div class="template-edit">
              <template-edit :edit-list="content" @changeContent="changeContent" />
            </div>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 12 }">
            <a-button type="primary" html-type="submit">提交</a-button>
          </a-form-item>
        </a-form>
      </template>
      <template slot="right">
        <pre-view :list="content"/>
      </template>
    </template-grail>
  </div>
</template>

<script>
import TemplateGrail from '@/components/TemplateGrail'
import PreView from '@/components/PreView'
import TemplateEdit from '@/components/TemplateEdit'
import { getFollowReplyDetail, putFollowReplyEnable } from '@/api/interaction'
import { repalceTag } from '@/utils/global'
export default {
  name: 'EditReply',
  components: {
    TemplateGrail,
    PreView,
    TemplateEdit
  },
  created () {
      this.getFollowReplyDetail()
  },
  data () {
      return {
          form: this.$form.createForm(this, { name: 'coordinated' }),
          editReplyData: {},
          content: []
      }
  },
  methods: {
    // 提交表单
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          const { pushType } = values
          console.log('Received values of form: ', values)
          this.putFollowReplyEnable(pushType)
        }
      })
    },
    // 发布二维码
    async putFollowReplyEnable (pushType) {
      const resultContent = repalceTag.dealListPost(JSON.parse(JSON.stringify(this.content)))
      const $par = {
        accountId: this.$route.query.accountId,
        content: JSON.stringify(resultContent),
        pushType,
        id: this.$route.query.id
      }
      if (this.content.length === 0) {
        this.$message.error('请填写content的值')
        return
      }
      const { data, code } = await putFollowReplyEnable($par)
      if (code === 200) {
        console.log(data)
      }
    },

    // 编辑去内容改变
    changeContent (item) {
      this.content = item
    },

    //   获取详情
    async getFollowReplyDetail () {
      const { accountId, id } = this.$route.query
      const { data, code } = await getFollowReplyDetail({ accountId }, id)
      if (code === 200) {
          console.log(data)
          this.editReplyData = data
          // JSON.parse(data.contents)
          this.content = JSON.parse(data.content) || []
          this.fillForm(data)
      }
    },
    // 反填表格
    fillForm (record) {
        const { pushType } = record
        this.form.setFieldsValue({ pushType })
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
</style>
