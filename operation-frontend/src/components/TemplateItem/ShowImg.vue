<template>
  <div class="ShowImg-box ">
    <div class="label" v-if="label">
      {{ label }}
    </div>
    <div
      class="ShowImg cp"
      @mouseenter="overlayShow = true"
      @mouseleave="overlayShow = false"
      @click="changeImg"
      v-if="imgUrl"
    >
      <img :src="imgUrl" alt="">
      <div class="overlay" v-if="overlayShow">
        更换图片
      </div>
    </div>
    <div
      class="upload-btn cp"
      v-else
      @click="openModal">
      <a-icon type="plus" />
    </div>

    <!-- 图片选择弹框 -->
    <image-view
      ref="image-view"
      @selectImg="selectImg"
    />

  </div>
</template>

<script>
import ImageView from '@/components/material/ImageView'
export default {
  name: 'ShowImg',
  components: {
    ImageView
  },
  props: {
      label: {
        type: String,
        default: ''
      },
      imgUrl: {
          type: String,
          default: ''
      }
  },
  data () {
    return {
      overlayShow: false
    }
  },
  methods: {
    openModal () {
      this.$refs['image-view'].openModal()
    },
    selectImg (item) {
      this.$emit('changeImg', item)
      this.visible = false
    },
    changeImg () {
      this.$refs['image-view'].openModal()
    }
  }
}
</script>
<style scoped lang="less">
@import '~./index.less';
.ShowImg-box {
  display: flex;
  .label {
    margin-right: 10px;
    width: 120px;
    text-align: right;
  }
}
.ShowImg {
    position: relative;
    width: 96px;
    height: 96px;
    border: 1px dashed #d8d8d8;
    background-color: #fff;
     margin-left: 10px;
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    .overlay {
      position: absolute;
      bottom: 0;
      width: 100%;
      height: 30px;
      line-height: 30px;
      text-align: center;
      font-size: 12px;
      background-color: rgba(0,0,0,.63);
      color: #fff;
      cursor: pointer;
    }
}

.upload-btn {
    position: relative;
    width: 96px;
    height: 96px;
    border: 1px dashed #d8d8d8;
    background-color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 32px;
    color: #686868;
    margin-left: 10px;
}
</style>
