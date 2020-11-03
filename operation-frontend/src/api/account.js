import request from '@/utils/request'

// 子账号管理
export function getUserList () {
    return request({
        url: `/wxoperate/auth/subManage`,
        method: 'GET'
    })
}
