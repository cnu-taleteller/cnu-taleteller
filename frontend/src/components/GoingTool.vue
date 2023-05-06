<template>
  <div class="container">
    <h1 @click="makeNewBook()"><img src="@/assets/icon.png" class="icon">새로만들기</h1>
    <hr>
    <div class="column">
      <div class="column-icon">
        <img src="@/assets/book.png">
        <small>미완성작품리스트</small>
      </div>
      <p></p>
      <div class="column-icon">
        <img src="@/assets/book.png">
        <small>미완성작품리스트</small>
      </div>
      <p></p>
      <div class="column-icon">
        <img src="@/assets/book.png">
        <small>미완성작품리스트</small>
      </div>
      <p></p>
      <div class="column-icon">
        <img src="@/assets/book.png">
        <small>미완성작품리스트</small>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    onClickNew() {
      this.$emit('change-testone', false);
    },
    // 새로 만드는 작품일 때, session에 toolState new 해줘야 시나리오 진입함
    makeNewBook(){
      sessionStorage.setItem('toolState', 'new');
      sessionStorage.removeItem('scenario');
      sessionStorage.removeItem('bookName');
      sessionStorage.removeItem("projectTitle");
      this.$router.push('/tool');
    }
  },
}
</script>

<style scoped>
.container h1 a {
  color: black;
  text-decoration: none;
}
.icon {
  height: 100px;
}

.column {
  width: 100%;
  height: 250px;
  display: flex;
  align-items: center;
}
.column p {
  padding-left: 10%;
}
.column-icon {
  width: 30%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}</style>
