<template>
  <div class="container">
    <hr>
    <h4 @click="makeNewBook()"><img src="@/assets/icon.png" class="icon"> 새로운 동화책 만들기</h4>
    <div class="button-container" v-if="!isDeleteMode">
      <button @click="toggleDeleteMode()">선택하기</button>
    </div>
    <div class="button-container" v-else>
      <button @click="cancelDeleteMode()">선택해제</button>
      <button @click="selectAllBooks()" :disabled="selectedBookList.length === bookList.length">
        {{ selectedBookList.length === bookList.length ? '모두 선택됨' : '모두 선택' }}
      </button>
      <button v-if="selectedBookList.length > 0" @click="deleteSelectedBooks()">삭제하기</button>
    </div>

    <div class="book-container">
      <div class="column" v-for="bookData, index in bookList" :key="index">
        <div v-if="!isDeleteMode">
          <div class="book-thumbnail" @click="updateExistingBook(bookData.bookId)">
            <img :src="bookData.bookThumbnail" alt="Book Thumbnail">
          </div>
          <div class="book-info">
            <small>{{bookData.bookName}}</small>
          </div>
        </div>
        <div v-else>
  <div class="book-thumbnail">
    <img :src="bookData.bookThumbnail" alt="Book Thumbnail">
  </div>
  <div class="book-info">
    <small>{{bookData.bookName}}</small>
    <span class="checkbox-container">
      <input type="checkbox" :value="bookData.bookId" v-model="selectedBookList">
    </span>
  </div>
</div>
    </div>
  </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: left;
}
.button-container {
  display: flex;
  justify-content: flex-end;
}

.button-container button {
  margin-left: 10px;
  background-color: #4aaee2;
  color: #fff;
  border: none;
  cursor: pointer;
}

.checkbox-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 5px;
}
.checkbox-container input[type="checkbox"] {
  margin-right: 5px;
}
.container h4 {
  width: 25%;
  height: 10%;
  margin: 5px;
  font-weight: bold;
  color: rgb(51, 51, 51);
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

.book-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  width: 100%;
}

.column {
  width: 23%;
  height: 100%;
  margin: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ffffff;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.column-icon img {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}

.column-icon {
  width: 30%;
  height: 80%;
  margin: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f5f5f5;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.column button {
  width: 10%;
  height: 30px;
  margin-bottom: 10px;
  background-color: #4aaee2;
  color: #fff;
  border: none;
  cursor: pointer;
}

.column-icon input[type="checkbox"] {
  margin-bottom: 5%;
}

.book-thumbnail {
  width: 100%;
  height: 60%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.book-thumbnail img {
  max-width: 100%;
  max-height: 100%;
}

.book-info {
  margin-top: 10%;
  text-align: center;
}

.book-info small {
  font-weight: bold;
}

</style>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      selectedBookList: [],
      bookList: [],
      isAllSelected: false,
      isDeleteMode: false,
    };
  },
  async created() {
    const userEmail = sessionStorage.getItem('user');

    if (userEmail == null) {
      alert('로그인 후 이용하세요');
      this.$router.push('/login');
    }

    const bookList = await axios.post(
      `${process.env.VUE_APP_API_PATH}/api/v1/book/userBookList`,
      userEmail
    );
    this.bookList = bookList.data;
  },
  methods: {
        selectAllBooks() {
      if (this.selectedBookList.length === this.bookList.length) {
        this.selectedBookList = [];
      } else {
        this.selectedBookList = this.bookList.map((bookData) => bookData.bookId);
      }
    },
    toggleDeleteMode() {
      this.isDeleteMode = true;
    },
    cancelDeleteMode() {
      this.isDeleteMode = false;
      this.selectedBookList = [];
    },
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
    makeNewBook() {
      this.$store.commit('setBookId', null);
      sessionStorage.removeItem('goingtool');
      sessionStorage.removeItem('bookId');
      this.$store.dispatch('clearSession');
      sessionStorage.setItem('toolState', 'new');
      this.$router.push({
        path: '/tool',
      });
    },
    async updateExistingBook(bookId) {
      await axios
        .get(`${process.env.VUE_APP_API_PATH}/api/v1/tool/scenario`, {
          params: {
            bookId: bookId,
          },
        })
        .then((res) => {
          sessionStorage.setItem('scenario', res.data);
          sessionStorage.setItem('select', true);
          sessionStorage.setItem('toolState', 'temp');
        });

      sessionStorage.setItem('bookId', bookId);
      this.$router.push({
        path: '/tool',
      });
    },
    async deleteSelectedBooks() {
      await axios
        .delete(`${process.env.VUE_APP_API_PATH}/api/v1/book/deleteBookList`, {
          data: this.selectedBookList,
        })
        .then((response) => {
          console.log(response);
          sessionStorage.setItem('goingtool', 'go');
          this.$router.go();
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
};
</script>
