import RangePicker from '@/components/RangePicker'

export default {
    components: {
      RangePicker
    },
    data () {
        return {
            currentTime: []
        }
    },
    methods: {
        // 获取初始时间
        initCuttentTime (num) {
            const start = new Date()
            const end = new Date()

            start.setTime(start.getTime() - 3600 * 1000 * 24 * num)
            this.currentTime = [`${start.getFullYear()}-${this.dealTime(start.getMonth() + 1)}-${this.dealTime(start.getDate())}`, `${end.getFullYear()}-${this.dealTime(end.getMonth() + 1)}-${this.dealTime(end.getDate())}`]
        },
        // 转换时间格式
        dealTime (time) {
            return time < 10 ? `0${time}` : time
        }
    }
}
