<template>
  <div class="selected-page-form">
    <div class="caption-menu-form">
      <button class="cation-btn" @click="addContent()">자막추가</button>
      <div class="caption-menu">
        <input type="number" min="10" max="50" class="caption-size" :value="this.fontSize"
          @input="fontSize = $event.target.value">
        <div class="caption-color" ref="contentColor">
          <div class="color-preview sp-colorize" ref="colorPreview" :value="this.currentColor"></div>
          <!-- <div class="color-picker" ref="colorPicker">▼</div> -->
        </div>
      </div>
    </div>
    <div class='page-form' ref="pageForm">
      <div class='selected-page'>
        <div class='drag-image' ref="dragImage">
          <div class='object' ref='pageObject'></div>
        </div>
        <!-- 이미지 우클릭 영역 -->
        <div id="popupMenu"
          style="display: none; position: absolute; background-color: white; border: 1px solid gray; z-index: 9999;">
          <ul class="file-order-form">
            <li class="file-order"><a @click="next(thisObjId)">앞으로</a></li>
            <li class="file-order"><a @click="back(thisObjId)">뒤로</a></li>
            <li class="file-order"><a @click="frontmost(thisObjId)">제일 앞으로</a></li>
            <li class="file-order"><a @click="lastBack(thisObjId)">제일 뒤로</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import html2canvas from 'html2canvas';

export default {
  //props로 toolView에서 보낸 데이터를 받음
  props: {
    currentPageList: Object,
    selectedMenu: String,
  },
  data() {
    return {
      thisObjId: '',
      imageIndex: 0,
      nextId: 1,
      data: null,
      fontSize: 20,
      inputValue: false,
      currentColor: '#000000',
    }
  },
  mounted() {
    const color = this.$refs.contentColor;
    const colorPreview = this.$refs.colorPreview;
    const dragArea = this.$refs.pageForm;
    const objArea = this.$refs.pageObject;
    const imageArea = this.$refs.dragImage;
    const popupMenu = document.querySelector("#popupMenu");

    let textArea;
    let currentColor;

    //디폴트 값으로 검은색
    colorPreview.style.backgroundColor = this.currentColor;

    // 컬러 선택 창 열기
    $(color).spectrum({
      type: "component",
      showPaletteOnly: true,
      togglePaletteOnly: true,
      hideAfterPaletteSelect: true,
      showInput: true,
      showInitial: true,
      change: function (color) {
        textArea = document.getElementById('textArea');
        if (textArea) {
          textArea.style.color = currentColor;
        }
        currentColor = color.toHexString();
        console.log(currentColor);
        colorPreview.style.backgroundColor = currentColor;
        this.currentColor = currentColor;
        this.currentPageList.caption.fontColor = currentColor;
        this.canvas();
      }.bind(this),
      move: function (color) {
        textArea = document.getElementById('textArea');
        if (textArea) {
          textArea.style.color = currentColor;
        }
        currentColor = color.toHexString();
        colorPreview.style.backgroundColor = currentColor;
        this.currentColor = currentColor;
        this.currentPageList.caption.fontColor = currentColor;
      }.bind(this)
    });

    let toolMenu = this;
    let active = false;
    let currentX;
    let currentY;
    let currentXOffset;
    let currentYOffset;
    let moveX;
    let moveY;
    let currentObjId = null;
    let currentObj;

    this.imageEventDrop(imageArea);
    this.imageEventDragOver(imageArea);
    objArea.addEventListener('mousedown', dragStart);
    objArea.addEventListener('mouseup', dragEnd);
    document.addEventListener('input', textInput);

    //오른쪽 마우스 클릭
    objArea.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      if (e.target.id.includes('background') || e.target.id.includes('textArea')) {
        return;
      }
      const targetObj = e.target.id;
      toolMenu.data = targetObj
      toolMenu.thisObjId = targetObj;
      popupMenu.style.left = e.pageX - dragArea.offsetLeft + "px";
      popupMenu.style.top = e.pageY - dragArea.offsetLeft + "px";
      popupMenu.style.display = "block";
    });

    document.addEventListener("click", function (e) {
      //그림 드래그 부분 제외 버튼 클릭 시 메뉴 안보이게
      if (e.target !== objArea && e.target !== popupMenu) {
        popupMenu.style.display = "none";
      }
      //만약 자막 부분이 아니라면 드래그 할 수 있도록
      if (e.target.id != "textArea") {
        toolMenu.inputValue = false;
      }
    });

    document.addEventListener('dblclick', function (e) {
      if (e.target.id == "textArea") {
        toolMenu.inputValue = true;
        e.target.contentEditable = true;
        e.target.focus();
        document.execCommand('selectAll', false, null);
      }
    });

    //드래그 시작부분(selected page)
    function dragStart(e) {
      if (e.button === 0 && !toolMenu.inputValue) {
        e.preventDefault();
        e.stopPropagation();
        currentObjId = e.target.id;
        currentX = e.pageX - dragArea.offsetLeft;
        currentY = e.pageY - dragArea.offsetTop;
        currentXOffset = e.pageX - dragArea.offsetLeft - e.offsetX;
        currentYOffset = e.pageY - dragArea.offsetTop - e.offsetY;
        document.body.style.cursor = "grabbing";
        e.target.style.opacity = "0.5";
        currentObj = e.target;
        active = true;
        document.addEventListener("mousemove", drag);
      }
    };

    //드래그 (selected page)
    function drag(e) {
      e.stopPropagation();
      e.preventDefault();
      if (active && currentObjId === currentObj.id) {
        currentObj.style.zIndex = '10'
        moveX = e.pageX - dragArea.offsetLeft;
        moveY = e.pageY - dragArea.offsetTop;
        requestAnimationFrame(() => {
          currentObj.style.left = currentXOffset + (moveX - currentX) + 'px';
          currentObj.style.top = currentYOffset + (moveY - currentY) + 'px';
        });
      }
    };

    //드래그 끝내는 부분 (selected page)
    function dragEnd(e) {
      if (e.target.id.includes('textArea')) {
        e.target.style.zIndex = '2';
      } else {
        e.target.style.zIndex = '1';
      }
      e.target.style.opacity = '1';
      let targetObj = e.target.id;
      if (e.target.id.includes('textArea')) {
        let result = toolMenu.currentPageList.caption;
        result.left = e.target.style.left;
        result.top = e.target.style.top;
      } else {
        let result = toolMenu.currentPageList.layerList.find(el => el.id === targetObj);
        result.style.left = e.target.style.left;
        result.style.top = e.target.style.top;
      }
      document.body.style.cursor = '';
      active = false;
      document.removeEventListener('mousemove', drag);
      toolMenu.canvas();
    };

    function textInput(e) {
      if (e.target.id == "textArea") {
        toolMenu.currentPageList.caption.content = e.target.innerText;
      }
    };
  },
  watch: {
    //currentPageList => pageList[현재 선택한 페이지 인덱스] 가 변경이 일어나면 실행이 되는 부분
    currentPageList() {
      this.updateContent();
      this.fontSize = parseInt(this.currentPageList.caption.fontSize);
      if (this.fontSize == NaN) this.fontSize = 10;
    },
    fontSize: function (newVal) {
      const textArea = document.getElementById('textArea');
      if (textArea) {
        textArea.style.fontSize = newVal + 'px';
        this.currentPageList.caption.fontSize = newVal;
      }
    },
  },
  methods: {
    //자막 보이게 하는 변경 값
    showTextArea() {
      console.log(this.currentPageList);
      this.$emit('change', true);
    },
    setFontSize() {
      const textarea = document.querySelector('textarea');
      textarea.style.fontSize = this.fontSize + "px";
    },
    setFontColor() {
      const textarea = document.querySelector('textarea');
      textarea.style.color = this.fontColor;
    },
    addContent() {
      if (this.currentPageList.caption.content !== null) {
        alert('한 페이지당 하나의 자막만 넣을 수 있습니다.');
        return;
      };
      const caption = this.currentPageList.caption;
      const objectArea = this.$refs.pageObject;
      const addDiv = document.createElement("div");
      addDiv.setAttribute("data-text-content", true);
      addDiv.style.width = "400px";
      addDiv.style.height = "200px";
      addDiv.style.left = "120px";
      addDiv.style.top = "200px"
      addDiv.style.fontWeight = "bold";
      addDiv.style.fontSize = this.fontSize + "px";
      addDiv.style.position = "absolute";
      addDiv.style.color = '#000000';
      addDiv.id = "textArea";
      addDiv.innerText = "자막 내용을 입력해주세요.";
      addDiv.style.zIndex = 2;
      objectArea.appendChild(addDiv);
      caption.content = '자막 내용을 입력해주세요.';
      caption.fontSize = addDiv.style.fontSize;
      caption.fontColor = addDiv.style.color;
      caption.width = addDiv.style.width;
      caption.height = addDiv.style.height;
      caption.left = addDiv.style.left;
      caption.top = addDiv.style.top;
      this.canvas();
    },
    canvas() {
      try {
        const imageArea = this.$refs.dragImage;
        html2canvas(imageArea, { useCORS: true }).then(canvas => {
          const ctx = canvas.getContext('2d');
          const img = new Image();
          img.crossOrigin = 'anonymous'; // CORS 허용
          img.onload = () => {
            // 이미지의 원본 크기 가져오기
            const originalWidth = img.width;
            const originalHeight = img.height;

            // 이미지 크기 축소 비율
            const reductionRatio = 0.35; // 30%로 축소

            // 축소된 이미지 크기 계산
            const reducedWidth = originalWidth * reductionRatio;
            const reducedHeight = originalHeight * reductionRatio;

            // 축소된 이미지 그리기
            canvas.width = reducedWidth;
            canvas.height = reducedHeight;
            ctx.drawImage(img, 0, 0, reducedWidth, reducedHeight);

            // 데이터 URL 생성
            const dataUrl = canvas.toDataURL('image/jpeg', 0.35); // JPEG 포맷, 압축률 70%
            this.currentPageList.thumbnail = dataUrl;
          }

          img.src = canvas.toDataURL();
        });
      }
      catch (err) {
        console.log(err);
      }
    },
    updateContent() {
      const objectElement = this.$refs.pageObject;

      //object div 안의 내용을 초기화
      while (objectElement.firstChild) {
        objectElement.removeChild(objectElement.firstChild);
      }

      //currentPageList를 기반으로 이미지를 새롭게 그림
      if (this.currentPageList.layerList != null) {
        const fragment = document.createDocumentFragment();
        for (const [index, image] of Object.entries(this.currentPageList.layerList)) {
          const imageEle = document.createElement('img');
          imageEle.src = image.fileId;
          imageEle.id = image.id;
          imageEle.style.left = image.style.left;
          imageEle.style.top = image.style.top;
          imageEle.style.position = image.style.position;
          imageEle.style.width = image.style.width;
          imageEle.style.height = image.style.height;
          imageEle.setAttribute('draggable', 'false');
          imageEle.style.zIndex = 1;
          fragment.appendChild(imageEle);
        }
        objectElement.appendChild(fragment);
      }

      if (this.currentPageList.caption.content !== null) {
        const caption = this.currentPageList.caption;
        const divEle = document.createElement('div');
        divEle.contentEditable = true;
        divEle.setAttribute("data-text-content", true);
        divEle.style.left = caption.left;
        divEle.style.top = caption.top;
        divEle.style.width = caption.width;
        divEle.style.height = caption.height;
        divEle.style.fontWeight = "bold";
        divEle.style.fontSize = caption.fontSize;
        divEle.style.position = "absolute";
        divEle.style.zIndex = 2;
        divEle.style.color = caption.fontColor;
        divEle.id = "textArea";
        divEle.innerText = caption.content;
        objectElement.appendChild(divEle);
      }
    },
    //이미지를 가장 뒤로 보내는 메소드
    lastBack(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${this.data}`);
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
      const secondChild = objectElement.children[1];
      if (objectElement.firstChild.id.includes('background')) {
        if (secondChild) {
          objectElement.insertBefore(elementDoc, secondChild);
          const item = this.currentPageList.layerList[indexOfElement];
          this.currentPageList.layerList.splice(indexOfElement, 1);
          this.currentPageList.layerList.splice(1, 0, item);
        }
      } else {
        const firstChild = objectElement.firstChild;
        objectElement.insertBefore(elementDoc, firstChild);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(0, 0, item);
      }
      this.canvas();
    },
    //이미지를 가장 앞으로 보내는 메소드
    frontmost(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${this.data}`);
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
      const item = this.currentPageList.layerList[indexOfElement];
      this.currentPageList.layerList.splice(indexOfElement, 1);
      this.currentPageList.layerList.splice(this.currentPageList.layerList.length, 0, item);
      objectElement.appendChild(elementDoc);
      this.canvas();
    },
    //이미지를 앞으로 보내는 메소드
    next(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${this.data}`);
      const nextImage = elementDoc.nextElementSibling;
      if (nextImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement + 1, 0, item);
        objectElement.insertBefore(elementDoc, nextImage.nextElementSibling);
        this.canvas();
      }
    },
    //이미지를 뒤로 보내는 메소드
    back(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${this.data}`);
      const previusImage = elementDoc.previousElementSibling;
      if (previusImage && previusImage.id.includes('background')) {
        return;
      }
      if (previusImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement - 1, 0, item);
        objectElement.insertBefore(elementDoc, elementDoc.previousElementSibling);
        this.canvas();
      }
    },
    //toolmenu 부분의 dragover
    imageEventDragOver(element) {
      element.addEventListener("dragover", (e) => {
        e.preventDefault();
        e.stopPropagation();
      });
    },
    //toolmenu 에서 selectedPage 로 드롭하는 부분
    imageEventDrop(element) {
      let nextId = this.nextId;
      let toolSelectedPageDrag = this.$refs.pageForm;
      element.addEventListener("drop", (e) => {
        e.preventDefault();
        e.stopPropagation();
        let rX = e.pageX - toolSelectedPageDrag.offsetLeft;
        let rY = e.pageY - toolSelectedPageDrag.offsetTop;
        let [data, x, y] = e.dataTransfer.getData("text/plain").split(',');
        if (data == null || x == null || y == null) {
          return;
        }
        let imageElement = document.querySelector(`.menu .image-list #item #${data}`);
        let cloneImageElement = imageElement.cloneNode();
        cloneImageElement.setAttribute("draggable", "false");
        cloneImageElement.id = this.selectedMenu + nextId++;
        if (this.selectedMenu == 'background') {
          const dragImageWidth = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('width');
          const dragImageHeight = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('height');
          cloneImageElement.style.left = "0px";
          cloneImageElement.style.top = "0px";
          cloneImageElement.style.width = dragImageWidth;
          cloneImageElement.style.height = dragImageHeight;
          cloneImageElement.style.position = "absolute";
          cloneImageElement.style.zIndex = 1;
          let newImage = {
            fileId: cloneImageElement.src,
            id: cloneImageElement.id,
            menu: this.selectedMenu,
            style: {
              left: cloneImageElement.style.left,
              top: cloneImageElement.style.top,
              position: cloneImageElement.style.position,
              width: cloneImageElement.style.width,
              height: cloneImageElement.style.height,
            },
          };
          let elementToRemove = Array.from(document.querySelectorAll(`.object img`))
            .find(el => el.id.includes('background'));
          if (elementToRemove) {
            this.currentPageList.layerList.splice(0, 1, newImage);
            elementToRemove.parentNode.removeChild(elementToRemove);
          } else {
            this.currentPageList.layerList.unshift(newImage);
          }
          document.querySelector('.object').insertBefore(cloneImageElement, document.querySelector('.object').firstChild);
        } else {
          cloneImageElement.style.left = (rX - x) + "px";
          cloneImageElement.style.top = (rY - y) + "px";
          cloneImageElement.style.position = "absolute";
          cloneImageElement.style.width = "100px";
          cloneImageElement.style.height = "100px";
          cloneImageElement.style.zIndex = 1;
          let newImage = {
            fileId: cloneImageElement.src,
            id: cloneImageElement.id,
            menu: this.selectedMenu,
            style: {
              left: cloneImageElement.style.left,
              top: cloneImageElement.style.top,
              position: cloneImageElement.style.position,
              width: cloneImageElement.style.width,
              height: cloneImageElement.style.height,
            },
          };
          document.querySelector('.object').appendChild(cloneImageElement);
          this.imageIndex = this.currentPageList.layerList.length;
          this.currentPageList.layerList[this.imageIndex] = newImage;
        }
        this.canvas();
      });
    },

  },
}

</script>
<style scoped>
.selected-page-form {
  width: 100%;
  height: 550px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.page-form {
  width: 80%;
  height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  touch-action: none;
  background-color: white;
  position: relative;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, .1), 0 0 0 1px #ddd;
}

textarea {
  position: relative;
  top: -100px;
  z-index: 100;
}

.selected-page {
  width: 100%;
  height: 500px;
}

.character-image {
  width: 150px;
  text-align: center;
}

.drag-image {
  width: 100%;
  height: 500px;
  /* height: 60vh; */
  position: absolute;
}

.object {
  position: absolute;
}

.file-order-form {
  list-style: none;
  padding: 1px;
  margin: 2px;
}

.file-order:hover {
  background-color: gray;
  color: white;
}

.caption-menu-form {
  width: 70%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 10px;
}

.caption-menu-form>button {
  background-color: gray;
  border: none;
  color: white;
  border-radius: 3px;
  padding: 5px 10px;
}

.caption-menu {
  display: flex;
  justify-content: center;
  align-items: center;
}

.caption-size {
  width: 60px;
  text-align: center;
  font-size: large;
  margin-right: 15px;
}

.caption-size::-webkit-inner-spin-button,
.caption-size::-webkit-outer-spin-button {
  opacity: 1;
}

.color-preview {
  width: 30px;
  height: 30px;
  border-radius: 3px;
}</style>