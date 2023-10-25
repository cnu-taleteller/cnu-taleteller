<template>
  <div class="mypage-all">
      <SideMenu></SideMenu>
      <div class="mypage-main">
          <div v-if="checkState === 'no'" class="mypage-content">
            <div class="passwrod-check-all">
              <div class="password-check-body">
                <h1>비밀번호 확인</h1>
                <form @submit.prevent="passwordCheck" class="password-check-form">
                  <div class="form-group">
                    <div class="password-input">
                      <input v-model="memberPassword" :type="passwordVisible ? 'text' : 'password'" id="password" name="password"
                        placeholder="비밀번호를 입력하십시오">
                      <span class="password-toggle" @click="togglePasswordVisibility">
                        <i class="material-icons">{{ passwordVisible ? 'visibility' : 'visibility_off' }}</i>
                      </span>
                    </div>
                  </div>
                  <button type="submit" :class="['btn-login', { 'btn-disabled': !isLoginEnabled }]">확인</button>
                </form>
              </div>
            </div>
          </div>
            <div v-else-if="checkState === 'yes'" class="mypage-content">
              <keep-alive>
                <component :is="component"></component>
              </keep-alive>
            </div>  
      </div>
  </div>
      </template>
      
    <script>
    import axios from 'axios';
    import sideMenu from '@/components/MyPage/SideMenu.vue';
      export default {
        data () {
          
        return {
          detailsResult: [],
          fade: false,
          linklist: [
            {content: '홈', link: '/'},
            {content: '회원 정보', link: '/mypage/member'},
            {content: '작품 관리', link: '/mypage/workmanage'},
            {content: '엽전 관리', link: '/mypage/pointmanage'},
            {content: '즐겨찾기', link: '/mypage/bookmark'}
          ],
          tab: "MemInfo",

          memberEmail: '',
          memberPassword: '',
          passwordVisible: false,
          checkState: 'no',
        }
      },
      
      computed: {
        component() {
          const tab = this.tab;
          //본문 버튼 클릭 시 changeTab메서드를 통해 tab값 변경
          //이후 tab 변수에 대입되어 동적 컴포넌트 구현
          return () => import(`@/components/MyPage/${tab}`);
        },
    
        pointTotal() {
          let sum = 0;
          return this.detailsResult.reduce((sum, result) => sum + result.payCount, 0);
        },

        isLoginEnabled() {
          return this.memberPassword;
        },
      },
      methods: {
          getSessionStorage(){
            var workname = sessionStorage.getItem("projectTitle");
            return workname;
          },
          
          changeTab(tab) {
                this.tab = tab;
          },
    
          togglePasswordVisibility() {
            this.passwordVisible = !this.passwordVisible;
          },

          passwordCheck() {
            if (!this.isLoginEnabled) {
              return;
            }
            this.memberEmail = sessionStorage.getItem('user')
            axios.get(`${process.env.VUE_APP_API_PATH}/api/member/pwcheck`, {
              params: {
                memberEmail: this.memberEmail,
                memberPassword: this.memberPassword,
              },
            })
            .then((response) => {
              if (response.data) {
                console.log("패스워드 확인 : "+response.data);
                console.log(sessionStorage.getItem('user'));
                this.checkState = 'yes';
              } else {
                alert("유효한 비밀번호가 아닙니다.");
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
        },
        created () {
          
        },
        components: {
          SideMenu: sideMenu,
        },
      }
    </script>
    
    <style scoped>
    .mypage-main {
      width: 80%;
      display: flex;
      flex-direction: column;
      border-left: 1px solid #ccc;
    }
    
    .mypage-content {
      width: 100%;
      height: 80vh;
      padding: 20px;
      overflow: hidden;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 5px 10px 10px 0;
    }

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

    .password-check-form {
      width: 300px;
    }

    .form-group {
      margin-bottom: 15px;
    }

    input {
      width: 100%;
      height: 40px;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 16px;
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
    
    .passwrod-check-all {
      width: 100%;
      height: 50vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }
    
    .password-check-body {
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

    .password-check-body h1 {
      margin-bottom:7%;
    }
    </style>