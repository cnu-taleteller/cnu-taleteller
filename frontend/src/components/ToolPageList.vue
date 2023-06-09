<template>
  <div class="left-side-bar">
    <div class="page-all">
      <draggable @change="saveSession()" v-model="pageList" :draggable-options="{ animation: 300, handle: '.page-body' }"
        class="page-list">
        <li v-for="page, index in pageList" :key="index" class="one-page">
          <div class="page-body" @click="clickPage(index)">
            <!-- 썸네일 부분 -->
            <img v-if="page.thumbnail !== '' && page.thumbnail !== null" :src="page.thumbnail"
              style="width:100%; height: 100%">
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
import axios from 'axios';
import draggable from 'vuedraggable';

export default {
  components: {
    draggable,
  },
  name: 'App',
  data() {
    return {
      bookId: null,
      pageList: [
        {
          pageId: 1,
          pageStatus: 1,
          caption: {
            captionState: 0,
            fontSize: '',
            fontColor: '',
            content: '',
            height: '',
            width: '',
            left: '',
            top: '',
          },
          thumbnail: '',
          layerList: [],
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
  async mounted() {
    if (this.bookId !== null) {
      const selPageLists = await axios.post('api/v1/tool/firstAccess/' + this.bookId);
      this.pageList = selPageLists.data.pageList;
      this.$emit('currentPageList', this.pageList[0]);
      this.$emit('pageList', this.pageList);
    } else {
      this.$emit('currentPageList', this.pageList[0]);
      this.$emit('pageList', this.pageList);
    }

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
            captionState: 0,
            fontSize: '',
            fontColor: '',
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
      // this.$emit('pageList', this.pageList);
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
  height: 90vh;
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
  width: 80%;
  height: 7vw;
  background-color: white;
  box-shadow: 1px 1px 5px rgba(139, 139, 139, 0.1);
}

label {
  opacity: 0.9;
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
}
</style>
