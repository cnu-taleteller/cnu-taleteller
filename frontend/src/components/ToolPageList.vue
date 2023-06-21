<template>
  <div class="left-side-bar">
    <div class="page-all">
      <draggable @start="onDragStart" @end="onDragEnd" @change="saveSession()" v-model="pageList" :draggable-options="{ animation: 300, handle: '.page-body' }"
        class="page-list" ref="pageList">
        <li v-for="page, index in pageList" :key="index" class="one-page">
          <div class="page-body" @click="clickPage(index, $event)" :class="{ 'selected': index === currentPageIndexNo }">
            <!-- 썸네일 부분 -->
            <img v-if="page.thumbnail !== '' && page.thumbnail !== null" :src="page.thumbnail"
              style="width:100%; height: 100%">
          </div>
          <label>
            {{ index + 1 }}
          </label>
        </li>
      </draggable>
    </div>
    <div>
      <button @click="addPage()"><img src="@/assets/icon.png" width="40"></button>
      <button @click="deletePage()"><img src="@/assets/trash.png" width="35" style="opacity: 0.8;"></button>
    </div>
  </div>
</template>

<script>
import draggable from 'vuedraggable';
import axios from 'axios';
import _ from 'lodash';

export default {
  components: {
    draggable,
  },
  name: 'App',
  data() {
    return {
      bookId: null,
      pageList: null,
      currentPageIndexNo: 0,
      stackStatus : [],
      stackIndex: 0,
      prevSaveStackIndex: 0,
      isStackChange: false,
      isPrev: false,
      mostRecentWorkPage: 0,
      isfirstPush : false,
      changePageIndexNo : 0,
      isRecentChange : false,
      isAdd : false,
      isDelete : false,
      isNoHaveNext : false,
      isHavaNext : false,
    }
  },
  computed: {
    allPageList() {
      return JSON.parse(JSON.stringify(this.pageList));
    }
  },
  watch: {
    allPageList: {
      handler: function (newPageList, oldPageList) {
        if (!this.isStackChange && newPageList) {

          //페이지 리스트의 currentPageIndexNo 부분을 css 속성 변경

          const newPageLists = JSON.parse(JSON.stringify(newPageList));

          //pageList 첫번째 변경이 된지 확인하는 부분
          if (oldPageList) {
            const oldPageLists = JSON.parse(JSON.stringify(oldPageList));
            if (!_.isEqual(newPageLists[0], oldPageLists[0])) {
              this.$store.commit('setCanSaveThumbNail', true);
            }
          }
          
          // const pageListMap = {
          //   value: JSON.parse(JSON.stringify(newPageList)),
          //   ctrlZPageNo: this.currentPageIndexNo,
          //   ctrlYPageNo: this.currentPageIndexNo,
          // };

          // this.changePageIndexNo = this.currentPageIndexNo;
          // this.stackStatus.push(pageListMap);
          // this.stackIndex = this.stackStatus.length - 1;

          // ctrl + z 오류가 있는 부분이 있어서 잠궈둠
          // const pageListMap = {
          //   value: newPageLists,
          //   ctrlZPageNo: this.currentPageIndexNo,
          //   ctrlYPageNo: this.currentPageIndexNo,
          // };

          // this.changePageIndexNo = this.currentPageIndexNo;
          // this.stackStatus.push(pageListMap);
          
          // this.stackIndex = this.stackStatus.length - 1;
          // console.log(this.stackStatus);
          // if (this.stackIndex - this.prevSaveStackIndex >= 2 && this.isPrev) {
          //   this.stackStatus.splice(this.prevSaveStackIndex + 1, this.stackIndex - this.prevSaveStackIndex - 1);
          //   this.isPrev = false;
          // }

          // if(this.isRecentChange) {
          //   if(!this.isAdd && !this.isDelete) { this.stackStatus[this.prevSaveStackIndex].ctrlZPageNo = this.currentPageIndexNo; }
          //   else if(this.isAdd) { this.stackStatus[this.prevSaveStackIndex].ctrlZPageNo = this.currentPageIndexNo - 1; this.isAdd = false; }
          //   else if(this.isDelete) { 
          //     if(this.isNoHaveNext) { this.stackStatus[this.prevSaveStackIndex].ctrlZPageNo = this.currentPageIndexNo + 1; this.isNoHaveNext = false; }
          //     else if(this.isHavaNext) { this.stackStatus[this.prevSaveStackIndex].ctrlZPageNo = this.currentPageIndexNo; this.isHavaNext = false; }
          //     this.isDelete = false;
          //   }
          //   this.isRecentChange = false;
          // };

          // if(this.isPrev === false) {
          //   this.prevSaveStackIndex = this.stackStatus.length - 1;
          // }
          // this.isStackPrev = false;
        }
        this.$emit('pageList', newPageList);
        this.$store.commit('setPageList', newPageList);
        this.$store.commit('setSaveState', true);
        this.isStackChange = false;
      },
      deep: true
    }
  },
  async created() {
    this.$store.commit('setCanSaveThumbNail', true);
    this.bookId = sessionStorage.getItem('bookId');
    this.firstLoadData(this.bookId);
  },

  mounted() {
    if (this.$route.path === '/tool') {
      document.addEventListener('keydown', this.ctrlZandY);
    }
  },

  beforeDestroy() {
    if (this.$route.path === '/tool') {
      document.removeEventListener('keydown', this.ctrlZandY);
    }
  },
  
  methods: {
    async firstLoadData(bookId) {
      if (bookId !== null) {
        const selPageLists = await axios.post(`${process.env.VUE_APP_API_PATH}/api/v1/tool/firstAccess/` + this.bookId);
        this.pageList = selPageLists.data.bookData.pageList;
        this.$emit('currentPageList', this.pageList[0]);
        this.$store.commit('setPageList', this.pageList);
        this.$emit('pageList', this.pageList);
      } else {
        this.pageList = [
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
              ttsVoice: '',
              ttsName: '',
              voiceList: [],
            },
            thumbnail: '',
            layerList: [],
          }
        ];
        this.$emit('currentPageList', this.pageList[0]);
        this.$store.commit('setPageList', this.pageList);
        this.$emit('pageList', this.pageList);
      };

      this.handleDataLoaded();
    },

    handleDataLoaded() {
      this.$nextTick(() => {
        const pageListElement = this.$refs.pageList.$el;
        const previousSelectedElement = pageListElement.querySelector('.page-body.selected');

        if (previousSelectedElement) {
          previousSelectedElement.style.outline = '1px solid rgb(39, 186, 255)';
        }
      });
    },

    // ctrlZandY(event) {
    //   if (event.ctrlKey) {
    //     const stack = JSON.parse(JSON.stringify(this.stackStatus));
    //     const stackLength = stack.length;
        
    //     if (event.key === 'z') {
    //       if(this.prevSaveStackIndex - 1 >= 0 && stackLength >= 1) {
    //         this.isStackChange = true;
    //         this.pageList = stack[this.prevSaveStackIndex - 1].value;
    //         this.mostRecentWorkPage = stack[this.prevSaveStackIndex - 1].ctrlZPageNo;
            
    //         this.prevSaveStackIndex -= 1;

    //         this.currentPageIndexNo = this.mostRecentWorkPage;
    //         this.$emit('currentPageList', this.pageList[this.mostRecentWorkPage]);

    //         this.isPrev = true;
    //         return;
    //       }
    //     };

    //     if (event.key === 'y') {
    //       if(this.prevSaveStackIndex + 1 <= stackLength - 1) {
    //         this.isStackChange = true;
    //         this.pageList = stack[this.prevSaveStackIndex + 1].value;
    //         this.mostRecentWorkPage = stack[this.prevSaveStackIndex + 1].ctrlYPageNo;
    //         console.log(this.pageList);
    //         this.prevSaveStackIndex += 1;

    //         this.currentPageIndexNo = this.mostRecentWorkPage;
    //         this.$emit('currentPageList', this.pageList[this.mostRecentWorkPage]);
            
    //         this.isPrev = true;
    //         return;
    //       }
    //     };
    //   };
    // },
    defalutReset() {
      this.currentPageIndexNo = items.length - 1;
    },
    //페이지 변경 시 그 페이지의 내용들을 보냄
    clickPage(index) {
      this.destroyPageOutLine();
      this.isRecentChange = true;
      this.currentPageIndexNo = index;
      this.toEmitCurrentPageList(index);
    },
    //페이지 추가부분
    addPage() {
      this.destroyPageOutLine();
      this.isAdd = true;
      let current = this.currentPageIndexNo;
      let self = this;
      let newNo = 1;
      if (self.pageList.concat().length > 0) {
        newNo = Math.max.apply(null, self.pageList.concat().map(function (item) { return item.pageId; })) + 1;
      }
      this.currentPageIndexNo += 1;
      this.pageList.splice(
        current + 1,
        0,
        {
          caption: {
            captionState: 0,
            fontSize: '',
            fontColor: '',
            content: '',
            height: '',
            width: '',
            left: '',
            top: '',
            ttsVoice:'',
            ttsName:'',
            recordedChunks: [],
          },
          thumbnail: '',
          pageId: newNo,
          pageStatus: 1,
          layerList: [],
        }
      );
      this.toEmitCurrentPageList(current + 1);
      this.saveSession();
    },
    saveSession() {
      sessionStorage.setItem(this.book_id, JSON.stringify(this.pageList));
    },
    deletePage() {
      this.destroyPageOutLine();
      this.isDelete = true;
      //리스트에 전 후 값이 있나 확인 다음이 있으면 다음으로 넘김 아니면 전으로 만약에 페이지가 1개뿐이다 이러면 삭제되고 바로 빈리스트 추가 //this.pageList[]
      if(this.pageList[this.currentPageIndexNo + 1] === undefined) {
        this.pageList.splice(this.currentPageIndexNo, 1);
        if(this.pageList.length === 0) {
          let current = this.currentPageIndexNo;
          let self = this;
          let newNo = 1;
          if (self.pageList.concat().length > 0) {
            newNo = Math.max.apply(null, self.pageList.concat().map(function (item) { return item.pageId; })) + 1;
          }
          this.currentPageIndexNo = 0;
          this.pageList.splice(
            current + 1,
            0,
            {
              caption: {
                captionState: 0,
                fontSize: '',
                fontColor: '',
                content: '',
                height: '',
                width: '',
                left: '',
                top: '',
                ttsVoice:'',
                ttsName:'',
                recordedChunks: [],
              },
              thumbnail: '',
              pageId: newNo,
              pageStatus: 1,
              layerList: [],
            }
          );
          this.toEmitCurrentPageList(current);
          return;
        }
        this.isNoHaveNext = true;
        this.currentPageIndexNo -= 1;
        this.toEmitCurrentPageList(this.currentPageIndexNo);
      } else if(this.pageList[this.currentPageIndexNo + 1] !== undefined) {
        this.isHavaNext = true;
        this.pageList.splice(this.currentPageIndexNo, 1);
        this.toEmitCurrentPageList(this.currentPageIndexNo);
      }
    },
    onDragEnd(event) {
      this.destroyPageOutLine();
      const newIndex = event.newIndex;
      this.currentPageIndexNo = newIndex;
      this.$nextTick(() => {
        const previousSelectedElement = this.$refs.pageList.$el.querySelector('.page-body.selected');
        if(previousSelectedElement) {
          previousSelectedElement.style.outline = '1px solid rgb(39, 186, 255)';
        }
      });
    },

    onDragStart(event) {
      this.destroyPageOutLine();
      const startIndex = event.oldIndex;
      this.currentPageIndexNo = startIndex;
      this.toEmitCurrentPageListForDrag(this.currentPageIndexNo);
    },

    toEmitCurrentPageList(currentPageIndexNo) {
      this.$emit('currentPageList', this.pageList[currentPageIndexNo]);
      this.$nextTick(() => {
        const previousSelectedElement = this.$refs.pageList.$el.querySelector('.page-body.selected');
        if(previousSelectedElement) {
          previousSelectedElement.style.outline = '1px solid rgb(39, 186, 255)';
        }
      });
    },

    toEmitCurrentPageListForDrag(currentPageIndexNo) {
      this.$emit('currentPageList', this.pageList[currentPageIndexNo]);
      const previousSelectedElement = this.$refs.pageList.$el.querySelector('.sortable-chosen');
      
      if(previousSelectedElement) {
        const childElement = previousSelectedElement.querySelector('.page-body');
        if(childElement) { childElement.style.outline = '1px solid rgb(39, 186, 255)'; }
      }

    },

    destroyPageOutLine() {
      const previousSelectedElement = this.$refs.pageList.$el.querySelector('.page-body.selected');
      if(previousSelectedElement) {
        previousSelectedElement.style.outline = '';
      }
    },
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
  /* height: 100%; */
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
  margin: 5px;
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