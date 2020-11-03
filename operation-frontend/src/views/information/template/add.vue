<template>
  <div class="template">
    <template-grail>
      <template slot="left">
        <a-form :form="form" :label-col="{ span:3 }" :wrapper-col="{ span: 20 }" @submit="handleSubmit">
          <a-form-item label="选择公众号">
            <account-view :accountId="accountId" />
          </a-form-item>
          <a-form-item label="消息名称">
            <a-input
              :maxLength="20"
              placeholder="请填写消息名称(仅供备注,不会发给粉丝),限20字符"
              v-decorator="['label', { rules: [{ required: true, message: '请填写消息名称' }] }]"
            />
          </a-form-item>
          <div class="template-edit-style">
            <a-form-item label="选择模板">
              <a-select
                style="width: 120px"
                v-model="templateData.templateId"
                @change="handleChange">
                <a-select-option :value="item.templateId" v-for="(item,index) in mouldList" :key="index">
                  {{ item.title }}
                </a-select-option>
              </a-select>
              <div class="condition-filter" style="marginLeft: -0px">
                <div class="no-mouldList" v-if="mouldList.length === 0">
                  <a-icon type="message" />
                  <div>
                    暂无模板消息, 请去公众号添加
                  </div>
                </div>
                <a-form-item v-else>
                  <!-- 模板数据 -->
                  <p class="edit-item" contenteditable="false" v-for="(item,index) in mouldData" :key="index">
                    <span v-if="item.title">{{ item.title }}: </span><span :style="{color:item.color}" v-html="item.value"></span>
                    <a-popover
                      class="popover"
                      trigger="click"
                      v-model="isShowPopover[index]"
                      @visibleChange="visibleChange"
                    >
                      <template slot="content" v-if="currentShowIndex === index" >

                        <div class="popover-content">
                          <div class="edit-content" :style="{color:item.color}">
                            <!-- @change="editChange" -->
                            <div-edit ref="edit" class="medium-editor-form" @change="editChange"></div-edit>
                          </div>
                          <div class="popover-item">
                            <span>字体颜色:</span>
                            <em :class="['color-item',{active:item.color==='#000000'}]" style="background-color: #000000;" @click="item.color='#000000'"></em>
                            <em :class="['color-item',{active:item.color==='#bf2828'}]" style="background-color: #bf2828;" @click="item.color='#bf2828'"></em>
                            <em :class="['color-item',{active:item.color==='#e9a844'}]" style="background-color: #e9a844;" @click="item.color='#e9a844'"></em>
                            <em :class="['color-item',{active:item.color==='#4a75f6'}]" style="background-color: #4a75f6;" @click="item.color='#4a75f6'"></em>
                            <em :class="['color-item',{active:item.color==='#ad2ed8'}]" style="background-color: #ad2ed8;" @click="item.color='#ad2ed8'"></em>
                            <em :class="['color-item',{active:item.color==='#999999'}]" style="background-color: #999999;" @click="item.color='#999999'"></em>
                          </div>
                          <div class="popover-item">
                            <span>占位符</span>
                            <span class="user-nick-name" @click="innerTag('粉丝昵称')">粉丝昵称</span>
                            <a-popover
                              placement="bottom"
                              trigger="click"
                            >
                              <div slot="content">
                                <emoji @clickEmoji="innerEmoji"></emoji>
                              </div>
                              <span class="user-nick-name">表情</span>
                            </a-popover>
                          </div>
                          <div class="save-btn">
                            <a-button @click="saveEditContent" size="small" type="primary">确定</a-button>
                          </div>
                        </div>
                      </template>
                      <a-icon contenteditable="false" type="edit" @click="handleShowPopClick(index)"/>
                    </a-popover>
                  </p>
                </a-form-item>

              </div>
            </a-form-item>
            <a-form-item label="点击跳转">
              <a-radio-group
                v-model="templateData.linkType"
                @change="changeLinkType"
              >
                <a-radio :value="0">
                  链接
                </a-radio>
                <a-radio :value="1">
                  小程序
                </a-radio>
              </a-radio-group>
              <div class="condition-filter" style="marginLeft: -0px">
                <a-input
                  v-if="templateData.linkType === 0"
                  placeholder="非必填,输入跳转链接，且必须以http://或http://开头"
                  v-decorator="['linkUrl', { rules: [{ required: true, message: '请填写跳转地址' }] }]"
                />
                <template v-else>
                  <a-input
                    v-model="templateData.linkAppId"
                    placeholder="填写小程序AppID，跳转小程序 需与当前公众号绑定关联关系"
                  />
                  <a-input
                    placeholder="填写小程序路径，例如：pages/index"
                    v-model="templateData.linkAppPage"
                  />
                </template>
              </div>
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
                v-model="templateData.type"
              >
                <a-radio :value="1">
                  按条件筛选粉丝
                </a-radio>
                <a-radio :value="0">
                  全部粉丝
                </a-radio>
              </a-radio-group>
            </a-form-item>
            <div class="condition-filter" v-if="templateData.type === 1">
              <a-form-item label="性别" >
                <a-radio-group
                  v-model="templateData.selectSex"
                >
                  <a-radio value="">
                    全部性别
                  </a-radio>
                  <a-radio :value="1">
                    仅男性粉丝
                  </a-radio>
                  <a-radio :value="2">
                    仅女性粉丝
                  </a-radio>
                  <a-radio :value="0">
                    未知性别
                  </a-radio>
                </a-radio-group>
              </a-form-item>
              <a-form-item label="关注时间" >
                <range-picker :default-value="templateData.selectSubscribeTime" @changeDate="changeDate"/>
              </a-form-item>
              <a-form-item label="地域">
                <a-cascader
                  v-model="templateData.ChangeCity"
                  style="width: 100%"
                  :options="city"
                  placeholder="请选择地区"
                  :fieldNames="fieldNames"
                />
              </a-form-item>
              <a-form-item label="标签">
                <div class="tagList">
                  <div
                    :class="['tagList-item', templateData.selectTag.includes(tag.id) ? 'tag-active' : '' ]"
                    @click="handelTag(tag)"
                    v-for="tag in tagList"
                    :key="tag.id">
                    {{ tag.name }}
                  </div>
                </div>
              </a-form-item>
            </div>
          </div>
          <a-form-item :wrapper-col="{ span: 12, offset: 10 }">
            <a-button type="primary" html-type="submit">
              提交
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template slot="right">
        <pre-view :list="mouldData" :task-title="templateData.templateName"/>
      </template>
    </template-grail>
  </div>
</template>

<script >
/*eslint-disable */
import { getTemplateTask, postTemplate, getTemplateDetail } from '@/api/information'
import { getTagList } from '@/api/fans'
import TemplateGrail from '@/components/TemplateGrail'
import RangePicker from '@/components/RangePicker'
import PreView from '@/components/PreView'
import AccountView from '@/components/AccountView'
import moment from 'moment'
import TemplateEdit from '@/components/TemplateEdit'
import { cascader } from 'ant-design-vue'
import { city } from '@/utils/city'
import DivEdit from '@/components/DivEdit'

import { repalceTag } from '@/utils/global'
export default {
  name: 'Template',
  components: {
    ACascader: cascader,
    TemplateGrail,
    PreView,
    AccountView,
    TemplateEdit,
    RangePicker,
    DivEdit,
    emoji: () => import('@/components/emoji')
  },
  async created () {
    const { accountId, id } = this.$route.query
    if (accountId) {
      this.accountId = accountId
      this.id = id
      await this.getTemplateTask()
      if (id) {
        await this.getTemplateDetail()
      }
      if (!id) {
        await this.handleChange(id)
      }
    }
    this.getTagList()
  },
  data () {
      return {
           form: this.$form.createForm(this, { name: 'coordinated' }),
           city: Object.freeze(city),
           fieldNames: { label: 'shortName', value: 'shortName', children: 'childList' },
           sendType: 2,
           customerList: [],
           isShowPopover: {}, // 显示
           currentEditContent: '', // 编辑好的内容
           currentShowIndex: 0,
           content: [],
           tagList: [],
           mouldList: [],
           mouldData: [],
           templateData: {
             templateId: '',
             linkType: 0,
             type: 0,
             selectTag: [],
             selectSubscribeTime: [], // 关注时间
             selectSex: '',
             sendTime: '',
             linkAppId: '',
             linkAppPage: '',
             ChangeCity: [],
             linkUrl: ''
           }
      }
  },
  methods: {
    moment,
    // 选择群发时间
    changeSendTime (date, dateString) {
      console.log(dateString)
      this.templateData.sendTime = dateString
    },
    // 确定模板任务的内容
    saveEditContent () {
       if (this.mouldData[this.currentShowIndex]) {
        this.mouldData[this.currentShowIndex].value = JSON.parse(JSON.stringify(this.currentEditContent))
      }
      this.isShowPopover[this.currentShowIndex] = false
      this.handleAfterLeave()
    },
    // 提交表单
    handleSubmit (e) {
        e.preventDefault()
        this.form.validateFields((err, values) => {
            if (!err) {
                const { label } = values
                const { mouldData, 
                      templateData: { 
                        templateId, 
                        linkType,
                        linkAppId,
                        ChangeCity,
                        type, 
                        selectSex, 
                        selectSubscribeTime, 
                        selectTag, 
                        templateName, 
                        sendTime,
                        linkUrl,
                        linkAppPage
                        }} = this
                console.log(repalceTag, sendTime, 'repalceTagrepalceTag')
                // 转换符合要求的数据结构
                const $mouldData = Object.create(null)
                mouldData.forEach(item => {
                  $mouldData[item.key] = {
                      value: repalceTag.replaceContent(item.value),
                      color: item.color,
                      title: item.title
                    }
                })
                console.log($mouldData)

                const [selectProvince, selectCity] = ChangeCity || []
                console.log(selectProvince, selectCity)
                const $par = {
                    accountId: this.$route.query.accountId,
                    linkAppId,
                    sendTime,
                    label,
                    selectSex: '',
                    type,
                    linkAppPage,
                    linkUrl,
                    templateName,
                    selectProvince,
                    selectCity,
                    templateId,
                    templateData: JSON.stringify($mouldData),
                    selectSubscribeTime: selectSubscribeTime.join(','),
                    selectTag: selectTag.join(','),
                    linkType
                }
                console.log($par)
                this.postTemplate($par)
            }
        })
    },
    // 增加客服消息
    async postTemplate ($par) {
      const { data, code } = await postTemplate($par)
      if (code === 200) {
          console.log(data)
          this.$route.replace('/information/template')
      }
    },
    // 查询模板消息详情
    async getTemplateDetail () {
      const { data, code } = await getTemplateDetail({ msgId: this.id })
      if (code === 200) {
          console.log(data)
          this.customerList = data
          this.templateData = data
          // 转化模板任务格式
        const plainObj = JSON.parse(data.templateData)
        const moduleData = Object.keys(plainObj).map(item => {
            return {
                key: item,
                title: plainObj[item].title,
                color: plainObj[item].color,
                value: repalceTag.reResult(plainObj[item].value)
            }
        })
        this.mouldData = moduleData

          this.fillForm(data)
      }
    },
    // 反填form表单
    fillForm (data) {
        const { label, type, selectProvince,linkAppPage, selectCity, linkAppId, linkType, linkUrl, selectSex, selectSubscribeTime, sendTime, selectTag } = data
        this.templateData.type = type ? 1 : 0
        this.templateData.linkAppId = linkAppId
        this.templateData.linkAppPage = linkAppPage
        this.templateData.linkType = linkType
        this.templateData.selectSex = typeof selectSex === 'object' ? '' : selectSex
        this.templateData.ChangeCity = [selectProvince, selectCity]
        this.templateData.type = type ? 1 : 0,
        this.templateData.selectSubscribeTime = selectSubscribeTime.split(',')
        this.templateData.selectTag = selectTag.split(',')
        this.form.setFieldsValue({
                              label,
                              linkUrl,
                              sendTime,
                            })
    },
        // 存储已选择的标签
    handelTag (item) {
      if (this.templateData.selectTag.includes(item.id)) {
        this.templateData.selectTag = this.templateData.selectTag.filter(id => {
          return id !== item.id
        })
      } else {
        this.templateData.selectTag.push(item.id)
      }
    },
    // 切换linktype时清楚内容
    changeLinkType () {
      console.log('ehkjda');
      this.templateData.linkUrl = ''
    },
    // 关注时间
    changeDate (dates) {
      this.templateData.selectSubscribeTime = dates
    },
    // 查询模板任务
    async getTemplateTask () {
      const { data, code } = await getTemplateTask(this.accountId)
      if (code === 200) {
        console.log(data, 'getTemplateTaskgetTemplateTask')
        if (data.hasOwnProperty('errcode')) {
          this.$message.error(data.errmsg)
        } else {
          this.mouldList = data.templateList
        }
      }
    },
    // 选择模板
    handleChange (val ) {
         console.log('val::', val)
         console.log(this.mouldList[0].templateId, 'this.mouldList[0].templateIdthis.mouldList[0].templateId');
      if (!val) {
        val = this.mouldList[0].templateId
      }
      const $mouldData = this.mouldList.find(mi => mi.templateId === val)
      this.templateData.templateId = val

      this.templateData.templateName = $mouldData.title
      console.log($mouldData)
      if ($mouldData) {
        const { content, title } = $mouldData
        console.log('$mouldData:', content)
        this.form.mould_name = title || ''
        if (content) {
          this.mouldData = this.dealMouldData(content)
        }
      }
    },
     /** 处理模板数据 */
    dealMouldData (content) {
      const result = []
      const $ArrContent = content.split('\n')
      $ArrContent.map(ai => {
        const par = {
          color: '',
          value: ''
        }
        if (ai.includes('：')) { // 有中文的
          const childArr = ai.split('：')
          if (childArr && childArr.length) {
            console.log(childArr)
            par['title'] = childArr[0]
            par['key'] = childArr[1].match(/{{(\S*)\./)[1]
          }
        } else {
          par['title'] = ''
          par['key'] = ai.match(/{{(\S*)\./)[1]
        }
        if (par.key) {
          result.push(par)
        }
      })
      return result
    },
    // 查询公众号标签列表
    async getTagList () {
      const { data, code } = await this.$store.dispatch('tag/getTagList', { accountId: this.$route.query.accountId })
      if (code === 200) {
          console.log(data)
        this.tagList   = data
      }
    },
    handleShowPopClick (index) {
      this.currentShowIndex = index
      if (this.mouldData[index] && this.mouldData[index].value) {
        console.log(this.mouldData[index].value)
        setTimeout(() => {
          this.handleEditInnerDom(this.mouldData[index].value)
        }, 300)
      }
      console.log(this.isShowPopover, index)
    },
    /** 监听标题的值变化 */
    editChange (ev) {
      console.log('监听标题的值变化')
      this.currentEditContent = ev
      this.$forceUpdate()
    },
    /** 富文本添加标签 */
    innerTag (text) {
      this.handleEditInnerDom(`<span contenteditable="false" class="user-nick-name"><span>${text}</span></span>`)
    },
    /** 插入表情 */
    innerEmoji (emoji) {
      this.handleEditInnerDom(`${emoji}`)
      this.isShowAddEmoji = false
    },
    visibleChange () {
      console.log(213213)
    },
      /** 隐藏pop */
    handleAfterLeave () {
      this.currentEditContent = ''
      this.currentShowIndex = ''
    },
        /**
     * 添加内容
     * edit inner
     * */
    handleEditInnerDom ($dom, $fun = 'innerDom') {
      if ($dom) {
        this.$nextTick(() => {
          const editBox = this.$refs.edit
          if (editBox && editBox.length) {
            console.log('$dom:::', $dom)
            editBox[0][$fun]($dom)
          }
        })
      }
    },
    //   选择公众号
    selectAccount (account) {

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

.template-create-container {
  @borderColor: rgb(216, 216, 216);

  .template-create-box {
    padding-top: 40px;
  }

  .create-content {
    padding: 30px 0;
    background: rgb(249, 249, 249);
    border-top: 1px solid rgb(233, 233, 233);
    border-bottom: 1px solid rgb(233, 233, 233);
  }

  .create-btn {
    padding: 20px;
  }

  .random-one {
    padding: 10px;
    background: #fff;
    margin-top: 20px;
    max-width: 500px;

    .el-input {
      &:first-child {
        margin-bottom: 10px;
      }
    }
  }

  .icon-question {
    position: absolute;
    left: -20px;
    top: 50%;
    transform: translateY(-50%);
  }

  .tool-tip {
    max-width: 100px;
  }

  .input-with-select {
    width: 200px;
  }

  .create-box {
    display: flex;
    margin-bottom: 15px;

    .create-box-title {
      width: 120px;
      text-align: right;
    }
  }

  .other-info-box {
    padding-top: 20px;
  }
}

.mould-box {
  position: relative;
  padding: 24px;
  margin-top: 20px;
  margin-bottom: 20px;
  background: #ffffff;
  border: 1px solid #e9e9e9;
  max-width: 673px;
  min-height: 198px;
  color: #1a1a1a;

  p.edit-item {

  }

  i.el-icon-edit {
    color: #3b74ff;
    padding-left: 10px;
    cursor: pointer;
    position: relative;
  }
}

.fans-tag {
  display: inline-block;
  padding: 2px 9px;
  margin-bottom: 10px;
  line-height: 20px;
  background: #fafafa;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  color: rgba(0, 0, 0, 0.62);
  cursor: pointer;
  margin-right: 10px;
  margin-top: 10px;

  &.active {
    background: #3b74ff;
    border-color: #fff;
    color: #fff;
  }
}

.popover-content {
  position: relative;
  color: rgba(0, 0, 0, 0.65);
  box-sizing: border-box;
}
.save-btn{
  position: absolute;
  right: 0;
  bottom: 5px;
}
.color-item {
  position: relative;
  display: inline-block;
  vertical-align: middle;
  width: 20px;
  height: 20px;
  border-radius: 2px;
  margin-right: 10px;
  cursor: pointer;
  overflow: hidden;

  &.active {
    box-shadow: 0 0 5px 0 #3b74ff;
  }
}
.popover-item{
  margin-top: 10px;
}
//   padding: 5px;
.edit-content{
  width: 300px;
  height: 80px;

  overflow: auto;
  border: 1px solid #e9e9e9;
}
.medium-editor-form {
    width: 100%;
    height: 100%;
}

.tag-active {
  background:#3b74ff !important;
  color: #fff !important;
}

.no-mouldList {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 30px;
  color: #aaa !important;
}
</style>
