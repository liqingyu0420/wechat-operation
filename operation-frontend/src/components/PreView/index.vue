<template>
  <div class="create-view bg-fff ">
    <div class="create-view-box " :class="addClass">
      <div class="header-img">
        <img src="http://s.weituibao.com/static/1564380070255/H5.png" alt="">
        <div class="header-title"><span>{{ title }}</span></div>
      </div>
      <div class="view-container custom-scroll-line">
        <template v-if="!taskTitle">

          <div v-for="(item,index) in list" :key="index">
            <!--图文-->
            <div v-if="item.type*1 === 1">
              <div
                :class="['inform-container image-text-container',{overlay: item.imagetextlist.length>1}]"
                v-for="(imageItem,imageIndex) in item.imagetextlist"
                :key="imageIndex">
                <img
                  v-if="item.imagetextlist.length===1"
                  class="avatar-image"
                  src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                  alt="">
                <div class="inform-box">
                  <div class="inform-content">
                    <div class="inform-title" v-html="imageItem.title"></div>
                    <div class="inform-desc">
                      <!--描述只有在一个图文时显示-->
                      <p v-if="item.imagetextlist.length===1">{{ imageItem.description }}</p>
                      <img
                        class="desc-image"
                        :src="imageItem.local_url || imageItem.picurl || imageItem.url || 'https://s.weituibao.com/static/1546084778317/img.png'"
                        alt="">
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!--文字模式-->
            <div v-if="item.type*1 === 3 && item.content" class="inform-container">
              <img
                class="avatar-image"
                src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                alt="">
              <div class="inform-box">
                <div class="inform-content">
                  <div class="inform-title no-bottom" v-html="item.content"></div>
                </div>
              </div>
            </div>

            <!--图片模式-->
            <div v-if="item.type*1 === 2" class="inform-container">
              <img
                class="avatar-image"
                src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                alt="">
              <img class="inform-image" :src="item.url" alt="">
            </div>

            <!-- 视频模式-->
            <img
              v-if="item.type*1 === 5"
              class="inform-video"
              src="https://s.weituibao.com/static/1545276720179/video.png"
              alt="">

            <!--音频模式-->
            <div v-if="item.type*1 === 4" class="inform-container audio-type">
              <img
                class="avatar-image"
                src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                alt="">
              <div class="inform-box">
                <div class="inform-content">
                  <!-- <svg-icon icon-class="yuyin" class-name="audio-class"/> -->
                  <a-icon type="audio" />
                </div>
              </div>
            </div>
          </div>
        </template>

        <!-- 模板任务 -->
        <div class="template-task" v-else>
          <div class="template-task-label">
            {{ taskTitle }}
          </div>
          <div class="template-task-item" v-for="item in list" :key="item.key">
            <span>  {{ item.title ? item.title + ':' : "" }}</span>
            <span class="template-task-item-value" :style="{color: item.color}" v-html="item.value"></span>
          </div>
        </div>
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
  import { repalceTag } from '@/utils/global'
  export default {
    name: 'PerView',
    props: {
      title: {
        type: String,
        default: '预览区'
      },
      list: {
        type: Array,
        default: () => {
          return []
        }
      },
      taskTitle: {
        type: String,
        default: ''
      },
      addClass: {
        type: String,
        default: ''
      }
    },
    filters: {},
    mounted () {
      this.handleList()
    },
    methods: {
      handleList () {
        if (this.list.length) {
          this.list.map(li => {
            /** 判断是不是图文 */
            if (Number(li.type) === 1 && li.imagetextlist.length) {
              li.imagetextlist.map(ii => {
                /** 替换粉丝标签 */
                ii.title = repalceTag.reResult(ii.title)
              })
            }
            /** 判断是不是文本 */
            if (Number(li.type) === 3) {
              /** 替换粉丝标签 */
              li.content = repalceTag.reResult(li.content)
            }
          })
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  @borderColor: rgb(216, 216, 216);
  .create-view {
    height: 640px;
    overflow-x: hidden;
    overflow-y: auto;
    padding: 20px 15px;
    // border-top: 1px solid @borderColor;
    // border-right: 1px solid @borderColor;
    // border-bottom: 1px solid @borderColor;
    border: none !important;
  }

  .create-view-box {
    background: #f9f9f9;
    box-shadow: 0 0 0 1px rgba(197, 197, 197, 0.58);
    height: 568px;
    width: 320px;

    &.marginAuto {
      margin: 0 auto;
    }

    .header-img {
      position: relative;
      margin-bottom: 10px;
      img {
        width: 320px;
        height: 58px;
        object-fit: contain;
      }

      .header-title {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        text-align: center;
        font-size: 16px;
        color: #000;
        padding-top: 23px;
      }
    }

    .view-container {
      font-size: 12px;
      max-height: 485px;
      overflow-x: hidden;
      overflow-y: auto;
      padding: 0 15px;

      .inform-video {
        width: 100%;
        margin-bottom: 20px;
      }

      .inform-image {
        width: 150px;
        margin-left: 15px;
        border-radius: 4px;
        height: 100%;
      }

      .inform-container {
        display: flex;
        margin-bottom: 20px;

        &.audio-type {
          .inform-content {
            width: 239px;
            height: 46px;
            display: flex;
            justify-content: flex-start;
            align-items: center;
            font-size: 22px;
            padding: 4px 8px;
          }
        }

        .avatar-image {
          width: 36px;
          height: 36px;
          object-position: center;
          object-fit: cover;
        }

        .inform-box {
          flex: 1;
          padding-left: 15px;
        }

        .inform-content {
          border: 1px solid @borderColor;
          background: #fff;
          padding: 8px;
          border-radius: 4px;

          .audio-class {
            font-size: 14px;
          }

          .inform-title {
            line-height: initial;
            margin-bottom: 10px;
          }

          .inform-desc {
            display: flex;

            p {
              flex: 1;
              color: #999999;
              overflow: hidden;
              line-height: 17px;
              font-size: 12px;
              display: -webkit-box;
              -webkit-line-clamp: 3;
              -webkit-box-orient: vertical;
              padding-right: 5px;
              word-break: break-word;
            }

            .desc-image {
              width: 48px;
              height: 48px;
              object-fit: cover;
              object-position: center;
            }
          }
        }
      }
    }
  }

  // 约课
  .template-task {
    background: #ffffff;
    border: 1px solid #e9e9e9;
    border-radius: 4px;
    margin: auto;
    padding: 16px 13px;
    box-sizing: border-box;
    font-size: 14px;
    color: #333333;
    line-height: 24px;
      &-label {
        font-size: 18px;
        margin-bottom: 10px;
      }
      &-item {
        &-value {
          padding-left: 5px;
        }
      }
  }
</style>
