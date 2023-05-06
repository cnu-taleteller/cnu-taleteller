<template>
    <header>
        <div class="bar">
            <h1 id="logo"><a href="/" class="log"><img src="@/assets/logo.png"></a></h1>
            <div class="search">
                <form @submit.prevent="search">
                    <select v-model="searchType">
                        <option value="" selected disabled>선택</option>
                        <option value="name">작성자</option>
                        <option value="title">제목</option>
                        <option value="content">작품 설명</option>
                    </select>
                    <input type="text" v-model="searchKeyword" />
                    <router-link :to="{ path: '/search', query: { searchType: searchType, searchKeyword: searchKeyword } }">
                        <button type="submit" :disabled="!canSearch">검색</button>
                    </router-link>
                </form>
            </div>
            <div class="member" v-if="isLoggedIn">
                <a href="/mypage/pointmanage">마이페이지</a>
                <a href="/" @click="logout">로그아웃</a>
            </div>
            <div class="member" v-else>
                <a href="/loginview">로그인</a>
                <a href="/signupview">회원가입</a>
            </div>
        </div>
    </header>
</template>

<script>
export default {
  data() {
    return {
      searchType: "",
      searchKeyword: "",
      canSearch: false,
      isLoggedIn: false,
    };
  },
  computed: {
    canSubmit() {
      return this.searchType !== "" && this.searchKeyword !== "";
    },
  },
  created() {
    this.checkLoginStatus();
  },
  methods: {
    search() {
      if (!this.canSubmit) {
        alert("검색어를 입력해주세요.");
        return;
      }
      this.$router.push({
        path: "/search",
        query: { searchType: this.searchType, searchKeyword: this.searchKeyword },
      });
    },
    checkLoginStatus() {
      this.isLoggedIn = sessionStorage.getItem("user") !== null;
    },
    logout() {
      sessionStorage.removeItem("user");
      this.isLoggedIn = false;
    },
  },
  watch: {
    searchType() {
      this.canSearch = true;
    },
    searchKeyword() {
      this.canSearch = true;
    },
  },
};
</script>

<style scoped>
#logo img {
    width: 40%;
    height: auto;
}

.bar {
    display: flex;
    align-items: center;
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

select,
input[type="text"],
button[type="submit"] {
    padding: 5px;
    border: 1px solid #ddd;
    border-radius: 3px;
    margin-left: 10px;
}

input[type="text"] {
    width: 25vw;
}

button[type="submit"] {
    background-color: gray;
    color: #ffffff;
    border: none;
    border-radius: 3px;
    cursor: pointer;
    margin-left: 10px;
}

button[type="submit"]:hover {
    background-color: darkgray;
}

.member a {
    display: inline-block;
    padding: 8px 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
    text-decoration: none;
    font-size: 16px;
    font-weight: bold;
    color: #333;
    background-color: #fff;
    transition: background-color 0.3s ease;
}</style>