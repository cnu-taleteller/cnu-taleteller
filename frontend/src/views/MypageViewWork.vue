<template>
<div class="mypage-all">
    <!-- <div class="mypage-header">
        <h3>헤더</h3>
    </div> -->
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
      <div class="mypage-search">
        <input type="text" class="search-bar">
        <input type="submit" value="검색" class="search-btn">
        <select class="sort-dropdown">
          <option>최신순</option>
          <option>추천순</option>
          <option>결제순</option>        
        </select>
      </div>
      <div class="mypage-content"> 
        <div class="thumbnail">
            <table>
                <thead>
                    <tr>
                        <td>
                            <!--동화책 썸네일 대체-->
                            <img src='../assets/logo.png'>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            {{getSessionStorage()}}
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</div>
</div>
  </template>
  <script>
  export default {
    name: 'left',
    data () {
    return {
      fade: false,
      linklist: [
        {content: '홈', link: '/'},
        {content: '회원 정보', link: '/mypage/member'},
        {content: '작품 관리', link: '/mypage/workmanage'},
        {content: '엽전 관리', link: '/mypage/pointmanage'},
        {content: '즐겨찾기', link: '/mypage/bookmark'}
      ]
    }
  },
    methods: {
      getSessionStorage(){
        var workname = sessionStorage.getItem("projectTitle");
        return workname;
      },
    },
    created () {
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
  .mypage-search{
    width: 100%;
    height: 70px;
    background:rgba(235, 235, 235, 0.6); 
    overflow: hidden;
    display: flex;
    margin: 5px 10px 10px 0;
  }
  .search-bar{
    height: 30px;
    margin: 20px 5px;
    margin-left: 5%;
  }
  .search-btn{
    height: 30px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .sort-dropdown{
    height: 30px;
    margin: 20px 5px;
    margin-left: 10px;
  }
  .mypage-content{
    width: 100%;
    height: 540px;
    background:rgba(235, 235, 235, 0.6); 
    padding:30px; 
    overflow: hidden;
    display: flex;
    margin: 5px 10px 10px 0;
  }
  .thumbnail{
      margin-left: 5%;
      width:200px;
      height:200px;
      background:rgba(255, 255, 255, 0.6); 
  }
  .thumbnail img{
      width:200px;
      height:200px;
  }
  .thumbnail table{
      font-size: 20px;
      font-weight:bold;
  }
</style>