<template>
  <div class='mypage-side'>
    <ul>
      <li v-for='list in linklist'>
      <router-link class="side-link" :to='list.link' >
        {{ list.content }}
      </router-link>
      </li>
    </ul>
  </div>
</template>
<script>
  import axios from "axios"
  export default {
    props: {
  
    },
    data() {
      return {
        linklist: [
          {content: '홈', link: '/'},
          {content: '회원 정보', link: '/mypage/member'},
          {content: '작품 관리', link: '/mypage/workmanage'},
          {content: '엽전 관리', link: '/mypage/pointmanage'},
          {content: '즐겨찾기', link: '/mypage/bookmark'}
        ],
      }
    },
    created() {
      
    },
    computed: {

    },
    methods: {
      
    },
  }
  
</script>
<style scoped>
  .mypage-side {
    width: 15%;
    float: left;
    height: 100%;
    font-weight: bold;
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

  .router-link-exact-active {
    background: #c3f0ff;
    border-radius: 15px;
  }

  .mypage-side a{
    padding-top: 15px;
    padding-bottom: 15px;
    padding-left: 20px;
    padding-right: 20px
  }
  .side-link {
    text-decoration: none;
    color: black;
    font-weight: bold;
  }
</style>