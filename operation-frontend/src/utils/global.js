// 定义全局方法
/*eslint-disable */
// 换取微信二维码
export const WXBaseUrl = 'https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket='

// 查询公众号素材的类型
export const materialType = [
    {
        label: '图片',
        type: 'image'
    },
    {
        label: '语音',
        type: 'voice'
    },
    {
        label: '视频',
        type: 'video'
    },
    {
        label: '缩略图',
        type: 'thumb'
    }
]
// 查询公众号素材的类型
export const msgType = [
    {
        type: 1,
        label: '图文'
    },
    {
        type: 2,
        label: '图片'
    },
    {
        type: 3,
        label: '文字'
    },
    {
        type: 4,
        label: '音频'
    },
    {
        type: 5,
        label: '视频'
    }
]

export const repalceTag = {
    dealListPost: (list) => {
      if (list && list.length) {
        list.map(li => {
          /** 判断是不是图文 */
          if (Number(li.type) === 1 && li.hasOwnProperty('imagetextlist') && li.imagetextlist.length) {
            li.imagetextlist.map(ii => {
              /** 替换粉丝标签 */
              const title = JSON.parse(JSON.stringify(ii.title))
              ii.title = title.replace('<span contenteditable="false" class="user-nick-name"><span>粉丝昵称</span></span>', '<fans.nickname>')
            })
          }
          /** 判断是不是文本 */
          if (Number(li.type) === 3) {
            /** 替换粉丝标签 */
            console.log(li, '我是li');
            if (li.hasOwnProperty('content')) {
              const content = JSON.parse(JSON.stringify(li.content))
              const str = '<span contenteditable="false" class="user-nick-name"><span>粉丝昵称</span></span>'
              li.content = content.replace(str, '<fans.nickname>')
            }
          }
          li.type = String(li.type)
        })
      }
      return list
    },
    replaceContent (content) {
      content = content.replace('<span contenteditable="false" class="user-nick-name"><span>粉丝昵称</span></span>', '<fans.nickname>')
      return content
    },
    reResult (content) {
      if (content) {
        content = content.replace('<fans.nickname>', '<span contenteditable="false" class="user-nick-name"><span>粉丝昵称</span></span>')
      }
      return content
    }
}

// 创建meta refer=nerver标签
export const createMetaScript = (that) => {
  const meta = document.createElement('meta')
  meta.setAttribute('name', 'referrer')
  meta.setAttribute('content', 'never')

  document.head.insertBefore(meta, document.querySelector('link'))


  that.$once('hook:beforeDestroy', () => {
    const metas = Array.from(document.querySelectorAll('meta'))
    const myMeta = metas.map(meta => {
      return meta.name === 'referrer' ? meta : false
    }).filter(ele => ele)[0]
    document.head.removeChild(myMeta)
  })
}

// 文件上传地址
export const uploadAdress = 'https://open.luojigou.vip/wxoperate/material'
