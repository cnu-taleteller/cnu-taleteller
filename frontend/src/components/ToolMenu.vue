<template>
    <div class="menu">
      <div class="menu-list">
        <button class="menu-btn" :class="{ active: selectedMenu === 'background' }" @click="setSelectedMenu('background')">배경</button>
        <button class="menu-btn" :class="{ active: selectedMenu === 'character' }" @click="setSelectedMenu('character')">캐릭터</button>
        <button class="menu-btn" :class="{ active: selectedMenu === 'scenario' }" @click="setSelectedMenu('scenario')">시나리오</button>
      </div>
      <div class="menu-form">
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
          <!-- 시나리오 -->
          <div v-else-if="selectedMenu == 'scenario'">
           <!-- gpt 시나리오 없을 때 -->
           <div v-if="gpt==true && (!finalScenario || finalScenario.length === 0)">
              <div class="spinner-border" role="status"></div>
              <p>열심히 작성중입니다.<br>조금만 기다려주세요!ㅠㅠ</p>
            </div>
            <!-- 내가 적은 시나리오 없을 때 -->
           <div v-else-if="gpt==false && (!finalScenario || finalScenario.length === 0)">
              <p>입력된 시나리오가 없습니다.<br>시나리오를 입력해주세요.</p>
              <button>추가</button>
            </div>
             <!-- 시나리오 있을 때 -->
             <div v-else>
              <p v-for="(story,index) in finalScenario" :key="index">
                {{ setScenarioLabel(index) }} <br>
                {{ story }}
              </p>
              <button @click="editScenario()">수정</button>
            </div>
          </div>
           
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
        selectedMenu: 'scenario',
        uploadId : 0,
        isUpload : false,
        imageIndex : 0,
      }
    },
    props : {
      currentPageList : Object,
      finalScenario : Array,
      gpt: Boolean
    },
    mounted() {
      const toolSelectedPageDrag = document.querySelector('.page-form .selected-page .drag-image');
      this.$emit('selectedMenu', this.selectedMenu);
      this.existingImageEventDragStart();
    },
    updated() {
      this.addImageEventDragstart();
    },
    methods: {
      // 시나리오 label 나누는 함수
      setScenarioLabel(index) {
        switch(index) {
          case 0:
            return '[도입]';
          case 1:
            return '[전개]';
          case 2:
            return '[위기]';
          case 3:
            return '[결말]';
          default:
            return '';
        }
      },
      // 시나리오 수정
      editScenario(){

      },
      async setImage(menu) {
        console.log(menu);
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
              height : "100px",
            });
          } else if (menu === 'character') {
            this.src = "/images/" + res.data;
            this.charList.push({
              src : this.src,
              id : 'item',
              draggable : "true",
              dataId : 'upload' + this.uploadId,
              height : "100px",
            });
          }
          console.log("전송 성공");
          document.getElementById("image").value="";
        } catch (e) {
          console.log(e);
        }
      },
      setSelectedMenu(menu) {
        this.selectedMenu = menu;
        this.$emit('selectedMenu', this.selectedMenu);
      },
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

  .menu-form {
    margin-top: 10px;
    overflow-y: scroll;
    height: 90%;
}
  .menu-form::-webkit-scrollbar {
  display: none;
}

.menu-btn {
  border: 1px solid gray;
  background-color: white;
}
.menu-btn.active {
  background-color: gray;
  color: white;
}
  </style>
  