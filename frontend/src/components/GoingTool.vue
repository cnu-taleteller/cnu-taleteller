<template>
  <div class="container">
    <hr>
    <h2>작품 대쉬보드</h2>
    <br>
    <br>
    <div class="row">
      <h3 class="title" @click="selectAllBooks()">작품 모두 선택</h3>
      <h4 @click="makeNewBook()"><img src="@/assets/icon.png" class="icon">     새로운 동화책 만들기</h4>
    </div>

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

<style scoped>

.container .row {
  display: flex;
  align-items: center;
}

.container .title {
  flex: 1;
  border: none;
}

.container h4{
  width: 25%;
  height: 10%;
  margin: 5px;
  background-color: #fceb6eb4;
  font-weight: bold;
  color: rgb(51, 51, 51);
  border-radius: 4px;
  border: none;
  cursor: pointer;
  text-align: center;
}
.icon {
  width: 10%;
  height: 10%;
  margin-top: 3%;
  margin-bottom: 3%;
}
.container{
  text-align: left;
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




<script>
import axios from 'axios';

export default {
  data() {
    return {
      selectedBookList: [],
      bookList : [],
      isAllSelected: false,
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
    selectAllBooks() {
      if (this.isAllSelected) {
        this.selectedBookList = [];
      } else {
        this.selectedBookList = this.bookList.map((bookData) => bookData.bookId);
      }
      this.isAllSelected = !this.isAllSelected;
    },
    onClickNew() {
      this.$emit('change-testone', false);
    },
    // 새로 만드는 작품일 때, session에 toolState new 해줘야 시나리오 진입함
    makeNewBook(){
      this.$store.commit('setBookId', null);
      sessionStorage.removeItem('goingtool');
      sessionStorage.removeItem('bookId');
      this.$store.dispatch('clearSession');
      sessionStorage.setItem('toolState', 'new');
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