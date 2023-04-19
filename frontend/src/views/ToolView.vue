<template>
  <div class="tool">
    <div class="tool-header">
    <ToolHeader></ToolHeader>
    </div>
    <div class="tool-content">
      <div class="tool-left">
      <ToolPageList @selectedPage="handle"></ToolPageList>
    </div>
      <div class="tool-right">
        <div class="tool-right-top">
          <ToolSelectedPage :selectedPageNo="Number(this.selectPageNo)" :imageList="this.imageList"></ToolSelectedPage>
          <ToolLayer></ToolLayer>
        </div>
        <div class="tool-right-bottom">
        <ToolMenu :selectedPageNo="Number(this.selectPageNo)" @imageList="setImageList"></ToolMenu>
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
      selectPageNo : 0,
      imageList : {},
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
.tool-header{
  height: 20%;
  width: 90%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.tool-content {
  height: 80%;
  width: 90%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.tool-right{
  width: 75%;
}
.tool-left{
  width: 15%;
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