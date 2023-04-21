<template>
  <div class="tool">
    <div class="tool-header">
    <ToolHeader></ToolHeader>
    </div>
    <div class="tool-content">
      <div class="tool-left">
      <ToolPageList @currentPageList="handlePageList"></ToolPageList>
    </div>
      <div class="tool-right">
        <div class="tool-right-top">
          <ToolSelectedPage :currentPageList="this.currentPageList" :imageIndex="this.imageIndex"></ToolSelectedPage>
          <ToolLayer></ToolLayer>
        </div>
        <div class="tool-right-bottom">
        <ToolMenu :currentPageList="this.currentPageList" @imageIndex="handleImageIndex"></ToolMenu>
      </div>
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
    ToolMenu: toolMenu
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
.tool-header{
  height: 20%;
  width: 90%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.tool-content {
  height: 80%;
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.tool-right{
  width: 80%;
}
.tool-left{
  width: 20%;
  height: 100%;
}
.tool-right-top {
  display: flex;
  height: 70%;
}
.tool-right-bottom{
  height: 30%;
}
</style>