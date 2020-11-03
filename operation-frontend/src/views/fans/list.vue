<template>
  <div class="FansList">
    <div class="FansList-left" >
      <div class="FansList-left-label">
        粉丝筛选
      </div>
      <div class="FansList-left-account">
        <div class="label">
          选择公众号
        </div>
        <div class="account" @click="openChooseAccount">
          <div>
            <img :src="account && account.headImage" alt="">
            <p class="textOverflow" style="width: 149px">{{ account && account.nickName }}</p>
          </div>
          <a-icon type="caret-down" />
        </div>
      </div>
      <div class="FansList-left-nickName">
        <a-input v-model="form.label" placeholder="请输入粉丝昵称或备注"></a-input>
      </div>
      <div class="FansList-left-gender">
        <div class="label">
          性别
        </div>
        <a-radio-group v-model="form.sex" button-style="solid">
          <a-radio-button value="">
            全部
          </a-radio-button>
          <a-radio-button value="1">
            男
          </a-radio-button>
          <a-radio-button value="0">
            女
          </a-radio-button>
          <a-radio-button value="2">
            未知
          </a-radio-button>
        </a-radio-group>
      </div>
      <div class="FansList-left-time">
        <div class="label">关注时间</div>
        <div>
          <a-date-picker
            :key="resetKey"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
            placeholder="开始时间"
            @change="(tiem, timeString ) => form.startTime = timeString"
          />
          <p>至</p>
          <a-date-picker
            :key="resetKey + 1"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
            placeholder="结束时间"
            @change="(tiem, timeString ) => form.endTime = timeString"
          />
        </div>
      </div>
      <div class="FansList-left-from">
        <div class="label">
          粉丝来源
        </div>
        <a-select v-model="form.scene" placeholder="请输入来源类型" style="width: 100%" >
          <a-select-option :value="item.value" v-for="item in AccountFromType" :key="item.index">
            {{ item.label }}
          </a-select-option>
        </a-select>
      </div>
      <div class="FansList-left-city">
        <div class="label">
          地域
        </div>
        <a-cascader
          :key="resetKey + 2"
          style="width: 100%"
          :options="city"
          placeholder="请选择地区"
          @change="onChangeCity"
          :fieldNames="fieldNames"
        />
      </div>

      <div class="FansList-left-tag">
        <div class="label">
          标签
        </div>
        <a-row class="tabg-list">
          <a-col :span="7" class="tabs-list-item" v-for="item in tagList" :key="item.id">
            <a-tag
              :color="form.tagId.includes(item.id)? 'pink': ''"
              class="cp"
              @click="pushTagId(item)"
            >
              {{ item.name }}
            </a-tag>
          </a-col>
        </a-row>
      </div>
      <div class="FansList-left-search">
        <a-button type="primary" @click="searchFansList">确定搜索</a-button>
        <a-button @click="resetCondition">重置条件</a-button>
      </div>
    </div>
    <div class="FansList-right">
      <div class="header">
        <div class="fans-total">
          共有<span>{{ pagination.total }}</span> 个粉丝
          <a-tooltip placement="right">
            <template slot="title">
              粉丝数量可能与微信后台不符，如需同步请手动点击【同步粉丝】。同步粉丝和打标签等任务完成后，可能需要10分钟左右才能刷新到页面上，请耐心等待。
            </template>
            <a-icon type="question-circle" />
          </a-tooltip>

        </div>
        <div class="action">
          <a-button type="primary" icon="tag" @click="addTag">打标签{{ selectedRowKeys.length? selectedRowKeys.length : '' }}</a-button>
          <a-button type="primary" icon="reload" @click="awaitFans">同步粉丝</a-button>
        </div>
      </div>
      <div class="table">
        <a-card>
          <a-table
            :pagination="pagination"
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            :columns="columns"
            :data-source="fansList"
            :loading="loading"
            @change="handleTableChange"
          >
            <template slot="sex" slot-scope="text, record">
              <a-tag :color="record.sex === 1 ? 'blue' : record.sex === 0 ? 'pink' : ''" >  {{ record.sex === 1 ? '男' : record.sex === 0 ? '女' : '—' }} </a-tag>
            </template>
            <template slot="time" slot-scope="text, record">
              {{ record.subscribeTime | filtersTime }}
            </template>
            <template slot="tags" slot-scope="text, record">
              <template v-if="record.tags">
                <a-tag
                  closable
                  @close="batchDeletdTag(record, tag)"
                  v-for="tag in record.tags.split(',')"
                  :key="tag"> {{ transferTag(tag) }}
                </a-tag>
              </template>
            </template>

            <template slot="action" slot-scope="text, record" >
              <a @click="curFansInfo = record; fansVisable=true; editRemark = false">详情</a>
            </template>
          </a-table>
        </a-card>
      </div>
    </div>
    <account-list @select="selectAccount" :maskClosable="true" ref="account-list"/>
    <tag-list @selectTag="selectTag" ref="tag-list"/>

    <!-- 粉丝个人信息 -->
    <a-modal v-model="fansVisable" >
      <template slot="title">
        {{ curFansInfo.nickName }}
        <a-icon
          :style="{color: curFansInfo.sex === 1 ? 'rgb(24, 144, 255)' : '#eb2f96'}"
          :type="curFansInfo.sex === 1 ? 'man' : 'woman'"
        />
        <!--
          -->
      </template>
      <div class="fans-box">
        <img class="fans-ava" :src="curFansInfo.headImgUrl" alt="">
        <div class="fans-remark">
          <template v-if="editRemark">
            备注： <a-input placeholder="按下Enter键确定修改" class="fans-remark-input" @pressEnter="fansRemark"></a-input>
          </template>
          <template v-else>
            备注： {{ curFansInfo.remark }} <a-icon type="edit" @click="editRemark = true"></a-icon>
          </template>
        </div>
        <div class="fans-city df">
          <div class="label">  地域 :</div> {{ curFansInfo.province }} - {{ curFansInfo.city }}
        </div>
        <div class="fans-subscribe df">
          <div class="label">  渠道来源 :</div> {{ curFansInfo.subscribe | filtersSubscribe }}
        </div>
        <div class="fans-subscribeTime df">
          <div class="label">  关注时间 :</div>{{ curFansInfo.subscribeTime }}
        </div>
        <div class="fans-lastInactiveTime df">
          <div class="label">  最后活跃时间 :</div> {{ curFansInfo.lastInactiveTime }}
        </div>
        <div class="fans-tag df">
          <div class="label"> 标签 :</div>
          <template v-if="curFansInfo.tags && !!curFansInfo.tags.length">
            <a-tag :color="curFansInfo.sex === 1 ? 'cyan' : 'pink' " v-for="item in curFansInfo.tags.split(',')" :key="item">
              {{ transferTag(item) }}
            </a-tag>
          </template>

        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
/*eslint-disable */
import { getAccount } from '@/api/publicAccount'
import { getFansList, awaitFans, batchAddTag, fansRemark, batchDeletdTag } from '@/api/fans'
import { getsubscribeScene } from '@/api/operation'
import { city } from '@/utils/city'
import { cascader } from 'ant-design-vue'
import moment from 'moment'
import AccountList from '@/components/AccountList'
import TagList from '@/components/TagList'
const columns = [
  {
    title: '粉丝',
    dataIndex: 'nickName',
    key: 'nickName',
    width: 220,
    scopedSlots: { customRender: 'nickName' }
  },
  {
    title: '性别',
    dataIndex: 'sex',
    key: 'sex',
    scopedSlots: { customRender: 'sex' }
  },
  {
    title: '关注时间',
    dataIndex: 'time',
    key: 'time',
    scopedSlots: { customRender: 'time' }
  },
  {
    title: '标签',
    dataIndex: 'tags',
    key: 'tags',
    scopedSlots: { customRender: 'tags' }
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  name: 'FansList',
  components: {
    ACascader: cascader,
    AccountList,
    TagList
  },
  filters: {
    filtersTime (timeStamp) {
      const date = new Date(timeStamp)
      const year = date.getFullYear()
      let month = date.getMonth() + 1
      let day = date.getDate()
      const hours = date.getHours()
      const minutes = date.getMinutes()
      const seconds = date.getSeconds()
      month = month < 10 ? '0' + month : month
      day = day < 10 ? '0' + day : day
      return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
    },
    filtersSubscribe (subscribe) {
      switch (subscribe) {
        case 1:
          return '公众号搜索'
        case 2:
          return '公众号迁移'
        case 3:
          return '名片分享'
        case 4:
          return '扫描二维码'
        case 5:
          return '图文页内名称点击'
        case 6:
          return '图文页右上角菜单'
        case 7:
          return '支付后关注'      
        case 8:
          return '微信广告'      
        case 9:
          return '其他'      
        default:
          break;
      }
    }
 },
  computed: {
    account () {
      return this.publicAccountList.filter(item => {
        return item.id === this.accountId
      })[0]
    }
  },
  watch: {
    accountId () {
     this.getFansList()
    }
  },
  async created () {
    await this.getAccount()
    await this.getTagList()
    this.getsubscribeScene()
  },
  data () {
    return {
      editRemark: false, // 是否正在编辑粉丝备注
      fansVisable: false, // 粉丝消息弹窗
      curFansInfo: {}, //当前粉丝信息
      columns,
      loading: false,
      accountId: '',
      publicAccountList: [],
      AccountFromType: [],
      tagList: [],
      fansList: [],
      city: Object.freeze(city),
      fieldNames: { label: 'shortName', value: 'shortName', children: 'childList' },
      form: {
        pageSize: 10,
        startTime: '',
        endTime: '',
        sex: '',
        label: '',
        province: '',
        city: '',
        scene: '',
        tagId: [] // 当前选中得tagid
      },
      pagination: {
        total: 0,
        pageSize: 10,
        current: 1,
        page: 1
      },
      selectedRows: [],
      selectedRowKeys: [],
      resetKey: 0
    }
  },
  methods: {
    moment,
    // 删除粉丝标签
    async batchDeletdTag (record, tag) {
      const $par = {
        accountId: record.accountId,
        openIds: record.openId,
        wxId: tag,
      }
      const {data, code }  = await batchDeletdTag($par)
      if (code === 200) {
        this.$message.success('删除成功')
      }
    },
    // 转换标签格式
    transferTag (id) {
      if (!id) {
        return ''
      }
      return this.tagList && this.tagList.find( tag => {
        return Number(tag.wxId) === Number(id) 
      }).name
    },
    // 给粉丝添加备注
    async fansRemark (item) {
      const $par = {
        accountId: this.curFansInfo.accountId,
        openId: this.curFansInfo.openId,
        remark: item.target.value
      }
      const { data, code } = await fansRemark($par)
      if (code === 200) {
        this.curFansInfo.remark = item.target.value
        this.$message.success('修改成功')
        this.editRemark = false
      }
    },
    // 打标签
    selectTag (record) {
      const { selectedRows, accountId } = this
        record.checkboxList.forEach(item => {
          const $par = {
            openIds: selectedRows.map(row => row.openId).join(','),
            accountId,
            wxId: item
          }
          this.batchAddTag($par, record.cb)
        })
    },
    async batchAddTag ($par, cb) {
      const { data, code } = await batchAddTag($par)
      if (code === 200) {
        this.$message.success('打标签成功')
        this.searchFansList()
        cb()
      }
    },
    // 打开标签弹窗
    addTag () {
      if (this.selectedRowKeys.length === 0) {
        this.$message.error('请先勾选粉丝')
        return
      }
      this.$refs['tag-list'].openModal(this.accountId)
    },
    // 同步粉丝
    async awaitFans () {
     const { code } = await awaitFans({ accountId: this.accountId })
     if (code === 200) {
       this.$message.success('粉丝信息正在从后台获取')
     }
    },
    selectAccount (account) {
      this.accountId = account[0].id
    },
    // 选择公众号列表
    openChooseAccount () {
      this.$refs['account-list'].openModal()
    },
    // 重置条件
    resetCondition () {
      this.form = this.$options.data().form
      this.resetKey++
      this.searchFansList()
    },
    // 保存已经选中得id
    pushTagId (item) {
      this.form.tagId.push(item.id)
    },
    // 搜索粉丝列表
    searchFansList () {
      this.selectedRowKeys = []
      this.selectedRows = []
      this.getFansList()
    },
    // 翻页
    handleTableChange (pagination, filters, sorter) {
      const { current } = pagination
      this.pagination.current = current
      this.pagination.page = current
      this.selectedRows = []
      this.selectedRowKeys = []
      this.getFansList()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRows = selectedRows
      this.selectedRowKeys = selectedRowKeys
    },
    // 选择城市
    onChangeCity (item) {
      this.form.province = item[0]
      this.form.city = item[1]
    },
    // 查询公众号粉丝列表
    async getFansList () {
      this.loading = true
      const { tagId } = this.form
      const { data, code } = await getFansList(this.accountId, { ...this.form, ...this.pagination, tagId: tagId.join(',') })
      if (code === 200) {
        this.loading = false
        const { records, total } = data
        this.fansList = records
        this.pagination.total = total
      }
    },
    // 查询标签列表
    async getTagList () {
      this.tagList = await this.$store.dispatch('tag/getTagList', { accountId: this.accountId })
    },
    // 粉丝来源类型
    async getsubscribeScene () {
      const { data, code } = await getsubscribeScene()
      if (code === 200) {
        this.AccountFromType = data
      }
    },
    // 公众号列表
    async getAccount () {
      const { data, code } = await getAccount()
      if (code === 200) {
        this.publicAccountList = Object.freeze(data)
        this.accountId = this.publicAccountList[0].id
      }
    }
  }
}
</script>
<style scoped lang="less">

.FansList {
  width: 100%;
  display: flex;
  position: relative;
  &-left {
    position: fixed;
    top: 85px;
    width: 240px;
    background: #fcfcfc;
    border-right: 1px solid #e9e9e9;
    height: calc(100vh - 85px);
    overflow-x: hidden;
    overflow-y: auto;
    &-label {
      height: 48px;
      display: flex;
      justify-content: center;
      align-items: center;
      border-bottom: 1px solid #d8d8d8;
      font-weight: bold;
      color: #000;
    }
    &-account {
      padding: 0 7px;
      border-bottom: 1px solid #d8d8d8;
      padding-bottom: 20px;
      padding-top: 25px;
      .label {

      }
      .account {
        height: 48px;
        border: 1px solid #d8d8d8;
        -webkit-box-pack: justify;
        -ms-flex-pack: justify;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 10px;
        cursor: pointer;
        div {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          img {
            width: 30px;
            height: 30px;
            object-fit: cover;
          }
          p {
            margin-bottom: 0;
            font-size: 16px;
            margin-left: 5px;
          }
        }

      }
    }
    &-nickName {
      padding: 20px 7px;
    }
    &-gender {
      padding: 0 7px;
      .label {
        margin-bottom: 10px;
      }
    }
    /deep/ .ant-radio-button-wrapper {
      margin-right: 2px;
    }
    &-time {
      padding: 20px 7px;
      /deep/ .ant-calendar-picker {
        width: 100%;
      }
      p {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 5px;
        margin-bottom: 5px;
      }
    }
    &-from {
      padding: 20px 7px;
      .label {
        margin-bottom: 10px;
      }
    }
    &-city {
      padding: 0 7px;
    }

    &-tag {
      padding: 20px 7px;
      margin-bottom: 70px;
      .tabs-list-item {
        margin-bottom: 5px;
      }
    }

    &-search {
      border-top: 1px solid #d8d8d8;
      border-bottom: 1px solid #d8d8d8;
      height: 57px;
      display: flex;
      justify-content: center;
      align-items: center;
      position: fixed;
      bottom: 0;
      width: 240px;
      background-color: #fcfcfc;
      z-index: 1;
      .ant-btn {
        margin-right: 10px;
      }
    }
  }

  &-right {
    width: calc(~"100% - 240px");
    position: absolute;
    left: 260px;
    padding: 0px 0px;
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      .fans-total {
        font-size: 16px;
        span {
          color: palevioletred;
        }
      }
      .ant-btn {
        margin-right: 10px;
      }
    }
    .table {
      // margin-bottom: 100px;
    }
  }
}

.label {
  margin-bottom: 10px;
}
.textOverflow {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  word-break: break-all;
}


// 粉丝信息
.fans-box {
  width: 100%;
  .label {
    width: 100px;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 0;
    margin-right: 10px;
  }
  .fans-ava {
    width: 60px;
    height: 60px;
    display: block;
    border-radius: 50%;
    object-fit: cover;
    margin: 0 auto;
  }
  .fans-remark {
    height: 32px;
    margin-top: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 30px;
    .fans-remark-input {
      width: 150px
    }
  }

  .df {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-bottom: 10px;
  }
}


</style>
