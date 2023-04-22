<template>
  <div class="left-side-bar">
    <h3>페이지</h3>
    <div class="page-all">
      <draggable @change="saveSession()" v-model="pageArr" :options="{ animation: 300, handle: '.page-body' }" class="page-list">
        <li v-for="page, index in pageArr" :key="index" class="one_page">
          <div class="page-body" @click="clickPage(index)"></div>
          <label>
            {{ page.pageNo }}
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
      pageArr: [],
      currentPageNo: 1,
    }
  },
  created() {
    this.book_id = sessionStorage.getItem('book_id');
    this.pageArr.push({
      pageNo: 1,
      pageStatus: 1, // 페이지 있으면 1, 삭제하면 0
    })
  },
  methods: {
    addPage(){
      let newNo = 1;
      if (this.pageArr.concat().length > 0) {
        newNo = Math.max.apply(null, this.pageArr.concat().map(
          function (page) { 
            return page.pageNo; 
          })) + 1;
      }

      this.pageArr.push({
        pageNo: newNo,
        pageStatus: 1, 
      })
      this.saveSession();
    },
    clickPage(index) {
      this.currentPageNo = index;
      this.$emit('selectedPage', this.pageArr[index].pageNo);
      console.log(this.pageArr[this.currentPageNo]);
    },
    saveSession(){
      sessionStorage.setItem(this.book_id, JSON.stringify(this.pageArr));
    }
    // deletePage(item, index) {
    //   this.items.splice(index, 1);
    // },
  },
  //페이지를 세션 저장해서 불러오기 2023 04 11 일단 기존 방식사용
  // mounted() {
  //   if (JSON.parse(sessionStorage.getItem('pageNumber')) == null) {
  //     this.items = [];
  //   } else {
  //     this.items = JSON.parse(sessionStorage.getItem('pageNumber'));
  //   };
  // }

}
</script>
<style>
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
