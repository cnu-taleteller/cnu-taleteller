<template>
  <div className="book-detail">
    <h1>{{ book && book.bookName }}</h1>
<!--    <p>{{ book && book.userName }}</p>-->
    <p>{{ book && book.bookDescription }}</p>
    <button @click="recommend">{{ book.bookRecommend }} 추천</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      book: null,
    };
  },
  created() {
    const bookId = this.$route.params.id;

    axios
        .get(`/api/book/detail/${bookId}`)
        .then((response) => {
          this.book = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
  },
  methods: {
    recommend() {
      const bookId = this.$route.params.id;

      axios.
          post(`/api/book/detail/${bookId}/recommend`)
          .then(response => {
            this.book = response.data;
          })
          .catch(error => {
            console.log(error);
          });
    },
  },
};
</script>