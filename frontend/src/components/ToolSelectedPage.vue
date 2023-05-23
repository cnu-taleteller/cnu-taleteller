<template>
  <div class="selected-page-form">
    <div class="caption-menu-form">
      <button class="cation-btn" @click="addContent()">자막추가</button>
      <div class="caption-menu">
        <input type="number" min="10" max="50" class="caption-size" :value="this.fontSize"
          @input="fontSize = $event.target.value">
        <div class="caption-color" ref="contentColor">
          <div class="color-preview sp-colorize" ref="colorPreview" :value="this.currentColor" @change="currentColor = $event.target.value"></div>
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
          style="display: none; position: absolute; background-color: white; border: 1px solid gray; z-index: 99;">
          <ul class="file-order-form">
            <li class="file-order"><a @click="next(thisObjId)" data-obj-type="popupMenu">앞으로</a></li>
            <li class="file-order"><a @click="back(thisObjId)" data-obj-type="popupMenu">뒤로</a></li>
            <li class="file-order"><a @click="frontmost(thisObjId)" data-obj-type="popupMenu">제일 앞으로</a></li>
            <li class="file-order"><a @click="lastBack(thisObjId)" data-obj-type="popupMenu">제일 뒤로</a></li>
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
    pageList : Array,
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
      result: null,
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
        textArea = document.querySelector('[data-text-content="true"]');
        if (textArea) {
          textArea.style.color = currentColor;
        }
        currentColor = color.toHexString();
        colorPreview.style.backgroundColor = currentColor;
        this.currentColor = currentColor;
        this.currentPageList.caption.fontColor = currentColor;
        this.canvas();
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

    this.imageEventDrop(imageArea);
    this.imageEventDragOver(imageArea);
    objArea.addEventListener('mousedown', dragStart);
    objArea.addEventListener('mouseup', dragEnd);
    document.addEventListener('input', textInput);

    //오른쪽 마우스 클릭
    objArea.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      if (e.target.id.includes('background') || e.target.dataset.textContent === 'true') {
        return;
      }
      const targetObj = e.target.id;
      toolMenu.data = targetObj
      toolMenu.thisObjId = targetObj;

      popupMenu.style.left = e.clientX - dragArea.offsetLeft + "px";
      popupMenu.style.top = e.clientY - dragArea.offsetLeft + "px";
      popupMenu.style.display = "block"; 
      
      if (e.target.dataset.objType === 'character' || e.target.dataset.objType === 'background' || e.target.dataset.objType !== 'caption') {
        toolMenu.removeResizableElement(resizableElement, e);
        e.target.style.outline = '#27BAFF solid 1px';
        toolMenu.resizableElement(resizableElement, e, e.target);
      }
    });

    imageArea.addEventListener("mousedown", function (e) {
      e.stopPropagation();
      toolMenu.inputValue = false;
      const textDiv = document.querySelector('[data-text-content="true"]');
      if (textDiv) {
        textDiv.contentEditable = false;
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
        if(textDiv) {
          textDiv.contentEditable = false;
          toolMenu.inputValue = false;
        }
      }

      if (e.button === 0 && !toolMenu.inputValue && !e.target.classList.contains('ui-resizable-handle')) {
        toolMenu.removeResizableElement(resizableElement, e);
        popupMenu.style.display = "none";
        target.style.outline = '#27BAFF solid 1px';

        targetObj = e.target.id;
        if (parentDiv) targetObj = parentDiv.id;

        if (targetObj === 'textArea') {
          result = toolMenu.currentPageList.caption;
        } else {
          result = toolMenu.currentPageList.layerList.find(el => el.id === targetObj);
        }

        if (target.dataset.objType === 'character' || target.dataset.objType === 'background' || target.dataset.objType === 'caption') {
          toolMenu.resizableElement(resizableElement, e, target);
        }
        currentObjId = targetObj;
        currentX = e.pageX - dragArea.offsetLeft;
        currentY = e.pageY - dragArea.offsetTop;
        currentXOffset = e.pageX - dragArea.offsetLeft - e.offsetX;
        currentYOffset = e.pageY - dragArea.offsetTop - e.offsetY;
        currentObj = e.target;
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
        if (e.target.dataset.textContent === 'true') {
          parentDiv.style.zIndex = '2';
          result.left = parentDiv.style.left;
          result.top = parentDiv.style.top;
        } else {
          result.style.left = currentObj.style.left;
          result.style.top = currentObj.style.top;
        }
        currentObj.style.opacity = '1';
        active = false;
        document.removeEventListener('mousemove', drag);
        toolMenu.canvas();
      }
    };

    function textInput(e) {
      let target = e.target;
      let parentDiv = target.closest('#textArea');
      if(parentDiv) {
        if (parentDiv.id == "textArea") {
        toolMenu.currentPageList.caption.content = target.innerText;
      }
      }
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
      console.log(textarea);
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

      const caption = this.currentPageList.caption;
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
      caption.content = '자막 내용을 입력해주세요.';
      caption.fontSize = textDiv.style.fontSize;
      caption.fontColor = textDiv.style.color;
      caption.width = addDiv.style.width;
      caption.height = addDiv.style.height;
      caption.left = addDiv.style.left;
      caption.top = addDiv.style.top;
      this.currentPageList.caption.captionState = 1;
      this.canvas();
    },

    // node_modules 폴더 안에 html2canvas.js 
    // 5766번째 줄: img.src = /^data:image/.test(src) ? src : src + '?' + new Date().getTime(); 로 수정
    canvas() {
      try {
        const imageArea = this.$refs.dragImage;
        const currentPage = this.currentPageList;
        const resizableElements = Array.from(imageArea.querySelectorAll('.ui-resizable-handle'));
        const ignoreElements = element => {
          return resizableElements.some(resizableElement => resizableElement.contains(element));
        };
        html2canvas(imageArea, { useCORS: true, ignoreElements }).then(canvas => {
          const ctx = canvas.getContext('2d');
          const img = new Image();
          img.crossOrigin = 'anonymous';
          img.onload = () => {
            const originalWidth = img.width;
            const originalHeight = img.height;
            const reductionRatio = 0.35;
            const reducedWidth = originalWidth * reductionRatio;
            const reducedHeight = originalHeight * reductionRatio;
            canvas.width = reducedWidth;
            canvas.height = reducedHeight;
            ctx.drawImage(img, 0, 0, reducedWidth, reducedHeight);
            const dataUrl = canvas.toDataURL('image/jpeg', 0.35);
            currentPage.thumbnail = dataUrl;
          };
          img.src = canvas.toDataURL();
        });
      } catch (err) {
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

          if (elementToRemove) {
            this.currentPageList.layerList.splice(0, 1, newImage);
            elementToRemove.parentNode.removeChild(elementToRemove);
          } else {
            this.currentPageList.layerList.unshift(newImage);
          }

          document.querySelector('.object').insertBefore(newDiv, document.querySelector('.object').firstChild);
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
          this.currentPageList.layerList[this.imageIndex] = newImage;
        }
        this.canvas();
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
    resizableElement(resizableElement, e, target) {
      let toolMenu = this;
      resizableElement = $(target).resizable({
          handles: 'n, e, s, w, ne, se, sw, nw',
          stop: () => {
            let target = e.target.id;
            let resizeTarget;
            if (target.includes('textArea')) {
              resizeTarget = toolMenu.currentPageList.caption;
              resizeTarget.left = e.target.style.left;
              resizeTarget.top = e.target.style.top;
              resizeTarget.width = e.target.style.width;
              resizeTarget.height = e.target.style.height;
            } else {
              resizeTarget = toolMenu.currentPageList.layerList.find(el => el.id === target);
              resizeTarget.style.left = e.target.style.left;
              resizeTarget.style.top = e.target.style.top;
              resizeTarget.style.width = e.target.style.width;
              resizeTarget.style.height = e.target.style.height;
            }
            toolMenu.canvas();
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

      if(this.currentPageList.caption.fontSize !== '') {
        this.fontSize = parseInt(this.currentPageList.caption.fontSize);
        console.log(this.fontSize);
      } else {
        this.fontSize = 20;
        console.log(this.fontSize);
      }
      
      if(this.currentPageList.caption.fontColor !== '') {
        this.currentColor = this.currentPageList.caption.fontColor;
        console.log(this.currentColor);
      } else {
        this.currentColor = '#000000';
        console.log(this.currentColor);
      }

      colorPreview.style.backgroundColor = this.currentColor;
    }
  },
}

//1. 현재 자막 부분 문제점 자막을 다 지우면 글자크기 작아짐... 다른 div 안에 글이 들어감 새로운 div 를 추가를 해서 거기에 글을 적게 하고 드래그를 전체로 하도록 해야할듯 --해결
//2. 썸네일 제작 부분 비동기 형식이라 만약에 canvas() 하기 전에 currentPageList가 변경이되면 해당 currentPageList의 썸네일이 변경됨... --해결
//3. 모듈화 해야함... resizable 모듈화 --해결
//4. 썸네일 크기가 다름
//5. 자막 더블클릭했을 때 마지막이면 글이 입력이 안됌

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
</style>