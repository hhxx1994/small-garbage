import Vue from 'vue'
import Vuex from 'vuex'
// import
Vue.use(Vuex)
// 创建基本状态
const state = {
  user: JSON.parse(sessionStorage.getItem('user')) || '', // 登录用户
  total: 0,
  searchData: JSON.parse(sessionStorage.getItem('searchData')),
}
// 创建改变状态的方法
const mutations = {
  // 用户登录
  LOGIN(state) {
    state.user = JSON.parse(sessionStorage.getItem('user'))
  },
  // 用户登出
  LOGOUT(state) {
    state.user = ''
  },
  HouseInfoTotal(state) {
    state.total = sessionStorage.getItem('hoseInfoTotal')
  },
  SearchData(state) {
    state.searchData = sessionStorage.getItem('searchData')
  },
}

// 创建驱动actions可以使得mutations得以启动
const actions = {
  // 用户登录
  // 这里先来一个驱动LOGIN的东西就叫login吧
  login({commit}) {
    commit('LOGIN')
  },
  // 用户登出
  logout({commit}) {
    commit('LOGOUT')
  },

  houseInfoTotal({commit}) {
    commit('HouseInfoTotal')
  },
  searchDataAction({commit}) {
    commit('SearchData')
  }

}

export default new Vuex.Store({
  state,
  mutations,
  actions
})
