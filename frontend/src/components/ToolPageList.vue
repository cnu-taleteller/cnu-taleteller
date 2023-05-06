<template>
  <div class="left-side-bar">
    <h3>페이지</h3>
    <div class="page-all">
      <draggable @change="saveSession()" v-model="pageList" :draggable-options="{ animation: 300, handle: '.page-body' }"
        class="page-list">
        <li v-for="page, index in pageList" :key="index" class="one_page">
          <div class="page-body" @click="clickPage(index)"></div>
          <label>
            {{ page.pageId }}
          </label>
        </li>
      </draggable>
    </div>
    <button @click="addPage()">페이지 추가</button>
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
      pageList: [
      {
          pageId : 1,
          pageStatus: 1,
          caption : {
            size: 10,
            content: null,
            location: null,
            isTextAreaVisible: false,
          },
          thumbnail: null,
          layerList : []
        }
      ],
      currentPageNo: 1,
      sortableOptions: {
        animation: 300,
        handle: '.page-body'
      },
    }
  },
  created() {
    this.book_id = sessionStorage.getItem('book_id');
  },
  mounted() {
    this.$emit('currentPageList', this.pageList[0]);
  },
  methods: {
    defalutReset() {
      this.currentPageNo = items.length - 1;
    },
    clickPage(index) {
      this.currentPageNo = index;
      this.$emit('currentPageList', this.pageList[index]);
    },
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
            size: 10,
            content: null,
            location: null,
            isTextAreaVisible: false,
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
  margin: 10px;
  height: 100%;
  border: 1px solid gray;
}

.page-all {
  width: 90%;
  height: 70vh;
  /* height: 80vh; */
  overflow-y: scroll;
}

.page-all::-webkit-scrollbar {
  display: none;
}

.one-page {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
}

.page-body {
  /* width: 80%; */
  height: 120px;
  border: 1px solid gray;
}

.page-list {
  list-style: none;
}
</style>
