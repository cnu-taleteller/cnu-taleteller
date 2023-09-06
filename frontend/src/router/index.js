import Vue from 'vue';
import VueRouter from 'vue-router';
import HomeView from '../views/HomeView.vue';
import ToolView from '../views/ToolView.vue';
import MyPageWork from '../views/MypageViewWork.vue';
import MyPagePoint from '../views/MypageViewPoint.vue';
import MyPageChargeResult from '../views/MypageViewChargeResult.vue';
import MyPageReturnResult from '../views/MypageViewReturnResult.vue';
import MyPageMemInfo from '../views/MypageViewMemInfo.vue';
import MyPageBookMark from '../views/MypageViewBookMark.vue';
import ToolSubmitView from '../views/ToolSubmitView.vue';
import SearchView from "../views/SearchView.vue";
import BookDetailView from "../views/BookDetailView.vue";
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import Preview from '../views/Preview.vue'
import ChangeKeyword from '../views/ToolChangeKeywordView.vue'
import ModalPassword from '../views/ModalPassword.vue'
import GoingTool from '../components/GoingTool.vue';
import AdminView from "@/views/AdminView.vue";
import ErrorView from "@/views/ErrorView.vue";



Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/tool',
    name: 'tool',
    component: ToolView
  },
  {
    path: "/toolSubmit",
    name: "ToolSubmit",
    component: ToolSubmitView
  },
  {
    path: "/keyword",
    name: "ChangeKeyword",
    component: ChangeKeyword
  },
  {
    path: "/preview",
    name: "Preview",
    component: Preview
  },
  {
    path: '/mypage/workmanage',
    name: 'MyPageWork',
    component: MyPageWork
  },
  {
    path: '/mypage/pointmanage',
    name: 'MyPagePoint',
    component: MyPagePoint
  },
  {
    path: '/mypage/chargeResult',
    name: 'MyPageChargeResult',
    component: MyPageChargeResult
  },
  {
    path: '/mypage/returnResult',
    name: 'MyPageReturnResult',
    component: MyPageReturnResult
  },
  {
    path: '/mypage/member',
    name: 'MyPageMemInfo',
    component: MyPageMemInfo
  },
  {
    path: '/mypage/bookmark',
    name: 'MyPageBookMark',
    component: MyPageBookMark
  },
  {
    path: '/search',
    name: 'Search',
    component: SearchView
  },
  {
    path: '/detail/:id',
    name: 'BookDetail',
    component: BookDetailView
  },
  {
    path: '/loginview',
    name: 'LoginView',
    component: LoginView
  },
  {
    path: '/signupview',
    name: 'SignupView',
    component: SignupView
  },
  {
    path: '/modalpassword',
    name: 'ModalPassword',
    component: ModalPassword
  },
  {
    path: '/goingTool',
    name: 'GoingTool',
    component: GoingTool
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminView
  },
  {
    path: '/error',
    name: 'Error',
    component: ErrorView
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router