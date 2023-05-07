<template>
  <div class="mypage-all">
    <!-- <div class="mypage-header">
        <h3>헤더</h3>
    </div> -->
    <div class="mypage-body">
      <div class='mypage-side'>
        <ul>
          <li v-for='list in linklist'>
            <router-link style=text-decoration:none;color:black; :to='list.link'>{{ list.content }}
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
                            <img src='@/assets/book1.png'>
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
  data() {
    return {
      fade: false,
      linklist: [
        { content: '홈', link: '/' },
        { content: '회원 정보', link: '/mypage/member' },
        { content: '작품 관리', link: '/mypage/workmanage' },
        { content: '엽전 관리', link: '/mypage/pointmanage' },
        { content: '즐겨찾기', link: '/mypage/bookmark' }
      ]
    }
  },
    methods: {
      getSessionStorage(){
        var workname = sessionStorage.getItem("bookName");
        return workname;
      },
    },
  created() {
  }
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

.mypage-side li:nth-child(3) {
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
  /* border-bottom: 2px solid #ccc; */
}

.mypage-content {
  padding: 20px;
  margin-top: 20px;
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

.mypage-wallet,
.tab-content {
  border: none;
}

.mypage-search {
  width: 100%;
  height: 80px;
  /* background:rgba(210, 210, 210, 0.6);    */
  overflow: hidden;
  display: flex;
  margin: 5px 10px 10px 0;
  border-radius: 15px;
}

.search-bar {
  height: 40px;
  margin: 20px 5px;
  margin-left: 5%;
}

.search-btn {
  width: 7%;
  height: 40px;
  margin-top: 20px;
  margin-bottom: 20px;
  background-color: #ccc;
  font-size: 20px;
  /* color:white; */
  border-width: 0;
  border-radius: 5px;
}

.sort-dropdown {
  width: 10%;
  margin: 20px 5px;
  margin-left: 10px;
}

.mypage-content {
  width: 100%;
  height: 80vh;
  border-top: 1px solid #ccc;
  /* background:rgba(233, 233, 233, 0.6); */
  padding: 20px;
  overflow: hidden;
  display: flex;
  margin: 5px 10px 10px 0;
  /* border-radius: 15px; */
}

.thumbnail {
  margin-left: 5%;
  width: 200px;
  height: 200px;
  border: none;
  background: rgba(255, 255, 255, 0.6);
  box-shadow: 2px 2px 10px rgba(0, 0, 0, .1);
}

.thumbnail img {
  width: 200px;
  height: 200px;
}

.thumbnail table {
  font-size: 20px;
  font-weight: bold;
}

</style>