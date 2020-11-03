<template>
  <a-modal width="900px" v-model="visible" title="选择图片" @cancel="handleCancel" @ok="handleOk">
    <custom-upload type="image" @success="success"/>
    <div class="ImageView">
      <a-col
        class="ImageView-item"
        v-for="item in imageData"
        :key="item.id">
        <img
          @click="handlerImg(item)"
          class="cp"
          :src="item.url"
          alt="">
        <div class="active-flag" v-if="imgInfo.id === item.id">
          <a-icon class="active-flag-icon" type="check" />
        </div>
      </a-col>
    </div>
    <custom-pagination :total="pagination.total" @change="changePage"/>
  </a-modal >
</template>

<script>
import { getMaterial } from '@/api/material'
import CustomPagination from '@/components/Pagination'
import { materialType } from '@/utils/global'
import CustomUpload from '@/components/upload'
import { createMetaScript } from '@/utils/global.js'
export default {
  name: 'ImageView',
  components: {
    CustomPagination,
    CustomUpload
  },
  computed: {
      modelStatus: {
          get () {
              return this.visible
          },
          set () {
              return false
          }
      }
  },
  created () {
    const { accountId } = this.$route.query
      if (accountId) {
          this.accountId = accountId
          this.getMaterial()
      }
  },
  mounted () {
    createMetaScript(this)
  },
  data () {
      return {
          visible: false,
          accountId: '',
          materialType,
          pagination: {
              page: 1,
              total: 0
          },
          imageData: [],

          imgInfo: {
              id: '',
              url: ''
          }
      }
  },

  methods: {
    //   页码切换
    changePage (page) {
        this.pagination.page = page
        this.getMaterial()
    },
    // 选中了当前的的图片
    handlerImg (item) {
        console.log(item)
        this.imgInfo = item
    },
    handleCancel () {
        this.visible = false
    },
    handleOk () {
        this.$emit('selectImg', this.imgInfo)
        this.visible = false
    },
    openModal () {
        this.visible = true
    },
    //   上传成功后查询图片
    success () {
        this.getMaterial()
    },
    // 查询图片素材
    async getMaterial () {
      const { accountId, pagination } = this
      const $par = {
          accountId,
          type: this.materialType[0].type,
          ...pagination
      }

      const { data, code } = await getMaterial($par)
      console.log(data, code, 'data')
      if (code === 200) {
          console.log(data)
          const { records, total } = data
          this.imageData = records
          this.pagination.total = total
          console.log(total)
      }
    }
  }
}
</script>
<style scoped lang="less">
.ImageView {
    display: grid;
    align-items:center;
    grid-template-columns: auto auto auto auto auto;
    grid-template-rows: auto auto auto auto auto;
    &-item {
        width: 128px !important;
        height: 112px !important;
        text-align: center;
        line-height: 140px;
        margin-bottom: 10px;
        overflow: hidden;
        border: 1px solid #e9e9e9;
        img {
            width: 128px;
            height: 112px;
            display: block;
            margin: 0 auto;
            object-fit: cover;
        }
        .active-flag {
            position: absolute;
            top: -16px;
            right: -7px;
            width: 20px;
            height: 40px;
            background: #3b74ff;
            transform: rotate(-45deg);
            display: flex;
            justify-content: center;
            align-items: center;
            &-icon {
                position: absolute;
                top: 12px;
                left: 0;
                color: #fff;
                font-size: 13px;
                transform: rotate(45deg);
                font-weight: bold;
            }
        }
    }
}
</style>
