<template>
    <div>
      <h1>Sign Up</h1>
      <hr>
      <form @submit.prevent="signup">
        이메일 : <input v-model="memberEmail" type="text" name="email"> <br>
        비밀번호 : <input v-model="memberPassword" type="password" name="password"> <br>
        이름 : <input v-model="memberName" type="text" name="name"> <br>
        전화번호 : <input v-model="memberPhone" type="text" name="phone"> <br>
        계좌번호 : <input v-model="memberAccount" type="text" name="account"> <br>
        <button type="submit">가입</button>
      </form> <br>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        memberEmail: '',
        memberPassword: '',
        memberName: '',
        memberPhone: '',
        memberAccount: '',
        memberInfo: '', // 반환된 데이터를 저장할 변수
      };
    },
    methods: {
      signup() {
        axios.post("/api/member/signup", {
          memberEmail: this.memberEmail,
          memberPassword: this.memberPassword,
          memberName: this.memberName,
          memberPhone: this.memberPhone,
          memberAccount: this.memberAccount,
        })
        .then(response => {
            console.log("성공했습니다");
            this.memberInfo = response.data; // 반환된 데이터를 변수에 저장
        })
        .catch(error => {
            console.log("실패했습니다");
            console.log(error);
        });
      }
    }
  };
  </script>