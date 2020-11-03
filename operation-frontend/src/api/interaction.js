import request from '@/utils/request'

// 查询二维码列表
export function getQrcode (params) {
    return request({
        url: `/wxoperate/qr`,
        method: 'GET',
        params
    })
}
// 查询二维码详情
export function getQrcodeDetail (id) {
    return request({
        url: `/wxoperate/qr/${id}`,
        method: 'GET'
    })
}
// 新增 or 修改 二维码
export function PostQrcode (data) {
    return request({
        url: `/wxoperate/qr`,
        method: data.id ? 'PUT' : 'POST',
        data
    })
}

// 查询被关注回复
export function getFollowReply () {
    return request({
        url: `/wxoperate/followReply`,
        method: 'GET'
    })
}
// 查询被关注回复详情
export function getFollowReplyDetail (params, id) {
    return request({
        url: `/wxoperate/followReply/${id}`,
        method: 'GET',
        params
    })
}
// 开启或者关闭被关注回复
export function enableInteraction (params, id) {
    return request({
        url: `/wxoperate/followReply/enable/${id}`,
        method: 'PUT',
        params
    })
}

// 修改被关注回复的状态
export function putFollowReplyEnable (data) {
    return request({
        url: `/wxoperate/followReply`,
        method: 'PUT',
        data
    })
}
// 查询智能推送列表
export function getPush () {
    return request({
        url: `/wxoperate/push`,
        method: 'GET'
    })
}
// 修改智能推送列表
export function putPush (data) {
    return request({
        url: `/wxoperate/push`,
        method: 'PUT',
        data
    })
}
// 查询智能推送详情
export function getPushDetail (params, id) {
    return request({
        url: `/wxoperate/push/${id}`,
        method: 'GET',
        params
    })
}
