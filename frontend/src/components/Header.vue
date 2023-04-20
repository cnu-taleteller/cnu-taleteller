<template>
    <header>
        <div class="search">
        <h1 id="logo"><a href="/" class="log"><img src="@/assets/logo.png"></a></h1>
            <div class="bar">
                <input type="text" placeholder="제목을 입력하세요">
                 <button>검색</button>
            </div>
            <div>
      <div v-if="testone">
        <button @click="goToSignUp">회원가입</button>
        <button @click="goToLogin">로그인</button>
      </div>
      <div v-else-if="!testone">
      <button @click="goToMyPage">마이페이지</button>
    </div>
    </div>
      </div>
    </header>
</template>

<script>
export default {
  data() {
    return {
      testone: false // 로그인 여부를 저장할 데이터
    }
  },
  methods: {
    goToSignUp() {
      this.$router.push('/signup')
    },
    goToLogin() {
      this.$router.push('/login')
      this.testone = false;
    },
    goToMyPage() {
      this.$router.push('/mypage')
      this.testone = false;
    }
  },
  mounted() {
    this.testone= true;
  }
}
</script>

<style>
#logo img{
  width: 40%;
  height: auto;
}
.search {
    display: flex;
    align-items: center;
  }
  
  .search input {
    width: 200px;
    height: 30px;
    border: none;
    border-radius: 20px;
    padding: 0 10px;
    margin-right: 10px;
  }
  
  .search button {
    height: 30px;
    border: none;
    border-radius: 20px;
    background-color: #bdbdbee4;
    color: #ffffff;
    padding: 0 20px;
  }
</style>