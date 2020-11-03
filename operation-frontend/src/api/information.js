import request from '@/utils/request'

// 查询客服消息
export function getCustomer (params) {
    return request({
        url: `/wxoperate/customer`,
        method: 'GET',
        params
    })
}

// 添加客服消息
export function postCustomer (data) {
    return request({
        url: `/wxoperate/customer`,
        method: 'POST',
        data
    })
}

// 查询客服消息详情
export function getCustomerDetail (msgId) {
    return request({
        url: `/wxoperate/customer/${msgId}`,
        method: 'GET'
    })
}

// 查询高级群发
export function getMass (params) {
    return request({
        url: `/wxoperate/group`,
        method: 'GET',
        params
    })
}

// 新建高级群发
export function postMass (data) {
    return request({
        url: `/wxoperate/group`,
        method: 'POST',
        data
    })
}

// 查询高级群发详情
export function getMassDetail (msgId) {
    return request({
        url: `/wxoperate/group/${msgId}`,
        method: 'GET'
    })
}

// 查询模板消息
export function getTemplate (params) {
    return request({
        url: `/wxoperate/template`,
        method: 'GET',
        params
    })
}

// 新增模板消息
export function postTemplate (data) {
    return request({
        url: `/wxoperate/template`,
        method: 'POST',
        data
    })
}
// 查询模板消息详情
export function getTemplateDetail (params) {
    return request({
        url: `/wxoperate/template/detail`,
        method: 'GET',
        params
    })
}

// 查询模板消息任务
export function getTemplateTask (accountId) {
    return request({
        url: `/wxoperate/template/${accountId}`,
        method: 'GET'
    })
}
