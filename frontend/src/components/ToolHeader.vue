<template>
  <div class="header">
    <div v-if="!edit" class="header-title">
      <input class="book-name-input" type="text" v-model="bookName" disabled>
      <img class="header-btn" @click="editBookName()" src="@/assets/pencil.png">
    </div>
    <div v-else class="header-title">
      <input class="book-name-input" type="text" v-model="bookName">
      <img  class="header-btn" @click="editBookName()" src="@/assets/check.png">
  </div>
    <div class="header-menu">
      <button @click="preview()">미리보기</button>
      <button @click="save()">임시저장</button>
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
      bookName: "동화책 이름",
      pop: null,
      edit: false,
    };
  },
  props: {
    pageList : Array,
    currentPageList : Object,
  },
  created() {
    if (sessionStorage.getItem('bookName')) {
      this.bookName = sessionStorage.getItem('bookName');
    }
  },
  methods: {
    editBookName() {
      this.edit = !!!this.edit;
    },
    saveBook() {
      sessionStorage.removeItem('scenario');
      sessionStorage.removeItem('scenarioKeyword');
      sessionStorage.setItem('bookName',this.bookName);
      this.$router.push('/ToolSubmit');
    },
    save() {
      axios.post('/api/tool/pages', this.pageList, {  
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        console.log(response);
      })
      .catch(error => {
        console.error(error)
      });
    },
    //미리보기 클릭시 새창을 띄우고 작품썸네일을 보여준다.
    preview() {
        let currentIndex = 0;
        let newWindow = window.open('', 'previewWindow', 'width=1100, height=600');
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
          #button-wrapper {
            position: fixed;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            z-index: 2;
          }
          #list-wrapper {
            position: fixed;
            transform: translate(-50%, -50%);
          }
        </style>
        `;
        const list = newWindow.document.getElementById('list');
        this.layerList(list, currentIndex, newWindow);
        this.captionList(list, currentIndex, newWindow);

        if (currentIndex == 0) {
            newWindow.document.querySelector('#prev').disabled = true;
        }

        if (currentIndex == this.pageList.length || currentIndex == this.pageList.length-1) {
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
            imageEle.style.left = image.style.left;
            imageEle.style.top = image.style.top;
            imageEle.style.position = image.style.position;
            imageEle.style.width = image.style.width;
            imageEle.style.height = image.style.height;
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
          divEle.contentEditable = true;
          divEle.setAttribute("data-text-content", true);
          divEle.style.left = caption.left;
          divEle.style.top = caption.top;
          divEle.style.width = caption.width;
          divEle.style.height = caption.height;
          divEle.style.fontWeight = "bold";
          divEle.style.fontSize = caption.fontSize;
          divEle.style.position ="absolute";
          divEle.style.zIndex = 1;
          divEle.id = "textArea";
          divEle.innerText = caption.content;
          list.appendChild(divEle);
        }
    }
  },
}
</script>
<style scoped>
.header {
  width: 100%;
  border: 1px solid gray;
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
.book-name-input{
  border: none;
  font-size: large;
  font-weight: bold;
  border-bottom: 1px solid gray;
  text-align: center;
}
.header-menu {
  width: 80%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.header-btn{
  width: 25px;
}
.header-btn:hover {
  opacity: 0.7;
}

button {
  margin-left: 15px;
}
</style>