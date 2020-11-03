<template>
  <div class="AudioMaterial">
    <custom-upload :type="isAudio ? 'audio' : 'video'" :title="isAudio? '上传音频' : '上传视频'"/>
    <a-table
      :columns="columns"
      :row-selection="rowSelection"
      :data-source="audioWithVideoData"
    >
    </a-table>
  </div>
</template>

<script>
import { getMaterial } from '@/api/material'

import { materialType } from '@/utils/global'
import CustomUpload from '@/components/upload'
const columns = [
  {
    title: '音频',
    dataIndex: 'name',
    key: 'name',
    ellipsis: true
  },
  {
    title: '时间',
    dataIndex: 'createTime',
    key: 'createTime'
  }
]
export default {
  name: 'AudioWithVideo',
  components: {
    CustomUpload
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    isAudio: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    modelStatus: {
        get () {
            return this.visible
        },
        set () {
            return false
        }
    },
    rowSelection () {
      return {
        type: 'radio',
        columnTitle: ' ', // 设置这行
        onChange: (selectedRowKeys, selectedRows) => {
          this.handelChange(selectedRows)
        },
        getCheckboxProps: record => ({
           props: {
            disabled: record.status === 1, // Column configuration not to be checked
            name: record.name
          }
        })
      }
    }
  },
  created () {
    const { accountId } = this.$route.query
    if (!this.isAudio) {
      this.columns[0].title = '视频'
    }
    if (accountId) {
      this.accountId = accountId
      this.getMaterial()
    }
  },
  data () {
      return {
          accountId: '',
          columns,
          materialType,
          pagination: {
              page: 1,
              total: 0
          },
          audioWithVideoData: [],
          currentInfo: {
              id: '',
              url: ''
          }

      }
  },
  methods: {
    // change 事件
    handelChange (selectedRows) {
       this.currentInfo = selectedRows[0]
    },

    reactiveParent () {
      this.$emit('onChange', this.currentInfo)
    },
          // 查询图片素材
    async getMaterial () {
      const { accountId, pagination } = this

      const $par = {
        accountId,
        type: this.isAudio ? this.materialType[1].type : this.materialType[2].type,
        ...pagination
      }

      const { data, code } = await getMaterial($par)
      console.log(data, code, 'data')
        if (code === 200) {
            console.log(data)
            const { records, total } = data
            this.audioWithVideoData = records
            console.log(total)
        }
      }
  }
}
</script>
<style scoped lang="less">

</style>
