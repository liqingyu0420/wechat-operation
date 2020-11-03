import request from '@/utils/request'

// 查询公众号标签列表
export function getTagList (params) {
    return request({
        url: `/wxoperate/tag/list`,
        method: 'GET',
        params
    })
}

// 修改公众号标签列表
export function editAddTag (params) {
    return request({
        url: `/wxoperate/tag/addTag`,
        method: params.id ? 'PUT' : 'POST',
        [params.id ? 'data' : 'params']: params
    })
}
// 查询公众号粉丝列表
export function getFansList (accountId, params) {
    return request({
        url: `/wxoperate/fans/${accountId}`,
        method: 'GET',
        params
    })
}

// 查询公众号粉丝列表
export function awaitFans (params) {
    return request({
        url: `/wxoperate/fans/syncUser`,
        method: 'GET',
        params
    })
}
// 批量添加标签
export function batchAddTag (params) {
    return request({
        url: `/wxoperate/fans/markTag`,
        method: 'POST',
        params
    })
}

// 批量删除标签
export function batchDeletdTag (params) {
    return request({
        url: `/wxoperate/fans/unMarkTag`,
        method: 'POST',
        params
    })
}

// 粉丝添加/修改备注
export function fansRemark (params) {
    return request({
        url: `/wxoperate/fans/addRemark`,
        method: 'POST',
        params
    })
}

// 同步标签
export function reloadTag (params) {
    return request({
        url: `/wxoperate/tag/syncTag`,
        method: 'GET',
        params
    })
}

// 删除标签
export function deleteTag (id) {
    return request({
        url: `/wxoperate/tag/delTag/${id}`,
        method: 'DELETE'
    })
}

// 预计发送粉丝数目
export function preFansNum (params) {
    return request({
        url: `/wxoperate/fans/preFansNum`,
        method: 'GET',
        params
    })
}
