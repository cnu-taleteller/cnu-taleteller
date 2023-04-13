<template>
    <div>
        <h2>저장하기</h2>
        <input type="text" v-model="contentName" disabled/>
        <button type="submit" @click.prevent="saveToSessionStorage">저장</button>
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
        this.contentName = this.$route.params.contentName;
    },
    methods: {
        saveToSessionStorage() {
            sessionStorage.setItem("projectTitle", this.contentName);
            this.$router.push("/my");
        },
    },
};
</script>