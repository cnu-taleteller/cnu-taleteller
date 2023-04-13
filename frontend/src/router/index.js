import Vue from 'vue'
import VueRouter from 'vue-router'
// import HomeView from '../views/HomeView.vue'
import ToolView from '../views/ToolView.vue'
import ToolSubmit from '../views/ToolSubmit.vue'
import MyPageView from '../views/MyView.vue'

Vue.use(VueRouter)

const routes = [
  // {
  //   path: '/',
  //   name: 'home',
  //   component: HomeView
  // },
  {
    path: '/tool',
    name: 'tool',
    component: ToolView
  },
  {
    path: '/tool',
    name: 'tool',
    component: ToolView
  },
  {
    path: "/toolSubmit",
    name: "ToolSubmit",
    component: ToolSubmit
  },
   {
    path: '/mypage',
    name: 'MyPage',
    component: MyPageView
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
