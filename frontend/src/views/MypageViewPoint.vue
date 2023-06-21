<template>
<div class="mypage-all">
  <div class="mypage-body">
    <SideMenu></SideMenu>
    <div class="mypage-main">
      <div class="mypage-wallet">
        <div class="point-wallet">
          현재 보유 엽전<img src="@/assets/coin.png" width="25"> <br><br> {{ pointTotal }} 개
        </div>
      </div>
      <div class="mypage-content">
        <div class="point-content">
          <div class="point-button">
          <button @click="changeTab('PointCharge')">충전하기</button>
          <button @click="changeTab('PointDetails')">엽전내역</button>
          <button @click="pointReturn(pointTotal*-1)">환급하기</button>
          <hr>
          </div>
          
            <keep-alive>
                <component :is="component"></component>
            </keep-alive>
          
        </div>
      </div>
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
      tab: "PointCharge",
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
  },
  methods: {
      getSessionStorage(){
        var workname = sessionStorage.getItem("projectTitle");
        return workname;
      },
      
      changeTab(tab) {
            this.tab = tab;
      },

      getDetails(){
        this.memberEmail = sessionStorage.getItem('user')
        axios.get(`${process.env.VUE_APP_API_PATH}/api/point/wallet/${this.memberEmail}`)
        .then((res) => {
          console.log(res);
          this.detailsResult = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      },

      pointReturn(returnValue){
        if(confirm("환급하시겠습니까?")){
          if(this.pointTotal==0){
            alert("환급할 엽전이 없습니다.")
          }
          else{
            this.memberEmail = sessionStorage.getItem('user')
            axios.post(`${process.env.VUE_APP_API_PATH}/api/point/return/${this.memberEmail}`, {
              returnPoint: returnValue,
            })
            .then((res) => {
              alert("환급 완료 : 엽전 "+res.data+"개");
              window.location.reload(true);
            })
            .catch((err) => {
              console.log(err);
            }); 
          }
        }
      },
      
  },
    created () {
      this.getDetails()
      //alert(window.location.search)
      //history.replaceState({}, null, location.pathname);
    },
    components: {
      SideMenu: sideMenu,
    },
    
  }
</script>

<style scoped>
.mypage-side {
  width: 15%;
  float: left;
  height: 100%;
}

.mypage-side ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
}

.mypage-side li {
  padding: 15px;
  margin-bottom: 5px;
  border-radius: 0px;
}

.mypage-side li:hover {
  background-color: #c3f0ff;
}
.mypage-side li:nth-child(4){
  background-color: #c3f0ff;
}

.side-link {
  text-decoration: none;
  color: black;
  font-weight: bold;
}
.mypage-main {
  width: 80%;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #ccc;
}

.mypage-wallet {
  display: flex;
  align-content: center;
  justify-content: center;
  width: 100%;
  padding: 20px;
  /* margin-bottom: 20px; */
}

.point-wallet {
  font-weight: bold;
  width: 300px;
  margin-top: 20px;
  /* border-bottom: 2px solid #ccc; */
}

.mypage-content {
  padding: 20px;
}

.point-button {
  text-align: center;
}

.point-button button {
  margin: 5px;
  padding: 10px 20px;
  background-color: #fceb6e;
  font-weight: bold;
  color: rgb(51, 51, 51);
  border-radius: 3px;
  border: none;
  cursor: pointer;
}

.point-button button:hover {
  opacity: 0.7;
}

.tab-content {
  padding: 20px;
  border-radius: 3px;
}

.hr-line {
  border-top: 1px solid #ccc;
  margin: 20px 0;
}

.mypage-wallet, .tab-content {
  border: none;
}
</style>