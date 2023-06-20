<template>
  <div class="wrap">
    <div class="tab">
      <button
          v-for="(tab, index) in tabList"
          :key="index"
          @click.prevent="handleTabClick(tab)"
          :class="{ active: currentTab === tab }"
      >
        {{ tab.name }}
      </button>
    </div>
    <div class="tab-content">
      <component :is="currentTab.component" v-if="loginState || tabHasAccess(currentTab)"></component>
    </div>
  </div>
</template>

<script>
import GoingTool from '../components/GoingTool.vue';
import RecommendationSort from '../components/RecommendationSort.vue';
import PopularitySort from '../components/PopularitySort.vue';

export default {
  name: "Tab",
  components: {
    GoingTool,
    RecommendationSort,
    PopularitySort
  },
  data() {
    return {
      currentTab: null,
      loginState: false,
      tabList: [
        { name: "🔥인기순", component: "PopularitySort", access: true },
        { name: "🆕최신순", component: "RecommendationSort", access: true },
        { name: "🛠️만들어보기", component: "GoingTool", access: false },
      ]
    };
  },
  created() {
    if (sessionStorage.getItem('user')) {
      this.loginState = true;
    }
    // 초기 탭 설정
    this.currentTab = this.tabList[1];
  },
  methods: {
    handleTabClick(tab) {
      if (!this.loginState && !this.tabHasAccess(tab)) {
        alert('로그인 후 이용해주세요!');
      } else {
        this.currentTab = tab;
      }
    },
    tabHasAccess(tab) {
      return this.loginState || tab.access;
    }
  }
};
</script>

<style scoped>
.tab button {
  background-color: white;
  border: none;
  font-size: 30px;
  color: #333;
  flex-grow: 1;
  border-radius: 15px;
  padding: 10px 20px;
}
.tab button:last-child {
  margin-right: 0;
  margin-left: 0;
}
.tab button.active {
  color: white;
  background: #4aaee2;
}
.tab{
  display: flex;
  justify-content: center;
  margin: 20px 10px;
  text-align: center;

}
/*기존 고잉 툴 백그라운드 컬러 */
/* .tab-content{
  background-color: rgba(202, 179, 175, 0.203);
} */
</style>



