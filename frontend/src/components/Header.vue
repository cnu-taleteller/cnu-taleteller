<template>
    <header>
        <div class="bar">
          <h1 id="logo"><a href="/" class="logo"><img src="@/assets/logo.png"></a></h1>
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


.bar {
    display: flex;
    align-items: center;
    height: 10vh;
    justify-content: space-between;
  }

#logo {
  height: 100%;
}

#logo img{
  width: auto;
  height: 100%;
}

.logo {
  display: block;
  height: 100%;
}

.member {
  display: flex;
  justify-content: flex-end;
  margin-right: 1vw;
  margin-bottom: 2.5vh;
}

.member a {
    display: inline-block;
    padding: 0 16px;
    text-decoration: none;
    font-size: 16px;
    font-weight: bold;
    color: #333;
    transition: background-color 0.3s ease;
}

</style>
