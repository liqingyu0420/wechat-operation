<template>
  <div class="TemplateGrail">
    <div class="template-grail-box">
      <div class="template-grail-box-left">
        <a-card class="card">
          <slot name="left" ></slot>
        </a-card>
      </div>
      <div class="template-grail-box-right">
        <slot name="right" ></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TemplateGrail'
}
</script>
<style scoped lang="less">
.TemplateGrail {
    overflow: hidden;
    overflow-x: auto;
    .template-grail-box {
        position: relative;
        display: flex;
        justify-content: space-between;
        align-items: start;
        // height: 800px;
        min-width: 1160px;
        &-left {
            width: calc(83vw - 380px);
            height: 100%;
            margin-right: 20px;
            .card {
                height: 100%;
                width: 100%;
                box-sizing: border-box;
                min-width: 766px;
            }
        }
        &-right {
            width: 380px;
            background: #fff;
            padding: 24px 20px 0;
            height: 800px;
            z-index: 1;
            box-sizing: border-box;
            // position: fixed;
            // right: 10px;
        }
    }
}

</style>
