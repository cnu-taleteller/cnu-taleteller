import Vue from 'vue'
import VueRouter from 'vue-router'
import ToolView from '../views/ToolView.vue'
import ToolSubmit from "../views/ToolSubmit.vue"

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: ToolView
  },
  {
    path: "/ToolSubmit",
    name: "ToolSubmit",
    component: ToolSubmit
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
