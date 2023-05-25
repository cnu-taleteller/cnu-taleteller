<template>
  <div class="scenario-form">
    <h2>키워드 변경하기</h2>
    <p class="form-ment">변경할 키워드를 입력하세요 ✏️
      <br>사건은 구체적이게 적을수록 좋습니다!
    </p>
    <div class="scenario-input-form">
      <p>1. 주인공은 누구인가요?</p>
      <input type="text" class="scenario-input" id="who" v-model="scenarioKeyword.who" placeholder="짱구가">
      <p>2. 언제 일어난 일인가요?</p>
      <input type="text" class="scenario-input" id="when" v-model="scenarioKeyword.when" placeholder="주말 아침에">
      <p>3. 어디서 일어난 일인가요?</p>
      <input type="text" class="scenario-input" id="where" v-model="scenarioKeyword.where" placeholder="숲에서">
      <p>4. 이 동화책의 주요 사건은 무엇인가요?</p>
      <input type="text" class="scenario-input" id="event" v-model="scenarioKeyword.event"
        placeholder="외계인을 만나 당황했지만 재밌게 놀았던 이야기">
      <button class="submit-btn" @click="setKeyword()">키워드 변경하기</button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      scenarioKeyword: {
        who: null,
        when: null,
        where: null,
        event: null
      }
    }
  },
  mounted() {
    this.scenarioKeyword = JSON.parse(sessionStorage.getItem('scenarioKeyword'));
  },
  methods: {
    setKeyword() {
      if (this.scenarioKeyword.who == null || this.scenarioKeyword.when == null ||
        this.scenarioKeyword.where == null || this.scenarioKeyword.event == null) {
        alert('빈 내용을 다 채워주세요!');
        return;
      }
      alert("키워드 저장 후 다시 받기 버튼을 클릭해주세요!");
      sessionStorage.setItem('scenarioKeyword', JSON.stringify(this.scenarioKeyword));
    }

  }
}

</script>
<style scoped>
.scenario-form::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("https://img.freepik.com/premium-photo/white-watercolor-papar-texture-background-for-cover-card-design-or-overlay-aon-paint-art-background_1962-2255.jpg");
  background-size: cover;
  background-position: center;
  opacity: 0.7;
  z-index: -1;
}

.scenario-form {
  width: 550px;
  height: 650px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.scenario-input-form {
  width: 70%;
}

.scenario-input-form>p {
  text-align: center;
  width: 100%;
  margin-bottom: 5px;
  margin-top: 20px;
}

h2 {
  font-weight: bold;
  color: #2f66ff;
  text-shadow: 2px 2px 2px #d3d3d3;
}
.form-ment {
  font-weight: bold;
  color: gray;
}
.scenario-input {
  width: 80%;
  height: 30px;
  outline: none;
  border: none;
  border-bottom: 2px solid #ccc;
  background: center;
}

.scenario-input:focus {
  box-shadow: 0px 0px 5px #cacaca;
  background-color: none;
  background-position: 2%;
}

textarea {
  font-size: 12px;
  resize: none;
  border: 1px solid #dfdfdf;
  padding: 2px 7px;
}

textarea::-webkit-scrollbar {
  width: 10px;
}

textarea::-webkit-scrollbar-thumb {
  background-color: rgb(223, 223, 223);
  border-radius: 10px;
  background-clip: padding-box;
  border: 2px solid transparent;
}

textarea::-webkit-scrollbar-track {
  background-color: white;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}

.submit-btn {
  margin-top: 30px;
  width: 50%;
  padding: 10px;
  border: none;
  background-color: #E4E4E4;
  font-weight: bold;
  color: #353535;
  border-radius: 3px;
  font-size: 15px;
}

.submit-btn:hover {
  opacity: 0.7;
}
</style>