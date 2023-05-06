<template>
    <div class="container">
      <h1 @click="makeNewBook()"><img src="@/assets/icon.png" class="icon">새로만들기</h1>
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
</style>
  