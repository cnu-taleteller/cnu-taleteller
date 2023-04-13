<template>
<div class="mypage">
    <div class="myheader">
        <h3>헤더</h3>
    </div>
    <div class="mypage_munu_content">
    <div class='left'>
        <ul>
          <li v-for='list in linklist'>
          <router-link
            style=text-decoration:none;color:black;
            :to='list.link' >{{ list.content }}
          </router-link>
          </li>
        </ul>
    </div>
    <div class="mypage_content">
    <div class="search_sort">
      <input type="text" class="mysearch">
      <input type="submit" value="검색" class="mysearchbtn">
      <select class="mysort">
        <option>최신순</option>
        <option>추천순</option>
        <option>결제순</option>        
      </select>
    </div>
    <div class="mymain"> 
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
        {content: '회원 정보', link: '/member'},
        {content: '작품 관리', link: '/my'},
        {content: '포인트 관리', link: '/pointmanage'},
        {content: '환급', link: '/return'}
      ]
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
      }
    },
    created () {
      this.resize()
    }
  }
  </script>
  <style>
    .mypage{
        width: 100%;
        height: 100%;
    }
    .mypage_munu_content{
        height: 80%;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .mypage_content{
        width: 100%;
        height: 100%;
        margin: 5px 10px 10px 5px;
    }
    .myheader{
    height: 80px;
    margin: 10px;
    background:rgba(200, 200, 200, 0.6);  
    display: flex;
    align-items: center;
    justify-content: space-around;
    }
    .left{
      width:300px; 
      height: 620px; 
      background:rgba(235, 235, 235, 0.6);  
      padding:40px 0; 
      overflow: hidden;
      display: flex;
      flex-direction: column;
      margin: 0 0 10px 10px;
    }
    .left ul {padding: 0 30px;}
    .left ul li {
        font-size:25px;
        height:75px;
        list-style:none;
        font-weight:bold;
    } 
    .search_sort{
      width: 100%;
      height: 70px;
      background:rgba(235, 235, 235, 0.6); 
      overflow: hidden;
      display: flex;
      margin: 5px 10px 10px 0;
    }
    .mysearch{
      height: 30px;
      margin: 20px 5px;
      margin-left: 5%;
    }
    .mysearchbtn{
      height: 30px;
      margin-top: 20px;
      margin-bottom: 20px;
    }
    .mysort{
      height: 30px;
      margin: 20px 5px;
      margin-left: 10px;
    }
    .mymain{
      width: 100%;
      height: 540px;
      background:rgba(235, 235, 235, 0.6); 
      padding:40px 0; 
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