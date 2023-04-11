<template>
    <div class="menu">
      <div class="menu_list">
        <button @click="setSelectedMenu('background')">배경</button>
        <button @click="setSelectedMenu('character')">캐릭터</button>
        {{ selectedMenu }}
      </div>
      <div v-if="selectedMenu == 'background'">
        <input type="file" @change="setImage('background')" accept="image/*" id="image">
        <img v-if="content.backgroundImage" :src="require(`@/assets/${content.backgroundImage}`)" height="100" 
        @click="uploadBackgorund(content.backgroundImage)">
      </div>
      <div v-else-if="selectedMenu == 'character'">
        <input type="file" @change="setImage('character')" accept="image/*" id="image">
        <img v-if="content.characterImage" :src="require(`@/assets/${content.characterImage}`)" height="100" 
        @click="uploadCharacter(content.characterImage)">
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
        selectedMenu: null
      }
    },
    props : {
        selectedPageNo : Number,
    },
    mounted() {
      this.selectedMenu = sessionStorage.getItem('selectedMenu');
      this.pageNo = this.selectedPageNo;
      if(sessionStorage.getItem(this.pageNo) == null ){
        this.content.backgroundImage = null;
        this.content.characterImage = null;
      } else {
        this.content = JSON.parse(sessionStorage.getItem(this.selectedPageNo));
      }
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
            this.content.backgroundImage = res.data;
            sessionStorage.setItem('backgroundImage', this.content.backgroundImage);
          } else if (menu === 'character') {
            this.content.characterImage = res.data;
            sessionStorage.setItem('characterImage', this.content.characterImage);
          }
          console.log("전송 성공");
        } catch (e) {
          console.log(e);
        }
      },
      uploadCharacter(image) {
        this.content.characterImage = image;
        sessionStorage.setItem(this.selectedPageNo, JSON.stringify(this.content));
        // location.reload();
      },
      uploadBackgorund(image) {
        this.content.backgroundImage = image;
        sessionStorage.setItem(this.selectedPageNo, JSON.stringify(this.content));
        // location.reload();
      },
      setSelectedMenu(menu) {
        this.selectedMenu = menu;
        sessionStorage.setItem('selectedMenu', menu);
      },
    },
  }
  </script>
  <style scoped>
  .menu {
    margin: 10px;
    width: 1200px;
    height: 150px;
    /* height: 60vh; */
    border: 1px solid gray;
  }
  
  .menu_list {
    text-align: left;
  }
  </style>
  