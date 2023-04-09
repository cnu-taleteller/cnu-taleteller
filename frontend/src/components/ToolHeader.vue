<template>
    <div class="header">
        <h3>{{ contentName }}</h3>
        <button>시나리오</button>
    </div>
</template>
<script>
export default {
    data() {
        return{
            contentName: "동화책 이름"
        }
    }
}

</script>
<style scoped>
.header{
    height: 50px;
    margin-top: 10px;
    border: 1px solid gray;
    display: flex;
    align-items: center;
    justify-content: space-around;
}
</style>