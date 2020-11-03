<template>
  <a-modal width="900px" v-model="visible" title="选择图片" @cancel="handleCancel" @ok="handleOk">
    <!-- <custom-upload type="image"/> -->
    <!-- <waterfall :line-gap="270" :watch="imageTextData"> -->
    <!-- each component is wrapped by a waterfall slot -->
    <!-- <waterfall-slot
        move-class="waterfall-slot"
        v-for="(item, index) in imageTextData"
        :width="270"
        :height="((item.content.news_item.length -1 ) * 80 + 120) + index * 10"
        :order="index"
        :key="item.media_id"
      > -->
    <!-- <a-row class="imageTextData-list"> -->
    <!-- v-for="item in imageTextData" :key="item.media_id"  -->
    <a-row class="imageTextData-list">

      <a-col
        :span="7"
        class="imageTextData-list-item cp"
        v-for="(item) in imageTextData"
        :key="item.media_id"
        @click="handlerImgText(item)"
      >
        <div class="imageTextData-list-item-content" v-for="(ele) in [item.content.news_item[0]]" :key="ele.thumb_url">
          <img :src="ele.thumb_url" alt="">
          <div class="imageTextData-list-item-content-text">
            {{ ele.title }}
          </div>
        </div>
        <div class="active-flag" v-if="imgTextInfo.media_id === item.media_id">
          <a-icon class="active-flag-icon" type="check" />
        </div>
        <!-- 类似原生的展现方式 -->
        <!-- <div class="imageTextData-list-item-addition" v-for="el in item.content.news_item.slice(1)" :key="el.thumb_url">
              <div class="left">
                {{ el.title }}
              </div>
              <div class="right">
                <img :src="el.thumb_url" alt="">
              </div>
            </div>
        -->
      </a-col>
    </a-row>
    <!-- </a-row> -->
    <!-- </waterfall-slot> -->
    <!-- </waterfall> -->

    <custom-pagination />
  </a-modal >
</template>

<script>
import { getArticlesWechat } from '@/api/material'
import CustomPagination from '@/components/Pagination'
import { materialType } from '@/utils/global'
import CustomUpload from '@/components/upload'
import Waterfall from 'vue-waterfall/lib/waterfall'
import WaterfallSlot from 'vue-waterfall/lib/waterfall-slot'
import { createMetaScript } from '@/utils/global.js'
export default {
  name: 'ImageText',
  components: {
    CustomPagination,
    CustomUpload,
    Waterfall,
    WaterfallSlot
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
          this.getArticlesWechat()
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
          imageTextData: [],

          imgTextInfo: {}
      }
  },
  methods: {
      // 选中了当前的的图片
      handlerImgText (item) {
          console.log(item)
          this.imgTextInfo = item
    },
      handleCancel () {
          this.visible = false
      },
      handleOk () {
          this.$emit('selectImgText', this.imgTextInfo)
          this.visible = false
      },
      openModal () {
          this.visible = true
      },
      // 查询图片素材
      async getArticlesWechat () {
        const { accountId, pagination } = this
        const $par = {
            accountId,
            ...pagination
        }
        const { data, code } = await getArticlesWechat($par)
        console.log(data, code, 'data')
        if (code === 200) {
            console.log(data)
            const { item, total } = data
            this.imageTextData = item
            console.log(total)
        }
      }
  }
}
</script>
<style scoped lang="less">
@import '../../utils/utils.less';
.ImageView {
    display: grid;
    align-items:center;
    grid-template-columns: auto auto auto auto auto;
    grid-template-rows: auto auto auto auto auto;
    &-item {
        width: 140px !important;
        height: 140px !important;
        text-align: center;
        line-height: 140px;
        margin-bottom: 10px;
        overflow: hidden;
        img {
            width: 140px;
            height: 140px;
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

.imageTextData-list {

    &-item {
        margin-bottom: 10px;
        border: 1px solid #e9e9e9;
        margin-right: 30px;
        position: relative;
        overflow: hidden;
        &-content {
            position: relative;
            img {
                width: 100%;
                height: 120px;
                object-fit: cover;
            }
            &-text {
                position: absolute;
                bottom: 0px;
                width: 100%;
                padding: 6px 10px;
                font-size: 14px;
                color: #fff;
            }
        }

        .imageTextData-list-item-addition {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 16px 0;
            border-bottom: 1px solid #e9e9e9;
            margin: 0 16px;
            .left {

            }
            .right {
                img {
                    width: 48px;
                    height: 48px;
                    object-fit: cover;
                }
            }
        }
    }
}

.vue-waterfall-slot {
    margin-bottom: 10px;
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
</style>
