<template>
  <div class="wrap">
    <div class="tab">
      <button
        v-for="(tab, index) in tabList"
        :key="index"
        @click.prevent="currentTab = tab"
        :class="{ active: currentTab === tab }"
      >
        {{ tab.name }}
      </button>
    </div>
    <div class="tab-content">
      <component :is="currentTab.component"></component>
    </div>
  </div>
</template>
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
.tab-content{
  background-color: rgba(202, 179, 175, 0.203);
}
</style>

<script>
import GoingTool from '../components/GoingTool.vue';
// import RecommendationSort from '../components/RecommendationSort.vue';
import PopularitySort from '../components/PopularitySort.vue';

export default {
  name: "Tab",
  components: {
    GoingTool,
  //  RecommendationSort,
    PopularitySort
  },
  data() {
    return {
      currentTab: null,
      tabList: [
        { name: "🔥인기순", component: "PopularitySort" },
        { name: "🆕최신순", component: "PopularitySort" },
        { name: "🛠️만들어보기", component: "GoingTool" },
      ]
    };
  },
  created() {
    // 초기 탭 설정
    this.currentTab = this.tabList[1];
  },
};
</script>

