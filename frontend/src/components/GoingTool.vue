<template>
  <div class="container">
    <h1 @click="makeNewBook()"><img src="@/assets/icon.png" class="icon">새로만들기</h1>
    <hr>
    <div class="column">
      <div class="column-icon" v-for="bookId, index in bookList" :key=index>
        <img src="@/assets/bookDummies/book.png" @click="updateExistingBook(bookId)">
        <small>미완성작품리스트</small>
        <p></p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';


export default {
  data() {
    return {
      bookList : [],
    }
  },
  async created() {
    const userEmail = sessionStorage.getItem('user');
    
    if(userEmail == null) {
      alert('로그인 후 이용하세요');
      this.$router.push('/login');
    }

    const bookList = await axios.post('api/v1/book/userBookList', userEmail);
    this.bookList = bookList.data;
  },
  methods: {
    onClickNew() {
      this.$emit('change-testone', false);
    },
    // 새로 만드는 작품일 때, session에 toolState new 해줘야 시나리오 진입함
    makeNewBook(){
      this.$store.commit('setBookId', null);
      sessionStorage.setItem('toolState', 'new');
      sessionStorage.removeItem('bookId');
      sessionStorage.removeItem('scenario');
      sessionStorage.removeItem('bookName');
      sessionStorage.removeItem("uploadCharList");
      sessionStorage.removeItem("uploadBackList");
      sessionStorage.removeItem("projectTitle");
      this.$router.push({
        path : '/tool'
      });
    },
    //기존 작품 접근 할 때
    updateExistingBook(bookId) {
      sessionStorage.setItem('bookId', bookId);
      this.$router.push({
        path : '/tool',
      });
    }
  },
}
</script>

<style scoped>
.container h1 a {
  color: black;
  text-decoration: none;
}
.icon {
  height: 100px;
}

.column {
  width: 100%;
  height: 250px;
  display: flex;
  align-items: center;
}
.column p {
  padding-left: 10%;
}
.column-icon {
  width: 30%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}</style>
