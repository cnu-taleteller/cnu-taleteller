<template>
  <div class="management-book">
    <table>
      <thead>
      <tr>
        <th>책번호</th>
        <th>책이름</th>
        <th>작성자</th>
        <th>카테고리</th>
        <th>작성일</th>
        <th>추천수</th>
        <th>상태</th>
        <th>공개</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="book in books" :key="book.bookId">
        <td>{{ book.bookId }}</td>
        <td>{{ book.bookName }}</td>
        <td>{{ book.member.memberName }}</td>
        <td>{{ book.bookCategory }}</td>
        <td>{{ book.bookRegdate }}</td>
        <td>{{ book.bookRecommend }}</td>
        <td v-if="book.bookPublic === '1'">비공개</td>
        <td v-else>공개</td>
        <button v-if="book.bookPublic === '1'" class="public-button" @click="setBookPublic(book.bookId)">공개</button>
        <button v-else class="private-button" @click="setBookPublic(book.bookId)">비공개</button>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      books: [],
    };
  },
  mounted() {
    this.fetchBooks();
  },
  methods: {
    fetchBooks() {
      axios
          .get(`${process.env.VUE_APP_API_PATH}/api/admin/book`)
          .then((response) => {
            this.books = response.data;
            this.books.forEach((book) => {
              book.bookRegdate = new Date(book.bookRegdate).toLocaleString("ko-KR");
            })
          })
          .catch((error) => {
            console.error(error);
          });
    },
    setBookPublic(bookId) {
      const book = this.books.find((b) => b.bookId === bookId);
      if (book) {
        if (book.bookPublic === "1") {
          if (confirm("해당 작품을 비공개 처리하시겠습니까?")) {
            axios
                .post(`${process.env.VUE_APP_API_PATH}/api/admin/book/public/${bookId}`, {
                  bookPublic: "0",
                })
                .then((response) => {
                  const updatedPublic = response.data;
                  const index = this.books.findIndex(
                      (b) => b.bookId === updatedPublic.bookId
                  );
                  if (index !== -1) {
                    this.books.splice(index, 1, updatedPublic);
                  }
                  this.fetchBooks();
                })
                .catch((error) => {
                  console.error(error);
                });
          }
        } else {
          if (confirm("해당 작품을 공개 처리하시겠습니까?")) {
            axios
                .post(`${process.env.VUE_APP_API_PATH}/api/admin/book/public/${bookId}`, {
                  bookPublic: "1",
                })
                .then((response) => {
                  const updatedPublic = response.data;
                  const index = this.books.findIndex(
                      (b) => b.bookId === updatedPublic.bookId
                  );
                  if (index !== -1) {
                    this.books.splice(index, 1, updatedPublic);
                  }
                  this.fetchBooks();
                })
                .catch((error) => {
                  console.error(error);
                });
          }
        }
      }
    },
  },
};
</script>

<style scoped>
.management-book {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 100vh;
}

table {
  width: 100%;
  border-collapse: collapse;
  border: none;
  margin-top: 20px;
}

thead th {
  background-color: #f0f0f0;
  font-weight: bold;
  border: none;
}

th, td {
  padding: 10px;
  text-align: center;
  border: none;
}

tbody tr:nth-child(even) {
  background-color: #f5f5f5;
}

.public-button, .private-button {
  padding: 8px 12px;
  font-size: 14px;
  margin-top: 10%;
}

.public-button {
  background-color: #4AAEE2;
  color: white;
  border: none;
  border-radius: 5px;
}

.private-button {
  background-color: #faab19;
  color: white;
  border: none;
  border-radius: 5px;
}
</style>
