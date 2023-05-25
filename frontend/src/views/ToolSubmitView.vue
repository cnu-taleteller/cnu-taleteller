<template>
  <div class="submit-form">
    <h3>제출하기</h3>
    <p>동화책 제작을 완료하셨으면 저장해서 모두가 볼 수 있게 해주세요!</p>
    <div class="submit-input">
      <p>동화책 제목은 무엇인가요?<br>
        <input type="text" v-model="book.bookName" />
      </p>
      <p>동화책에 대한 설명을 써주세요.<br>
        <input type="text" v-model="book.bookDescription" /></p>
      <div>
        <select class="form-select form-select-sm" v-model="book.bookCategory">
          <option selected value=null disabled>카테고리를 선택해주세요.</option>
          <option value="교육">교육</option>
          <option value="창작">창작</option>
          <option value="전래동화">전래동화</option>
        </select>
      </div>
      <br>
      <div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" v-model="book.bookPublic" name="inlineRadioOptions" id="inlineRadio1" value="0">
          <label class="form-check-label" for="inlineRadio1">작품 공개할게요</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" v-model="book.bookPublic" name="inlineRadioOptions" id="inlineRadio2" value="1">
          <label class="form-check-label" for="inlineRadio2">작품 비공개할게요</label>
        </div>
      </div>
      <button @click=submitBook()>완료했어요😊</button>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      book:{
        bookId: null,
        bookName: "",
        bookDescription: "",
        bookCategory: null,
        bookPublic: "0",
        bookStatus: "submit",
      }
    };
  },
  mounted() {
    this.book.bookName = sessionStorage.getItem("bookName");
    this.book.bookId = Number(sessionStorage.getItem("bookId"));
  },
  methods: {
    saveToSessionStorage() {
      sessionStorage.setItem("bookName", this.bookName);
      sessionStorage.setItem("bookDescription", this.bookDescription);
      this.$router.push("/mypage/workmanage");
    },
    async submitBook(){
      if(this.book.bookName==="" || this.book.bookDescription === "" || this.book.bookCategory === null){
        alert("입력하지 않거나, 선택하지 않은 값이 있습니다.");
        return;
      }
      await axios.post("/api/v1/book", this.book)
      .then((res) => {
        alert("제출 완료되었습니다");
        sessionStorage.removeItem('scenario');
        sessionStorage.removeItem('uploadBackList');
        sessionStorage.removeItem('uploadCharList');
        this.$router.push("/");
      })
      .catch((err)=>{
        console.log(err);
        alert("제출 실패");
      })
    }
  },
};
</script>
<style scoped>
.submit-form {
  height: 75vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.submit-input {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px solid #ccc;
  width: 65vh;
  height: 65%;
}

.submit-input>p {
  margin-top: 10px;
}

input[type="text"] {
  width: 40vh;
  outline: none;
  border: none;
  border-bottom: 2px solid #ccc;
}

input[type="text"]:focus {
  box-shadow: 0px 0px 5px #cacaca;
  background-color: none;
  background-position: 2%;
}

button {
  padding: 5px 10px;
  margin-top: 20px;
  border-radius: 3px;
  background-color: #bce9ff;
  border: none;
}

button:hover {
  opacity: 0.7;
}</style>