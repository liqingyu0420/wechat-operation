<template>
  <div class="ActionCard">
    <a-card hoverable style="width: 370px">
      <div class="headers">
        <div class="headers-left">
          <img :src="itemData.headImage" alt="">
          <p>{{ itemData.nikeName }}</p>
        </div>
        <div class="headers-right">
          <a-tooltip>
            <template slot="title" v-if="!itemData.setUp">
              没有设置内容， 无法推送
            </template>
            <a-switch :disabled="!itemData.setUp" :checked="itemData.enable" @change="onChange" />
          </a-tooltip>
        </div>
      </div>

      <template v-if="itemData.setUp">
        <div class="content">
          <p v-if="itemData.pushTimer">推送时间：{{ itemData.pushTimer | filterPushTimer }}</p>
          <p>推送方式：{{ itemData.pushType | filterPushType }}</p>
          <p>推送内容：{{ itemData.content | filtercontent }}</p>
        </div>

      </template>
      <template v-else>
        <div class="no-setting" :style="{height: !itemData.setUp && itemData.hasOwnProperty('pushTimer') ? '97px !important' : '73px'}">
          未设置
        </div>
      </template>

      <template slot="actions" class="ant-card-actions">
        <a-icon @click="jumpEditReply" key="edit" type="edit" />
        <!-- <a-icon key="delete" disabled type="delete" /> -->
        <a-icon key="ellipsis" type="smile" />
      </template>
    </a-card>
  </div>
</template>

<script>
import { pushTypeList } from '../interactionConfig'
export default {
  name: 'ActionCard',
  props: {
      itemData: {
          type: Object,
          required: true
      }
  },
  filters: {
    filterPushType (pushType) {
      return pushTypeList.filter(item => {
        return item.type === pushType
      })[0].label
    },
    filtercontent (content) {
      return JSON.parse(content).length + '条消息'
    },
    filterPushTimer (pushTimer) {
      const timeType = pushTimer.substr(pushTimer.length - 1, 1)
      const count = pushTimer.substr(0, pushTimer.length - 1)
      return count + (timeType === 'h' ? '小时' : '分钟')
    }
  },
  data () {
    return {
      pushTypeList
    }
  },
  methods: {
      //  跳转到编辑页
      jumpEditReply () {
        this.$emit('jumpEditReply', this.itemData)
      },
      onChange (checked) {
          const $par = {
            enableVal: checked,
            id: this.itemData.id
          }

          this.$emit('enableInteraction', $par)
      }
  }
}
</script>
<style scoped lang="less">
.ActionCard {
    .headers {
        display: flex;
        justify-content: space-between;
        align-items: center;

        box-sizing: border-box;
        &-left {
            display: flex;
            align-items: center;
            img {
                width: 46px;
                height: 46px;
                object-fit: cover;
                margin-right: 10px;
            }
            p {
                margin-bottom: 0;
                font-size: 14px;
                line-height: 14px;
                color: #333333;
                margin: 2px 0;
                display: -webkit-box;
                display: -ms-flexbox;
                display: flex;
                width: 100%;
            }
        }
    }
    .content {
      padding-top: 20px;
      box-sizing: border-box;
      p {
        margin-bottom: 5px;
      }
    }
}
.no-setting {
    width: 280px;
    font-size: 18px;
    color: #ccc;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
