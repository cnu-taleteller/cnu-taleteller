<template>
    <header>
        <div class="bar">
          <h1 id="logo"><a href="/" class="logo"><img src="@/assets/logo.png"></a></h1>
            <div class="member" v-if="isLoggedIn">
                <a href="/mypage/pointmanage">마이페이지</a>
                <button @click="logout()">로그아웃</button>
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
      isLoggedIn: false,
    };
  },
  mounted() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      this.isLoggedIn = sessionStorage.getItem("user") !== null;
    },
    logout() {
      sessionStorage.removeItem("user");
      this.isLoggedIn = false;
      window.location.href = "/";
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

.member button,
.member a {
    display: inline-block;
    padding: 0 16px;
    text-decoration: none;
    font-size: 16px;
    font-weight: bold;
    color: #333;
    background-color : white;
    transition: background-color 0.3s ease;
    border: none;
}
</style>
