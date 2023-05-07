<template>
  <div class="container">
    <div v-for="(bookDummy, index) in bookDummies" :key="index" class="book-dummies" v-if="bookDummy.title !== null">
      <div class="wrapper" @click="goToDetail(bookDummy.id)">
        <img :src="require(`@/assets/bookDummies/${bookDummy.image}`)" :alt="bookDummy.title">
        <div>
          <p>{{ bookDummy.title }}</p>
          <button>#동화</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      bookDummies: [
        { id: 1, title: sessionStorage.getItem('bookName'), image: 'book.png', description: sessionStorage.getItem('bookDescription')},
        { id: 2, title: '양치질은 싫어요', image: 'book1.jpg', description: '설명' },
        { id: 3, title: '수탉과 돼지', image: 'book2.png', description: '설명' },
        { id: 4, title: '나도 편식할 거야', image: 'book3.jpg', description: '설명' },
        { id: 5, title: '또박또박 읽고 써요', image: 'book4.jpg', description: '설명' },
        { id: 6, title: '회사에 다녀요', image: 'book5.jpg', description: '설명' },
        { id: 7, title: '오래 친구들', image: 'book6.jpg', description: '설명' },
        { id: 8, title: '그림 형제', image: 'book7.jpg', description: '설명' },
        { id: 9, title: '못난이 아기 잠자리', image: 'book8.jpg', description: '설명' },
        { id: 10, title: '미녀와 야수', image: 'book9.jpg', description: '설명' }
      ],
      bookName: null,
      bookDescription: null
    }
  },
  created() {
    sessionStorage.setItem('bookDummies', JSON.stringify(this.bookDummies));
    this.bookName = sessionStorage.getItem('bookName');
  },
  methods: {
    goToDetail(id) {
      this.$router.push({ path: `/detail/${id}` });
    },
  },
}
</script>

<style scoped>
.container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}

.book-dummies {
  margin-top: 5%;
  flex-basis: 20%;
  text-align: center;
  margin-left: 5%;
}

.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 80%;
  background: white;
  box-sizing: border-box;
  border: lightgray solid 1px;
  padding-bottom: 20px;
}

.wrapper img {
  width: 100%;
  height: 70%;
  object-fit: cover;
}

.wrapper p {
  font-weight: bold;
  font-size: 1.2rem;
}

.wrapper button {
  background-color: #97c93e;
  padding: 3px 10px;
  border: none;
  color: white;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 14px;
  margin: 5px;
  cursor: pointer;
  border-radius: 5px;
  font-weight: bold;
}
</style>
