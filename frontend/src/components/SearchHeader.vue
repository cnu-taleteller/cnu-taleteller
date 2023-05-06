<template>
  <div class="bar">
    <div class="search">
      <form>
        <select v-model="searchType">
          <option value="" selected disabled>선택</option>
          <option value="name">작성자</option>
          <option value="title">제목</option>
          <option value="content">작품 설명</option>
        </select>
        <div class="search-wrapper">
          <input type="text" v-model="searchKeyword" />
          <button type="submit" @click="search">&#x1F50D;</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchType: "",
      searchKeyword: "",
      canSearch: false
    };
  },
  computed: {
    canSubmit() {
      return this.searchType !== "" && this.searchKeyword !== null
    },
  },
  methods: {
    search(event) {
      event.preventDefault();

      if (!this.canSubmit) {
        alert("검색 키워드나 검색어를 확인해 주세요.");
        this.$router.push({ path: "/" });
      }
      if (this.searchType === "" || this.searchKeyword === "") {
        alert("검색 키워드나 검색어를 확인해 주세요.");
        this.$router.push({ path: "/" });
      }
      else {
        this.$router.push({ path: "/search", query: { searchType: this.searchType, searchKeyword: this.searchKeyword } });
      }
    },
  },
  watch: {
    searchType() {
      this.canSearch = true
    },
    searchKeyword() {
      this.canSearch = true
    }
  }
};
</script>

<style scoped>
.bar {
  display: flex;
  align-items: center;
  height: 5vh;
  justify-content: center;
}

.search {
  display: flex;
  justify-content: center;
  align-items: center;
}

form {
  display: flex;
  justify-content: center;
  align-items: center;
}

select, input[type="text"], button[type="submit"] {
  border: none;
  border-bottom: 2px solid lightgray;
  padding: 5px;
  margin-left: 10px;
  font-size: 1.2rem;
  font-weight: bold;
  background: none;
}

select:focus, input[type="text"]:focus {
  outline: none;
  border-bottom: 2px solid #4AAEE2;
}

.search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

button[type="submit"] {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  background-color: transparent;
  color: #1872A3;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  margin-right: 10px;
  transition: color .2s ease-in-out;
}

</style>