<template>
  <div class="header">
    <div v-if="!edit" class="header-title">
      <input class="book-name-input" type="text" v-model="bookName" disabled>
      <img class="header-btn" @click="editBookName()" src="@/assets/pencil.png">
    </div>
    <div v-else class="header-title">
      <input class="book-name-input" type="text" v-model="bookName">
      <img  class="header-btn" @click="editBookName()" src="@/assets/check.png">
  </div>
    <div class="header-menu">
      <button>임시저장</button>
      <button @click="saveBook()">제출</button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      bookName: "동화책 이름",
      pop: null,
      edit: false,
    };
  },
  created() {
    if (sessionStorage.getItem('bookName')) {
      this.bookName = sessionStorage.getItem('bookName');
    }
  },
  methods: {
    editBookName() {
      this.edit = !!!this.edit;
    },
    saveBook() {
      sessionStorage.removeItem('scenario');
      sessionStorage.removeItem('scenarioKeyword');
      sessionStorage.setItem('bookName',this.bookName);
      this.$router.push('/ToolSubmit');
    }
  },
}
</script>
<style scoped>
.header {
  width: 100%;
  background-color: rgb(222, 222, 222);
  /* border: 1px solid gray; */
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title {
  margin: 5px;
  width: 20%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.book-name-input{
  border: none;
  font-size: large;
  font-weight: bold;
  border-bottom: 1px solid gray;
  text-align: center;
}
.header-menu {
  width: 80%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.header-btn{
  width: 25px;
}
.header-btn:hover {
  opacity: 0.7;
}

button {
  margin-left: 15px;
}
</style>