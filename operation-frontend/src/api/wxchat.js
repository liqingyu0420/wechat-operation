import request from '@/utils/request'

// 查询公众号列表
export function getAccount () {
    return request({
        url: `/wxoperate/account`,
        method: 'GET'
    })
}
