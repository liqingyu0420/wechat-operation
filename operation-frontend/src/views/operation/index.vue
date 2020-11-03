<template>
  <div class="OperationCenter">
    <statistic class="statistic"/>
    <data-view />
  </div>

</template>

<script>
import Statistic from './Statistic'
import DataView from './data-view'
export default {
  name: 'OperationCenter',
  components: {
      Statistic,
      DataView
  }
}
</script>
<style scoped lang="less">
.OperationCenter {
    .statistic {
        margin-bottom: 20px;
    }
}
</style>
