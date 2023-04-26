<template>
  <div class='page-form'>
    <div class='selected-page'>
      <div class='drag-image'>
        <div class='object' ref='pageObject'>
        </div>
      </div>
    </div>
    <!-- <div id="popupMenu" style="display: none; position: absolute; background-color: white; border: 1px solid gray; z-index: 9999;">
      <ul>
        <li><a @click="next(thisObjId)">앞으로</a></li>
        <li><a @click="back(thisObjId)">뒤로</a></li>
        <li><a @click="frontmost(thisObjId)">제일 앞으로</a></li>
        <li><a @click="lastBack(thisObjId) ">제일 뒤로</a></li>
      </ul>
    </div> -->
  </div>
</template>
<script>
export default {
  props: {
    currentPageList: Object,
    imageIndex: Number,
  },
  data() {
    return {
      thisObjId : '',
    }
  },
  mounted() {
    const dragArea = document.querySelector('.page-form');
    const objArea = document.querySelector('.page-form .selected-page .drag-image .object');
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

    objArea.addEventListener('mousedown', dragStart);
    objArea.addEventListener('mouseup', dragEnd);
    objArea.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      if(e.target.dataset.layerId.includes('background')) {
        return;
      }
      const targetObj = e.target.dataset.layerId;
      toolMenu.thisObjId = targetObj;
      // const popupMenu = document.querySelector("#popupMenu");
      // popupMenu.style.left = e.pageX - dragArea.offsetLeft + "px";
      // popupMenu.style.top = e.pageY - dragArea.offsetLeft + "px";
      // popupMenu.style.display = "block";
    });

    document.addEventListener("mousedown", function(e) {
      if (e.target !== objArea && e.target !== popupMenu) {
          popupMenu.style.display = "none";
      }
    });


    function dragStart(e) {
      if(e.button === 0) {
        e.stopPropagation();
        e.preventDefault();
        currentObjId = e.target.dataset.layerId;
        currentX = e.pageX - dragArea.offsetLeft;
        currentY = e.pageY - dragArea.offsetTop;
        currentXOffset = e.pageX - dragArea.offsetLeft - e.offsetX;
        currentYOffset = e.pageY - dragArea.offsetTop - e.offsetY;
        document.body.style.cursor = 'grabbing';
        e.target.style.opacity = '0.5';
        currentObj = e.target;
        active = true;
        document.addEventListener('mousemove', drag);
      }
    };

    function drag(e) {
      e.stopPropagation();
      e.preventDefault();
      if (active && currentObjId === currentObj.dataset.layerId) {
        moveX = e.pageX - dragArea.offsetLeft;
        moveY = e.pageY - dragArea.offsetTop;
        requestAnimationFrame(() => {
          currentObj.style.left = currentXOffset + (moveX - currentX) + 'px';
          currentObj.style.top = currentYOffset + (moveY - currentY) + 'px';
          currentObj.style.zIndex = '10';
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
  },
  methods: {
    updateContent() {
      const objectElement = this.$refs.pageObject;
      while (objectElement.firstChild) {
        objectElement.removeChild(objectElement.firstChild);
      }
      if (this.currentPageList.layerList != null) {
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
          objectElement.appendChild(imageEle);
        }
      }
    },
    lastBack(layerId) {
      const objectDocument = document.querySelector('.object');
      const elementDoc = document.querySelector(`.object #item[data-layer-id='${layerId}']`);
      //firstchild의 layerId가 background를 포함하고 있으면 밑의 방식 만약 없다면 .firstchild 로 변경 if eles 문
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
      if(objectDocument.firstChild.dataset.layerId.includes('background')) {
        const secondChild = objectDocument.children[1];
        if(secondChild) {
          objectDocument.insertBefore(elementDoc, secondChild);
          // let indexOfSecondeElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === secondChild.dataset.layerId);
          const item = this.currentPageList.layerList[indexOfElement];
          this.currentPageList.layerList.splice(indexOfElement, 1);
          this.currentPageList.layerList.splice(1, 0, item);
        }
      } else {
        const firstChild = objectDocument.firstChild;
        objectDocument.insertBefore(elementDoc, firstChild);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(0, 0, item);
      }
    },
    frontmost(layerId) {
      const objectDocument = document.querySelector('.object');
      const elementDoc = document.querySelector(`.object #item[data-layer-id='${layerId}']`);
      let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
      const item = this.currentPageList.layerList[indexOfElement];
      this.currentPageList.layerList.splice(indexOfElement, 1);
      this.currentPageList.layerList.splice(this.currentPageList.layerList.length, 0, item);
      objectDocument.appendChild(elementDoc);
    },
    next(layerId) {
      const objectDocument = document.querySelector('.object');
      const elementDoc = document.querySelector(`.object #item[data-layer-id='${layerId}']`);
      const nextImage = elementDoc.nextElementSibling;
      if(nextImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement + 1, 0, item);
        objectDocument.insertBefore(elementDoc, nextImage.nextElementSibling);
      }
    },
    back(layerId) {
      const objectDocument = document.querySelector('.object');
      const elementDoc = document.querySelector(`.object #item[data-layer-id='${layerId}']`);
      const previusImage = elementDoc.previousElementSibling;
      if (previusImage) {
        let indexOfElement = this.currentPageList.layerList.findIndex(obj => obj.layerId === layerId);
        const item = this.currentPageList.layerList[indexOfElement];
        this.currentPageList.layerList.splice(indexOfElement, 1);
        this.currentPageList.layerList.splice(indexOfElement - 1, 0, item);
        objectDocument.insertBefore(elementDoc, elementDoc.previousElementSibling);
      }
    }
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