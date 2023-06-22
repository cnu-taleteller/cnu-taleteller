<template>
  <div class="book">
    <div class="book-detail">
      <div class="book-image">
        <img :src="bookThumbnail" :alt="bookName"></div>
      <div class="book-info">
        <h1>{{ bookName }}</h1>
        <p>{{ memberName }}</p>
        <p>{{ bookDescription }}</p>
      </div>
    </div>
    <div class="book-button">
      <button class="btn1" @click="toggleRecommend">{{ bookRecommend }} 추천</button>
      <button class="btn2" @click="toggleBookmark">즐겨찾기</button>
      <button class="btn3" @click="togglePreview">{{ this.paymentCheck }}</button>
    </div>
    <Reply :replies="replies" @addReply="addReply" @editReply="editReply" @deleteReply="deleteReply" />
  </div>
</template>

<script>
import Reply from '../components/Reply.vue';
import axios from "axios";

export default {
  components: {
    Reply
  },
  data() {
    return {
      book: null,
      bookName: null,
      bookDescription: null,
      bookRecommend: null,
      replies: [],
      memberId: null,
      memberName: null,
      paymentCheck: '결제',
      isRecommended: false,
      isBookmark: false,
      isPayment: false,
      bookThumbnail: null,
      bookData: null,
      payResult: [],
    };
  },
  created() {
    const id = this.$route.params.id;

    axios
        .get(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/${id}`)
        .then((response) => {
          this.book = response.data.book;
          this.bookName = this.book.bookName;
          this.bookDescription = this.book.bookDescription;
          this.memberName = this.book.member.memberName;
          this.bookRecommend = this.book.bookRecommend;
          this.bookThumbnail = this.book.bookThumbnail;
          this.replies = response.data.replies;
          this.replies.forEach((reply) => {
            reply.replyRegdate = new Date(reply.replyRegdate).toLocaleString("ko-KR");
          });
        })
        .catch((error) => {
          console.log(error);
        });

    this.payCheck();
    this.getWallet();
    
  },
  computed: {
    pointTotal() {
      let sum = 0;
      return this.payResult.reduce((sum, result) => sum + result.payCount, 0);
    },
  },
  methods: {
    // 댓글
    addReply(replyContent) {
      const id = this.$route.params.id;

      axios
          .post(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/reply`, {
            bookId: this.book.bookId,
            memberEmail: sessionStorage.getItem('user'),
            replyContent: replyContent,
          })
          .then((response) => {
            const replyContent = response.data;
            console.log(response.data);
            this.replies.push(replyContent);
            this.fetchBookDetail(id);
          })
          .catch((error) => {
            console.log(error);
          });
    },
    editReply(replyId, replyContent) {
      const id = this.$route.params.id;

      axios
          .put(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/reply/${replyId}`, {
            replyContent: replyContent,
          })
          .then((response) => {
            const updatedReply = response.data;
            const index = this.replies.findIndex((reply) => reply.replyId === replyId);
            if (index !== -1) {
              this.replies.splice(index, 1, updatedReply);
              this.fetchBookDetail(id);
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },
    deleteReply(replyId) {
      axios
          .delete(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/reply/${replyId}`)
          .then(() => {
            const index = this.replies.findIndex((reply) => reply.replyId === replyId);
            if (index !== -1) {
              this.replies.splice(index, 1);
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },

    // 추천
    toggleRecommend() {
      if (this.isRecommended) {
        this.unrecommend();
      } else {
        this.recommend();
      }
    },
    recommend() {
      const id = this.$route.params.id;

      axios
          .post(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/${id}/recommend`, {
            bookId: this.book.bookId,
            memberEmail: sessionStorage.getItem('user')
          })
          .then((response) => {
            this.bookRecommend = response.data.bookRecommend;
            alert("작품을 추천합니다!");
            this.isRecommended = true;
            this.fetchBookDetail(id);
          })
          .catch(error => {
            console.log(error);
          });
    },
    unrecommend() {
      const id = this.$route.params.id;

      axios
          .delete(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/${id}/recommend`, {
            data: {
              bookId: this.book.bookId,
              memberEmail: sessionStorage.getItem('user')
            }
          })
          .then(() => {
            alert("작품 추천을 취소합니다!");
            this.isRecommended = false;
            this.fetchBookDetail(id);
          })
          .catch(error => {
            console.log(error);
          });
    },

    // 즐겨찾기
    toggleBookmark() {
      if (this.isBookmark == true) {
        this.unbookmark();
      } else {
        this.bookmark();
      }
    },
    bookmark(e) {
      const id = this.$route.params.id;

      if (confirm("즐겨찾기하시겠습니까?")) {
        axios
            .post(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/${id}/bookmark`, {
              bookId: this.book.bookId,
              memberEmail: sessionStorage.getItem('user')
            })
            .then(() => {
              this.isBookmark = true;
              this.fetchBookDetail(id);
              const bookmark = confirm("즐겨찾기되었습니다! 즐겨찾기 창으로이동하시겠습니까?");
              if (bookmark == true) {
                this.$router.push({ path: `/mypage/bookmark` });
              } else {
                e.preventDefault();
                console.log("페이지 이동 안 함")
              }
            })
            .catch((error) => {
              console.log(error);
            });
      }
    },
    unbookmark() {
      const id = this.$route.params.id;

      axios
          .delete(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/${id}/bookmark`, {
            data: {
              bookId: this.book.bookId,
              memberEmail: sessionStorage.getItem('user')
            }
          })
          .then(() => {
            alert("즐겨찾기를 취소합니다!");
            this.isBookmark = false;
            this.fetchBookDetail(id);
          })
          .catch(error => {
            console.log(error);
          });
    },

    fetchBookDetail(id) {
      axios
          .get(`${process.env.VUE_APP_API_PATH}/api/v1/book/detail/${id}`)
          .then((response) => {
            this.book = response.data.book;
            this.bookName = this.book.bookName;
            this.bookDescription = this.book.bookDescription;
            this.memberName = this.book.member.memberName;
            this.bookRecommend = this.book.bookRecommend;
            this.replies = response.data.replies;
            this.replies.forEach((reply) => {
              reply.replyRegdate = new Date(reply.replyRegdate).toLocaleString("ko-KR");
            });
          })
          .catch((error) => {
            console.log(error);
          });
    },

    togglePreview() {
      if (this.isPayment) {
        this.bookPreview();
      } else {
        this.bookPayment();
      }
    },
    bookPayment() {
      if(sessionStorage.getItem('user')==null){
        alert("로그인이 필요합니다.");
      }
      else if (confirm("결제하시겠습니까?")) {
        if(this.pointTotal<20){
          alert("엽전 부족 : 현재 "+this.pointTotal+"개");
        }else{
          const id = this.$route.params.id;
          axios.post(`${process.env.VUE_APP_API_PATH}/api/point/bookPayment/${id}`, {
            bookId: this.book.bookId,
            memberEmail: sessionStorage.getItem('user')
          })
          .then((res) => {
            alert("상품이 결제되었습니다.")
            this.paymentCheck = "감상"
            this.isPayment = true
            console.log(res);
          })
          .catch((err) => {
            console.log(err);
          });
        }
      }
    },

    bookPreview() {
      const id = this.$route.params.id;
      axios
          .post(`${process.env.VUE_APP_API_PATH}/api/v1/tool/firstAccess/${id}`)
          .then((response) => {
            const pageLists = response.data;
            console.log(pageLists);
            if (pageLists.bookData && pageLists.bookData.pageList) {
              this.pageList = pageLists.bookData.pageList;
              this.$emit('currentPageList', this.pageList[0]);
              this.$store.commit('setPageList', this.pageList);
              this.$emit('pageList', this.pageList);
            }
          })
          .catch((error) => {
            console.error(error);
          });
        window.open('/preview', 'previewWindow', 'width=1100, height=600');
    },

    getWallet(){
      if(this.memberEmail = sessionStorage.getItem('user')){
        axios.get(`${process.env.VUE_APP_API_PATH}/api/point/wallet/${this.memberEmail}`)
        .then((res) => {
          console.log(res);
          this.payResult = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      }
    },

    payCheck() {
      if(this.memberEmail = sessionStorage.getItem('user')) {
        const id = this.$route.params.id;
        axios.get(`${process.env.VUE_APP_API_PATH}/api/v1/book/paycheck/${id}`, {
          params: {
            memberEmail: this.memberEmail,
          },
        })
        .then((response) => {
          if(response.data) {
            console.log("본인 작품 및 결제 여부 : "+response.data);
            this.paymentCheck = "감상";
            this.isPayment = true;
          } else {
            console.log("본인 작품 및 결제 여부 : "+response.data);
          }
        })
        .catch((error) => {
          console.log("확인 실패했습니다");
          console.log(error);
        });
      }
    },   
  },
}
</script>

<style scoped>
.book {
  margin-top: 5%;
  margin-left: 15%;
  margin-right: 15%;
}

.book-detail {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 5%;
}

.book-image {
  flex-basis: 40%;
}

.book-image img {
  max-width: 70%;
  height: auto;
}

.book-info {
  flex-basis: 40%;
  text-align: left;
}

.book-button {
  display: flex;
  flex-wrap: wrap;
  margin-left: 5%;
}

.book-button button {
  flex-basis: 33.3%;
  padding: 10px 20px;
  font-size: 16px;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.book-button .btn1 {
  background-color: #97C93E;
}

.book-button .btn2 {
  background-color: #EE7CAE;
}

.book-button .btn3 {
  background-color: #4AAEE2;
}

</style>
