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
        //리스트 변경 해야함.
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
      currentPageList : Object,
    },
    mounted() {
      const toolSelectedPageDrag = document.querySelector('.selected-page .drag-image');
      this.existingImageEventDragStart();
      this.imageEventDragOver(toolSelectedPageDrag);
      this.imageEventDrop(toolSelectedPageDrag);
    },
    updated() {
      this.addImageEventDragstart();
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
      //기존에 있는 이미지에 dragStart 이벤트를 줌
      existingImageEventDragStart() {
        document.querySelectorAll(".menu .image-list #item").forEach((element) => {
            element.addEventListener("dragstart", (e) => {
                const x = e.offsetX;
                const y = e.offsetY;
                e.dataTransfer.setData("text/plain", `${e.target.dataset["id"]}, ${x}, ${y}`);
            });
        });
      },
      addImageEventDragstart() {
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
          let imageElement = document.querySelector(`.menu .image-list #item[data-id=${data}]`);
          let cloneImageElement = imageElement.cloneNode();
          cloneImageElement.setAttribute("draggable", "false");
          // 기본적으로 0,1,2 를 오브젝트 아이디로 줌 각각을 구별하기 위해 고유 id 를 주거나해야할듯??
          let imageId = nextId++;
          cloneImageElement.dataset.objId = imageId;
          if(this.selectedMenu == 'background') {
            const toolSelectedPageDrag = document.querySelector('.selected-page .drag-image');
            const dragImageWidth = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('width');
            const dragImageHeight = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('height');
            cloneImageElement.style.left = "0px";
            cloneImageElement.style.top = "0px";
            cloneImageElement.style.width = dragImageWidth;
            cloneImageElement.style.height = dragImageHeight;
            cloneImageElement.style.position = "absolute";
            cloneImageElement.style.zIndex = 1;
          }else {
            cloneImageElement.style.left = (rX - x) + "px";
            cloneImageElement.style.top = (rY - y) + "px";
            cloneImageElement.style.position = "absolute";
            cloneImageElement.style.width = cloneImageElement.width;
            cloneImageElement.style.height = cloneImageElement.height;
            cloneImageElement.style.zIndex = 1;
          }
          let newImage = {
            src : cloneImageElement.src,
            id : 'item',
            objId : String(imageId),
            menu: this.selectedMenu,
            style : {
              left : cloneImageElement.style.left,
              top : cloneImageElement.style.top,
              position : cloneImageElement.style.position,
              width : cloneImageElement.style.width,
              height : cloneImageElement.style.height,
            },
          };
          //만약 레이어위치 변경 해야하면 변경할 수 도 있는 부분(2023-04-20)
          this.imageIndex = this.currentPageList.imageList.length;
          this.currentPageList.imageList[this.imageIndex] = newImage;
          document.querySelector('.selected-page .drag-image .object').appendChild(cloneImageElement);
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
  