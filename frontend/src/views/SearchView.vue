<template>
  <div class="search">
    <h1>검색 결과</h1>
    <ul>
      <li v-for="result in searchResults" :key="result.bookId">
        <router-link :to="{ path: `/detail/${result.bookId}` }">{{ result.bookName }}</router-link>
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
    const keyword = this.$route.query.keyword;

    axios
        .get("/api/book/search", {
          params: {
            searchType: searchType,
            keyword: keyword,
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