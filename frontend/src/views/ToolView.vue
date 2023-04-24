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
        <ToolPageList @currentPageList="handlePageList"></ToolPageList>
      </div>
      <div class="tool-center">
        <ToolSelectedPage :currentPageList="this.currentPageList" :imageIndex="this.imageIndex"></ToolSelectedPage>
      </div>
      <div class="tool-right">
        <ToolMenu :currentPageList="this.currentPageList" @imageIndex="handleImageIndex"></ToolMenu>
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
      currentPageList : {},
      imageIndex : 0,
    }
  },
  mounted() {
    if(sessionStorage.getItem('recentlyClickPageNo') != null) {
      this.selectPageNo = sessionStorage.getItem('recentlyClickPageNo');
    }
  },
  components: {
    ToolHeader: toolHeader,
    ToolPageList: toolPageList,
    ToolSelectedPage: toolSelectedPage,
    ToolLayer: toolLayer,
    ToolMenu: toolMenu,
    ToolScenario: toolScenario
  },
  methods : {
    handlePageList(currentPageList) {
      this.currentPageList = currentPageList;
    },
    handleImageIndex(imageIndex) {
      this.imageIndex = imageIndex;
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