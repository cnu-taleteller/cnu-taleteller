<template>
  <div class="tool">
    <ToolHeader></ToolHeader>
    <div class="tool_content">
      <ToolPageList></ToolPageList>
      <div class="tool_right">
        <div class="tool_right_top">
          <ToolSelectedPage></ToolSelectedPage>
          <ToolLayer></ToolLayer>
        </div>
        <ToolMenu></ToolMenu>
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

    }
  },
  components: {
    ToolHeader: toolHeader,
    ToolPageList: toolPageList,
    ToolSelectedPage: toolSelectedPage,
    ToolLayer: toolLayer,
    ToolMenu: toolMenu
  }
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