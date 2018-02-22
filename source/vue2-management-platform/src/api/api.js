import axios from 'axios'

// dev
let base = '/learn'
// // build
// let base = ''
// 注册接口
export const ReginUser = params => {
  return axios.post(`${base}/regin`, params)
}
// 登录接口
export const LoginUser = params => {
  return axios.get(`${base}/login`, {params: params})
}

