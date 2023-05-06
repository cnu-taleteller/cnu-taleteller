<template>
  <div class="signup-container">
    <h1>회원가입</h1>
    <hr>
    <form @submit.prevent="signup" class="signup-form">
      <div class="form-group">
        <div class="input-label">
          <label for="email">이메일</label>
          <span class="signup-message">{{ IdMsg }}</span>
        </div>
        <div class="input-wrapper">
          <input v-model="memberEmail" type="text" name="email" id="email" placeholder="이메일">
          <button type="button" class="check-button" @click="checkEmail">중복체크</button>
        </div>
      </div>
      <div class="form-group">
        <div class="input-label">
          <label for="password">비밀번호</label>
          <span class="signup-message">{{ PasswordMsg }}</span>
        </div>
        <div class="password-input">
          <input v-model="memberPassword" :type="passwordVisible ? 'text' : 'password'" name="password" id="password" placeholder="비밀번호" @input="limitAccountLength">
          <span class="password-toggle" @click="togglePasswordVisibility">
            <i class="material-icons">{{ passwordVisible ? 'visibility' : 'visibility_off' }}</i>
          </span>
        </div>
      </div>
      <div class="form-group">
        <div class="input-label">
          <label for="name">성명</label>
        </div>
      <input v-model="memberName" type="text" name="name" id="name" placeholder="성명">
      </div>
      <div class="form-group">
        <div class="input-label">
          <label for="phone">전화번호</label>
        </div>
      <input v-model="memberPhone" type="text" name="phone" id="phone" placeholder="전화번호">
      </div>
      <div class="form-group">
        <div class="input-label">
          <label for="account">계좌번호</label>
        </div>
        <div class="account-input">
          <input v-model="accountPart1" type="text" name="account1" id="account1" placeholder="계좌번호" @input="limitAccountLength">
          <!-- <input v-model="accountPart2" :type="passwordVisible ? 'text' : 'password'" name="account2" id="account2" placeholder="계좌번호">
          <input v-model="accountPart3" type="text" name="account3" id="account3" placeholder="계좌번호">
          <input v-model="accountPart4" :type="passwordVisible ? 'text' : 'password'" name="account4" id="account4" placeholder="계좌번호"> -->
        </div>
      </div>
      <button type="submit" :class="['btn-signup', { 'btn-disabled': !isSignupEnabled }]"> 가입</button>
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
        memberName: '',
        memberPhone: '',
        memberAccount: '',
        memberInfo: '', // 반환된 데이터를 저장할 변수
        passwordVisible: false,
        IdMsg: '로그인 시에 사용됩니다.',
        PasswordMsg: '14자 이내로 작성',
        accountPart1: '',
        // accountPart2: '',
        // accountPart3: '',
        // accountPart4: '',
      };
    },
    computed: {
    isSignupEnabled() {
      return (this.memberEmail && this.memberPassword && this.memberName && this.memberPhone && this.accountPart1 &&  this.IdMsg === '사용할 수 있는 이메일입니다');
    },
  },
    methods: {
      limitAccountLength() {
        if (this.accountPart1.length > 14) {
        this.accountPart1 = this.accountPart1.slice(0, 14);
        }
        else if (this.memberPassword.length > 14) {
        this.memberPassword = this.memberPassword.slice(0, 14);
        }
      },
      signup() {
        if (!this.isSingupEnabled) {
        return;
      }
      const memberAccount = this.accountPart1;

        axios.post("/api/member/signup", {
          memberEmail: this.memberEmail,
          memberPassword: this.memberPassword,
          memberName: this.memberName,
          memberPhone: this.memberPhone,
          memberAccount: memberAccount,
        })
        .then(response => {
            console.log("성공했습니다");
            this.memberInfo = response.data; // 반환된 데이터를 변수에 저장
        })
        .catch(error => {
            console.log("실패했습니다");
            console.log(error);
        });
      },
      togglePasswordVisibility() {
      this.passwordVisible = !this.passwordVisible;
      },
      checkEmail() {
        if (!this.memberEmail) {
          this.IdMsg = "이메일을 입력하세요";
          return;
        }
        this.IdMsg = "중복 체크 중...";
        axios.post("/api/member/checkemail", {
          memberEmail: this.memberEmail,
        })
      .then((response) => {
        console.log("중복 체크 성공");
        console.log(response.data);
        if (response.data) {
          this.IdMsg = "중복된 이메일입니다";
        } else {
          this.IdMsg = "사용할 수 있는 이메일입니다";
        }
      })
      .catch((error) => {
        console.log("중복 체크 오류");
        console.log(error);
      });
      },
    },
  };
  </script>

<style scoped>
.input-wrapper {
  display: flex;
  align-items: center;
  width: 100%;
}

.check-button {
  margin-left: 7px;
  font-size: 13px;
  white-space: nowrap;
  border: none;
  border-radius: 3px;
  background-color: #cccccc;
  color: black;
}

hr {
  width: 50%;
}
.signup-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.signup-form {
  width: 300px;
}
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.input-label {
  display: flex;
  align-items: center;
}

label {
  margin-right: 5px;
  font-size: 13px;
  margin-bottom: 5px;
  font-weight: bold;
}

.signup-message {
  font-size: 11px;
  color: rgb(252, 25, 25);
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
  border-color: #ffdd00;
  border-width: 2px;
  outline: none;
}

input#password:focus {
  border-color: #ffdd00;
  border-width: 2px;
  outline: none;
}

input::placeholder {
  font-size: 13px;
}
.btn-signup {
  width: 100%;
  height: 40px;
  background-color: #ffdd00;
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
}
</style>