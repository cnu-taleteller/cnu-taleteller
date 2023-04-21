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
      pageArr: [
          {
          pageNo : 1,
          pageStatus: 1,
          imageList : [
            {
              src : '/images/field.png',
              id : 'item',
              objId : '0',
              menu: 'background',
              draggable : "true", 
              style : {
                left : "0px",
                top : "0px",
                position : "absolute",
                width: '1200px',
                height: '800px',
              },
            },
          ]
        }
      ],
      currentPageNo: 1,
    }
  },
  methods: {
    defalutReset() {
      this.currentPageNo = items.length - 1;
    },
    clickPage(index) {
      this.currentPageNo = index;
      this.$emit('currentPageList', this.pageArr[index]);
    },
    addPage() {
      let currnet = this.currentPageNo;
      var self = this;
      var newNo = 1;
      if (self.pageArr.concat().length > 0) {
        newNo = Math.max.apply(null, self.pageArr.concat().map(function (item) { return item.pageNo; })) + 1;
      }
      this.pageArr.splice(
        currnet + 1,
        0,
        {
          pageNo: newNo,
          pageStatus: 1,
          imageList : [],
        }
      );
      this.currentPageNo += 1;
      this.saveSession();
    },
    deleteItem(item, index) {
      this.items.splice(index, 1);
    },
    saveSession(){
      sessionStorage.setItem(this.book_id, JSON.stringify(this.pageArr));
    },
  },
}
</script>
<style>
.left-side-bar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 10px;
  height: 620px;
  border: 1px solid gray;
}

.page-all {
  width: 90%;
  height: 500px;
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
  width: 80%;
  height: 100px;
  border: 1px solid gray;
}

.page-list {
  list-style: none;
}

</style>
