<template>
    <div class="menu">
      <div class="menu-list">
        <button @click="setSelectedMenu('background')">배경</button>
        <button @click="setSelectedMenu('character')">캐릭터</button>
        {{ selectedMenu }}
      </div>
      <div class="image-list">
        <div class="image-list-char" v-show="selectedMenu == 'character'" v-for="item, index in charList">
          <img :src="item.src" :draggable="item.draggable" :id="item.id" :data-id="item.dataId" :style="{ height : item.height}">
        </div>
        <div class="image-list-back" v-show="selectedMenu == 'background'" v-for="item, index in backList">
          <img :src="item.src" :draggable="item.draggable" :id="item.id" :data-id="item.dataId" :style="{ height : item.height}">
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
        image: null,
        pageNo: 0,
        content: {
          backgroundImage: null,
          characterImage : null
        },
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
            height : "100px",
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
        notUploadImageList : false,
        nextId : 0,
        imageList : {},
        xOffsetId : {},
        yOffsetId : {},
        uploadId : 0,
        isUpload : false,
      }
    },
    props : {
        selectedPageNo : Number,
    },
    mounted() {
      if(!this.notUploadImageList) {
        this.haveImageEvent();
        this.notUploadImageList = true;
      };
      
      const toolSelectedPageDrag = document.querySelector('.selected-page .drag-image');
      const container = document.querySelector('.selected-page .drag-image .object');
      
      let toolMenu = this;
      let active = false;
      let currentX;
      let currentY;
      let initialX;
      let initialY;
      let xOffset = 0;
      let yOffset = 0;
      let currentImage = null;
      
      this.imageEventDragOver(toolSelectedPageDrag);
      this.imageEventDrop(toolSelectedPageDrag);
      
      container.addEventListener("mousedown", dragStart, {capture:false});
      container.addEventListener("mouseup", dragEnd, {capture:false});
      container.addEventListener("mousemove", drag, {capture:false});

      function dragStart(e) {
                e.stopPropagation();
                e.preventDefault();
                currentImage = e.target;
            if (e.type === "touchstart") {
                initialX = e.touches[0].clientX - xOffset;
                initialY = e.touches[0].clientY - yOffset;
            } else {
                initialX = e.clientX - toolMenu.xOffsetId[e.target.dataset.ids];
                initialY = e.clientY - toolMenu.yOffsetId[e.target.dataset.ids];
            }
                active = true;
                document.body.style.cursor = 'grabbing';
                e.target.style.opacity = '0.5';
            }

            function dragEnd(e) {
                let imageId = e.target.dataset.ids;
                toolMenu.imageList[imageId].image.style.zIndex = "1";
                e.target.style.opacity = '1';
                active = false;
            }

            function drag(e) {
                if (active && currentImage === e.target) {
                    currentX = e.clientX - initialX;
                    currentY = e.clientY - initialY;
                    toolMenu.xOffsetId[e.target.dataset.ids] = currentX;
                    toolMenu.yOffsetId[e.target.dataset.ids] = currentY;
                    let imageId = e.target.dataset.ids;
                    setTranslate(currentX, currentY, toolMenu.imageList[imageId].image);
                }else if(currentImage !== e.target){
                    e.preventDefault();
                    e.stopPropagation();
                }
            }

            function setTranslate(xPos, yPos, el) {
                el.style.transform = "translate3d(" + xPos + "px, " + yPos + "px, 0)";
                el.style.zIndex = "10";
            }
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
          if (menu === 'background') {
            this.isUpload = true;
            this.content.backgroundImage = res.data;
            sessionStorage.setItem('backgroundImage', this.content.backgroundImage);
            this.src = "/images/" + this.content.backgroundImage;
            this.backList.push({
              src : this.src,
              id : 'item',
              draggable : "true",
              dataId : 'upload' + this.uploadId,
              height : "1000",
            });
          } else if (menu === 'character') {
            this.isUpload = true;
            this.content.characterImage = res.data;
            sessionStorage.setItem('characterImage', this.content.characterImage);
            this.src = "/images/" + this.content.characterImage;
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
      uploadCharacter(image) {
        this.content.characterImage = image;
        sessionStorage.setItem(this.selectedPageNo, JSON.stringify(this.content));
      },
      uploadBackgorund(image) {
        this.content.backgroundImage = image;
        sessionStorage.setItem(this.selectedPageNo, JSON.stringify(this.content));
      },
      setSelectedMenu(menu) {
        this.selectedMenu = menu;
        sessionStorage.setItem('selectedMenu', menu);
      },
      //기존이미지에 이벤트 리스너 추가
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
            if (x != undefined && y != undefined && data != undefined) {
              let newDiv = document.createElement('div');
              newDiv.id = "test";
              let newElement = document.querySelector(`.menu .image-list #item[data-id=${data}]`);
              let cloneNewElement = newElement.cloneNode();
              cloneNewElement.style.position = "absolute";
              cloneNewElement.style.left = (rX - x) + "px";
              cloneNewElement.style.top = (rY - y) + "px";
              cloneNewElement.style.zIndex = 1;
              cloneNewElement.setAttribute("draggable", "false");
              let imageId = nextId++;
              cloneNewElement.dataset.ids = imageId;
              let newImage = {
                pageNo : this.selectedPageNo,
                image : cloneNewElement,
              };
              this.imageList[imageId] = newImage;
              this.xOffsetId[imageId] = 0;
              this.yOffsetId[imageId] = 0;
              this.$emit('imageList', this.imageList);
              if(this.selectedMenu==='background') {
                cloneNewElement.style.width = 1200 + 'px';
                cloneNewElement.style.height = 450 + 'px';  
              }
              newDiv.appendChild(cloneNewElement);
              document.querySelector('.selected-page .drag-image .object').appendChild(newDiv);
            }
        });
      },
    },
  }
  </script>
  <style scoped>
  .menu {
    margin: 10px;
    height: 80vh;
    border: 1px solid gray;
  }
  
  .menu-list {
    text-align: left;
  }

  .image-list{
    display: flex;
    align-items: center;
    justify-content: center;
  }
  </style>
  