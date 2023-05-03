<template>
  <div class="search-result">
    <h1>검색 결과</h1>
      <ul>
        <li v-for="result in searchResults" :key="result.bookId">
          <router-link :to="{ path: `/detail/${result.bookId}` }" class="book-link">
              <img src="@/assets/book.png" class="book-image">
              <p class="book-title"> {{ result.bookName }} </p>
          </router-link>
      </li>
      </ul>
    <p v-if="searchResults.length === 0">검색 결과가 없습니다.</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      searchResults: [],
    };
  },
  created() {
    const searchType = this.$route.query.searchType;
    const searchKeyword = this.$route.query.searchKeyword;

    axios
        .get("/api/book/search", {
          params: {
            searchType: searchType,
              searchKeyword: searchKeyword,
          },
        })
        .then((response) => {
          this.searchResults = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
  },
};
</script>

<style>
.search-result {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
}

ul {
    list-style: none;
    margin: 0;
    padding: 0;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}

li {
    width: calc(25% - 30px);
    margin-bottom: 30px;
    text-align: center;
}

.book-link {
    text-decoration: none;
    color: black;
}

.book-image {
    width: 80%;
    height: auto;
    object-fit: contain;
}

.book-title {
    font-size: 1.2rem;
    font-weight: bold;
}

@media (max-width: 640px) {
    li {
        width: calc(50% - 10px);
    }

    .book-image {
        width: 70%;
    }

    .book-title {
        font-size: 1rem;
    }
}
</style>