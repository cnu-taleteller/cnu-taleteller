<template>
  <div class="left-side-bar">
    <div class="page-all">
      <draggable @start="onDragStart" @end="onDragEnd" @change="saveSession()" v-model="pageList" :draggable-options="{ animation: 300, handle: '.page-body' }"
        class="page-list" ref="pageList">
        <li v-for="page, index in pageList" :key="index" class="one-page">
          <p v-if="index == 0">1 페이지는 동화책 표지로 지정됩니다.</p>
          <div class="page-body" @click="clickPage(index, $event)" :class="{ 'selected': index === currentPageIndexNo }">
            <!-- 썸네일 부분 -->
            <img v-if="page.thumbnail !== '' && page.thumbnail !== null" :src="page.thumbnail"
              style="width:100%; height: 100%">
          </div>
          <label>
            {{ index == 0 ? '표지' : index+ 1 }}
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
      stackStatus: [],
      stackIndex: 0,
      prevSaveStackIndex: 0,
      isStackChange: false,
      isPrev: false,
      mostRecentWorkPage: 0,
      isfirstPush: false,
      changePageIndexNo: 0,
      isRecentChange: false,
      isAdd: false,
      isDelete: false,
      isNoHaveNext: false,
      isHaveNext: false,
      isFirstAccessData: true,
    }
  },
  computed: {
    allPageList() {
      return JSON.parse(JSON.stringify(this.pageList));
    }
  },
  watch: {
    allPageList: {
      handler: async function (newPageList, oldPageList) {
        if (!this.isStackChange && newPageList) {
          console.log(this.currentPageIndexNo);
          //pageList 첫번째 변경이 된지 확인하는 부분 -> 썸네일을 저장 해야하나 확인
          if (oldPageList) {
            if (!_.isEqual(newPageList[0], oldPageList[0])) {
              this.$store.commit('setCanSaveThumbNail', true);
            }
          };

          //기본적으로 배열과 컨트롤z 했을때 갈 페이지 컨트롤 y 했을때 올 페이지 매핑
          const pageListMap = {
            value: newPageList,
            ctrlZPageNo: this.currentPageIndexNo,
            ctrlYPageNo: this.currentPageIndexNo,
          };

          //음성 녹음 tts 들어오면 리스트 초기하고 새로 추가
          if(this.$store.getters.getIsVoiceInput) {
            this.stackStatus = [];
            this.$store.commit('setIsVoiceInput', false);
          }
          
          //stack 에 추가
          this.stackStatus.push(pageListMap);

          //stack 배열 길이가 15을 넘어갈 시 길이를 15 유지함
          if (this.stackStatus.length > 15) {
            this.stackStatus.shift();
          }

          //stack의 stackIndex를 stack 길이로 맞춰줌 최대 20
          this.stackIndex = this.stackStatus.length - 1;

          //컨트롤 z 했다가 추가를 하면 그 사이 값 제거
          if (this.stackIndex - this.prevSaveStackIndex >= 2) {
            this.stackStatus.splice(this.prevSaveStackIndex + 1, this.stackIndex - this.prevSaveStackIndex - 1);
          }

          //그 사이값을 제거를 하고나서 stack 배열에서 이전으로 되돌리거나 돌아온 인덱스를 초기화 해줌
          this.prevSaveStackIndex = this.stackStatus.length - 1;

          //추가나 삭제를 하면 현재 ctrl z 페이지 넘버 변경
          this.updateCtrlZPageNo(this.prevSaveStackIndex, this.isAdd, this.isDelete, this.isHaveNext, this.isNoHaveNext, this.stackStatus, this.currentPageIndexNo);

        }
        
        this.$emit('pageList', newPageList);
        this.$store.commit('setPageList', newPageList);
        this.isFirstAccess(this.isFirstAccessData);

        this.isHaveNext = false;
        this.isNoHaveNext = false;
        this.isStackChange = false;
        this.isAdd = false;
        this.isDelete = false;
      },
      deep: true
    },
  },
  async created() {
    this.$store.commit('setCanSaveThumbNail', true);
    this.$store.commit('setCurrentPageIndexNo', this.currentPageIndexNo);
    this.bookId = sessionStorage.getItem('bookId');
    this.firstLoadData(this.bookId);
  },

  mounted() {
    if (this.$route.path === '/tool') {
      document.addEventListener('keydown', this.ctrlZandY);
    }
  },

  methods: {
    async firstLoadData(bookId) {
      if (bookId !== null) {
        const selPageLists = await axios.post(`${process.env.VUE_APP_API_PATH}/api/v1/tool/firstAccess/` + this.bookId);
        this.pageList = selPageLists.data.bookData.pageList;
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
      }

      this.$emit('currentPageList', this.pageList[0]);
      this.$store.commit('setPageList', this.pageList);
      this.$emit('pageList', this.pageList);

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

    //ctrl + z and ctrl + y
    ctrlZandY(event) {
      if (!this.$store.getters.getIsCaptionInput) {
        if (event.ctrlKey) {
          const stack = JSON.parse(JSON.stringify(this.stackStatus));
          const stackLength = stack.length;

          //z click
          if (event.key === 'z') {
            if (this.prevSaveStackIndex - 1 >= 0 && stackLength >= 1 && this.$store.getters.getIsFinishDrop) {
              this.destroyPageOutLine();
              this.isStackChange = true;
              this.pageList = stack[this.prevSaveStackIndex - 1].value;
              this.mostRecentWorkPage = stack[this.prevSaveStackIndex - 1].ctrlZPageNo;

              this.prevSaveStackIndex -= 1;

              this.currentPageIndexNo = this.mostRecentWorkPage;
            
              this.$emit('currentPageList', this.pageList[this.mostRecentWorkPage]);

              this.toEmitCurrentPageList(this.currentPageIndexNo);
              return;
            }
          };

          //y click
          if (event.key === 'y') {
            if (this.prevSaveStackIndex + 1 <= stackLength - 1 && this.$store.getters.getIsFinishDrop) {
              this.destroyPageOutLine();
              this.isStackChange = true;
              this.pageList = stack[this.prevSaveStackIndex + 1].value;
              this.mostRecentWorkPage = stack[this.prevSaveStackIndex + 1].ctrlYPageNo;

              this.prevSaveStackIndex += 1;

              this.currentPageIndexNo = this.mostRecentWorkPage;
              
              this.$emit('currentPageList', this.pageList[this.mostRecentWorkPage]);

              this.toEmitCurrentPageList(this.currentPageIndexNo);
              return;
            }
          };
        };
      };
    },

    defalutReset() {
      this.currentPageIndexNo = items.length - 1;
    },

    //페이지 변경 시 그 페이지의 내용들을 보냄
    async clickPage(index) {
      this.destroyPageOutLine();
      this.isRecentChange = true;

      const isCanvasRunning = this.$store.getters.getCanvasCompleted;

      if (!isCanvasRunning) {
        await this.waitForCanvas();
      }

      this.currentPageIndexNo = index;
      this.toEmitCurrentPageList(index);
    },

    async waitForCanvas() {
      let timeout = 0;
      const reconfirm = 200;

      return new Promise((resolve, reject) => {
        const checkCanvas = () => {
          if (this.$store.getters.getCanvasCompleted) {
            resolve();
          } else if (timeout >= 5000) {
            reject(new Error('Timeout'));
          } else {
            setTimeout(checkCanvas, reconfirm);
            timeout += reconfirm;
          }
        };
        checkCanvas();
      });
    },

    //페이지 추가부분
    addPage() {
      this.destroyPageOutLine();
      this.isAdd = true;
      this.$store.commit('setIsChange', false);
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
            ttsVoice: '',
            ttsName: '',
            voiceList: [],
          },
          thumbnail: '',
          pageId: newNo,
          pageStatus: 1,
          layerList: [],
        }
      );
      this.toEmitCurrentPageList(current + 1);
      this.$store.commit('setCurrentPageIndexNo', this.currentPageIndexNo);
      this.saveSession();
    },

    saveSession() {
      sessionStorage.setItem(this.book_id, JSON.stringify(this.pageList));
    },

    deletePage() {
      this.destroyPageOutLine();
      this.$store.commit('setIsChange', false);
      this.isDelete = true;
      //리스트에 전 후 값이 있나 확인 다음이 있으면 다음으로 넘김 아니면 전으로 만약에 페이지가 1개뿐이다 이러면 삭제되고 바로 빈리스트 추가 //this.pageList[]
      if (this.pageList[this.currentPageIndexNo + 1] === undefined) {
        this.pageList.splice(this.currentPageIndexNo, 1);
        if (this.pageList.length === 0) {
          let current = this.currentPageIndexNo;
          let self = this;
          let newNo = 1;
          if (self.pageList.concat().length > 0) {
            newNo = Math.max.apply(null, self.pageList.concat().map(function (item) { return item.pageId; })) + 1;
          }
          this.currentPageIndexNo = 0;
          this.$store.commit('setCurrentPageIndexNo', this.currentPageIndexNo);
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
                ttsVoice: '',
                ttsName: '',
                voiceList: [],
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
      } else if (this.pageList[this.currentPageIndexNo + 1] !== undefined) {
        this.isHaveNext = true;
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
        if (previousSelectedElement) {
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
      this.$store.commit('setCurrentPageIndexNo', currentPageIndexNo);
      this.$emit('currentPageList', this.pageList[currentPageIndexNo]);
      this.$nextTick(() => {
        const previousSelectedElement = this.$refs.pageList.$el.querySelector('.page-body.selected');
        if (previousSelectedElement) {
          previousSelectedElement.style.outline = '1px solid rgb(39, 186, 255)';
        }
      })
    },

    toEmitCurrentPageListForDrag(currentPageIndexNo) {
      this.$store.commit('setCurrentPageIndexNo', currentPageIndexNo);
      this.$emit('currentPageList', this.pageList[currentPageIndexNo]);
      if (this.$refs.pageList) {
        const previousSelectedElement = this.$refs.pageList.$el.querySelector('.sortable-chosen');
        if (previousSelectedElement) {
          const childElement = previousSelectedElement.querySelector('.page-body');
          if (childElement) { childElement.style.outline = '1px solid rgb(39, 186, 255)'; }
        }
      }
    },

    destroyPageOutLine() {
      if (this.$refs.pageList) {
        const previousSelectedElement = this.$refs.pageList.$el.querySelector('.page-body.selected');
        if (previousSelectedElement) {
          previousSelectedElement.style.outline = '';
        }
      }
    },

    updateCtrlZPageNo(prevSaveStackIndex, isAdd, isDelete, isHaveNext, isNoHaveNext, stackStatus, currentPageIndexNo) {

      if (prevSaveStackIndex > 0 && (!isAdd && !isDelete)) {
        stackStatus[prevSaveStackIndex - 1].ctrlZPageNo = currentPageIndexNo;
      }

      if (prevSaveStackIndex > 0 && isAdd) {
        stackStatus[prevSaveStackIndex - 1].ctrlZPageNo = currentPageIndexNo - 1;
      }

      if (prevSaveStackIndex > 0 && isDelete) {
        if (isHaveNext) {
          stackStatus[prevSaveStackIndex - 1].ctrlZPageNo = currentPageIndexNo;
        } else if (isNoHaveNext) {
          stackStatus[prevSaveStackIndex - 1].ctrlZPageNo = currentPageIndexNo + 1;
        }
      }
    },

    addVoice() {
      
    },

    isFirstAccess() {
      if(this.isFirstAccessData) {
        this.$store.commit('setSaveState', false);
        this.isFirstAccessData = false;
      } else {
        this.$store.commit('setSaveState', true);
      }
    },
    async waitForCanvas() {
      let timeout = 0;
      const reconfirm = 200;

      return new Promise((resolve, reject) => {
        const checkCanvas = () => {
          if (this.$store.getters.getCanvasCompleted) {
            resolve();
          } else if (timeout >= 5000) {
            reject(new Error('Timeout'));
          } else {
            setTimeout(checkCanvas, reconfirm);
            timeout += reconfirm;
          }
        };
        checkCanvas();
      });
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