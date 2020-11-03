<template>
  <div class="MassAdd">
    <template-grail>
      <template slot="left">
        <a-form :form="form" :label-col="{ span:3 }" :wrapper-col="{ span: 20 }" @submit="handleSubmit">
          <a-form-item label="选择公众号">
            <!-- @selectAccount="selectAccount" -->
            <account-view :accountId="accountId" />
          </a-form-item>
          <a-form-item label="群发时间">
            <a-date-picker
              v-decorator="['sendTime', { rules: [{ required: true, message: '请填写群发时间' }] }]"
              format="YYYY-MM-DD HH:mm:ss"
              :show-time="{ defaultValue: moment('00:00:00', 'HH:mm:ss') }"
            />
          </a-form-item>
          <a-form-item label="群发粉丝">
            <a-radio-group
              v-model="massData.type"
            >
              <a-radio :value="1">
                按条件筛选粉丝
              </a-radio>
              <a-radio :value="2">
                全部粉丝
              </a-radio>
            </a-radio-group>
          </a-form-item>

          <div class="condition-filter" v-if="massData.type === 1">
            <a-form-item label="性别" >
              <a-radio-group
                v-model="massData.selectSex"
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
            <a-form-item label="关注时间" >
              <range-picker @changeDate="changeDate" />
            </a-form-item>
            <a-form-item label="地域">
              <a-cascader

                v-model="massData.ChangeCity"
                style="width: 100%"
                :options="city"
                placeholder="请选择地区"
                :fieldNames="fieldNames"
              />
            </a-form-item>
            <a-form-item label="标签">
              <div class="tagList">
                <div
                  :class="['tagList-item', massData.selectTag.includes(tag.id) ? 'tag-active' : '' ]"
                  @click="handelTag(tag)"
                  v-for="tag in tagList"
                  :key="tag.id">
                  {{ tag.name }}
                </div>
              </div>
            </a-form-item>
          </div>
          <a-form-item label="手机号">
            <a-input
              :maxLength="11"
              class="phone-input"
              placeholder="选填"
              v-decorator="['phone', { rules: [{ required: false }] }]"
            />
            <a-checkbox
              v-decorator="['repeatSend']"
              checked >
              若有文章判定为转载，继续群发
            </a-checkbox>
          </a-form-item>
          <a-form-item label="消息类型" class="template-edit-style">
            <div class="content">
              <msg-type @chooseType="chooseType" :currentEditFormType="massData.msgType"/>
              <!-- 图文 -->
              <div
                @click="$refs['image-text'].openModal()"
                class="image-text cp"
                v-if="massData.msgType * 1 === 1"
              >
                <template v-if=" content[0] && content[0].type === 1 && content[0].hasOwnProperty('imagetextlist')">
                  <a-icon type="check-circle" />
                  已选择一张图文消息[ <a href="javascript:;"> 修改</a>]
                </template>
                <template v-else>
                  <a-icon type="plus"></a-icon>图文消息
                </template>
              </div>
              <!-- 图片 -->
              <div
                @click="$refs['image-view'].openModal()"
                class="image-text cp"
                v-if="massData.msgType * 1 === 2">
                <template v-if="content[0] && content[0].hasOwnProperty('url') && content[0].type === 2">
                  <a-icon type="check-circle" />
                  已选择一张图片消息[ <a href="javascript:;"> 修改</a>]
                </template>
                <template v-else>
                  <a-icon type="plus"></a-icon>图片消息
                </template>
              </div>
              <!-- 文字 -->
              <text-view
                :item-data=" content[0].type === 3 ? content[0] : {}"
                :is-show="false"
                @changeText="changeText"
                v-if="massData.msgType * 1 === 3"
              />
              <!-- 音频 -->
              <audiot-view
                v-if="massData.msgType * 1 === 4"
                :audioUrl="content[0].type === 4 ? content[0] : {}"
                @changeAudit="changeAudit"/>
              <!-- 视频 -->
              <video-view
                v-if="massData.msgType * 1 === 5"
                :videoUrl="content[0].type === 5 ? content[0] : {}"
                @changeVideo="changeVideo"/>
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

    <!-- 图片选择弹框 -->
    <image-view
      ref="image-view"
      @selectImg="selectImg"
    />
    <!-- 图文选择弹框 -->
    <image-text
      ref="image-text"
      @selectImgText="selectImgText"
    />
  </div>
</template>

<script>
import { postMass } from '@/api/information'
import TemplateGrail from '@/components/TemplateGrail'
import RangePicker from '@/components/RangePicker'
import PreView from '@/components/PreView'
import AccountView from '@/components/AccountView'
import moment from 'moment'
import TemplateEdit from '@/components/TemplateEdit'
import { cascader } from 'ant-design-vue'
import { city } from '@/utils/city'
import MsgType from '@/components/MsgType'
import { msgType, repalceTag } from '@/utils/global'
import TextView from '@/components/TemplateItem/TextItem'
import AudiotView from '@/components/TemplateItem/AudioItem'
import VideoView from '@/components/TemplateItem/VideoItem'
import ImageView from '@/components/material/ImageView'
import ImageText from '@/components/material/ImageText'
export default {
  name: 'MassAdd',
  components: {
    ACascader: cascader,
    TemplateGrail,
    PreView,
    AccountView,
    TemplateEdit,
    RangePicker,
    MsgType,
    TextView,
    AudiotView,
    VideoView,
    ImageView,
    ImageText
  },

  created () {
    const { accountId } = this.$route.query
    if (accountId) {
      this.accountId = accountId
    }
    this.getTagList()
  },
  mounted () {
    if (this.accountId) {
      this.fillForm()
    }
  },
  data () {
      return {
          msgType,
           form: this.$form.createForm(this, { name: 'coordinated' }),
           city: Object.freeze(city),
           fieldNames: { label: 'shortName', value: 'shortName', children: 'childList' },
           accountId: '',
           customerList: [],
           content: [],
           tagList: [],
           massData: {
             type: 1,
             ChangeCity: [],
             repeatSend: true,
             selectSubscribeTime: [],
             msgType: 1,
             selectSex: '',
             selectTag: [], // 已选择的标签
             media_id: ''
           }
      }
  },
  methods: {
    moment,
    // 选择图文
    selectImgText (item) {
      console.log(item, 'itemitem')
      const imagetextlist = item.content.news_item.map(item => {
        item.url = item.thumb_url
        return item
      })
      this.content = [{ imagetextlist, type: this.massData.msgType }]
      this.massData.media_id = item.media_id
    },
    // 选择视频
    changeVideo (item) {
      this.content = [{ ...item, type: this.massData.msgType }]
      this.massData.media_id = item.media_id
    },
    // 选择音频
    changeAudit (item) {
      console.log(item, 'item')
      this.content = [{ ...item, type: this.massData.msgType }]
      this.massData.media_id = item.media_id
    },
    // 确定文字
    changeText (item) {
      this.content = [{ type: this.massData.msgType, content: item }]
    },
    // 确定选择图片
    selectImg (item) {
      console.log(item)
      this.content = [{ ...item, type: this.massData.msgType }]
    },
    // 内容类型切换
    chooseType (value) {
      console.log(value.type)
      const { type } = value
      this.massData.msgType = type
      this.content = [{ type }]
    },
    // 提交表单
    handleSubmit (e) {
        e.preventDefault()
        this.form.validateFields((err, values) => {
            if (!err) {
                const { sendTime, phone } = values
                const { msgType, type, selectTag, repeatSend, selectSubscribeTime, selectSex, ChangeCity: [selectProvince, selectCity] } = this.massData

                let $content

                if (msgType === 3) {
                  $content = JSON.stringify(repalceTag.dealListPost(JSON.parse(JSON.stringify(this.content))))
                } else {
                  $content = JSON.stringify([{ media_id: this.massData.media_id }])
                }

                const $par = {
                    accountId: this.accountId,
                    sendTime: moment(new Date(sendTime._d).getTime()).format('YYYY-MM-DD hh:mm:ss'),
                    content: $content,
                    selectSex,
                    type,
                    selectTag: selectTag.join(','),
                    msgType,
                    selectSubscribeTime: selectSubscribeTime.join(','),
                    phone,
                    selectProvince,
                    repeatSend: repeatSend ? 1 : 0,
                    selectCity
                }
                this.postMass($par)
            }
        })
    },
    // 增加高级群发
    async postMass ($par) {
      const { data, code } = await postMass($par)
      if (code === 200) {
          console.log(data)
          this.$message.success('添加成功')
          this.$router.replace('/information/mass')
      }
    },
    // 反填form表单
    fillForm () {
        const { type, label, content, selectCity, selectProvince, selectSex, sendTime, repeatSend } = this.massData
        this.form.setFieldsValue({ label, type, content, selectCity, selectProvince, selectSex, sendTime, repeatSend })
    },
    // 存储已选择的标签
    handelTag (item) {
      if (this.massData.selectTag.includes(item.id)) {
        this.massData.selectTag = this.massData.selectTag.filter(id => {
          return id !== item.id
        })
      } else {
        this.massData.selectTag.push(item.id)
      }
    },
    // 查询公众号标签列表
    async getTagList () {
      this.tagList = await this.$store.getters.tagList
    },
    // 群发时间
    changeDate (dates) {
      console.log(dates)
      this.selectSubscribeTime = dates
    },
    changeContent (content) {
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
    margin-left: 120px;
    margin-bottom: 10px;
}

.tagList {
    .tagList-item {
        display: inline-block;
        padding: 2px 9px;
        margin-bottom: 10px;
        line-height: 20px;
        background: #fafafa;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
        color: rgba(0,0,0,.62);
        cursor: pointer;
        margin-right: 10px;

    }
}

.tag-active {
  background:#3b74ff !important;
  color: #fff !important;
}

.phone-input {
  width: 200px;
  margin-right: 10px;
}

.content {
    width: 700px;
    // width: 690px;
    // min-height: 640px;
    // max-height: 1280px;
    // padding: 20px 15px;
    // background-color: #fff;
    // border: 1px solid #d8d8d8;
    // overflow-y: auto;
    // overflow-x: hidden;
}
// 图文
.image-text {
    position: relative;
    border: 1px dashed #d8d8d8;
    background-color: #fff;
    height: 150px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: 130px;
    margin-top: 10px;
    margin-bottom: 10px;
}

</style>
