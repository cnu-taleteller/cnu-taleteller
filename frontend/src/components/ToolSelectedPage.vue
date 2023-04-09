<template>
    <div class="selected_page">
        <h3>선택한 페이지</h3>
        <p>{{ pageNo }}</p>
    </div>
</template>
<script>
export default {
    data(){
        return{
            pageNo: 1
        }
    },
    mounted() {
        this.pageNo = sessionStorage.getItem('seletedPage');
        console.log(this.pageNo);
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
</style>