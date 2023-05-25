<template>
  <div class="preview-form">
    <img :src="pageList[currentIndex].thumbnail" width="960px" height="600px">
    <div class="preview-menu">
      <button class="menu-btn" @click="prevImage" :disabled="currentIndex === 0">이전</button>
      <button class="menu-btn" @click="nextImage" :disabled="currentIndex === pageList.length - 1">다음</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pageList: null,
      currentIndex: 0,
    };
  },
  created() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const pageListParam = urlParams.get('pageList');

    if (pageListParam) {
      this.pageList = JSON.parse(decodeURIComponent(pageListParam));
      console.log(this.pageList);
    }
  },
  methods: {
    prevImage() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
      }
    },
    nextImage() {
      if (this.currentIndex < this.pageList.length - 1) {
        this.currentIndex++;
      }
    },
  },
};
</script>

<style scoped>
.preview-form {
  height: 700px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.thumbnail-container {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}
.preview-menu {
  margin-top: 10px;
}
.menu-btn {
  padding: 5px 10px;
  background-color: white;
  border: none;
  border-radius: 3px;
}

.menu-btn:hover {
  background-color: rgb(223, 223, 223);
  border: none;
}

.menu-btn.active {
  background-color: gray;
  color: white;
}

</style>
