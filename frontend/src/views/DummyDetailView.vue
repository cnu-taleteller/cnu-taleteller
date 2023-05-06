<template>
  <div class="book" v-if="bookDummy">
    <div class="book-detail" v-if="projectTitle">
      <div class="book-image">
        <img src="@/assets/bookDummies/book.png" :alt="projectTitle">
      </div>
      <div class="book-info">
        <!-- 이름이랑 설명은 세션에 저장되지 않아서 우선 비워 뒀습니다 -->
        <h1>{{ projectTitle }}</h1>
        <p>설명</p>
      </div>
    </div>
    <div class="book-detail">
      <div class="book-image">
        <img :src="require(`@/assets/bookDummies/${bookDummy.image}`)" :alt="bookDummy.title">
      </div>
      <div class="book-info">
        <h1>{{ bookDummy.title }}</h1>
        <p>설명</p>
      </div>
    </div>
    <div class="book-button">
      <button class="btn1" @click="recommend">{{ recommendCount }} 추천</button>
      <button class="btn2">즐겨찾기</button>
      <button class="btn3">결제</button>
    </div>
    <div class="book-reply">
      <form @submit.prevent="addReply">
        <input type="text" v-model="newReply" placeholder="댓글을 입력하세요">
        <button type="submit">등록</button>
      </form>
      <ul class="reply-list">
        <li v-for="reply in replies" :key="reply.id" class="reply-item">
          <div class="reply-author">이름</div>
          <div class="reply-content">{{ reply.content }}</div>
          <div class="reply-time">{{ currentTime }}</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      bookDummy: null,
      projectTitle: null,
      recommendCount: 0,
      replies: [],
      newReply: '',
      currentTime: new Date().toLocaleString()
    };
  },
  created() {
    const bookDummies = JSON.parse(sessionStorage.getItem('bookDummies'));
    const id = this.$route.params.id;
    this.bookDummy = bookDummies.find(book => book.id === Number(id));
    const bookWithId10 = bookDummies.find(book => book.id === 10);
    this.projectTitle = bookWithId10.title;
    setInterval(() => {
      const now = new Date();
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');
      this.currentTime = `${hours}:${minutes}:${seconds}`;
    }, 1000);

    const savedReplies = JSON.parse(sessionStorage.getItem('replies'));
    if (savedReplies) {
      this.replies = savedReplies;
    }
  },
  methods: {
    addReply() {
      if (!this.newReply.trim()) {
        return;
      }
      const newId = this.replies.length + 1;
      this.replies.push({
        id: newId,
        content: this.newReply
      });
      this.newReply = '';
      sessionStorage.setItem('replies', JSON.stringify(this.replies));
    },
    recommend() {
      if (sessionStorage.getItem('recommend') === 'true') {
        this.recommendCount--;
        sessionStorage.setItem('recommend', false);
        alert('이미 추천하셨습니다!');
      } else {
        this.recommendCount++;
        sessionStorage.setItem('recommend', true);
      }
    },
  }
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

.book-reply form {
  display: flex;
  margin-left: 5%;
  margin-top: 5%;
}

.book-reply input[type="text"] {
  flex-basis: 90%;
  padding: 10px;
  font-size: 16px;
  color: #333;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.book-reply button[type="submit"] {
  flex-basis:10%;
  padding: 10px 20px;
  font-size: 16px;
  color: #fff;
  border: none;
  border-radius: 5px;
  background-color: #faab19;
  cursor: pointer;
  margin-left: 10px;
}

.book-reply button[type="submit"]:hover {
  background-color: #e79e16;
}

.book-reply button[type="submit"]:active {
  background-color: #e79e16;
}

.reply-list {
  list-style-type: none;
  padding-left: 0;
}

.reply-item {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}

.reply-author {
  font-weight: bold;
  flex-basis: 20%;
  align-self: flex-start;
}

.reply-content {
  flex-basis: 60%;
  text-align: left;
}

.reply-time {
  flex-basis: 20%;
  align-self: flex-end;
}
</style>