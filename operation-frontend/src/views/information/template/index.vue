<template>
  <div class="Template">
    <a-card>
      <div class="headers">
        <div class="headers-left">
          <query-type class="query-type" @queryType="queryType"/>
        </div>
        <div class="headers-right">
          <a-button type="primary" icon="plus" @click="$refs['account-list'].openModal()">新增模板消息</a-button>
        </div>
      </div>
      <a-table :columns="columns" :data-source="customerList">
        <template slot="type" slot-scope="text, record">
          {{ record.type === 1 ? '筛选' : '全部' }}
        </template>
        <template slot="accountName" slot-scope="text, record">
          <img class="accountName-img" :src="record.headImage" alt="">
          <span class="accountName-nickName">{{ record.nickName }}</span>
        </template>
        <template slot="status" slot-scope="text, record">
          {{ record.status | filtersStatus }}
        </template>
        <!-- slot-scope="text, record" -->
        <template slot="action" slot-scope="text, record">
          <a @click="headerQueryDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="headerCopy(record)">复制</a>
        </template>
      </a-table>
      <account-list ref="account-list" @select="jumpAddTemplate"/>
      <!--详情弹窗  -->
      <a-drawer width="400px" :visible="detailModel" @close="detailModel = false" title="预览模板" >
        <pre-view :list="curLookCodeItem" :task-title="curLookCodeTitle" />
      </a-drawer>
    </a-card>
  </div>
</template>

<script>
import { getTemplate, getTemplateTask } from '@/api/information'
import RangePicker from '@/components/RangePicker'
import QueryType from '../components/QueryType'
import AccountList from '@/components/AccountList'
import PreView from '@/components/PreView'
import { repalceTag } from '@/utils/global'
const columns = [
    {
        title: '消息ID',
        dataIndex: 'id',
        key: 'id',
        width: 100,
        ellipsis: true
    },
    {
        title: '公众号',
        dataIndex: 'accountName',
        key: 'accountName',
        width: 240,
        ellipsis: true,
        scopedSlots: { customRender: 'accountName' }
    },
    {
        title: '消息名称',
        dataIndex: 'label',
        key: 'label',
        width: 140,
        ellipsis: true
    },
    {
        title: '发送时间',
        dataIndex: 'sendTime',
        key: 'sendTime'
    },
    {
        title: '模板名称',
        dataIndex: 'templateName',
        key: 'templateName'
    },
    {
        title: '发送对象',
        dataIndex: 'type', // 0 全部 1 筛选
        key: 'type',
        scopedSlots: { customRender: 'type' }
    },
    {
        title: '发送人数',
        dataIndex: 'sendNum',
        key: 'sendNum'
    },
    {
        title: '发送状态',
        dataIndex: 'status', // 0 ''已发送 1 '未发生' 2 '发送中' 3 '发送终止' 4 发送失败
        key: 'status',
        scopedSlots: { customRender: 'status' }
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
        scopedSlots: { customRender: 'action' }
    }
]
export default {
  name: 'Template',
  components: {
      RangePicker,
      QueryType,
      AccountList,
      PreView
  },
  filters: {
      filtersStatus (status) {
          let result
          switch (status) {
              case 0:
                  result = '已发送'
                  break
              case 1:
                  result = '未发送'
                  break
              case 2:
                  result = '发送中'
                  break
              case 3:
                  result = '发送终止'
                  break
              case 4:
                  result = '发送失败'
                  break
          }

          return result
      }
  },
  created () {
      this.getTemplate()
  },
  data () {
      return {
        columns,
        detailModel: false,
        curLookCodeItem: [],
        curLookCodeTitle: '',
        form: {
            page: 1,
            startDate: '',
            endDate: '',
            status: ''
        },
        customerList: []
      }
  },
  methods: {
    //   复制模板消息
     headerCopy (record) {
        this.$router.push({
            path: '/information/template/add',
            query: {
                accountId: record.accountId,
                id: record.id
            }
        })
    },
    // 查看详情
    headerQueryDetail (record) {
        this.detailModel = true

        const plainObj = JSON.parse(record.templateData)
        const moduleData = Object.keys(plainObj).map(item => {
            return {
                key: item,
                title: plainObj[item].title,
                color: plainObj[item].color,
                value: repalceTag.reResult(plainObj[item].value)
            }
        })
        this.curLookCodeTitle = record.templateName
        this.curLookCodeItem = moduleData
    },
   // 跳转页码
    async jumpAddTemplate (account) {
        console.log(account)
        const { data, code } = await getTemplateTask(account[0].id)
      if (code === 200) {
        if (data.hasOwnProperty('errcode')) {
          this.$message.error(data.errmsg)
        } else {
            this.$router.push({
                path: '/information/template/add',
                query: {
                    accountId: account[0].id
                }
            })
        }
      }
    },
    //  查询状态
    queryType (status) {
        this.form.status = status
        this.getTemplate()
    },
    //   查询客服消息模板
    async getTemplate () {
      const { data, code } = await getTemplate(this.form)
      if (code === 200) {
          console.log(data)
          const { records } = data
          this.customerList = records
      }
    }
  }
}
</script>
<style scoped lang="less">
.Template {
    .headers {
        margin-bottom: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        &-left {
            display: flex;
            justify-content: center;
            align-items: center;
            .query-type {
                margin-left: 20px;
                margin-right: 20px;
            }
        }
    }
}
.accountName-img {
    width: 40px;
    height: 40px;
    object-fit: cover;
    margin-right: 5px;
}
</style>
