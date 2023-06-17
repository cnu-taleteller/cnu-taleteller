<template>
  <div class="container">
    <h1 @click="makeNewBook()"><img src="@/assets/icon.png" class="icon">새로만들기</h1>
    <hr>
    <div class="column">
      <button v-if="selectedBookList.length > 0" @click="deleteSelectedBooks()">삭제하기</button>
      <div class="column-icon" v-for="bookData, index in bookList" :key=index>
        <input type="checkbox" :value="bookData.bookId" v-model="selectedBookList">
        <img :src="bookData.bookThumbnail" @click="updateExistingBook(bookData.bookId)">
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
      selectedBookList: [],
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
      sessionStorage.removeItem('goingtool');
      sessionStorage.removeItem('bookId');
      this.$store.dispatch('clearSession');
      this.$router.push({
        path : '/tool'
      });
    },
    //기존 작품 접근 할 때
    async updateExistingBook(bookId) {
      await axios.get('/api/v1/tool/scenario', {
        params: {
          bookId: bookId
        }
      })
        .then(res => {
          sessionStorage.setItem('scenario', res.data);
          sessionStorage.setItem('select', true);
          sessionStorage.setItem('toolState', 'temp');
        })

      sessionStorage.setItem('bookId', bookId);
      this.$router.push({
        path : '/tool',
      });
    },
    async deleteSelectedBooks() { 
      await axios.delete('api/v1/book/deleteBookList', { data : this.selectedBookList }).then(response => {
        console.log(response);
        sessionStorage.setItem('goingtool','go');
        this.$router.go();
      }).catch(err => {
        console.error(err);
      }); 
    },
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
