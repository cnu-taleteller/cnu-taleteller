<template>
    <div class="menu">
        <div class="menu_list">
            <button @click="setSelectedMenu('background')">배경</button>
            <button @click="setSelectedMenu('character')">캐릭터</button>
            {{ selectedMenu }}
        </div>
        <div v-if="selectedMenu=='background'">
            <input type="file" @change="setImage()" accept="image/*" id="image">
            <img v-if="image!=null" :src="require(`@/assets/${image}`)" width="150" @click="uploadBackgorund(image)">
        </div>
        <div v-else-if="selectedMenu=='character'">
            <input type="file" @change="setImage()" accept="image/*" id="image">
            <img v-if="image!=null" :src="require(`@/assets/${image}`)" width="150" @click="uploadCharacter(image)">
        </div>
        </div>
</template>
<script>
import axios from 'axios';
export default {
    data(){
        return{
            image: null,
            selectedMenu: null,
            backgroundImage: null
        }
    },
    mounted(){
        this.selectedMenu = sessionStorage.getItem('selectedMenu');
    },
    methods: {
        async setImage(){
            let frm = new FormData();
            let imageFile = document.getElementById("image");
            frm.append("image", imageFile.files[0]);
            
            await axios.post("/api/users/image", frm, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
            .then((res)=>{
                this.image = res.data;
                sessionStorage.setItem('image', this.image);
                console.log("전송 성공");
            })
            .catch((e)=> {
                console.log(e);
            })
        },
        uploadCharacter(image){
            sessionStorage.setItem('uploadCharacter', image);
            location.reload();  
        },
        uploadBackgorund(image){
            sessionStorage.setItem('uploadBackgorund', image);
            location.reload();  
        },
        setSelectedMenu(menu){
            this.selectedMenu = menu;
            sessionStorage.setItem('selectedMenu', menu);
        }
 }
}

</script>
<style scoped>
.menu{
    margin: 10px;
    width: 1200px;
    height: 150px;
    /* height: 60vh; */
    border: 1px solid gray;
}
.menu_list{
    text-align: left;
}
</style>