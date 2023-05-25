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
      <button @click="test()">저장테스트</button>
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
    if (sessionStorage.getItem('bookId')) {
      this.bookId = sessionStorage.getItem('bookId');
    }

  },
  methods: {
    editBookName() {
      this.edit = !!!this.edit;
    },
    // 제출
    saveBook() {
      // 시나리오 선택되어야 진행
      const select = sessionStorage.getItem('select');
      console.log(select);
      if (!select || select == 'false') {
        alert('시나리오 선택 후 진행해주세요');
        return;
      }
      sessionStorage.removeItem('scenario');
      sessionStorage.removeItem('scenarioKeyword');
      sessionStorage.setItem('bookName', this.bookName);
      this.$router.push('/ToolSubmit');
    },
    // 임시 저장
    async saveTmp() {
      const select = sessionStorage.getItem('select');

      if (!select || select === 'false') {
        alert('시나리오 선택 후 진행해주세요!');
        return;
      }
      else {
        // 최초 저장
        if (this.bookId == null) {
          await axios.post("/api/v1/book/", {
            bookName: this.bookName,
            bookStatus: "temp",
            email: sessionStorage.getItem('user'),
            pageList : this.pageList,
          })
            .then((res) => {
              console.log(res.data.bookId);
              this.bookId = res.data.bookId;
              sessionStorage.setItem('bookId', this.bookId);
              this.saveScenario();
              this.saveUploadFile();
              this.saveThumbnail();
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
            pageList : this.pageList,
          })
            .then((res) => {
              console.log(res);
              this.saveScenario();
              this.saveUploadFile();
              this.saveThumbnail();
              alert('임시저장 완료');
            })
            .catch((error) => {
              console.log(error);
              alert('임시저장 실패');
            });
        }
      }
    },
    async saveThumbnail() {
      for (let i = 0; i < this.pageList.length; i++) {
        const dataUrl = this.pageList[i].thumbnail;
        const base64Data = dataUrl.split(',')[1];
        const fileName = `${this.bookId}_${i}_thumbnail.png`;
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
            this.pageList[i].thumbnail = this.s3.uploadedUrl;
          } catch (error) {
            console.error(error);
          }
          console.log(`Thumbnail ${i} 처리 완료`);
        }
        catch (err) {
          console.error(`Thumbnail ${i} 처리 실패:`, err);
          alert('서버 문제로 파일 처리에 실패하였습니다. 잠시 후 다시 시도해주세요🙇‍♀️');
        }
      }
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
    preview() {
      const screenWidth = window.screen.width;
      const screenHeight = window.screen.height;
      const windowWidth = 1000;
      const windowHeight = 700;
      const left = (screenWidth - windowWidth) / 2;
      const top = (screenHeight - windowHeight) / 2;

      const queryString = `pageList=${encodeURIComponent(JSON.stringify(this.pageList))}`;
      window.open(`/preview?${queryString}`, 'previewWindow', `width=${windowWidth}, height=${windowHeight}, left=${left}, top=${top}`);
    },
    test() {
      console.log(JSON.stringify(this.pageList));
      axios.post('/api/tool/dataTest/1', {"pageList" : this.pageList})
        .then(res => {
          console.log(res);
          console.log('success');
        }).catch(err => {
          console.log(err);
          console.log('fail');
        })
    }
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