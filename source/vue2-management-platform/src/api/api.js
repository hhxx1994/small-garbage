import axios from 'axios'

// dev
let base = '/api/user'
// // build
// let base = ''
// 注册接口
export const ReginUser = params => {
  let param = new URLSearchParams();
  param.append("username", params['username']);
  param.append("password", params['password']);
  return axios.post(`${base}/regin`, param)
}
// 登录接口
export const LoginUser = params => {
  return axios.get(`${base}/login`, {params: params})
}

