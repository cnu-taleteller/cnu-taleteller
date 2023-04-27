<template>
    <div>
      <div class='main-selected-page'>
        {{this.currentPageList.caption.content}}
        {{this.currentPageList.caption.isTextAreaVisible}}
        <button @click="showTextArea">자막 추가</button>
        <select v-model="fontSize" @change="setFontSize">
            <option>10</option>
            <option>12</option>
            <option>14</option>
            <option>16</option>
            <option>18</option>
            <option>20</option>
        </select>
        <input type="color" v-model="fontColor" @change="setFontColor">
      </div>
      <div class='page-form' ref="pageForm">
        <div class='selected-page'>
          <div class='drag-image' ref="dragImage">
            <div class='object' ref='pageObject'>

            </div>
        </div>
      </div>
    <div id="popupMenu" style="display: none; position: absolute; background-color: white; border: 1px solid gray; z-index: 9999;">
      <ul class="file-order-form">
        <li class="file-order"><a @click="next(thisObjId)">앞으로</a></li>
        <li class="file-order"><a @click="back(thisObjId)">뒤로</a></li>
        <li class="file-order"><a @click="frontmost(thisObjId)">제일 앞으로</a></li>
        <li class="file-order"><a @click="lastBack(thisObjId) ">제일 뒤로</a></li>
      </ul>
    </div>
    </div>
    <textarea v-if="this.currentPageList.caption.isTextAreaVisible" v-model="textareaValue"></textarea>
  </div>
</template>
<script>
export default {
  props: {
    currentPageList: Object,
    selectedMenu: String,
  },
  data() {
    return {
      thisObjId : '',
      imageIndex : 0,
      nextId : 1,
      textareaValue: this.currentPageList.caption.content,
      isTextAreaVisible: this.currentPageList.caption.isTextAreaVisible,
      fontSize: 12,
      fontColor: "#000000",
    }
  },
  mounted() {
    this.setFontSize();
    this.setFontColor();
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
    objArea.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      if(e.target.dataset.layerId.includes('background')) {
        return;
      }
      const targetObj = e.target.dataset.layerId;
      toolMenu.thisObjId = targetObj;
      popupMenu.style.left = e.pageX - dragArea.offsetLeft + "px";
      popupMenu.style.top = e.pageY - dragArea.offsetLeft + "px";
      popupMenu.style.display = "block";
    });

    document.addEventListener("click", function(e) {
      if (e.target !== objArea && e.target !== popupMenu) {
          popupMenu.style.display = "none";
      }
    });


    function dragStart(e) {
      if (e.button === 0) {
        e.stopPropagation();
        e.preventDefault();
        currentObjId = e.target.dataset.layerId;
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

    function drag(e) {
      e.stopPropagation();
      e.preventDefault();
      if (active && currentObjId === currentObj.dataset.layerId) {
        currentObj.style.zIndex = '10'
        moveX = e.pageX - dragArea.offsetLeft;
        moveY = e.pageY - dragArea.offsetTop;
        requestAnimationFrame(() => {
          currentObj.style.left = currentXOffset + (moveX - currentX) + 'px';
          currentObj.style.top = currentYOffset + (moveY - currentY) + 'px';
        });
      }
    };

    function dragEnd(e) {
      e.target.style.zIndex = '1';
      e.target.style.opacity = '1';
      let targetObj = e.target.dataset.layerId;
      let result = toolMenu.currentPageList.layerList.find(el => el.layerId === targetObj);
      result.style.left = e.target.style.left;
      result.style.top = e.target.style.top;
      document.body.style.cursor = '';
      active = false;
      document.removeEventListener('mousemove', drag);
    };
  },
  watch: {
    currentPageList() {
      this.updateContent();
    },
      //자막 내용 보내기
    textareaValue(newValue) {
      this.$emit('textareaValueChanged', newValue);
    },
  },
  methods: {
      //자막 보이게 하는 변경 값
    showTextArea() {
      console.log(this.currentPageList);
      this.$emit('change', true);
    },
    setFontSize(){
      const textarea = document.querySelector('textarea');
      textarea.style.fontSize=this.fontSize+"px";
    },
    setFontColor(){
      const textarea = document.querySelector('textarea');
      textarea.style.color=this.fontColor;
    },
    updateContent() {
      const objectElement = this.$refs.pageObject;
      while (objectElement.firstChild) {
        objectElement.removeChild(objectElement.firstChild);
      }
      if (this.currentPageList.layerList != null) {
        const fragment = document.createDocumentFragment();
        for (const [index, image] of Object.entries(this.currentPageList.layerList)) {
          const imageEle = document.createElement('img');
          imageEle.src = image.fileId;
          imageEle.id = image.id;
          imageEle.dataset.layerId = image.layerId;
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
    lastBack(layerId) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#item[data-layer-id='${layerId}']`);
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
      const secondChild = objectElement.children[1];
      if (objectElement.firstChild.dataset.layerId.includes('background')) {
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
    },
    frontmost(layerId) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#item[data-layer-id='${layerId}']`);
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
      const item = this.currentPageList.layerList[indexOfElement];
      this.currentPageList.layerList.splice(indexOfElement, 1);
      this.currentPageList.layerList.splice(this.currentPageList.layerList.length, 0, item);
      objectElement.appendChild(elementDoc);
    },
    next(layerId) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#item[data-layer-id='${layerId}']`);
      const nextImage = elementDoc.nextElementSibling;
      if(nextImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement + 1, 0, item);
        objectElement.insertBefore(elementDoc, nextImage.nextElementSibling);
      }
    },
    back(layerId) {
      const objectElement = this.$refs.pageObject;
      const elementDoc = objectElement.querySelector(`#item[data-layer-id='${layerId}']`);
      const previusImage = elementDoc.previousElementSibling;
      if(previusImage && previusImage.dataset.layerId.includes('background')) {
        return;
      }
      if (previusImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement - 1, 0, item);
        objectElement.insertBefore(elementDoc, elementDoc.previousElementSibling);
      }
    },
    imageEventDragOver(element) {
        element.addEventListener("dragover", (e) => {
            e.preventDefault();
            e.stopPropagation();
        });
    },
    imageEventDrop(element) {
        let nextId = this.nextId;
        let toolSelectedPageDrag = document.querySelector('.page-form');
        element.addEventListener("drop", (e) => {
          e.preventDefault();
          e.stopPropagation();
          let rX = e.pageX - toolSelectedPageDrag.offsetLeft;
          let rY = e.pageY - toolSelectedPageDrag.offsetTop;
          let [data, x, y] = e.dataTransfer.getData("text/plain").split(',');
          let imageElement = document.querySelector(`.menu .image-list #item[data-id=${data}]`);
          let cloneImageElement = imageElement.cloneNode();
          let imageId = this.selectedMenu + nextId++;
          cloneImageElement.setAttribute("draggable", "false");
          cloneImageElement.dataset.layerId = imageId;
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
              id : 'item',
              layerId : String(imageId),
              menu: this.selectedMenu,
              style : {
                left : cloneImageElement.style.left,
                top : cloneImageElement.style.top,
                position : cloneImageElement.style.position,
                width : cloneImageElement.style.width,
                height : cloneImageElement.style.height,
              },
            };
            let elementToRemove = Array.from(document.querySelectorAll('.object #item[data-layer-id]'))
              .find(el => el.dataset.layerId.includes('background'));
            if (elementToRemove) {
              layerListRemove = elementToRemove.dataset.layerId;
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
            cloneImageElement.style.width = cloneImageElement.width;
            cloneImageElement.style.height = cloneImageElement.height;
            cloneImageElement.style.zIndex = 1;
            let newImage = {
              fileId : cloneImageElement.src,
              id : 'item',
              layerId : String(imageId),
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
          }
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
.main-selected-page{
    width: 100%;
    /* height: 450px; */
    border: 1px solid gray;
}
textarea{
    position: relative;
    top: -100px;
    z-index: 100;
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
}
.file-order-form{
  list-style:none;
  padding: 1px;
  margin: 2px;
}
.file-order:hover {
  background-color: gray;
  color: white;
}
</style>