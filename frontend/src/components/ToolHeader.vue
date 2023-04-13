<template>
    <div class="header">
        <div class="header-title">
        <h5>
            {{ contentName }}
            <button @click="updateContentName"><img src="@/assets/pencil.png" width="20px"></button>
        </h5>
        </div>
        <div class="header-menu">
        <button>시나리오</button>
        <button>임시저장</button>
        <button><router-link :to="{ name: 'ToolSubmit', params: { contentName: contentName } }">제출</router-link></button>
        </div>
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
}
</script>
<style scoped>
.header{
    width: 90%;
    /* margin-top: 10px; */
    padding: 0.5%;
    border-bottom: 1px solid gray;
    display: flex;
    align-items: center;
    justify-content: center;
}
.header-title {
    width: 30%;
    display: flex;
    justify-content: center;
    align-items: center;
}
.header-menu{
    width: 70%;
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

button {
    margin-left: 15px;
}
</style>