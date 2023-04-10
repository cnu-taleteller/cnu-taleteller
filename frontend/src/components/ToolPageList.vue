<template>
  <div class="left_side_bar">
    <h3>페이지</h3>
    <div class="page_all">
      <!-- <div @click="clickPage(-1)">??</div> -->
      <draggable v-model="items" handle=".handle" class="page_list">
        <div v-for="item, index in items" :key="index" class="one_page">
          <div class="page_body" @click="clickPage(index)"></div>
          <label>
            {{ item.no }}
          </label>
        </div>
      </draggable>
      <!-- <div @click="defalutReset()">
        리셋
      </div> -->
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
      firstItem: [],
      items: [],
      currentPageNo: 1,
    }
  },
  methods: {
    defalutReset() {
      this.currentPageNo = items.length - 1;
    },
    clickPage(index) {
      alert('click ' + (index + 1) + ' motion');
      this.currentPageNo = index;
      sessionStorage.clear();
      sessionStorage.setItem('selectedPage', index+1);
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

      if (self.items.concat().length > 0) {
        newNo = Math.max.apply(null, self.items.concat().map(function (item) { return item.no; })) + 1;
      }

      this.items.splice(
        currnet + 1,
        0,
        {
          no: newNo,
          name: '추가' + newNo,
        }
      );
      this.currentPageNo += 1;
    },
    deleteItem(item, index) {
      this.items.splice(index, 1);
    },
  },
  // created() {
  //     axios.get('/static/list.json').then(response => {
  //       this.firstItem = response.data.clickPageList[0];
  //       let cnt = 0;
  //       for(let i=1; i<response.data.clickPageList.length; i++) {
  //         this.items[cnt] = response.data.clickPageList[i];
  //         cnt++;
  //       }
  //     });
  //   }
}
</script>
<style>
.left_side_bar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 10px;
  width: 200px;
  height: 620px;
  /* height: 90vh; */
  border: 1px solid gray;
}

.page_all {
  width: 200px;
  height: 500px;
  /* height: 80vh; */
  overflow-y: scroll;
}
.page_all::-webkit-scrollbar {
  display: none;
}

.one_page {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
}

.page_body {
  width: 150px;
  height: 150px;
  border: 1px solid gray;
}

.page_list {
  list-style: none;
}

</style>
