<template>
    <div class="selected_page">
        <h3>{{ selectedPageNo }}</h3>
        <div class="selected_page" v-if="content.backgroundImage" :style="{ 'background-image': `url(${require('@/assets/' + content.backgroundImage)})`, 'background-size': 'cover', 'background-repeat': 'no-repeat' }">
            <img class="character_image" v-if="content.characterImage" :src="require(`@/assets/${content.characterImage}`)">
        </div>
    </div>
</template>
<script>
export default {
    props : {
        selectedPageNo : Number,
    },
    data(){
        return{
            pageNo: '',
            content: {
                backgroundImage: null,
                characterImage : null
            },
            selectedPageItems : [],
        }
    },
    mounted() {
        // this.pageNo = sessionStorage.getItem('selectedPage');
        // if(sessionStorage.getItem(this.pageNo) == null ){
        //     this.content.backgroundImage = null;
        //     this.content.characterImage = null;
        // }
        // else {
        //     this.content = JSON.parse(sessionStorage.getItem(this.pageNo));
        // }
        this.pageNo = this.selectedPageNo;
        this.updateContent();
    },
    watch: {
        selectedPageNo() {
            this.updateContent();
        }
    },
    methods: {
        updateContent() {
            if (sessionStorage.getItem(this.selectedPageNo) == null) {
                this.content.backgroundImage = null;
                this.content.characterImage = null;
            } else {
                this.content = JSON.parse(sessionStorage.getItem(this.selectedPageNo));
            }
        }
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