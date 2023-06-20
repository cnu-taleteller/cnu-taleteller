<template>
  <div id=list-wrapper>
    <div class="selected-page-form">
      <div id="page-form" ref="pageForm">

      </div>
    </div>
    <div id="button-wrapper">
      <audio :src="ttsUrl" controls autoplay></audio>
      <button id="prev" @click="prev()">이전</button>
      <button id="next" @click="next()">다음</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pageList: null,
      currentIndex: 0,
    };
  },
  
  computed: {
     ttsUrl() {
       const caption = this.pageList[this.currentIndex].caption.ttsName;
       return caption;
     },
  },

  created() {
    this.pageList = this.$store.getters.getPageList;
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const pageListParam = urlParams.get('pageList');

    if (pageListParam) {
      this.pageList = JSON.parse(decodeURIComponent(pageListParam));
      console.log(this.pageList);
    }
  },

  mounted() {
    const list = this.$refs.pageForm;
    this.layerList(list, this.currentIndex);
    this.captionList(list, this.currentIndex);

    if (this.currentIndex == 0) {
      document.querySelector('#prev').disabled = true;
    }

    if (this.currentIndex == this.pageList.length || this.currentIndex == this.pageList.length - 1) {
      document.querySelector('#next').disabled = true;
    }
  },
  
  methods: {
    prevImage() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
      }
    },
    nextImage() {
      if (this.currentIndex < this.pageList.length - 1) {
        this.currentIndex++;
      }
    },
    playTTS() {
      const audioElement = this.$refs.audioPlayer;

      // 음성 파일 경로 설정
      const ttsUrl = this.ttsUrl;
      audioElement.src = ttsUrl;

      // 음성 재생
      audioElement.play();
    },
    next() {
      const list = this.$refs.pageForm;

      while (list.firstChild) {
        list.removeChild(list.firstChild);
      }

      if (this.currentIndex < this.pageList.length - 1) {
        this.currentIndex++;
      }

      if (this.currentIndex == this.pageList.length - 1) {
        document.querySelector('#next').disabled = true;
      }

      this.layerList(list, this.currentIndex);
      this.captionList(list, this.currentIndex);

      if (this.currentIndex > 0) {
        document.querySelector('#prev').disabled = false;
      } else {
        document.querySelector('#prev').disabled = true;
      }
    },
    prev() {
      const list = this.$refs.pageForm;

      while (list.firstChild) {
        list.removeChild(list.firstChild);
      }

      if (this.currentIndex > 0) {
        this.currentIndex--;
      }

      while (list.firstChild) {
        list.removeChild(list.firstChild);
      }

      this.layerList(list, this.currentIndex);
      this.captionList(list, this.currentIndex);

      if (this.currentIndex == 0) {
        document.querySelector('#prev').disabled = true;
      }

      if (this.currentIndex < this.pageList.length - 1) {
        document.querySelector('#next').disabled = false;
      } else {
        document.querySelector('#next').disabled = true;
      }
    },

    layerList(list, currentIndex) {
      if (this.pageList[currentIndex].layerList != null) {
        const fragment = document.createDocumentFragment();
        for (const [index, image] of Object.entries(this.pageList[currentIndex].layerList)) {
          const divEle = document.createElement('div');
          divEle.id = image.id;
          divEle.style.left = image.style.left;
          divEle.style.top = image.style.top;
          divEle.style.position = image.style.position;
          divEle.style.width = image.style.width;
          divEle.style.height = image.style.height;
          divEle.setAttribute('draggable', 'false');
          divEle.setAttribute('data-obj-type', image.menu);
          divEle.style.zIndex = 1;
          divEle.style.backgroundImage = `url(${image.fileId})`;
          divEle.style.backgroundRepeat = 'no-repeat';
          if (image.menu === 'background') divEle.style.backgroundSize = 'cover';
          else divEle.style.backgroundSize = 'contain';
          fragment.appendChild(divEle);
          
        };
        list.appendChild(fragment);
      };
    },

    captionList(list, currentIndex) {
      if (this.pageList[currentIndex].caption.captionState !== 0) {
        const caption = this.pageList[currentIndex].caption;
        const addDiv = document.createElement('div');
        const textDiv = document.createElement("div");

        textDiv.setAttribute("data-text-content", true);
        addDiv.setAttribute('data-obj-type', 'caption');
        addDiv.style.width = caption.width;
        addDiv.style.height = caption.height;
        addDiv.style.left = caption.left;
        addDiv.style.top = caption.top;
        textDiv.style.height = "100%";
        textDiv.style.fontWeight = "bold";
        textDiv.style.fontSize = caption.fontSize;
        textDiv.style.color = caption.fontColor;
        textDiv.innerText = caption.content;
        addDiv.style.position = "absolute";
        addDiv.id = "textArea";
        addDiv.style.zIndex = 2;
        addDiv.appendChild(textDiv);
        list.appendChild(addDiv);
      }
    },
  },
};
</script>

<style scoped>
/* .preview-form {
  height: 700px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.thumbnail-container {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}
.preview-menu {
  margin-top: 10px;
}
.menu-btn {
  padding: 5px 10px;
  background-color: white;
  border: none;
  border-radius: 3px;
}

.menu-btn:hover {
  background-color: rgb(223, 223, 223);
  border: none;
}

.menu-btn.active {
  background-color: gray;
  color: white;
} */
#list-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

#button-wrapper {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translate(-50%);
  z-index: 2;
}

#page-form {
  width: 765px;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  overflow: hidden;
  touch-action: none;
  position: relative;
}

.selected-page-form {
  width: 900px;
  height: 550px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
</style>
