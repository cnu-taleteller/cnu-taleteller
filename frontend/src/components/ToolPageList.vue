<template>
  <div class="left-side-bar">
    <div class="page-all">
      <draggable @change="saveSession()" v-model="pageList" :draggable-options="{ animation: 300, handle: '.page-body' }"
        class="page-list">
        <li v-for="page, index in pageList" :key="index" class="one_page">
          <div class="page-body" @click="clickPage(index)">
            <!-- 썸네일 부분 -->
            <img v-if="page.thumbnail != null" :src="page.thumbnail" style="width:90%; height: 90%">
          </div>
          <label>
            {{ page.pageId }}
          </label>
        </li>
      </draggable>
    </div>
    <div>
      <button @click="addPage()"><img src="@/assets/icon.png" width="40"></button>
      <button><img src="@/assets/trash.png" width="35" style="opacity: 0.8;"></button>
    </div>
  </div>
</template>

<script>
import draggable from 'vuedraggable';

export default {
  components: {
    draggable,
  },
  name: 'App',
  data() {
    return {
      book_id: null,
      //현재 사용중인 데이터
      pageList: [
        {
          pageId : 1,
          pageStatus: 1,
          caption : {
            fontSize: "20px",
            fontColor : '#000000',
            content: null,
            height: null,
            width: null,
            left: null,
            top: null,
          },
          thumbnail: null,
          layerList: []
        }
      ],
      currentPageNo: 1,
    }
  },
  watch: {
  pageList: {
    handler: function (newPageList) {
      this.$emit('pageList', newPageList);
    },
    deep: true,
  },
},
  created() {
    this.book_id = sessionStorage.getItem('book_id');
  },
  mounted() {
    //기본적으로 DOM에 내용이 만들어지면 배열의 첫번째 요소를 보냄 들어오면 1번 페이지를 보여주기 위해서
    this.$emit('currentPageList', this.pageList[0]);
    this.$emit('pageList', this.pageList);
  },
  methods: {
    defalutReset() {
      this.currentPageNo = items.length - 1;
    },
    //페이지 변경 시 그 페이지의 내용들을 보냄
    clickPage(index) {
      this.currentPageNo = index;
      this.$emit('currentPageList', this.pageList[index]);
    },
    //페이지 추가부분
    addPage() {
      let currnet = this.currentPageNo;
      var self = this;
      var newNo = 1;
      if (self.pageList.concat().length > 0) {
        newNo = Math.max.apply(null, self.pageList.concat().map(function (item) { return item.pageId; })) + 1;
      }
      this.pageList.splice(
        currnet + 1,
        0,
        {
          caption: {
            fontSize: "20px",
            fontColor : '#000000',
            content: null,
            height: null,
            width: null,
            left: null,
            top: null,
          },
          thumbnail: null,
          pageId: newNo,
          pageStatus: 1,
          layerList: [],
        }
      );
      this.currentPageNo += 1;
      this.saveSession();
    },
    saveSession() {
      sessionStorage.setItem(this.book_id, JSON.stringify(this.pageList));
    }
    // deletePage(item, index) {
    //   this.items.splice(index, 1);
    // },
  },
}
</script>
<style scoped>
.left-side-bar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-right: 1px solid #dfdfdf;
  height: 100%;
}

.page-all {
  width: 90%;
  height: 80vh;
  overflow-y: scroll;
}

.page-all::-webkit-scrollbar {
  width: 10px;
}

.page-all::-webkit-scrollbar-thumb {
  background-color: rgb(194, 194, 194);
  border-radius: 10px;
  background-clip: padding-box;
  border: 2px solid transparent;
}

.page-all::-webkit-scrollbar-track {
  background-color: white;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}

.one-page {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
}

.page-body {
  width: 70%;
  height: 7vw;
  border: #ccc solid 1px;
}

label {
  opacity: 0.95;
}

.page-list {
  list-style: none;
}

button {
  margin-right: 20px;
  border: none;
  background-color: #F7F7F7;
}

button:hover {
  opacity: 0.7;
}</style>
