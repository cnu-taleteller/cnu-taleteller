<template>
<div class="mypage-all">
    <!-- <div class="mypage-header">
        <h3>헤더</h3>
    </div> -->
  <div class="mypage-body">
      <div class='mypage-side'>
          <ul>
            <li v-for='list in linklist'>
            <router-link class="side-link" :to='list.link' >
              {{ list.content }}
            </router-link>
            </li>
          </ul>
      </div>
    <div class="mypage-main">
      <div class="mypage-wallet">
        <div class="point-wallet">
          보유 엽전 : {{ pointTotal }} 개
          <button @click="pointReturn(pointTotal*-1)" class="point-returnbtn">환급하기</button>
        </div>
      </div>
      <div class="mypage-content">
        <div class="point-content">
          <div class="point-button">
          <button @click="changeTab('PointCharge')">충전하기</button>
          <button @click="changeTab('PointDetails')">엽전내역</button>
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
        axios.get("/api/point/wallet")
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
          axios.post("/api/point/return", {
            returnPoint: returnValue,
          })
          .then((res) => {
            console.log(res);
          })
          .catch((err) => {
            console.log(err);
          });  
        }
      },

      
    },
    created () {
      this.getDetails()
      //alert(window.location.search)
      //history.replaceState({}, null, location.pathname);
    },
    
  }
</script>
<style scoped>
    .mypage-all{
      width: 100%;
      height: 100%;
    }
    .mypage-body{
      width: 90%;
      height: 80%;
      margin:auto;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .mypage-main{
      width: 100%;
      height: 100%;
      margin: 5px 10px 10px 5px;
    }
    .mypage-header{
      height: 80px;
      margin: 10px;
      background:rgba(210, 210, 210, 0.6);  
      display: flex;
      align-items: center;
      justify-content: space-around;
    }
    .mypage-side{
      width:300px; 
      height: 720px; 
      background:rgba(210, 210, 210, 0.6);  
      padding:40px 0; 
      overflow: hidden;
      display: flex;
      flex-direction: column;
      margin: 0 0 10px 10px;
      border-radius: 15px;
    }
    .side-link{
      text-decoration:none;
      color:black;
    }
    .mypage-side ul {padding: 0 30px;}
    .mypage-side li {
      font-size:25px;
      height:75px;
      list-style:none;
      font-weight:bold;
      background-color: orange;
      border-radius: 15px;
      margin: 5%;
    }
    .mypage-side a{
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 15px;
      
    }
    .mypage-side a:hover{
      background-color: yellow;
      color:black;
    }
    .mypage-side li:nth-child(4){
      background-color: yellow;
      color:black;
    }
  
    .mypage-wallet{
      width: 100%;
      height: 100px;
      background:rgba(210, 210, 210, 0.6);    
      overflow: hidden;
      display: flex;
      margin: 5px 10px 10px 0;
      border-radius: 15px;
      color: black;
    }
    .mypage-content{
      width: 100%;
      height: 610px;
      background:rgba(233, 233, 233, 0.6);
      padding:20px; 
      overflow: hidden;
      display: flex;
      margin: 5px 10px 10px 0;
      border-radius: 15px;
    }
    .point-wallet{
      margin: auto;
      width: 80%;
      height: 70px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: orange;
      border-radius: 10px;
      font-weight:bold;
      font-size: 30px;
    }
    
    .point-content{
      width: 100%;
      height: 100%;
      border-radius: 15px;
    }
    .point-button{
      margin: 3%;
    }
    .point-button button{
      width: 20%;
      height: 70px;
      font-weight: bold;
      font-size: 25px;
      border-radius: 10px;
      border-width: 0;
      margin: 1%;
      background-color:orange;
    }
    .point-button button:focus{
      background-color: yellow;
    }
    .point-button button:hover{
      background-color: yellow;
    }
    .point-returnbtn{
      width: 15%;
      height: 70%;
      margin-left: 5%;
      font-weight: bold;
      font-size: 22px;
      border-radius: 10px;
      border-width: 0;
    }
    .point-returnbtn:hover{
      background-color: yellow;
    }

</style>