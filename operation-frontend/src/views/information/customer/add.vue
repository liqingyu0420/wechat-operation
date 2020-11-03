<template>
  <div class="CustomerAdd">
    <template-grail>
      <template slot="left">
        <a-form :form="form" :label-col="{ span:3 }" :wrapper-col="{ span: 20 }" @submit="handleSubmit">
          <a-form-item label="选择公众号">
            <account-view :accountId="accountId" @selectAccount="selectAccount"/>
          </a-form-item>
          <a-form-item label="消息名称">
            <a-input
              :maxLength="20"
              placeholder="请填写消息名称(仅供备注,不会发给粉丝),限20字符"
              v-decorator="['label', { rules: [{ required: true, message: '请填写消息名称' }] }]"
              v-model="customerData.label"
            />
          </a-form-item>
          <a-form-item label="群发时间">
            <a-date-picker
              v-decorator="['sendTime', { rules: [{ required: true, message: '请填写群发时间' }] }]"
              @change="changeSendTime"
              format="YYYY-MM-DD HH:mm:ss"
              :show-time="{ defaultValue: moment('00:00:00', 'HH:mm:ss') }"
            />
          </a-form-item>
          <a-form-item label="群发粉丝">
            <a-radio-group
              v-model="customerData.type"
            >
              <a-radio :value="1">
                按条件筛选粉丝
              </a-radio>
              <a-radio :value="0">
                全部粉丝
              </a-radio>
            </a-radio-group>
            <!-- <span>符合条件约 10 人（如与实际粉丝数差别大，请同步粉丝）</span> -->
          </a-form-item>
          <a-form-item label="性别" v-if="customerData.type === 1">
            <a-radio-group
              v-model="customerData.selectSex"
            >
              <a-radio value="">
                全部性别
              </a-radio>
              <a-radio :value="1">
                仅男性粉丝
              </a-radio>
              <a-radio :value="0">
                仅女性粉丝
              </a-radio>
              <a-radio :value="2">
                未知性别
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="地域" v-if="customerData.type === 1">
            <a-cascader
              v-decorator="['ChangeCity', { rules: [{ required: false, message: '请选择粉丝性别'}]}]"
              v-model="customerData.ChangeCity"
              style="width: 100%"
              :options="city"
              placeholder="请选择地区"
              :fieldNames="fieldNames"
            />
          </a-form-item>
          <a-form-item label="内容" class="template-edit-style">
            <div class="content">
              <msg-type @chooseType="chooseType" :currentEditFormType="customerData.currentEditFormType" :len="3"/>

              <!-- 图文 -->
              <image-text :hide-delete="true" :item-data="content[0]" v-if="customerData.currentEditFormType === 1"/>

              <!-- 图片 -->
              <show-img
                label="图片"
                :imgUrl="content[0].url"
                v-else-if="customerData.currentEditFormType === 2"
                @changeImg="changeImg"
              />

              <!-- 文字消息 -->
              <template v-if="customerData.currentEditFormType * 1 === 3">
                <text-view
                  :item-data="content[0]"
                  @changeText="changeText"
                />
              </template>
            </div>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 12 }">
            <a-button type="primary" html-type="submit">
              提交
            </a-button>
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
import { postCustomer, getCustomerDetail } from '@/api/information'
import TemplateGrail from '@/components/TemplateGrail'
import PreView from '@/components/PreView'
import AccountView from '@/components/AccountView'
import moment from 'moment'
import TemplateEdit from '@/components/TemplateEdit'
import { cascader } from 'ant-design-vue'
import { city } from '@/utils/city'
import { repalceTag } from '@/utils/global'
import MsgType from '@/components/MsgType'
import TextView from '@/components/TemplateItem/TextItem'
import ImageText from '@/components/TemplateItem/ImageTextItem'
import ShowImg from '@/components/TemplateItem/ShowImg'
export default {
  name: 'CustomerAdd',
  components: {
    ACascader: cascader,
    TemplateGrail,
    PreView,
    AccountView,
    TemplateEdit,
    MsgType,
    TextView,
    ImageText,
    ShowImg
  },
  computed: {
    mulWatchKey () {
      const { type, selectSex, ChangeCity } = this.customerData
      return { type, selectSex, ChangeCity }
    }
  },
  watch: {
    mulWatchKey: {
      handler (newValue, oldValue) {
        console.log(newValue, oldValue)
        this.preFansNum()
      }
    }
  },
  created () {
    const { accountId } = this.$route.query
    if (accountId) {
      this.accountId = accountId
    }
  },
  mounted () {
    if (this.accountId) {
       this.fillForm()
    }
  },
  data () {
      return {
           form: this.$form.createForm(this, { name: 'coordinated' }),
           city: Object.freeze(city),
           fieldNames: { label: 'shortName', value: 'shortName', children: 'childList' },
           accountId: '',
           customerData: {
             label: '',
             type: 1,
             currentEditFormType: 1,
             sendTime: '',
             selectSex: '',
             ChangeCity: []
           },
           content: []
      }
  },
  methods: {
    moment,
    //   预计发送粉丝
    async preFansNum () {
      const { selectSex, subscribeTime, ChangeCity: [selectProvince, selectCity] } = this.customerData
      const $par = {
        accountId: this.accountId,
        selectProvince,
        selectCity,
        selectSex,
        subscribeTime
      }
      console.log($par)
        await this.$store.dispatch('fans/preFansNum', $par)
    },
    // 提交表单
    handleSubmit (e) {
        e.preventDefault()
        this.form.validateFields((err, values) => {
            if (!err) {
                const { ChangeCity, label, selectSex } = values
                 const resultContent = repalceTag.dealListPost(JSON.parse(JSON.stringify(this.content)))
                const { sendTime, type } = this.customerData
                const [selectProvince, selectCity] = ChangeCity

                const $par = {
                    accountId: this.accountId,
                    content: JSON.stringify(resultContent),
                    sendTime,
                    label,
                    selectSex,
                    type: type,
                    selectProvince,
                    selectCity
                }
                this.postCustomer($par)
            }
        })
    },
    // 增加客服消息
    async postCustomer ($par) {
      const { code } = await postCustomer($par)
      if (code === 200) {
        this.$message.success('新增成功')
        this.$router.push('/information/customer')
      }
    },
     // 选择群发时间
    changeSendTime (date, dateString) {
      console.log(dateString)
      this.customerData.sendTime = dateString
    },
    // 图片消息
    changeImg (item) {
      console.log(item)
      this.content = [{ ...item, type: this.customerData.currentEditFormType }]
    },
    // 文字消息
    changeText (item) {
      this.content = [{ content: item, type: this.customerData.currentEditFormType }]
    },
    // 切换上传类型
    chooseType (value) {
      const { type } = value
      this.customerData.currentEditFormType = type
      if (type === 1) {
        this.content = [{ type: 1, imagetextlist: [{}] }]
      }
    },
    // 查询客服消息详情
    async getCustomerDetail () {
      const { data, code } = await getCustomerDetail(this.id)
      if (code === 200) {
          console.log(data)
          this.customerList = data
          this.content = JSON.parse(data.content)
          this.fillForm()
      }
    },
    // 反填form表单
    fillForm () {
        const copyFlag = this.$route.query.type
        const { type, label, content, selectCity, selectProvince, selectSex, sendTime } = Number(copyFlag) ? this.$store.getters.copyInformation : this.customerData
        console.log(content, 'contentcontent')
        this.content = content ? JSON.parse(content) : [{ type: 1, imagetextlist: [{}] }]
        this.customerData.sendTime = sendTime
        this.$set(this.customerData, 'currentEditFormType', Number(this.content[0].type))
        this.$forceUpdate()
        this.form.setFieldsValue({ type, label, ChangeCity: [selectProvince, selectCity], selectSex, sendTime })
    },
    //   选择公众号
    selectAccount (account) {
      this.accountId = account[0].id
    },
    changeContent (content) {
      console.log('hello 11')
      this.content = content
    }

  }
}
</script>
<style scoped lang="less">
.condition-filter {
    padding: 10px;
    background: #fff;
    border: 1px solid #d8d8d8;
    margin-top: 20px;
    width: 60%;
}

/deep/ .ant-form {
    border-bottom: none;
    margin-bottom: 20px;
}
</style>
