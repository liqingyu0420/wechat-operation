import request from '@/utils/request'

// 获取指标总览
export function getSummary () {
    return request({
        url: `/wxoperate/index/summary`,
        method: 'GET'
    })
}
// 查询单个公众号昨日统计数据概览
export function getSingleSummary (accountId) {
    return request({
        url: `/wxoperate/index/single/${accountId}`,
        method: 'GET'
    })
}
// 查询单个公众号--粉丝增长
export function getFansAdd (accountId, params) {
    return request({
        url: `/wxoperate/index/growth/${accountId}`,
        method: 'GET',
        params
    })
}

// 查询单个公众号--粉丝属性
export function getFansPro (accountId, params) {
    return request({
        url: `/wxoperate/index/property/${accountId}`,
        method: 'GET',
        params
    })
}
// 查询粉丝关注公众号来源类型
export function getsubscribeScene () {
    return request({
        url: `/wxoperate/index/subscribeScene`,
        method: 'GET'
    })
}
// 查询单个公众号--粉丝活跃度
export function getFansActive (accountId, params) {
    return request({
        url: `/wxoperate/index/inactive/${accountId}`,
        method: 'GET',
        params
    })
}
// 查询单个公众号--粉丝忠诚度
export function getFansLoyal (accountId, params) {
    return request({
        url: `/wxoperate/index/remain/${accountId}`,
        method: 'GET',
        params
    })
}
// 查询单个公众号--图文影响力
export function getArticlesInfluence (accountId, params) {
    return request({
        url: `/wxoperate/index/articles/${accountId}`,
        method: 'GET',
        params
    })
}
// 查询单个公众号--互动消息
export function getToNews (accountId, params) {
    return request({
        url: `/wxoperate/index/interactMsg/${accountId}`,
        method: 'GET',
        params
    })
}
