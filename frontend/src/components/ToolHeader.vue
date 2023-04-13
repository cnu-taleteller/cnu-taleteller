<template>
    <div class="header">
        <h3>{{ contentName }}</h3>
        <button @click="updateContentName">제목변경</button>
        <button>시나리오</button>
        <button><router-link :to="{ name: 'ToolSubmit', params: { contentName: contentName } }">제출</router-link></button>
    </div>
</template>
<script>
export default {
        data() {
            return {
                contentName: "동화책 이름",
            };
        },
        methods: {
            updateContentName() {
                const inputValue = window.prompt("새로운 동화책 이름을 입력하세요.");
                if (inputValue) {
                    this.contentName = inputValue;
                }
            },
        },
};
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