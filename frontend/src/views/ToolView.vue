<template>
  <div class="tool">
    <ToolHeader></ToolHeader>
    <div class="tool_content">
      <ToolPageList @selectedPage="handle"></ToolPageList>
      <div class="tool_right">
        <div class="tool_right_top">
          <ToolSelectedPage :selectedPageNo="Number(this.selectPageNo)" :imageList="this.imageList"></ToolSelectedPage>
          <ToolLayer></ToolLayer>
        </div>
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
}
.tool_content {
  height: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool_right_top {
  display: flex;
}
</style>