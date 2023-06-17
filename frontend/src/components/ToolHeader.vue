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
      <button @click="makeFirstThumbNail()">썸네일만들기</button>
      <button @click="preview2()">미리보기2</button>
      <button @click="saveTmp('temp')">임시저장</button>
      <button @click="saveBook()">제출</button>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      s3: {
        preSignedUrl: null,
        encodedFileName: null,
        uploadedUrl: null,
      },
      isSave: false,
      isPreviewDialogVisible: false,
      bookName: "",
      edit: false,
      bookId: null,
      finalScenario: []
    };
  },
  props: {
    scenarioKeyword: Object,
    pageList: Array,
    currentPageList: Object,
    toolState: String,
    pageObject: HTMLDivElement,
  },
  created() {
    if (sessionStorage.getItem('bookName')) {
      this.bookName = sessionStorage.getItem('bookName');
    }
    this.bookId = sessionStorage.getItem('bookId');
    if (this.bookId) {
      this.isSave = true;
    }
  },
  methods: {
    editBookName() {
      this.edit = !!!this.edit;
    },
    // 제출
    async saveBook() {
      const select = sessionStorage.getItem('select');

      if (!select || select == 'false') {
        alert('시나리오 선택 후 진행해주세요');
        return;
      }
      await this.saveTmp('submit');
      sessionStorage.setItem('bookName', this.bookName);
      this.$router.push('/ToolSubmit');
    },
    async setCanvasCompleted() {
      await this.$store.dispatch('setCanvasCompleted', true)
    },
    // 임시 저장
    async saveTmp(status) {
      //canvas 작업 기다림
      const isCanvasRunning = this.$store.getters.getCanvasCompleted;

      if (!isCanvasRunning) {
        await this.waitForCanvas();
      }

      //저장이 가능한 상태인가 -> pageList 가 변경이 되었는가
      const saveState = this.$store.getters.getSaveState;

      const select = sessionStorage.getItem('select');

      if (!select || select == 'false') {
        alert('시나리오 선택 후 진행해주세요');
        return;
      }
      else {
        if (this.isSave == false && saveState) {
          await axios.post("/api/v1/book/tmp", {
            bookName: this.bookName,
            bookStatus: 'temp',
            email: sessionStorage.getItem('user'),
            pageList: this.pageList,
          })
            .then((res) => {
              this.bookId = res.data;
              sessionStorage.setItem('bookId', this.bookId);
              this.saveThumbnailScenario();
              this.saveUploadFile();
              this.saveVoice();
              this.isSave = true;
              if (status === 'temp') alert('저장 완료');
            })
            .catch((err) => {
              console.error(err);
              alert('서버 오류로 저장에 실패하였습니다. 잠시 후 이용해주세요.🥲')
            })
        }
        else {
          if (saveState) {
            await axios.post("/api/v1/book/" + this.bookId, {
              bookName: this.bookName,
              bookStatus: 'temp',
              pageList: this.pageList,
            })
              .then((res) => {
                console.log(res);
                this.saveUploadFile();
                this.saveThumbnailScenario();
                this.saveVoice();
                this.isSave = true;
                if (status === 'temp') alert('저장 완료');
              })
              .catch((error) => {
                console.log(error);
                alert('서버 오류로 저장에 실패하였습니다. 잠시 후 이용해주세요.🥲')
              });
          }
        }
        this.$store.commit('setSaveState', false);
      }
    },

    async saveThumbnailScenario() {
      const scenario = sessionStorage.getItem('scenario');
      const saveThumbnail = this.$store.getters.getCanSaveThumbNail;
      if (this.pageList && saveThumbnail) {
        const pageOneThumbNail = this.pageList[0].thumbnail;
        let base64Data;

        if (pageOneThumbNail === '') {
          this.pageList[0].thumbnail = this.canvas();
          base64Data = this.pageList[0].thumbnail.split(',')[1];
        } else { base64Data = pageOneThumbNail.split(',')[1]; }

        const fileName = `${this.bookId}_thumbnail.png`;
        try {
          const res = await axios.get('/api/v1/tool/s3/image', {
            params: { fileName: fileName }
          });
          const preSignedUrl = res.data.preSignedUrl;
          const encodedFileName = res.data.encodedFileName;
          const blob = this.base64ToBlob(base64Data);
          this.s3.preSignedUrl = preSignedUrl;
          this.s3.encodedFileName = encodedFileName;
          try {
            await axios.put(this.s3.preSignedUrl, blob);
            this.s3.uploadedUrl = `${process.env.VUE_APP_S3_PATH}/${this.s3.encodedFileName}`;
          } catch (error) {
            console.error(error);
          }
          console.log("썸네일 처리 완료");

          await axios.post('/api/v1/book/thumbnailScenario', {
            bookId: this.bookId,
            bookThumbnail: this.s3.uploadedUrl,
            scenario: scenario
          });

          this.$store.commit('setCanSaveThumbNail', false);
        } catch (err) {
          console.error(`Thumbnail 처리 실패:`, err);
          alert('서버 문제로 파일 처리에 실패하였습니다. 잠시 후 다시 시도해주세요🙇‍♀️');
        };
      };
      return null;
    },

    base64ToBlob(base64Data, contentType = '') {
      const binaryString = window.atob(base64Data);
      const arraybuffer = new ArrayBuffer(binaryString.length);
      const view = new Uint8Array(arraybuffer);
      for (let i = 0; i < binaryString.length; i++) {
        view[i] = binaryString.charCodeAt(i) & 0xff;
      }
      return new Blob([arraybuffer], { type: contentType });
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

      if (uploadBackList === null && uploadCharList === null) return;

      else if (uploadBackList === null) {
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
    async saveVoice() {
      const voiceList = JSON.parse(sessionStorage.getItem('voiceList'));
      if (voiceList === null) {
        return;
      } else {
        await axios.post("/api/v1/tool/uploadVoice/" + this.bookId, {
          voiceList,
        })
          .then((res) => {
            console.log(res);
          })
          .catch((error) => {
            console.log(error);
          });
      }

    },

    preview() {
      const chk = confirm('임시저장 후 이용하실 수 있습니다. 저장하시겠습니까?');
      if (!chk) return;
      try {
        this.saveTmp('temp');
      } catch {
        alert('서버 오류로 저장에 실패하였습니다. 잠시 후 이용해주세요.🥲')
        return;
      }

      setTimeout(() => {
        const screenWidth = window.screen.width;
        const screenHeight = window.screen.height;
        const windowWidth = 1000;
        const windowHeight = 700;
        const left = (screenWidth - windowWidth) / 2;
        const top = (screenHeight - windowHeight) / 2;

        const queryString = `pageList=${encodeURIComponent(JSON.stringify(this.pageList))}`;
        window.open(`/preview?${queryString}`, 'previewWindow', `width=${windowWidth}, height=${windowHeight}, left=${left}, top=${top}`);
      }, 2000);
    },

    preview2() {
      window.open('/preview', 'previewWindow', 'width=1100, height=600');
    },
    
    canvas() {
      const imageArea = this.pageObject;
      const canvas = document.createElement('canvas');
      const reductionRatioPageSize = 0.5;

      const width = imageArea.offsetWidth;
      const height = imageArea.offsetHeight;

      const reducedWidth = Math.floor(width * reductionRatioPageSize);
      const reducedHeight = Math.floor(height * reductionRatioPageSize);
      
      canvas.width = reducedWidth;
      canvas.height = reducedHeight;

      const context = canvas.getContext('2d');

      context.fillStyle = 'white';
      context.fillRect(0, 0, reducedWidth, reducedHeight);

      const imageUrl = canvas.toDataURL();
      console.log(imageUrl);
      return imageUrl;
    },
  }
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