<template>
<div class="mypage-all">
    <div class="mypage-header">
        <h3>헤더</h3>
    </div>
  <div class="mypage-body">
      <div class='mypage-side'>
          <ul>
            <li v-for='list in linklist'>
            <router-link
              style=text-decoration:none;color:black;
              :to='list.link' >{{ list.content }}
            </router-link>
            </li>
          </ul>
      </div>
    <div class="mypage-main">
      <div class="mypage-wallet">
        <div class="point-wallet">
          <h1>보유 엽전 0개</h1>
        </div>
      </div>
      <div class="mypage-content">
        <div class="point-content">
          <div class="point-button">
          <button @click="changeTab('PointCharge')">충전하기</button>
          <button @click="changeTab('PointDetails')">충전내역</button>
          </div>
          <div>
            <keep-alive>
                <component :is="component"></component>
            </keep-alive>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
  </template>
<script>
  export default {
    data () {
    return {
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
        }
  },
    methods: {
      resize: function () {
        $(window).resize(function () {
          var height = (window.innerHeight / 2) + 'px'
          $('#left_btn').css({'top': height})
        })
      },
      getSessionStorage(){
        var workname = sessionStorage.getItem("projectTitle");
        return workname;
      },
      changeTab(tab) {
            this.tab = tab;
      },
    },
    created () {
      //this.resize()
    }
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
      background:rgba(200, 200, 200, 0.6);  
      display: flex;
      align-items: center;
      justify-content: space-around;
    }
    .mypage-side{
      width:300px; 
      height: 620px; 
      background:rgba(235, 235, 235, 0.6);  
      padding:40px 0; 
      overflow: hidden;
      display: flex;
      flex-direction: column;
      margin: 0 0 10px 10px;
    }
    .mypage-side ul {padding: 0 30px;}
    .mypage-side ul li {
        font-size:25px;
        height:75px;
        list-style:none;
        font-weight:bold;
    } 
    .mypage-wallet{
      width: 100%;
      height: 100px;
      background:rgba(235, 235, 235, 0.6); 
      overflow: hidden;
      display: flex;
      margin: 5px 10px 10px 0;
    }
    .mypage-content{
      width: 100%;
      height: 510px;
      background:rgba(235, 235, 235, 0.6); 
      padding:20px; 
      overflow: hidden;
      display: flex;
      margin: 5px 10px 10px 0;
    }
    .point-wallet{
      margin: auto;
      width: 80%;
      height: 70px;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 1px solid gray;
      border-radius: 10px;
    }
    .point-content{
      width: 100%;
      height: 100%;
      border: 1px solid gray; 
    }
    .point-button{
      margin: 5%;
    }
    .point-button button{
      width: 30%;
      height: 50px; 
    }

</style>