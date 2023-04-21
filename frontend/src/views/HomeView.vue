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
<style>
.tab button {
  background-color: white;
  border: 1px white;
  font-size: 50px;
  margin-right: 15%;
}
.tab button:last-child {
  margin-right: 0;
  margin-left: 0;
}
.tab button.active {
  color: green;
}
.tab{
  margin: 10px;
  border: 4px solid #d3cccc;
  display: flex;
  justify-content: center;
  margin: 10px;
  text-align: center;
}
.tab-content{
  background-color: rgba(202, 179, 175, 0.203);
}
</style>

<script>
import GoingTool from '../components/GoingTool.vue';

export default {
  name: "Tab",
  components: {
    GoingTool,

  },
  data() {
    return {
      currentTab: null,
      tabList: [
        { name: "인기순", component: "Employ" },
        { name: "추천순", component: "Career" },
        { name: "만들어보기", component: "GoingTool" },
      ],
    };
  },
  created() {
    // 초기 탭 설정
    this.currentTab = this.tabList[1];
  },
};
</script>

