<template>
  <div class="left-side-bar">
    <h3>페이지</h3>
    <button @click="test">데이터확인</button>
    <div class="page-all">
      <draggable v-model="bookId" :options="{animation:300, handle:'.page-body'}" class="page-list">
        <li v-for="item, index in bookId" :key="index" class="one_page">
          <div class="page-body" @click="clickPage(index)"></div>
          <label>
            {{ item.pageNumber || 0 }}
          </label>
        </li>
      </draggable>
    </div>
    <button @click="insertItem()">페이지 추가</button>
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
      // firstItem: [],
      bookId: [
        {
          pageNumber : 1,
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
      this.$emit('bookIdList', this.bookId[index]);
    },
    checkMove(evt) {
      console.log('draggedContext', evt.draggedContext);
      console.log('relatedContext', evt.relatedContext);
      return (evt.draggedContext.element.name !== 'first');
    },
    insertItem() {
      let currnet = this.currentPageNo;
      var self = this;
      var newNo = 1;
      if (self.bookId.concat().length > 0) {
        newNo = Math.max.apply(null, self.bookId.concat().map(function (item) { return item.pageNumber; })) + 1;
      }
      this.bookId.splice(
        currnet + 1,
        0,
        {
          pageNumber: newNo,
          imageList : [],
        }
      );
      this.currentPageNo += 1;
    },
    deleteItem(item, index) {
      this.items.splice(index, 1);
    },
    test() {
      console.log(this.bookId);
    }
  },
  mounted() {
    
  }
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
