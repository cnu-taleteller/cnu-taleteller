<template>
  <div>
    <form @submit.prevent="login">
    아이디(이메일) : <input v-model="memberEmail" type="text" name="email"> <br>
    비밀번호 : <input v-model="memberPassword" type="password" name="password"> <br> 
    <button type="submit">로그인</button>
  </form> 
  </div>
</template>

<script>
import axios from 'axios';

export default {
data() {
  return {
    memberEmail: '',
    memberPassword: '',
    user: '', // 반환된 데이터를 저장할 변수
  };
},
methods: {
  login() {
    axios.post("/api/member/login", {
      memberEmail: this.memberEmail,
      memberPassword: this.memberPassword,
    })
    .then(response => {
      if (response.data) {
          console.log("성공했습니다");
          this.sum = response.data; // 반환된 데이터를 변수에 저장
          this.$router.push("/?user=테스트");
        } else {
          console.log("아이디와 비밀번호를 다시 입력하세요");
        }
    })
    .catch(error => {
        console.log("실패했습니다");
        console.log(error);
    });
  }
}
};
</script>

<style>

</style>