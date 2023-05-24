<template>
    <div class="mem-info-all">
      <div class="mem-info-body">
        <h1>회원 정보</h1>
        <form @submit.prevent="memModify" class="mem-modify-form">
          <div v-for="result in memberResult" :key="result.memberId" class="form-group">
            <label for="password"></label>
            <input type="text" placeholder="이름" v-model="result.memberName" disabled>
            <input type="text" placeholder="이메일" v-model="result.memberEmail" disabled>
            <div class="password-input">
              <input v-model="memberPassword" :type="passwordVisible ? 'text' : 'password'" id="password" name="password"
                placeholder="비밀번호">
              <span class="password-toggle" @click="togglePasswordVisibility">
                <i class="material-icons">{{ passwordVisible ? 'visibility' : 'visibility_off' }}</i>
              </span>
            </div>    
            <input type="text" placeholder="전화번호" v-model="result.memberPhone" id="phone">
            <input type="text" placeholder="계좌번호" v-model="result.memberAccount" id="account">
          </div>
          <button type="submit" :class="['btn-modify', { 'btn-disabled': !isLoginEnabled }]">수정</button>
          <button @click="memDropout()" type="button" :class="['btn-dropout']">탈퇴</button>
        </form>
      </div>
    </div>
  </template>
  <script>
  import axios from "axios"
  export default {
    props: {
  
    },
    data() {
      return {

        kakaopayUrl: "",
        clientKey: process.env.VUE_APP_CHARGE_KEY,

        memberEmail: '',
        memberPassword: '',
        memberPhone: '',
        memberAccount: '',
        passwordVisible: false,

        memberResult: [],
      }
    },
    created() {
      this.getMemberInfo()
    },
    computed: {
      isLoginEnabled() {
        return this.memberPassword;
      },
    },
    methods: {
      getMemberInfo() {
      this.memberEmail = sessionStorage.getItem('user')
      axios.get("/api/member/details", {
        params: {
          memberEmail: this.memberEmail,
        },
      })
        .then((res) => {
          console.log(res);
          this.memberResult = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      },

      memDropout() {
        if(confirm("탈퇴하시겠습니까?")){
          this.memberEmail = sessionStorage.getItem('user')
          axios.patch(`/api/member/dropout/${this.memberEmail}`, { 
            memberAuth: "Drop",
          })
          .then((res) => {
            if (res.data) {
              console.log("회원 탈퇴 : "+res.data);
              alert("탈퇴되었습니다.")
            } 
          })
          .catch((err) => {
            console.log(err);
          });
        }
      },

      memModify() {
        if(confirm("수정하시겠습니까?")){
          this.memberEmail = sessionStorage.getItem('user')
          axios.patch(`/api/member/modify/${this.memberEmail}`, { 
            memberPassword: this.memberPassword,
            memberPhone: document.getElementById("phone").value,
            memberAccount: document.getElementById("account").value,
          })
          .then((res) => {
            if (res.data) {
              console.log("정보 수정 : "+res.data);
              alert("수정되었습니다.")
            } 
          })
          .catch((err) => {
            console.log(err);
          });
        }
      },

      togglePasswordVisibility() {
        this.passwordVisible = !this.passwordVisible;
      },
  
    },
  }
  
  </script>
  <style scoped>
  .form-group {
    margin-bottom: 15px;
  }

  a {
  text-decoration: none;
  color: #262626;
  position: relative;
  }

  a::before {
    content: "";
    position: absolute;
    width: 100%;
    height: 1px;
    bottom: 0;
    left: 0;
    background-color: #000000;
    transform: scaleX(0);
    transition: transform 0.3s ease-in-out;
  }

  a:hover {
    opacity: 0.7;
    color: #262626
  }

  a:hover::before {
    transform: scaleX(1);
  }

  hr {
    width: 50%;
  }
  .mem-modify-form {
    width: 300px;
  }
  .form-group {
    margin-bottom: 15px;
  }

  label {
    display: block;
    font-size: 16px;
    margin-bottom: 5px;
  }

  input {
    width: 100%;
    height: 40px;
    margin-bottom: 5%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
  }

  input:focus {
    border-color: #fceb6e;
    border-width: 2px;
    outline: none;
  }

  input::placeholder {
    font-size: 13px;
  }
  .btn-modify {
    width: 45%;
    height: 40px;
    margin-right: 10%;
    background-color: #fceb6e;
    color: #000000;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
    font-weight: bold;
  }
  .btn-dropout {
    width: 45%;
    height: 40px;
    background-color: #ff7c25;
    color: #000000;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
    font-weight: bold;
  }
  .btn-disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
  .password-input {
    position: relative;
  }
  .password-toggle {
    position: absolute;
    top: 50%;
    right: 10px;
    transform: translateY(-50%);
    cursor: pointer;
  }
  .password-toggle i {
    font-size: 20px;
    color: #888;
  }
  .mem-info-all {
    width: 100%;
    height: 70vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  .mem-info-body {
    width: 600px;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: none;
    border-radius: 10px;
    box-shadow: 2px 2px 10px rgba(0, 0, 0, .1), 0 0 0 1px #ddd;
  }

  .mem-info-body h1 {
    margin-bottom:7%;
  }
  </style>