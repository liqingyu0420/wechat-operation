<template>
  <div class="TemplateEdit">
    <!-- 左侧列表区域 -->
    <div class="menu-list">
      <draggable
        :list="list"
        ghost-class="ghost"
      >
        <div
          :class="['menu-list-item', menuIndex === index ? 'avtive' : '', 'cp']"
          v-for="(item, index) in list"
          @click="menuIndex = index; currentEditFormType = item.type"
          :key="index">
          <span>{{ index + 1 }}</span>
          <p>{{ item.type | filterLabel }}</p>
        </div>
      </draggable>

      <div
        class="menu-list-item-add cp"
        @click="addMenu"
        v-if=" list.length < 10 "
      >
        <a-tooltip>
          <template slot="title">
            拖拽菜单表，可以调换顺序
          </template>
          <a-icon type="question-circle" />
        </a-tooltip>
        <div class="add-btn">
          <span>+</span>
          <p>新建</p>
        </div>

      </div>
    </div>
    <!-- 右侧内容区 -->
    <div class="operation-panel">
      <div class="operation-panel-item" >
        <msg-type @chooseType="chooseType" :currentEditFormType="currentType"/>
        <!-- 图文 -->
      </div>
      <template v-if="currentType * 1 === 1">
        <image-text
          :item-data="list[menuIndex]"
          :add-status="addImageText"
        />
        <div class="operation-btn">
          <a-button
            v-if="list.length > 1"
            type="danger"
            class="add-image-text"
            @click="deleteOptionCard">
            删除该选项卡
          </a-button>
          <a-button type="primary" class="add-image-text" @click="addImageText = !addImageText">
            添加图文
          </a-button>
        </div>
      </template>

      <!-- 图片 -->
      <template v-else-if="currentType * 1 === 2">
        <show-img
          :imgUrl="list[menuIndex].url"
          @changeImg="changeImg"
          label="添加图片"
        />
      </template>
      <!-- 文字 -->
      <template v-else-if="currentType * 1=== 3">
        <text-view :item-data="list[menuIndex]" @changeText="changeText"/>
      </template>
      <!-- 音频 -->
      <template v-else-if="currentType * 1 === 4">
        <audiot-view :audioUrl="list[menuIndex]" @changeAudit="changeAudit"/>
      </template>
      <!-- 视频 -->
      <template v-else>
        <video-view :videoUrl="list[menuIndex]" @changeVideo="changeVideo"/>
      </template>
    </div>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import MsgType from '@/components/MsgType'
import ImageText from '@/components/TemplateItem/ImageTextItem'
import ShowImg from '@/components/TemplateItem/ShowImg'
import ImageView from '@/components/material/ImageView'
import TextView from '@/components/TemplateItem/TextItem'
import AudiotView from '@/components/TemplateItem/AudioItem'
import VideoView from '@/components/TemplateItem/VideoItem'
import { msgType } from '@/utils/global'
export default {
  name: 'TemplateEdit',
  components: {
    draggable,
    MsgType,
    ImageText,
    ShowImg,
    ImageView,
    TextView,
    AudiotView,
    VideoView
  },
  props: {
    editList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  filters: {
    filterLabel (target) {
      return msgType.filter(item => {
        return Number(item.type) === Number(target)
      })[0].label
    }
  },
  computed: {
    currentType () {
      return Number(this.list[this.menuIndex].type)
    }
  },
  watch: {
    editList: {
      handler () {
          console.log(this.editList, 'this.editList')
        if (this.editList.length === 0) {
           console.log('hello worldworld')
          this.list = [ { type: 1, imagetextlist: [] } ]
        } else {
          this.list = this.editList
          console.log(this.editList, 'this.list')
        }
      },
      deep: true,
      immediate: true
    },
    list: {
      handler () {
        console.log('hello list')
        this.$emit('changeContent', this.list)
      },
      deep: true
    }

  },
  created () {

  },
  data () {
    return {
      msgType, // 查询公众号素材的类型
      addImageText: false,
      showImg: {
        id: '',
        url: ''
      },
      menuIndex: 0,
      list: [ { type: 1, imagetextlist: [] } ], //  二维码内容
      editInformationIndex: 0, // 当前编辑的消息类型
      currentEditFormType: 0
    }
  },
  methods: {

    // 选择文字
    changeText (item) {
      console.log(item, 'itemitme')
      this.$set(this.list, this.menuIndex, { content: item, type: 3 })
    },
    // 选择音频
    changeAudit (item) {
      this.$set(this.list, this.menuIndex, { ...item, type: 4 })
    },
    // 选择音频
    changeVideo (item) {
      this.$set(this.list, this.menuIndex, { ...item, type: 5 })
    },
    // 选择图片
    changeImg (item) {
      this.$set(this.list, this.menuIndex, { ...item, type: 2 })
    },
    // 删除选项卡
    deleteOptionCard () {
      this.list.splice(this.menuIndex, 1)
      if (this.menuIndex !== 0) {
        this.menuIndex--
      }
    },
    // 文章类型切换
    chooseType (value) {
      console.log(value, 'valuevaluevalue')
      const { type } = value
      if (type === 1) {
        console.log('hhhhhhhhhhhhh')
         this.$set(this.list, this.menuIndex, { imagetextlist: [], type: 1 })
      } else {
        this.$set(this.list, this.menuIndex, { type })
      }
    },
    // 新增menu
    addMenu () {
      this.list.push({
        type: 1,
        imagetextlist: []
      })
    }
  }
}
</script>
<style scoped lang="less">
.TemplateEdit {
  display: flex;
  // padding: 40px;
  padding-top: 10px;
}
.menu-list {
  width: 120px;
  &-item {
    width: 100%;
    height: 36px;
    position: relative;
    margin-bottom: 5px;
    height: 37px;
    border: 1px solid #d8d8d8;
    margin-right: -1px;
    display: flex;
    justify-content: center;
    align-items: center;
    span {
      width: 40px;
      height: 36px;
      display: block;
      border-right: 1px solid #d8d8d8;
      text-align: center;

    }
    p {
      width: 80px;
      margin-bottom: 0;
      text-align: center;
      height: 36px;
    }
  }
  .menu-list-item-add {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 36px;
    width: 140px;
    margin-left: -18px;
   /deep/ .anticon-question-circle {
      color: #000;
    }
    .add-btn {
      display: flex;
      justify-content: center;
      align-items: center;
      border: 1px dashed #d8d8d8;
      box-sizing: border-box;
      height: 36px;
      span {
          width: 40px;
          height: 36px;
          display: block;

          text-align: center;
          font-size: 22px;
          line-height: 36px;
        }
      p {
          width: 80px;
          margin-bottom: 0;
          text-align: center;
          height: 36px;

        }
    }

  }
  .avtive {
    background: #fff;
    border-right: 1px solid #fff;
  }
}
.operation-panel {
  width: 890px;
  min-height: 640px;
  max-height: 1280px;
  padding: 20px 15px;
  border: 1px solid #d8d8d8;
  overflow-y: auto;
  overflow-x: hidden;
  background: #fff;
  margin-left: -1px;
  box-sizing: border-box;
}

.operation-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  /deep/ .ant-btn-danger {
    margin-right: 20px;
  }
}

</style>
