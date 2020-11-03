// 获取
export const getItem = (key) => {
   const data = window.localStorage.getItem(key)
   try {
    return JSON.parse(data)
   } catch (error) {
     return data
   }
}

// 存储
export const setItem = (key, value) => {
    const data = typeof value === 'object' ? JSON.stringify(value) : value
    window.localStorage.setItem(key, data)
}

// 移除
export const removeItem = (key) => {
    window.localStorage.removeItem(key)
}
