<template>
  <div class="RangePick">
    <a-range-picker
      @change="changeDate"
      format="YYYY/MM/DD"
      :default-value="defaultDate"
    >
      <template slot="dateRender" slot-scope="current">
        <div class="ant-calendar-date" >
          {{ current.date() }}
        </div>
      </template>
    </a-range-picker>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'RangePick',
  props: {
      defaultValue: {
        type: Array,
        default: () => {
          return []
        }
      }
  },
  watch: {
    defaultValue: {
      handler () {
        if (this.defaultValue.length === 0) return
        this.defaultDate = [moment(this.defaultValue[0].replace(/-/g, '/'), 'YYYY/MM/DD'), moment(this.defaultValue[1].replace(/-/g, '/'), 'YYYY/MM/DD')]
      },
      immediate: true
    }
  },
  data () {
    return {
      defaultDate: []
    }
  },
  methods: {
    changeDate (empty, dates) {
        const dateArr = dates.map(date => {
        return date.replace(/\//g, '-')
      })
        this.$emit('changeDate', dateArr)
    }
  }
}
</script>
<style scoped lang="less">

</style>
