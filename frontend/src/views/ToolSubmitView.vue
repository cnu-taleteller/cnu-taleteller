<template>
    <div class="submit-form">
        <h3>제출하기</h3>
        <p>동화책 제작을 완료하셨으면 저장해서 모두가 볼 수 있게 해주세요!</p>
        <div class="submit-input">
            <p>동화책 제목은 무엇인가요?<br><input type="text" v-model="bookName"/></p>
            <p>동화책에 대한 설명을 써주세요.<br><input type="text" v-model="bookDescription"/></p>
            <!-- <p>작품의 공개여부</p>
            <div class="submit-radio">
                <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1">
                <label class="form-check-label" for="inlineRadio1">공개</label>
                <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2">
                <label class="form-check-label" for="inlineRadio2">비공개</label>
            </div> -->
            <button @click.prevent="saveToSessionStorage()">완료했어요😊</button>
        </div>
      
    </div>
</template>

<script>
export default {
    data() {
        return {
            bookName: '',
            bookDescription: "",
        };
    },
    created() {
        this.bookName = sessionStorage.getItem('bookName');
    },
    methods: {
        saveToSessionStorage() {
            sessionStorage.setItem("bookName", this.bookName);
            sessionStorage.setItem("bookDescription", this.bookDescription);
            this.$router.push("/mypage/workmanage");
        },
    },
};
</script>
<style scoped>
.submit-form{
    height: 75vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.submit-input{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2px solid #ccc;
    width: 65vh;
    height: 50%; 
}

.submit-input>p{
    margin-top: 10px;
}

input[type="text"]{
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

button{
    padding: 5px 10px;
    margin-top: 10px;
    border-radius: 3px;
    background-color: #bce9ff;
    border: none;
}
button:hover{
    opacity: 0.7;
}
</style>