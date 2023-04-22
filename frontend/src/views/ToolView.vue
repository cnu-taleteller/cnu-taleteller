<template>
  <div class="tool">
    <div class="tool-header">
      <ToolHeader></ToolHeader>
    </div>
    <!-- <div class="tool-contnet">
      <ToolScenario></ToolScenario>
    </div> -->
    <div class="tool-content">
      <div class="tool-left">
        <ToolPageList @selectedPage="handle"></ToolPageList>
      </div>
      <div class="tool-center">
        <ToolSelectedPage :selectedPageNo="Number(this.selectPageNo)" :imageList="this.imageList"></ToolSelectedPage>
      </div>
      <div class="tool-right">
        <ToolMenu :selectedPageNo="Number(this.selectPageNo)" @imageList="setImageList"></ToolMenu>
      </div>
    </div>
  </div>
</template>
<script>
import toolHeader from '@/components/ToolHeader.vue';
import toolPageList from '@/components/ToolPageList.vue';
import toolSelectedPage from '@/components/ToolSelectedPage.vue';
import toolLayer from '@/components/ToolLayer.vue';
import toolMenu from '@/components/ToolMenu.vue';
import toolScenario from '@/components/ToolScenario.vue';

export default {
  data() {
    return {
      selectPageNo: 0,
      imageList: {},
    }
  },
  mounted() {
    if (sessionStorage.getItem('recentlyClickPageNo') != null) {
      this.selectPageNo = sessionStorage.getItem('recentlyClickPageNo');
    }
  },
  components: {
    ToolHeader: toolHeader,
    ToolPageList: toolPageList,
    ToolSelectedPage: toolSelectedPage,
    ToolLayer: toolLayer,
    ToolMenu: toolMenu,
    ToolScenario : toolScenario
  },
  methods: {
    handle(selectedPage) {
      this.selectPageNo = selectedPage;
    },
    setImageList(imageList) {
      this.imageList = imageList;
    },
  },
}

</script>
<style scoped>
.tool {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.tool-header {
  height: 10%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool-content {
  height: 90%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool-left {
  height: 100%;
  width: 15%;
}

.tool-center {
  height: 100%;
  width: 60%;
}

.tool-right {
  height: 100%;
  width: 25%;
}</style>