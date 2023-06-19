<template>
  <div class="selected-page-form">
    <div class="caption-menu-form">
      <button class="cation-btn" @click="addContent()">자막추가</button>
      <div class="caption-menu">
        <input type="number" min="10" max="50" class="caption-size" :value="this.fontSize"
          @input="fontSize = $event.target.value">
        <div class="caption-color" ref="contentColor">
          <div class="color-preview sp-colorize" ref="colorPreview" :value="this.currentColor"
            @change="currentColor = $event.target.value"></div>
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
          style="display: none; position: absolute; background-color: white; border: 1px solid gray; z-index: 99;">
          <ul class="file-order-form">
            <li class="file-order"><button class="btn positionBtn" @click="next(thisObjId)" data-obj-type="popupMenu"
                :disabled="isDisabled">앞으로</button></li>
            <li class="file-order"><button class="btn positionBtn" @click="back(thisObjId)" data-obj-type="popupMenu"
                :disabled="isDisabled">뒤로</button></li>
            <li class="file-order"><button class="btn positionBtn" @click="frontmost(thisObjId)" data-obj-type="popupMenu"
                :disabled="isDisabled">제일 앞으로</button></li>
            <li class="file-order"><button class="btn positionBtn" @click="lastBack(thisObjId)" data-obj-type="popupMenu"
                :disabled="isDisabled">제일 뒤로</button></li>
            <li class="file-order"><button class="btn positionBtn" @click="elementRemove(thisObjId)"
                data-obj-type="popupMenu">삭제</button></li>
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
    pageList: Array,
  },
  data() {
    return {
      thisObjId: null,
      thisTarget: null,
      imageIndex: 0,
      nextId: 1,
      fontSize: 20,
      inputValue: false,
      currentColor: '#000000',
      result: null,
      isDisabled: true,
      isCaptionChange: false,
    }
  },
  mounted() {
    const color = this.$refs.contentColor;
    const colorPreview = this.$refs.colorPreview;
    const dragArea = this.$refs.pageForm;
    const objArea = this.$refs.pageObject;
    const imageArea = this.$refs.dragImage;
    const popupMenu = document.querySelector("#popupMenu");

    const dragImage = this.$refs.dragImage;
    this.$emit('pageObjects', dragImage);

    let textArea;
    let currentColor;

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
        textArea = document.querySelector('[data-text-content="true"]');
        if (textArea) {
          textArea.style.color = currentColor;
        }
        currentColor = color.toHexString();
        colorPreview.style.backgroundColor = currentColor;
        this.currentColor = currentColor;
        this.canvas().then((todataUrl) => {
          this.currentPageList.caption.fontColor = currentColor;
          this.currentPageList.thumbnail = todataUrl;
        });
      }.bind(this),
      move: function (color) {
        textArea = document.querySelector('[data-text-content="true"]');
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
    let result;
    let targetObj;
    let target;
    let parentDiv;
    let resizableElement;
    let contentText;

    this.imageEventDrop(imageArea);
    this.imageEventDragOver(imageArea);
    objArea.addEventListener('mousedown', dragStart);
    objArea.addEventListener('mouseup', dragEnd);

    document.addEventListener('input', (e) => {
      toolMenu.isCaptionChange = true;
      contentText = textInput(e);
    });

    //오른쪽 마우스 클릭
    objArea.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      target = e.target;

      if (target.id.includes('background') || target.dataset.textContent === 'true') {
        toolMenu.isDisabled = true;
      } else {
        toolMenu.isDisabled = false;
      }

      if (target.dataset.textContent === 'true') {
        parentDiv = target.closest('#textArea');
        if (parentDiv) {
          target = parentDiv;
          const targetObj = parentDiv.id;
          toolMenu.thisObjId = targetObj;
          toolMenu.thisTarget = parentDiv;
        }
      } else {
        const targetObj = e.target.id;
        toolMenu.thisObjId = targetObj;
        toolMenu.thisTarget = target;
      }

      popupMenu.style.left = e.clientX - dragArea.offsetLeft + "px";
      popupMenu.style.top = e.clientY - dragArea.offsetLeft + "px";
      popupMenu.style.display = "block";

      if (target.dataset.objType === 'character' || target.dataset.objType === 'background' || target.dataset.objType === 'caption') {
        toolMenu.removeResizableElement(resizableElement, e);
        target.style.outline = '#27BAFF solid 1px';
        toolMenu.resizableElement(resizableElement, target);
      }
    });

    document.addEventListener("mousedown", function (e) {
      e.stopPropagation();
      toolMenu.inputValue = false;
      const textDiv = document.querySelector('[data-text-content="true"]');
      if (textDiv) {
        textDiv.contentEditable = false;
        if(contentText && toolMenu.isCaptionChange) {
          toolMenu.canvas().then((todataUrl) => {
            toolMenu.currentPageList.caption.content = contentText;
            toolMenu.currentPageList.thumbnail = todataUrl;
            toolMenu.isCaptionChange = false;
          });
        }
      }
      if (e.target.dataset.objType !== "character" && e.target.dataset.objType !== "background" && e.target.dataset.objType !== "caption" && e.target.dataset.objType !== 'popupMenu') {
        popupMenu.style.display = "none";
        toolMenu.removeResizableElement(resizableElement, e);
      }
    });

    document.addEventListener("click", function (e) {
      popupMenu.style.display = "none";
    })

    document.addEventListener("dblclick", function (e) {
      if (e.target.dataset.textContent == "true" && e.button === 0) {
        toolMenu.inputValue = true;
        e.target.contentEditable = true;
        e.target.focus();
      }
    });

    //드래그 시작부분(selected page)
    function dragStart(e) {
      e.stopPropagation();
      target = e.target;
      parentDiv = target.closest('#textArea');
      const textDiv = document.querySelector('[data-text-content="true"]');

      if (parentDiv) {
        target = parentDiv;
      } else {
        if (textDiv) {
          textDiv.contentEditable = false;
          toolMenu.inputValue = false;
        }
      }

      if (e.button === 0 && !toolMenu.inputValue && !e.target.classList.contains('ui-resizable-handle')) {
        toolMenu.removeResizableElement(resizableElement, e);
        popupMenu.style.display = "none";
        target.style.outline = '#27BAFF solid 1px';

        targetObj = target.id;
        if (parentDiv) targetObj = parentDiv.id;

        if (targetObj === 'textArea') {
          result = toolMenu.currentPageList.caption;
        } else {
          result = toolMenu.currentPageList.layerList.find(el => el.id === targetObj);
        }

        if (target.dataset.objType === 'character' || target.dataset.objType === 'background' || target.dataset.objType === 'caption') {
          toolMenu.resizableElement(resizableElement, target);
        }
        currentObjId = targetObj;
        currentObj = target;
        currentX = e.pageX - dragArea.offsetLeft;
        currentY = e.pageY - dragArea.offsetTop;
        currentXOffset = e.pageX - dragArea.offsetLeft - e.offsetX;
        currentYOffset = e.pageY - dragArea.offsetTop - e.offsetY;
        if (parentDiv) currentObj = parentDiv;
        currentObj.style.opacity = '0.5';
        active = true;
        document.addEventListener("mousemove", drag);
      }
    };

    //드래그 (selected page)
    function drag(e) {
      e.stopPropagation();
      e.preventDefault();
      if (active && currentObjId === e.target.id && e.target.dataset.objType == 'character' || 'background') {
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
      if (active) {
        currentObj.style.opacity = '1';
        if (targetObj === 'textArea') {
          toolMenu.canvas().then((todataUrl) => {
            result.left = currentObj.style.left;
            result.top = currentObj.style.top;
            toolMenu.currentPageList.thumbnail = todataUrl;
          });
        } else {
          toolMenu.canvas().then((todataUrl) => {
            result.style.left = currentObj.style.left;
            result.style.top = currentObj.style.top;
            toolMenu.currentPageList.thumbnail = todataUrl;
          });
        }
        active = false;
        document.removeEventListener('mousemove', drag);
      }
    };

    //자막 입력 부분
    function textInput(e) {
      let target = e.target;
      let parentDiv = target.closest('#textArea');
      if (parentDiv) {
        if (parentDiv.id == "textArea") {
          let text = target.innerText;
          return text;
        }
      }
      return null;
    };
  },

  watch: {
    //currentPageList => pageList[현재 선택한 페이지 인덱스] 가 변경이 일어나면 실행이 되는 부분
    currentPageList() {
      this.updateContent();
      this.changeCaptionElement();
    },

    fontSize: function (newVal) {
      this.fontSize = newVal;
      const textArea = document.querySelector('[data-text-content="true"]');
      if (textArea) {
        console.log(newVal);
        textArea.style.fontSize = newVal + 'px';
        this.canvas().then((todataUrl) => {
          this.currentPageList.caption.fontSize = newVal + '';
          this.currentPageList.thumbnail = todataUrl;
        });
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
      if (this.currentPageList.caption.captionState === 1) {
        alert('한 페이지당 하나의 자막만 넣을 수 있습니다.');
        return;
      };

      const currentPageList = this.currentPageList;
      const objectArea = this.$refs.pageObject;
      const addDiv = document.createElement("div");
      const textDiv = document.createElement("div");

      textDiv.setAttribute("data-text-content", true);
      addDiv.setAttribute('data-obj-type', 'caption');
      addDiv.style.width = "400px";
      addDiv.style.height = "200px";
      addDiv.style.left = "120px";
      addDiv.style.top = "200px"
      textDiv.style.height = "100%";
      textDiv.style.fontWeight = "bold";
      textDiv.style.fontSize = this.fontSize + "px";
      textDiv.style.color = this.currentColor;
      textDiv.innerText = "자막 내용을 입력해주세요.";
      addDiv.style.position = "absolute";
      addDiv.id = "textArea";
      addDiv.style.zIndex = 2;
      addDiv.appendChild(textDiv);
      objectArea.appendChild(addDiv);

      this.canvas().then((todataUrl) => {
        currentPageList.caption.content = '자막 내용을 입력해주세요.';
        currentPageList.caption.fontSize = textDiv.style.fontSize;
        currentPageList.caption.fontColor = textDiv.style.color;
        currentPageList.caption.width = addDiv.style.width;
        currentPageList.caption.height = addDiv.style.height;
        currentPageList.caption.left = addDiv.style.left;
        currentPageList.caption.top = addDiv.style.top;
        currentPageList.caption.captionState = 1;
        currentPageList.thumbnail = todataUrl;
      });
    },

    async canvas() {
      try {
        this.$store.commit('setCanvasCompleted', false);
        const imageArea = this.$refs.dragImage;
        const img = new Image();
        img.crossOrigin = 'anonymous';

        const reductionRatioPageSize = 0.5;
        const reductionRatio = 0.8;

        const resizableElements = Array.from(imageArea.querySelectorAll('.ui-resizable-handle'));
        const resizableElementSet = new Set(resizableElements);
        const ignoreElements = element => resizableElementSet.has(element);

        const canvas = await html2canvas(imageArea, { useCORS: true, ignoreElements });
        const ctx = canvas.getContext('2d');

        await new Promise((resolve, reject) => {
          const dataUrl = canvas.toDataURL('image/png', reductionRatio);
          img.src = dataUrl;
          img.onload = () => resolve();
          img.onerror = reject;
        });

        const reducedWidth = Math.floor(img.width * reductionRatioPageSize);
        const reducedHeight = Math.floor(img.height * reductionRatioPageSize);

        canvas.width = reducedWidth;
        canvas.height = reducedHeight;

        ctx.drawImage(img, 0, 0, reducedWidth, reducedHeight);
        const todataUrl = canvas.toDataURL('image/png', reductionRatio);

        this.$store.commit('setCanvasCompleted', true);
        return Promise.resolve(todataUrl);
      } catch (err) {
        this.$store.commit('setCanvasCompleted', true);
        console.log(err);
        return Promise.reject(err);
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
        }
        objectElement.appendChild(fragment);
      }

      if (this.currentPageList.caption.captionState !== 0) {
        const caption = this.currentPageList.caption;

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
        objectElement.appendChild(addDiv);
      }
    },

    //이미지를 가장 뒤로 보내는 메소드
    lastBack(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${id}`);
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
      const secondChild = objectElement.children[1];
      if (objectElement.firstChild.id.includes('background')) {
        if (secondChild) {
          objectElement.insertBefore(elementDoc, secondChild);
        } else {
          return;
        }
      } else {
        const firstChild = objectElement.firstChild;
        objectElement.insertBefore(elementDoc, firstChild);
      }
      this.canvas().then((todataUrl) => {
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(0, 0, item);
        this.currentPageList.thumbnail = todataUrl;
      });
    },

    //이미지를 가장 앞으로 보내는 메소드
    frontmost(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${id}`);
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
      const item = this.currentPageList.layerList[indexOfElement];
      objectElement.appendChild(elementDoc);
      this.canvas().then((todataUrl) => {
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(this.currentPageList.layerList.length, 0, item);
        this.currentPageList.thumbnail = todataUrl;
      });
    },

    //이미지를 앞으로 보내는 메소드
    next(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${id}`);
      const nextImage = elementDoc.nextElementSibling;
      if (nextImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement + 1, 0, item);
        objectElement.insertBefore(elementDoc, nextImage.nextElementSibling);
        this.canvas().then((todataUrl) => {
          this.currentPageList.layerList.splice(indexOfElement, 1);
          this.currentPageList.layerList.splice(indexOfElement + 1, 0, item);
          this.currentPageList.thumbnail = todataUrl;
        });
      }
    },

    //이미지를 뒤로 보내는 메소드
    back(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${id}`);
      const previusImage = elementDoc.previousElementSibling;
      if (previusImage && previusImage.id.includes('background')) {
        return;
      }
      if (previusImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
        const item = this.currentPageList.layerList[indexOfElement];
        objectElement.insertBefore(elementDoc, elementDoc.previousElementSibling);
        this.canvas().then((todataUrl) => {
          this.currentPageList.layerList.splice(indexOfElement, 1);
          this.currentPageList.layerList.splice(indexOfElement - 1, 0, item);
          this.currentPageList.thumbnail = todataUrl;
        });
      }
    },

    //요소 삭제하는 메서드
    elementRemove(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${id}`);
      if (elementDoc) {
        elementDoc.remove();
        if (id.includes('background') || id.includes('character')) {
          let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
          this.canvas().then((todataUrl) => {
            this.currentPageList.layerList.splice(indexOfElement, 1);
            this.currentPageList.thumbnail = todataUrl;
          });
        } else if (id.includes('textArea')) {
          this.canvas().then((todataUrl) => {
            this.currentPageList.caption.captionState = 0;
            this.currentPageList.caption.fontColor = '';
            this.currentPageList.caption.fontSize = '';
            this.currentPageList.caption.content = '';
            this.currentPageList.caption.height = '';
            this.currentPageList.caption.width = '';
            this.currentPageList.caption.left = '';
            this.currentPageList.caption.top = '';
            this.currentPageList.thumbnail = todataUrl;
          });
        }
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

        let newDiv = document.createElement('div');
        newDiv.setAttribute("draggable", "false");
        newDiv.setAttribute('data-obj-type', this.selectedMenu);
        newDiv.id = this.selectedMenu + nextId++;
        newDiv.style.position = "absolute";
        newDiv.style.zIndex = 1;

        if (this.selectedMenu == 'background') {
          const dragImageWidth = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('width');
          const dragImageHeight = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('height');
          newDiv.style.left = "0px";
          newDiv.style.top = "0px";
          newDiv.style.width = dragImageWidth > 800 ? dragImageWidth : "800px";
          newDiv.style.height = dragImageHeight > 550 ? dragImageHeight : "550px";

          let newImage = {
            fileId: imageElement.src,
            id: newDiv.id,
            menu: this.selectedMenu,
            style: {
              left: newDiv.style.left,
              top: newDiv.style.top,
              position: newDiv.style.position,
              width: newDiv.style.width,
              height: newDiv.style.height,
            },
          };

          newDiv.style.backgroundImage = `url(${imageElement.src})`;
          newDiv.style.backgroundRepeat = 'no-repeat';
          newDiv.style.backgroundSize = 'cover';

          let elementToRemove = Array.from(document.querySelectorAll(`.object div`))
            .find(el => el.id.includes('background'));

          document.querySelector('.object').insertBefore(newDiv, document.querySelector('.object').firstChild);
          
          if (elementToRemove) {
            elementToRemove.parentNode.removeChild(elementToRemove);
            this.canvas().then((todataUrl) => {
              this.currentPageList.layerList.splice(0, 1, newImage);
              this.currentPageList.thumbnail = todataUrl;
            });
          } else {
            this.canvas().then((todataUrl) => {
              this.currentPageList.layerList.unshift(newImage);
              this.currentPageList.thumbnail = todataUrl;
            });
          };
        } else {
          newDiv.style.left = (rX - x) + "px";
          newDiv.style.top = (rY - y) + "px";
          newDiv.style.width = "100px";
          newDiv.style.height = "100px";
          newDiv.style.zIndex = 1;

          let newImage = {
            fileId: imageElement.src,
            id: newDiv.id,
            menu: this.selectedMenu,
            style: {
              left: newDiv.style.left,
              top: newDiv.style.top,
              position: newDiv.style.position,
              width: newDiv.style.width,
              height: newDiv.style.height,
            },
          };

          newDiv.style.backgroundImage = `url(${imageElement.src})`;
          newDiv.style.backgroundRepeat = 'no-repeat';
          newDiv.style.backgroundSize = 'contain';

          document.querySelector('.object').appendChild(newDiv);
          this.imageIndex = this.currentPageList.layerList.length;

          this.canvas().then((todataUrl) => {
            this.currentPageList.layerList[this.imageIndex] = newImage;
            this.currentPageList.thumbnail = todataUrl;
          });
        };
      });
    },
    removeResizableElement(resizableElement, e) {
      const resizableElements = document.querySelectorAll('.ui-resizable');
      resizableElements.forEach(element => {
        if (element !== resizableElement && !element.contains(e.target)) {
          $(element).resizable('destroy');
          element.style.outline = '';
        };
      });
    },
    resizableElement(resizableElement, target) {
      let toolMenu = this;
      resizableElement = $(target).resizable({
        handles: 'n, e, s, w, ne, se, sw, nw',
        stop: () => {
          let targetId = target.id;
          let resizeTarget;
          if (targetId === 'textArea') {
            toolMenu.canvas().then((todataUrl) => {
              resizeTarget = toolMenu.currentPageList.caption;
              resizeTarget.left = target.style.left;
              resizeTarget.top = target.style.top;
              resizeTarget.width = target.style.width;
              resizeTarget.height = target.style.height;
              toolMenu.currentPageList.thumbnail = todataUrl;
            });
          } else {
            toolMenu.canvas().then((todataUrl) => {
              resizeTarget = toolMenu.currentPageList.layerList.find(el => el.id === targetId);
              resizeTarget.style.left = target.style.left;
              resizeTarget.style.top = target.style.top;
              resizeTarget.style.width = target.style.width;
              resizeTarget.style.height = target.style.height;
              toolMenu.currentPageList.thumbnail = todataUrl;
            });
          }
        }
      });

      resizableElement.find('.ui-resizable-handle').css({
        'position': 'absolute',
        'width': '6px',
        'height': '6px',
        'background': '#5BB6F3',
        'border': '2px solid #fff',
        'box-shadow': '0 1px 1px rgba(0,0,0,.3)',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-s').css({
        'left': '50%',
        'top': '100%',
        'cursor': 's-resize',
        'margin': '0 0 0 -5px',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-ne').css({
        'left': '100%',
        'top': '0',
        'cursor': 'ne-resize',
        'margin': '-10px 0 0',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-se').css({
        'left': '100%',
        'top': '100%',
        'cursor': 'se-resize',
        'margin': '0',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-sw').css({
        'left': '0',
        'top': '100%',
        'cursor': 'sw-resize',
        'margin': '0 0 0 -10px',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-nw').css({
        'left': '0',
        'top': '0',
        'cursor': 'nw-resize',
        'margin': '-10px 0 0 -10px',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-n').css({
        'left': '50%',
        'top': '0',
        'cursor': 'n-resize',
        'margin': '-10px 0 0 -5px',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-e').css({
        'left': '100%',
        'top': '50%',
        'cursor': 'e-resize',
        'margin': '-5px 0 0',
      });

      resizableElement.find('.ui-resizable-handle.ui-resizable-w').css({
        'left': '0',
        'top': '50%',
        'cursor': 'w-resize',
        'margin': '-5px 0 0 -10px',
      });
    },
    changeCaptionElement() {
      const colorPreview = this.$refs.colorPreview;

      if (this.currentPageList.caption.fontSize !== '') {
        this.fontSize = parseInt(this.currentPageList.caption.fontSize);
      } else {
        this.fontSize = 20;
      }

      if (this.currentPageList.caption.fontColor !== '') {
        this.currentColor = this.currentPageList.caption.fontColor;
      } else {
        this.currentColor = '#000000';
      }

      colorPreview.style.backgroundColor = this.currentColor;
    }
  },
}

</script>
<style scoped>
.selected-page-form {
  width: 900px;
  height: 550px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.page-form {
  width: 85%;
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
}

.ui-resizable-handle {
  position: absolute;
  width: 6px;
  height: 6px;
  background: #5BB6F3;
  border: 2px solid #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .3);
}

.ui-resizable-handle.ui-resizable-s {
  left: 50%;
  top: 100%;
  cursor: s-resize;
  margin: 0 0 0 -5px;
}

.ui-resizable-handle.ui-resizable-ne {
  left: 100%;
  top: 0;
  cursor: ne-resize;
  margin: -10px 0 0;
}

.ui-resizable-handle.ui-resizable-se {
  left: 100%;
  top: 100%;
  cursor: se-resize;
  margin: 0;
}

.ui-resizable-handle.ui-resizable-sw {
  left: 0;
  top: 100%;
  cursor: sw-resize;
  margin: 0 0 0 -10px;
}

.ui-resizable-handle.ui-resizable-nw {
  left: 0;
  top: 0;
  cursor: nw-resize;
  margin: -10px 0 0 -10px;
}

.ui-resizable-handle.ui-resizable-n {
  left: 50%;
  top: 0;
  cursor: n-resize;
  margin: -10px 0 0 -5px;
}

.ui-resizable-handle.ui-resizable-e {
  left: 100%;
  top: 50%;
  cursor: e-resize;
  margin: -5px 0 0;
}

.ui-resizable-handle.ui-resizable-w {
  left: 0;
  top: 50%;
  cursor: w-resize;
  margin: -5px 0 0 -10px;
}

.positionBtn {
  display: inline-block;
  background: none;
  border: none;
  outline: 0;
}

</style>