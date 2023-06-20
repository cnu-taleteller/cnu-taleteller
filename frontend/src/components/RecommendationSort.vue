<template>
  <div class="container">
    <div v-for="(book, index) in books" :key="index" class="book">
      <div class="wrapper" @click="goToDetail(book.bookId)">
        <img :src="book.bookThumbnail" :alt="book.bookName">
        <div>
          <p>{{ book.bookName }}</p>
          <button>{{ book.bookCategory }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      books: [],
    }
  },
  mounted() {
    this.fetchBooks();
  },
  methods: {
    goToDetail(id) {
      this.$router.push({ path: `/detail/${id}` });
    },
    fetchBooks() {
      axios.get('/api/v1/book/list')
          .then(response => {
            this.books = response.data;
            return this.books.sort((a, b) => new Date(b.bookRegdate) - new Date(a.bookRegdate));
          })
          .catch(error => {
            console.log(error);
          });
    },
  },
}
</script>

<style scoped>
.container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}

.book {
  margin-top: 5%;
  flex-basis: 20%;
  text-align: center;
  margin-left: 5%;
}

.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 80%;
  background: white;
  box-sizing: border-box;
  border: lightgray solid 1px;
  padding-bottom: 20px;
}

.wrapper img {
  width: 100%;
  height: 70%;
  object-fit: cover;
}

.wrapper p {
  font-weight: bold;
  font-size: 1.2rem;
}

.wrapper button {
  background-color: #97c93e;
  padding: 3px 10px;
  border: none;
  color: white;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 14px;
  margin: 5px;
  cursor: pointer;
  border-radius: 5px;
  font-weight: bold;
}

</style>