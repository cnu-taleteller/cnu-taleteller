<template>
    <div class="selected_page">
        <!-- <div v-if="backgroundImage!=null" :style="{ 'background-image': `url(@/assets/${backgroundImage})` }"> -->
        <h3>선택한 페이지</h3>
        <img class="character_image" v-if="characterImage!=null" :src="require(`@/assets/${characterImage}`)">
        <!-- </div> -->
    </div>
</template>
<script>
export default {
    data(){
        return{
            pageNo: 0,
            characterImage: null,
            backgroundImage: null
        }
    },
    mounted() {
        this.characterImage = sessionStorage.getItem('uploadCharacter');
        this.backgroundImage = sessionStorage.getItem('uploadBackgorund');
    },
}

</script>
<style scoped>
.selected_page{
    margin: 10px;
    width: 950px;
    height: 450px;
    /* height: 60vh; */
    border: 1px solid gray;
}
.character_image{
    width: 150px;
    text-align: center;
}
</style>