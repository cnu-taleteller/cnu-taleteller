<template>
  <div class='page-form' ref="pageForm">
    <div class='selected-page'>
      <div class='drag-image' ref="dragImage">
        <div class='object' ref='pageObject'></div>
      </div>
    </div>
    <div id="popupMenu" style="display: none; position: absolute; background-color: white; border: 1px solid gray; z-index: 9999;">
      <ul>
        <li><a @click="next(thisObjId)">앞으로</a></li>
        <li><a @click="back(thisObjId)">뒤로</a></li>
        <li><a @click="frontmost(thisObjId)">제일 앞으로</a></li>
        <li><a @click="lastBack(thisObjId) ">제일 뒤로</a></li>
      </ul>
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
      thisObjId : '',
      imageIndex : 0,
      nextId : 1,
      data : null,
    }
  },
  mounted() {
    const dragArea = this.$refs.pageForm;
    const objArea = this.$refs.pageObject;
    const imageArea = this.$refs.dragImage;
    const popupMenu = document.querySelector("#popupMenu");

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
    
    //오른쪽 마우스 클릭
    objArea.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      if(e.target.id.includes('background')) {
        return;
      }
      const targetObj = e.target.id;
      toolMenu.data = targetObj
      toolMenu.thisObjId = targetObj;
      popupMenu.style.left = e.pageX - dragArea.offsetLeft + "px";
      popupMenu.style.top = e.pageY - dragArea.offsetLeft + "px";
      popupMenu.style.display = "block";
    });

    //그림 드래그 부분 제외 버튼 클릭 시 메뉴 안보이게
    document.addEventListener("click", function(e) {
      if (e.target !== objArea && e.target !== popupMenu) {
          popupMenu.style.display = "none";
      }
    });

    //드래그 시작부분(selected page)
    function dragStart(e) {
      if (e.button === 0) {
        e.stopPropagation();
        e.preventDefault();
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
      e.target.style.zIndex = '1';
      e.target.style.opacity = '1';
      let targetObj = e.target.id;
      let result = toolMenu.currentPageList.layerList.find(el => el.id === targetObj);
      result.style.left = e.target.style.left;
      result.style.top = e.target.style.top;
      document.body.style.cursor = '';
      active = false;
      document.removeEventListener('mousemove', drag);
      html2canvas(imageArea).then(canvas => {
        const dataUrl = canvas.toDataURL('image/png');
        toolMenu.currentPageList.thumbnail = dataUrl;
      });
    };
  },
  watch: {
    //currentPageList => pageList[현재 선택한 페이지 인덱스] 가 변경이 일어나면 실행이 되는 부분
    currentPageList() {
      this.updateContent();
    },
  },
  methods: {
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
      const imageArea = this.$refs.dragImage;
      html2canvas(imageArea).then(canvas => {
        const dataUrl = canvas.toDataURL('image/png');
        this.currentPageList.thumbnail = dataUrl;
      });
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
      const imageArea = this.$refs.dragImage;
      html2canvas(imageArea).then(canvas => {
        const dataUrl = canvas.toDataURL('image/png');
        this.currentPageList.thumbnail = dataUrl;
      });
    },
    //이미지를 앞으로 보내는 메소드
    next(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${this.data}`);
      const nextImage = elementDoc.nextElementSibling;
      if(nextImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement + 1, 0, item);
        objectElement.insertBefore(elementDoc, nextImage.nextElementSibling);
        const imageArea = this.$refs.dragImage;
        html2canvas(imageArea).then(canvas => {
          const dataUrl = canvas.toDataURL('image/png');
          this.currentPageList.thumbnail = dataUrl;
        });
      }
    },
    //이미지를 뒤로 보내는 메소드
    back(id) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#${this.data}`);
      const previusImage = elementDoc.previousElementSibling;
      if(previusImage && previusImage.id.includes('background')) {
        return;
      }
      if (previusImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.id === id);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement - 1, 0, item);
        objectElement.insertBefore(elementDoc, elementDoc.previousElementSibling);
        const imageArea = this.$refs.dragImage;
        html2canvas(imageArea).then(canvas => {
          const dataUrl = canvas.toDataURL('image/png');
          this.currentPageList.thumbnail = dataUrl;
        });
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
          if(data == null || x == null || y == null) {
            return;
          }
          let imageElement = document.querySelector(`.menu .image-list #item #${data}`);
          let cloneImageElement = imageElement.cloneNode();
          cloneImageElement.setAttribute("draggable", "false");
          cloneImageElement.id = this.selectedMenu + nextId++;
          if(this.selectedMenu == 'background') {
            const dragImageWidth = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('width');
            const dragImageHeight = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('height');
            cloneImageElement.style.left = "0px";
            cloneImageElement.style.top = "0px";
            cloneImageElement.style.width = dragImageWidth;
            cloneImageElement.style.height = dragImageHeight;
            cloneImageElement.style.position = "absolute";
            cloneImageElement.style.zIndex = 1;
            let newImage = {
              fileId : cloneImageElement.src,
              id : cloneImageElement.id,
              menu: this.selectedMenu,
              style : {
                left : cloneImageElement.style.left,
                top : cloneImageElement.style.top,
                position : cloneImageElement.style.position,
                width : cloneImageElement.style.width,
                height : cloneImageElement.style.height,
              },
            };
            let elementToRemove = Array.from(document.querySelectorAll(`.object img`))
              .find(el => el.id.includes('background'));
              console.log(elementToRemove);
            if (elementToRemove) {
              this.currentPageList.layerList.splice(0, 1, newImage);
              elementToRemove.parentNode.removeChild(elementToRemove);
            } else {
              this.currentPageList.layerList.unshift(newImage);
            }
            document.querySelector('.object').insertBefore(cloneImageElement, document.querySelector('.object').firstChild);
          }else {
            cloneImageElement.style.left = (rX - x) + "px";
            cloneImageElement.style.top = (rY - y) + "px";
            cloneImageElement.style.position = "absolute";
            cloneImageElement.style.width = "100px";
            cloneImageElement.style.height = "100px";
            cloneImageElement.style.zIndex = 1;
            let newImage = {
              fileId : cloneImageElement.src,
              id : cloneImageElement.id,
              menu: this.selectedMenu,
              style : {
                left : cloneImageElement.style.left,
                top : cloneImageElement.style.top,
                position : cloneImageElement.style.position,
                width : cloneImageElement.style.width,
                height : cloneImageElement.style.height,
              },
            };
            document.querySelector('.object').appendChild(cloneImageElement);
            this.imageIndex = this.currentPageList.layerList.length;
            this.currentPageList.layerList[this.imageIndex] = newImage;
            console.log(this.currentPageList);
          }
          const imageArea = this.$refs.dragImage;
          html2canvas(imageArea).then(canvas => {
            const dataUrl = canvas.toDataURL('image/png');
            this.currentPageList.thumbnail = dataUrl;
          });
        });
      },
  },
}

</script>
<style scoped>
.page-form {
    width: 100%;
    height: 450px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    touch-action: none;
    position: relative;
    border: 1px solid gray;
}

.selected-page {
  width: 100%;
  height: 450px;
}

.character-image {
  width: 150px;
  text-align: center;
}

.drag-image {
  width: 100%;
  height: 450px;
  /* height: 60vh; */
  position: absolute;
}

.object {
  position: absolute;
}</style>