<template>
  <div
    :id="editId"
    class="mt-edit"
    contenteditable="true"
    @focus="handleKeyup"
    @click="handleClick"
    @keyup="handleKeyup"
    @input="getVal">
  </div>
</template>

<script>
  export default {
    name: 'DivEdit',
    data () {
      return {
        lastEditRange: '',
        editId: ''
      }
    },
    mounted () {
      this.editId = 'edit_' + new Date().getTime()
    },
    methods: {
      handleClick () {
        if (this.lastEditRange.startOffset > 0) {
          this.handleKeyup()
        }
      },
      handleKeyup () {
        const divEdit = document.getElementById(this.editId)
        // 编辑框设置焦点
        divEdit.focus()
        this.setLastEditRange(divEdit)
      },
      setLastEditRange (divEdit) {
        const selection = getSelection()
        const currentGetRangeAt = selection.getRangeAt(0)
        if (currentGetRangeAt.startOffset === 0 && this.lastEditRange.startOffset > 0) {
          window.getSelection().removeAllRanges()
          window.getSelection().addRange(this.lastEditRange)
          this.lastEditRange = selection.getRangeAt(0)
        } else {
          this.lastEditRange = currentGetRangeAt
        }
      },
      /** 插入dom节点 */
      innerDom (innerStr) {
        const divEdit = document.getElementById(this.editId)
        console.log(divEdit, 'divEditdivEdit')
        divEdit.focus()
        if (!innerStr) return // 防止直接调用   divEdit.focus() 方法获取焦点报错
        this.setLastEditRange()
        // console.log('插入dom节点', innerStr)
        innerStr = innerStr.replace(/\r\n/g, '')
        const range = this.lastEditRange
        range.collapse(false)
        const hasR = range.createContextualFragment(innerStr)
        const hasRlastChild = hasR.lastChild
        range.insertNode(hasR)
        // console.log(range,divEdit)
        if (hasRlastChild) {
          range.setStartAfter(hasRlastChild)
          range.collapse(true)
        }
        window.getSelection().removeAllRanges()
        window.getSelection().addRange(range)
        divEdit.focus()
        this.getVal()
      },
      handleInner (innerStr) {
        let range
        // 获取编辑框对象
        const divEdit = document.getElementById(this.editId)
        // 编辑框设置焦点
        divEdit.focus()
        // 获取选定对象
        const selection = getSelection()
        // 判断是否有最后光标对象存在
        if (this.lastEditRange) {
          // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
          selection.removeAllRanges()
          selection.addRange(this.lastEditRange)
        }
        // 判断选定对象范围是编辑框还是文本节点
        if (selection.anchorNode.nodeName !== '#text') {
          // 如果是编辑框范围。则创建表情文本节点进行插入
          const emojiText = document.createTextNode(innerStr)
          // 如果是编辑框范围。则创建表情文本节点进行插入
          if (divEdit.childNodes.length > 0) {
            // 如果文本框的子元素大于0，则表示有其他元素，则按照位置插入表情节点
            for (let i = 0; i < divEdit.childNodes.length; i++) {
              if (i === selection.anchorOffset) {
                divEdit.insertBefore(emojiText, divEdit.childNodes[i])
              }
            }
          } else {
            // 否则直接插入一个表情元素
            divEdit.appendChild(emojiText)
          }
          // 创建新的光标对象
          range = document.createRange()
          // 光标对象的范围界定为新建的表情节点
          range.selectNodeContents(emojiText)
          // 光标位置定位在表情节点的最大长度
          range.setStart(emojiText, emojiText.length)
          // 使光标开始和光标结束重叠
          range.collapse(true)
          // 清除选定对象的所有光标对象
          selection.removeAllRanges()
          // 插入新的光标对象
          selection.addRange(range)
        } else {
          // 如果是文本节点则先获取光标对象
          range = selection.getRangeAt(0)
          // 获取光标对象的范围界定对象，一般就是textNode对象
          const textNode = range.startContainer
          // 获取光标位置
          const rangeStartOffset = range.startOffset
          // 文本节点在光标位置处插入新的表情内容
          textNode.insertData(rangeStartOffset, innerStr)
          // 光标移动到到原来的位置加上新内容的长度
          range.setStart(textNode, rangeStartOffset + innerStr.length)
          // 光标开始和光标结束重叠
          range.collapse(true)

          // 清除选定对象的所有光标对象
          selection.removeAllRanges()
          // 插入新的光标对象
          selection.addRange(range)
        }
        // 无论如何都要记录最后光标对象
        this.lastEditRange = selection.getRangeAt(0)
        /// /console.log('无论如何都要记录最后光标对象:', this.lastEditRange);
        this.getVal()
      },
      getVal () {
        const divEdit = document.getElementById(this.editId)
        this.$emit('change', divEdit.innerHTML)
      }
    }
  }
</script>

<style lang="less" scoped>
  .mt-edit {
    line-height: initial;
    min-height: 36px;
    width: 100%;
    padding: 10px;
  }
</style>
