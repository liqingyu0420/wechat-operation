import request from '@/utils/request'

// const api = {
//   user: '/user',
//   role: '/role',
//   service: '/service',
//   permission: '/permission',
//   permissionNoPager: '/permission/no-pager',
//   orgTree: '/org/tree'
// }

const timestamp = new Date().getTime()
// 获取验证码
export function getVerifyCode () {
    return request({
        url: `/wxoperate/auth/captcha?timestamp=${timestamp}`,
        method: 'GET'
    })
}
// 用户登录
export function Login (parameter) {
    return request({
        url: `/wxoperate/auth/login`,
        method: 'POST',

        data: parameter
    })
}

// 子账号列表
export function getAuthList () {
    return request({
        url: `/wxoperate/auth/subManage`,
        method: 'GET'
    })
}
// 子账号前缀
export function getAuthSubPre () {
    return request({
        url: `/wxoperate/auth/subPre`,
        method: 'GET'
    })
}
// 子账号创建
export function postAuth (data) {
    return request({
        url: `/wxoperate/auth/subCreate`,
        method: 'POST',
        data
    })
}
// 子账号冻结/解冻
export function authOpration (userId) {
    return request({
        url: `/wxoperate/auth/subManage/${userId}`,
        method: 'POST'
    })
}
// 子账号更新信息
export function putAuth (data) {
    return request({
        url: `/wxoperate/auth/subUpdate`,
        method: 'PUT'
    })
}
