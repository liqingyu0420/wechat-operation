<template>
  <div class="EditPush">
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
          <a-form-item label="时间设置" >
            <div >
              <a-input-number
                :min="1"
                v-decorator="['pushTimer', { rules: [{ required: true, message: '请选择时间设置' }] }]"
              >
              </a-input-number>
              <a-select
                v-decorator="['pushTimerType', { rules: [{ required: true, message: '请选择时间设置' }], options: {initialValue: 'h'} }]"
                style="width: 80px">
                <a-select-option value="h">
                  小时
                </a-select-option>
                <a-select-option value="m">
                  分钟
                </a-select-option>
              </a-select>
            </div>
          </a-form-item>
          <a-form-item label="时间设置" >
            <a-time-picker
              v-decorator="['startTime', { rules: [{ required: true, message: '请选择开始时间' }] }]"
              placeholder="请选择开始时间"
              @change="onchangeStartTime"
              format="HH:mm"
            />
            <!--   :value="startTime.momentStartTime" -->
            至
            <!--  :value="endTime.momentStartTime" -->
            <a-time-picker
              v-decorator="['endTime', { rules: [{ required: true, message: '请选择结束时间' }] }]"
              placeholder="请选择结束时间"
              format="HH:mm"
              :disabled="!startTime.timeString && !$route.query.id"
              :disabledHours="mulEndTimeDisabled.disabledHours"
              :disabledMinutes="mulEndTimeDisabled.disabledMinutes"
              @change="onchangeEndTime"
            />
          </a-form-item>
          <a-form-item label="触发条件" >
            <!-- @change="onChange" -->
            <a-checkbox-group
              v-decorator="['pushTrigger', { rules: [{ required: true, message: '请选择结束时间' }] }]"
            >
              <a-checkbox value="a">
                关注公众号
              </a-checkbox>
              <a-checkbox value="b">
                发送消息给公众号
              </a-checkbox>
              <a-checkbox value="c">
                点击菜单
              </a-checkbox>
            </a-checkbox-group>
          </a-form-item>
          <a-form-item label="推送限制" >
            每个粉丝每天接收智能推送不超过&nbsp;
            <a-input-number
              v-decorator="['pushLimit', { rules: [{ required: true, message: '请选择推送限制' }] }]"
              :min="1"
            />条
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
import { getPushDetail, putPush } from '@/api/interaction'
import { repalceTag } from '@/utils/global'
import moment from 'moment'
export default {
  name: 'EditPush',
  components: {
    TemplateGrail,
    PreView,
    TemplateEdit
  },
  computed: {
      mulEndTimeDisabled () {
        console.log(this.startTime.timeString.split(':').map(item => Number(item)))
      const [startHours, startMinutes] = this.startTime.timeString.split(':').map(item => Number(item))
      const [endHours] = this.endTime.timeString.split(':').map(item => Number(item))
      return {
        disabledHours () {
          return new Array(startHours).fill().map((item, index) => index)
        },
        disabledMinutes () {
          if (startHours < endHours) {
            return []
          } else {
            return new Array(startMinutes).fill().map((item, index) => index)
          }
        }
      }
    }
  },
  created () {
      this.getPushDetail()
  },
  data () {
      return {
          form: this.$form.createForm(this, { name: 'coordinated' }),
          editReplyData: {},
          content: [],
          startTime: {
            momentStartTime: '',
            timeString: ''
          },
          endTime: {
            momentStartTime: '',
            timeString: ''
          }
      }
  },
  methods: {
    moment,
    // 结束时间改变
    onchangeEndTime (times, timeString) {
      this.endTime.momentStartTime = moment(timeString, 'HH:mm')
      this.endTime.timeString = timeString
    },
    // 开始时间改变
    onchangeStartTime (times, timeString) {
      this.endTime = {
            momentStartTime: '',
            timeString: ''
      }
      if (!timeString) {
        this.startTime.timeString = ''
        this.startTime.momentStartTime = ''
        return
      }
      this.startTime.timeString = timeString
      this.startTime.momentStartTime = moment(timeString, 'HH:mm')
    },
    // 提交表单
    handleSubmit (e) {
      e.preventDefault()
      if (this.content.length === 0) {
        this.$message.error('请填写content的值')
        return
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          const { pushType, pushLimit, pushTimer, pushTrigger, pushTimerType } = values
          const { startTime: { timeString: startTime }, endTime: { timeString: endTime } } = this
          console.log('Received values of form: ', values)
          // this.putPush(pushType)
          console.log(pushType, pushLimit, pushTimer, pushTrigger)
          const refers = ['a', 'b', 'c']
          const pushTriggerResult = []
          refers.forEach(refer => {
            const rederIndex = pushTrigger.indexOf(refer)
            if (rederIndex !== -1) {
              pushTriggerResult.push(1)
            } else {
              pushTriggerResult.push(0)
            }
          })
          const resultContent = repalceTag.dealListPost(JSON.parse(JSON.stringify(this.content)))
          const $par = {
            accountId: this.$route.query.accountId,
            pushLimit,
            pushTimer: pushLimit + pushTimerType,
            pushType,
            pushTrigger: pushTriggerResult.join(''),
            quiet: startTime + '-' + endTime,
            content: JSON.stringify(resultContent),
            id: this.$route.query.id
          }
          console.log($par)
          this.putPush($par)
        }
      })
    },
    // 发布二维码
    async putPush ($par) {
      const { data, code } = await putPush($par)
      if (code === 200) {
        console.log(data)
      }
    },

    // 编辑去内容改变
    changeContent (item) {
      this.content = item
    },

    //   获取详情
    async getPushDetail () {
      const { accountId, id } = this.$route.query
      const { data, code } = await getPushDetail({ accountId }, id)
      if (code === 200) {
          console.log(data)
          this.editReplyData = data
          this.content = JSON.parse(data.content) || []
          this.fillForm(data)
      }
    },
    // 反填表格
    fillForm (record) {
      console.log(record, 'recordrecord')
      const { pushType, quiet, pushTimer, pushLimit, pushTrigger } = record || {
        pushTimerType: 'h',
        startTime: '',
        endTime: '',
        pushType: 0
      }

      const pushTimerType = pushTimer.substr(pushTimer.length - 1, 1)
      const pushTimerResult = pushTimer.substr(0, pushTimer.length - 1)
      const pushTriggerResult = []
      console.log(String(pushTrigger).split(''))
      String(pushTrigger).split('').forEach((item, index) => {
        if (index === 0) {
          pushTriggerResult[index] = item * 1 === 1 ? 'a' : ''
        } else if (index === 1) {
          pushTriggerResult[index] = item * 1 === 1 ? 'b' : ''
        } else if (index === 2) {
          pushTriggerResult[index] = item * 1 === 1 ? 'c' : ''
        }
      })

      console.log(pushType, pushTriggerResult)
      this.startTime.timeString = quiet.split('-')[0]
      this.endTime.timeString = quiet.split('-')[1]
      const $par = {
        pushType,
        pushTimerType,
        pushTimer,
        pushTimerResult,
        pushLimit,
        pushTrigger: pushTriggerResult,
        startTime: moment(`${quiet.split('-')[0]}`, 'hh-mm'),
        endTime: moment(`${quiet.split('-')[1]}`, 'hh-mm')
      }
      console.log($par)
      this.form.setFieldsValue({ ...$par })
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

        /deep/ .ant-time-picker {
          width: 170px;
        }
</style>
