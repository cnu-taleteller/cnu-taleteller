<template>
    <div>
        <h2>저장하기</h2>
        <input type="text" v-model="contentName" disabled/>
        <button @click.prevent="saveToSessionStorage()">저장</button>
    </div>
</template>

<script>
export default {
    data() {
        return {
            contentName: '',
        };
    },
    created() {
        this.contentName = sessionStorage.getItem('bookName');
    },
    methods: {
        saveToSessionStorage() {
            sessionStorage.removeItem('bookName');
            sessionStorage.setItem("projectTitle", this.contentName);
            this.$router.push("/mypage");
        },
    },
};
</script>