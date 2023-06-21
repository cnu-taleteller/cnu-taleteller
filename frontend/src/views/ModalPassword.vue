<template>
    <div>
      <div class="container my-auto">
        <div class="row">
          <div class="card z-index-0 fadeIn3 fadeInBottom">
            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
              <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                <h4 class="text-white font-weight-bolder text-center mt-2 mb-0">비밀번호 찾기</h4>
              </div>
            </div>
            <div class="card-body">
              <form role="form" class="text-start" @submit.prevent="sendEmail">
                <p>입력한 이메일로 임시 비밀번호가 전송됩니다.</p>
                <div class="input-group input-group-outline my-3">
                  <label class="form-label">Email</label>
                  <input type="email" v-model="userEmail" class="form-control" required>
                </div>
                <div class="text-center">
                  <button type="submit" class="btn bg-gradient-primary w-100 my-4 mb-2">비밀번호 발송</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
  
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        userEmail: '',
      };
    },
    methods: {
      sendEmail() {
        const formData = new FormData();
        formData.append('memberEmail', this.userEmail);
  
        axios
          .post(`${process.env.VUE_APP_API_PATH}/api/member/sendEmail`, formData)
          .then((response) => {
            if (response.data === 'yes') {
              alert('임시 비밀번호를 전송했습니다.');
              window.location.href = '/LoginView';
            } else if (response.data === 'no'){
              alert('가입되지 않은 이메일입니다.');
              location.reload();
            }
          })
          .catch((error) => {
            console.error('에러 체크!!', error);
          });
      },
    },
  };
  </script>
  