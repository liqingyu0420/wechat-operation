import request from '@/utils/request'

/*
    ${accountId}
    ${type} image  voice thumb
    ${page} 页码
*/

// 查询公众号对应的素材
export function getMaterial (params) {
    return request({
        url: `/wxoperate/material`,
        method: 'GET',
        params
    })
}
// 查询公众号列表
export function uploadMaterial (params) {
    return request({
        url: `/wxoperate/material`,
        method: 'POST',
        params
    })
}

// 从微信服务器查询公众号图文
export function getArticlesWechat (params) {
    return request({
        url: `/wxoperate/articles/wechat`,
        method: 'GET',
        params
    })
}
// 查询素材详情
export function getMaterialDetail (params) {
    return request({
        url: `/wxoperate/material/materialDetail`,
        method: 'GET',
        params
    })
}
