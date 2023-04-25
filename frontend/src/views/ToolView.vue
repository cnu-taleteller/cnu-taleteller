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
      currentPageList: {},
      imageIndex: 0,
      bookId: null, // 작품 번호
      pageList: [ // 작품안에 있는 페이지 여러 개 배열로 - 인덱스가 order
        {
          pageId: 1, // 작품마다 페이지 고유한 번호 
          pageStatus: 1, // 페이지 있으면 1, 삭제하면 0
          // 자막 관련
          caption: {
            size: 10,
            content: null,
            location: null,
          },
          thumbnail: null,
          // 페이지 안에 있는 파일들(레이어)
          layerList: [
            {
              id: 'item',
              layerId: '0',
              fileId: '',
              menu: 'background',
              draggable: 'true',
              style: {
                width: '1200px', // 가로사이즈
                height: '800px', // 세로사이즈
                left: "0px", // x 좌표
                top: "0px", // y 좌표
                position: "absolute",
              },
            },
          ]
        }
      ],
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
    ToolScenario: toolScenario
  },
  methods: {
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
}
</style>