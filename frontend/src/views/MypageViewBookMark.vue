<template>
  <div class="mypage-all">
    <div class="mypage-body">
      <SideMenu></SideMenu>
      <div class="mypage-main">
      <div class="mypage-search">
        <form>
          <div class="search-wrapper">
            <input type="text" v-model="searchKeyword" placeholder="마이페이지 작품검색" class="search-bar">
            <button type="submit" @click="search">&#x1F50D;</button>
          </div>
        </form>
      </div>
      <div v-if="this.searchCheck == false" class="mypage-content">
        <div v-for="result in bookMarkResult" :key="result.bookId" class="thumbnail">
            <table @click="goToDetail(result.bookId)">
                <thead>
                    <tr>
                        <td>
                            <!--동화책 썸네일 대체-->
                            <img v-if="result.bookThumbnail == null" src='@/assets/book1.png'>
                            <img v-else v-bind:src='result.bookThumbnail'>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                          {{ result.bookName }}
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
      </div>
      <div v-else-if="this.searchCheck == true" class="mypage-content">
        <div v-for="result in bookMarkResult.filter((result) => result.bookName.includes(searchKeyword))" :key="result.bookId" class="thumbnail">
            <table @click="goToDetail(result.bookId)">
                <thead>
                    <tr>
                        <td>
                            <!--동화책 썸네일 대체-->
                            <img v-if="result.bookThumbnail == null" src='@/assets/book1.png'>
                            <img v-else v-bind:src='result.bookThumbnail'>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                          {{ result.bookName }}
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
import axios from "axios"
import sideMenu from '@/components/MyPage/SideMenu.vue';
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
      ],
      bookMarkResult: [],
      searchKeyword: "",
      searchCheck: false,
    }
  },
    methods: {
      getSessionStorage(){
        var workname = sessionStorage.getItem("bookName");
        return workname;
      },

      getBookMark() {
      this.memberEmail = sessionStorage.getItem('user')
      axios.get(`${process.env.VUE_APP_API_PATH}/api/v1/book/mybookmark/${this.memberEmail}`)
        .then((res) => {
          console.log(res);
          this.bookMarkResult = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      },

      goToDetail(id) {
        this.$router.push({ path: `/detail/${id}` });
      },

      search(event) {
        event.preventDefault();
          if (this.searchKeyword === "") {
            this.searchCheck = false;
          }
          else {
            this.searchCheck = true;
          }
      },

    },
  created() {
    this.getBookMark()
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
  padding: 20px;
  margin-top: 20px;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
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
  cursor: pointer;
}

.thumbnail table tbody{
  background: whitesmoke;
}

.thumbnail a {
  text-decoration: none;
  color: black;
}

select, input[type="text"], button[type="submit"] {
  border: none;
  border-bottom: 2px solid lightgray;
  padding: 5px;
  font-size: 1.2rem;
  font-weight: bold;
  background: none;
}

select:focus, input[type="text"]:focus {
  outline: none;
  border-bottom: 2px solid #4AAEE2;
}
.search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  margin-left: 5%;
}

button[type="submit"] {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  background-color: transparent;
  color: #1872A3;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  margin-right: -10px;
  transition: color .2s ease-in-out;
}

</style>