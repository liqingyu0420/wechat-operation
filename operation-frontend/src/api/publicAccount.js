import request from '@/utils/request'

// 查询公众号列表
export function getAccount () {
    return request({
        url: `/wxoperate/account`,
        method: 'GET'
    })
}

// 删除微信公众号
export function deleteAccount (accountId) {
    return request({
        url: `/wxoperate/account/${accountId}`,
        method: 'DELETE'
    })
}

// 获取预授权码地址，微信认证公众号
export function getAccountPreAuth () {
    return request({
        url: `/wxoperate/account/preAuth`,
        method: 'GET'
    })
}

// 根据id查询微信公众号
export function getAccountDetail (id) {
    return request({
        url: `/wxoperate/account/${id}`,
        method: 'GET'
    })
}

// 确认授权的微信公众号
export function confirmAccount (params) {
    return request({
        url: `/wxoperate/account/confirmAccount`,
        method: 'GET',
        params
    })
}
