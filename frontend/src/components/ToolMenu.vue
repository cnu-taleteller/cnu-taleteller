<template>
    <div class="menu">
      <div class="menu-list">
        <button @click="setSelectedMenu('background')">배경</button>
        <button @click="setSelectedMenu('character')">캐릭터</button>
        {{ selectedMenu }}
      </div>
      <div class="image-list">
        <div class="image-list-char" v-show="selectedMenu == 'character'">
          <img :src="item.src" :draggable="item.draggable" :id="item.id" :data-id="item.dataId" :style="{ height : item.height}" v-for="item, index in charList">
        </div>
        <div class="image-list-back" v-show="selectedMenu == 'background'">
          <img :src="item.src" :draggable="item.draggable" :id="item.id" :data-id="item.dataId" :style="{ height : item.height}" v-for="item, index in backList">
        </div>
      </div>
      <div class="uploadImage">
        <div v-if="selectedMenu == 'background'">
          <input type="file" @change="setImage('background')" accept="image/*" id="image">
        </div>
        <div v-else-if="selectedMenu == 'character'">
          <input type="file" @change="setImage('character')" accept="image/*" id="image">
        </div>
      </div>
    </div>
  </template>
  <script>
  import axios from 'axios';
  export default {
    data() {
      return {
        pageNo: 0,
        charList: [
          {
            src : '/images/pngwing.com.png',
            id : 'item',
            draggable : "true",
            dataId : 'i1',
            height : "100",
          },
          {
            src : '/images/pngwing2.com.png',
            id : 'item',
            draggable : "true",
            dataId : 'i2',
            height : "100",   
          }],
        backList: [
          {
            src : '/images/field.png',
            id : 'item',
            draggable : 'true',
            dataId : 'i3',
            height : '100px',
          }],
        selectedMenu: 'character',
        nextId : 1,
        uploadId : 0,
        isUpload : false,
        imageIndex : 0,
      }
    },
    props : {
        bookIdList : Object,
    },
    mounted() {
      const toolSelectedPageDrag = document.querySelector('.selected-page .drag-image');
      this.haveImageEvent();
      this.imageEventDragOver(toolSelectedPageDrag);
      this.imageEventDrop(toolSelectedPageDrag);
    },
    updated() {
      if(this.isUpload) {
        const uploadImage = document.querySelector(`.menu .image-list #item[data-id=upload${this.uploadId}]`);
        if(uploadImage !== null) {
          uploadImage.addEventListener('dragstart', e => {
            const x = e.offsetX;
            const y = e.offsetY;
            e.dataTransfer.setData("text/plain", `${e.target.dataset["id"]}, ${x}, ${y}`);
          });
        }
      this.uploadId++;
      }
      this.isUpload = false;
    },
    methods: {
      async setImage(menu) {
        try {
          let frm = new FormData();
          let imageFile = document.getElementById("image");
          frm.append("image", imageFile.files[0]);
          const res = await axios.post(`/api/users/image?menu=${menu}`, frm, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
          this.isUpload = true;
          if (menu === 'background') {
            this.src = "/images/" + res.data;
            this.backList.push({
              src : this.src,
              id : 'item',
              draggable : "true",
              dataId : 'upload' + this.uploadId,
              height : "100",
            });
          } else if (menu === 'character') {
            this.src = "/images/" + res.data;
            this.charList.push({
              src : this.src,
              id : 'item',
              draggable : "true",
              dataId : 'upload' + this.uploadId,
              height : "100",
            });
          }
          console.log("전송 성공");
        } catch (e) {
          console.log(e);
        }
      },
      setSelectedMenu(menu) {
        this.selectedMenu = menu;
      },
      haveImageEvent() {
        document.querySelectorAll(".menu .image-list #item").forEach((element) => {
            element.addEventListener("dragstart", (e) => {
                const x = e.offsetX;
                const y = e.offsetY;
                e.dataTransfer.setData("text/plain", `${e.target.dataset["id"]}, ${x}, ${y}`);
            });
        });
      },
      imageEventDragOver(element) {
        element.addEventListener("dragover", (e) => {
            e.preventDefault();
            e.stopPropagation();
        });
      },
      imageEventDrop(element) {
        let nextId = this.nextId;
        element.addEventListener("drop", (e) => {
          e.preventDefault();
          e.stopPropagation();
          let rX = e.pageX - document.querySelector('.selected-page').offsetLeft;
          let rY = e.pageY - document.querySelector('.selected-page').offsetTop;
          let [data, x, y] = e.dataTransfer.getData("text/plain").split(',');
          let newElement = document.querySelector(`.menu .image-list #item[data-id=${data}]`);
          let cloneNewElement = newElement.cloneNode();
          cloneNewElement.setAttribute("draggable", "false");
          // 기본적으로 0,1,2 를 오브젝트 아이디로 줌 각각을 구별하기 위해
          let imageId = nextId++;
          cloneNewElement.dataset.objId = imageId;
          if(this.selectedMenu == 'background') {
            const toolSelectedPageDrag = document.querySelector('.selected-page .drag-image');
            const dragImageWidth = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('width');
            const dragImageHeight = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('height');
            cloneNewElement.style.left = "0px";
            cloneNewElement.style.top = "0px";
            cloneNewElement.style.width = dragImageWidth;
            cloneNewElement.style.height = dragImageHeight;
            cloneNewElement.style.position = "absolute";
            cloneNewElement.style.zIndex = 1;
          }else {
            cloneNewElement.style.left = (rX - x) + "px";
            cloneNewElement.style.top = (rY - y) + "px";
            cloneNewElement.style.position = "absolute";
            cloneNewElement.style.width = cloneNewElement.width;
            cloneNewElement.style.height = cloneNewElement.height;
            cloneNewElement.style.zIndex = 1;
          }
          let newImage = {
            src : cloneNewElement.src,
            id : 'item',
            objId : String(imageId),
            menu: this.selectedMenu,
            style : {
              left : cloneNewElement.style.left,
              top : cloneNewElement.style.top,
              position : cloneNewElement.style.position,
              width : cloneNewElement.style.width,
              height : cloneNewElement.style.height,
            },
          };
          //만약 레이어위치 변경 해야하면 변경할 수 도 있는 부분(2023-04-20)
          this.imageIndex = this.bookIdList.imageList.length;
          this.bookIdList.imageList[this.imageIndex] = newImage;
          document.querySelector('.selected-page .drag-image .object').appendChild(cloneNewElement);
        });
      },
    },
  }
  </script>
  <style scoped>
  .menu {
    margin: 10px;
    /* width: 1200px; */
    height: 150px;
    border: 1px solid gray;
  }
  
  .menu-list {
    text-align: left;
  }

  .imageList{
    display: flex;
    align-items: center;
    justify-content: center;
  }
  </style>
  