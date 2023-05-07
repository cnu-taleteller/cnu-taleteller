<template>
  <div class="container">
    <div v-for="(bookDummy, index) in filteredBookDummies" :key="index" class="book-dummies">
      <div class="wrapper" @click="goToDetail(bookDummy.id)">
        <img :src="require(`@/assets/bookDummies/${bookDummy.image}`)" :alt="bookDummy.title">
        <div>
          <p>{{ bookDummy.title }}</p>
          <button>#동화</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      bookDummies: [],
      searchType: "",
      searchKeyword: "",
    }
  },
  created() {
    this.bookDummies = JSON.parse(sessionStorage.getItem('bookDummies'));
    const query = this.$route.query;
    this.searchType = query.searchType;
    this.searchKeyword = query.searchKeyword;
  },
  computed: {
    filteredBookDummies() {
      if (this.searchType === "name") {
        return this.bookDummies.filter((bookDummy) =>
            bookDummy.title.includes(this.searchKeyword)
        );
      } else if (this.searchType === "title") {
        return this.bookDummies.filter((bookDummy) =>
            bookDummy.title.includes(this.searchKeyword)
        );
      } else if (this.searchType === "content") {
        return this.bookDummies.filter((bookDummy) =>
            bookDummy.category.includes(this.searchKeyword)
        );
      } else {
        return this.bookDummies;
      }
    },
  },
  methods: {
    goToDetail(id) {
      this.$router.push({ path: `/detail/${id}` });
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

.book-dummies {
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