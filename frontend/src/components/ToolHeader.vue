<template>
  <div class="header">
    <div v-if="!edit" class="header-title">
      <input class="book-name-input" type="text" v-model="bookName" placeholder="동화책 이름을 입력해주세요." @click="editBookName()">
      <img class="header-btn" @click="editBookName()" src="@/assets/pencil.png">
    </div>
    <div v-else class="header-title">
      <input class="book-name-input" type="text" v-model="bookName" @keyup.enter="editBookName()">
      <img class="header-btn" @click="editBookName()" src="@/assets/check.png">
    </div>
    <div class="header-menu" v-if="toolState != 'new' && toolState != 'gpt'">
      <button @click="preview()">미리보기</button>
      <button @click="saveTmp()">임시저장</button>
      <button @click="saveBook()">제출</button>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      isPreviewDialogVisible: false,
      bookName: null,
      pop: null,
      edit: false,
      bookId: null,
      finalScenario: []
    };
  },
  props: {
    scenarioKeyword: Object,
    pageList: Array,
    currentPageList: Object,
    toolState: String
  },
  created() {
    if (sessionStorage.getItem('bookName')) {
      this.bookName = sessionStorage.getItem('bookName');
    }
    this.bookId = this.$store.getters.getBookId;
  },
  methods: {
    editBookName() {
      this.edit = !!!this.edit;
    },
    // 제출
    saveBook() {
      // 시나리오 선택되어야 진행
      const select = sessionStorage.getItem('select');
      
      if (!select || select == 'false') {
        alert('시나리오 선택 후 진행해주세요');
        return;
      }
      sessionStorage.removeItem('scenario');
      sessionStorage.removeItem('scenarioKeyword');
      sessionStorage.setItem('bookName', this.bookName);
      this.$router.push('/ToolSubmit');
    },
    async setCanvasCompleted() {
      await this.$store.dispatch('setCanvasCompleted', true)
    },
    // 임시 저장
    async saveTmp() {
      try {
        const isCanvasRunning = this.$store.getters.getCanvasCompleted;

        if (!isCanvasRunning) {
          await this.waitForCanvas();
        }

        const saveState = this.$store.getters.getSaveState;
        const select = sessionStorage.getItem('select');

        if (saveState) {
          if (select != null) {
            alert('시나리오 선택 후 진행해주세요!');
            return;
          } else {
            // 최초 저장
            if (this.bookId == null) {
              await axios.post("/api/v1/book/", {
                bookName: this.bookName,
                bookStatus: "temp",
                email: sessionStorage.getItem('user'),
                pageList: this.pageList,
              })
                .then((res) => {
                  console.log(res.data.bookId);
                  this.$store.commit('setBookId', res.data.bookId);
                  this.saveScenario();
                  this.saveUploadFile();
                  alert('임시저장 완료');
                })
                .catch((err) => {
                  console.error(err);
                  alert('임시저장 실패');
                })
            }
            else {
              // 이미 bookId 있을 때
              await axios.post("/api/v1/book/" + this.bookId, {
                bookName: this.bookName,
                bookStatus: "temp",
                pageList: this.pageList,
              })
                .then((res) => {
                  console.log(res);
                  this.saveScenario();
                  this.saveUploadFile();
                  alert('임시저장 완료');
                })
                .catch((error) => {
                  console.log(error);
                  alert('임시저장 실패');
                });
            }
          }
          this.$store.commit('setSaveState', false);
        }
      } catch (err) {
        console.error(err);
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

    async saveUploadFile() {
      const uploadCharList = JSON.parse(sessionStorage.getItem('uploadCharList'));
      const uploadBackList = JSON.parse(sessionStorage.getItem('uploadBackList'));
      if (uploadBackList === null && uploadBackList === null) return;

      if (uploadBackList === null) {
        await axios.post("/api/v1/tool/uploadFile/" + this.bookId, {
          uploadCharList,
        })
          .then((res) => {
            console.log(res);
          })
          .catch((error) => {
            console.log(error);
          });
      }
      else if (uploadCharList === null) {
        await axios.post("/api/v1/tool/uploadFile/" + this.bookId, {
          uploadBackList,
        })
          .then((res) => {
            console.log(res);
          })
          .catch((error) => {
            console.log(error);
          });
      }
      else {
        await axios.post("/api/v1/tool/uploadFile/" + this.bookId, {
          uploadBackList, uploadCharList
        })
          .then((res) => {
            console.log(res);
          })
          .catch((error) => {
            console.log(error);
          });
      }

    },
    async saveScenario() {
      const scenario = sessionStorage.getItem('scenario');
      if (scenario === null) return;

      await axios.post("/api/v1/tool/scenario/" + this.bookId, JSON.stringify(scenario), {
      headers: {
          'Content-Type': 'application/json'
        }
      })
        .then((res) => {
          console.log(res);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    //미리보기 클릭시 새창을 띄우고 작품썸네일을 보여준다.
    preview() {
      let currentIndex = 0;

      const screenWidth = window.screen.width;
      const screenHeight = window.screen.height;
      const windowWidth = 810;
      const windowHeight = 600;
      const left = (screenWidth - windowWidth) / 2;
      const top = (screenHeight - windowHeight) / 2;

      let newWindow = window.open('', 'previewWindow', `width=${windowWidth}, height=${windowHeight}, left=${left}, top=${top}`);
      newWindow.document.body.innerHTML = `
        <div id=list-wrapper>
          <div id="list"></div>
        </div>
        <div id="button-wrapper">
          <button id="prev" onclick="prev()">이전</button>
          <button id="next" onclick="next()">다음</button>
        </div>
        `;
      newWindow.document.head.innerHTML = `
        <style>
        @import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Sans+KR&display=swap');
        
          #button-wrapper {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            z-index: 3;
          }
          #list-wrapper{
            width: 100%;
            height: 94%;
          }
          #list {
            width: 800px;
            height: 500px;
          }
          button {
            margin-left: 10px;
            padding: 5px 10px;
            background-color: white;
            border: 1px solid #ccc;
            font-size: 14px;
          }
          button:hover {
            background-color: #ccc;
            border:none;
          }

          body {
  font-family: 'IBM Plex Sans KR', Avenir, Helvetica, Arial, sans-serif;
}
      
        </style>
        `;
      const list = newWindow.document.getElementById('list');
      this.layerList(list, currentIndex, newWindow);
      this.captionList(list, currentIndex, newWindow);

      if (currentIndex == 0) {
        newWindow.document.querySelector('#prev').disabled = true;
      }

      if (currentIndex == this.pageList.length || currentIndex == this.pageList.length - 1) {
        newWindow.document.querySelector('#next').disabled = true;
      }

      newWindow.prev = () => {
        if (currentIndex > 0) {
          currentIndex--;
        }

        while (list.firstChild) {
          list.removeChild(list.firstChild);
        }

        this.layerList(list, currentIndex, newWindow);
        this.captionList(list, currentIndex, newWindow);

        if (currentIndex == 0) {
          newWindow.document.querySelector('#prev').disabled = true;
        }

        if (currentIndex < this.pageList.length - 1) {
          newWindow.document.querySelector('#next').disabled = false;
        } else {
          newWindow.document.querySelector('#next').disabled = true;
        }
      };

      newWindow.next = () => {
        if (currentIndex < this.pageList.length - 1) {
          currentIndex++;
        }

        if (currentIndex == this.pageList.length - 1) {
          newWindow.document.querySelector('#next').disabled = true;
        }

        while (list.firstChild) {
          list.removeChild(list.firstChild);
        }

        this.layerList(list, currentIndex, newWindow);
        this.captionList(list, currentIndex, newWindow);

        if (currentIndex > 0) {
          newWindow.document.querySelector('#prev').disabled = false;
        } else {
          newWindow.document.querySelector('#prev').disabled = true;
        }
      };
    },
    layerList(list, currentIndex, newWindow) {
      if (this.pageList[currentIndex].layerList != null) {
        for (const [index, image] of Object.entries(this.pageList[currentIndex].layerList)) {
          const imageEle = newWindow.document.createElement('img');
          imageEle.src = image.fileId;
          imageEle.id = image.id;
          if (image.id.includes("background")) {
            imageEle.style.left = 0;
            imageEle.style.top = 0;
            imageEle.style.width = '100%';
            imageEle.style.height = '100%';
          }
          else {
            imageEle.style.left = image.style.left;
            imageEle.style.top = image.style.top;
            imageEle.style.width = image.style.width;
            imageEle.style.height = image.style.height;
          }
          imageEle.style.position = image.style.position;
          imageEle.setAttribute('draggable', 'false');
          imageEle.style.zIndex = 1;
          list.appendChild(imageEle);
        };
      };
    },
    captionList(list, currentIndex, newWindow) {
      if (this.pageList[currentIndex].caption.content !== null) {
        const caption = this.pageList[currentIndex].caption;
        const divEle = newWindow.document.createElement('div');
        divEle.style.left = caption.left;
        divEle.style.top = caption.top;
        divEle.style.width = caption.width;
        divEle.style.height = caption.height;
        divEle.style.fontWeight = "bold";
        divEle.style.fontSize = caption.fontSize;
        divEle.style.position = "absolute";
        divEle.style.textAlign = "center";
        divEle.style.color = caption.fontColor;
        divEle.innerText = caption.content;
        divEle.style.zIndex = 2;
        list.appendChild(divEle);
      }
    },
  },
}
</script>
<style scoped>
.header {
  width: 100%;
  height: 5vh;
  /* height: 100%; */
  /* background-color: #6CC4F0;  */
  background-color: #bce9ff;
  border: 1px solid rgb(231, 231, 231);
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title {
  margin: 5px;
  width: 20%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.book-name-input {
  height: 4vh;
  border: none;
  outline: none;
  font-size: 15px;
  font-weight: bold;
  border-bottom: 2px solid gray;
  background-color: #bce9ff;
  text-align: center;
}

.header-menu {
  width: 80%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.header-btn {
  width: 20px;
}

.header-btn:hover {
  opacity: 0.7;
}

button {
  background-color: #bce9ff;
  margin-right: 20px;
  border: none;
  /* background-color: white; */
  padding: 5px 10px;
  font-weight: bold;
}

button:hover {
  opacity: 0.7;
  background-color: #bce9ff;
}
</style>