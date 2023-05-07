<template>
  <div class="login-container">
    <h1>로그인</h1>
    <hr>
    <form @submit.prevent="login" class="login-form">
      <div class="form-group">
        <label for="email"></label>
        <input v-model="memberEmail" type="text" id="email" name="email" placeholder="이메일">
      </div>
      <div class="form-group">
        <label for="password"></label>
        <div class="password-input">
          <input v-model="memberPassword" :type="passwordVisible ? 'text' : 'password'" id="password" name="password"
            placeholder="비밀번호">
          <span class="password-toggle" @click="togglePasswordVisibility">
            <i class="material-icons">{{ passwordVisible ? 'visibility' : 'visibility_off' }}</i>
          </span>
        </div>
      </div>
      <button type="submit" :class="['btn-login', { 'btn-disabled': !isLoginEnabled }]">로그인</button>
    </form>
    <p></p>
    <button class="btn-kakao-login"><img src="@/assets/kakao_login.png"></button>
    <br>

    <p>
      <a>비밀번호 찾기</a>
      <span> | </span>
      <a href="/signupview">회원가입</a>
    </p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      memberEmail: '',
      memberPassword: '',
      user: '',
      passwordVisible: false,
    };
  },
  computed: {
    isLoginEnabled() {
      return this.memberEmail && this.memberPassword;
    },
  },
  methods: {
    login() {
      if (!this.isLoginEnabled) {
        return;
      }

      axios.post("/api/member/login", {
        memberEmail: this.memberEmail,
         memberPassword: this.memberPassword,
      })
  .then((response) => {
    if (response.data) {
      console.log("로그인 성공");
      console.log(response.data);
      sessionStorage.setItem('user', this.memberEmail);
      console.log(sessionStorage.getItem('user'));
      window.location.href = "/"; // 리다이렉트할 경로
    } else {
      alert("로그인에 실패하였습니다.\n아이디 비밀번호 확인 후 다시 진행해주세요.");
      this.$router.go();
      console.log(response.data);
      // 실패 처리를 원하는 방식으로 구현
    }
      })
      .catch((error) => {
        console.log("실패했습니다");
        console.log(error);
      });
    },
    togglePasswordVisibility() {
      this.passwordVisible = !this.passwordVisible;
    },
  },
};
</script>

<style scoped>
.btn-kakao-login {
  border: none;
  outline: none;
  background-color: white;
}

.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
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


.login-form {
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
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

input#email:focus {
  border-color: #fceb6e;
  border-width: 2px;
  outline: none;
}

input#password:focus {
  border-color: #fceb6e;
  border-width: 2px;
  outline: none;
}

input::placeholder {
  font-size: 13px;
}


.btn-login {
  width: 100%;
  height: 40px;
  background-color: #fceb6e;
  color: #000000;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
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
}</style>