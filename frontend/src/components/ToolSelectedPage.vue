<template>
    <div class="selected_page" v-if="content.backgroundImage" :style="{ 'background-image': `url(${require('@/assets/' + content.backgroundImage)})`, 'background-size': 'cover', 'background-repeat': 'no-repeat' }">
        <h3>선택한 페이지</h3>
        <img class="character_image" v-if="content.characterImage" :src="require(`@/assets/${content.characterImage}`)">
    </div>
</template>
<script>
export default {
    data(){
        return{
            pageNo: 0,
            content: {},
        }
    },
    mounted() {
        this.pageNo = sessionStorage.getItem('selectedPage');
        this.content = JSON.parse(sessionStorage.getItem(this.pageNo));
    },
    methods: {
    }
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