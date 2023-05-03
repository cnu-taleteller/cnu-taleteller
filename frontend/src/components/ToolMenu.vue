<template>
  <div class="menu">
    <div class="menu-list">
      <button class="menu-btn" :class="{ active: selectedMenu === 'background' }"
        @click="setSelectedMenu('background')">배경</button>
      <button class="menu-btn" :class="{ active: selectedMenu === 'character' }"
        @click="setSelectedMenu('character')">캐릭터</button>
      <button class="menu-btn" :class="{ active: selectedMenu === 'scenario' }"
        @click="setSelectedMenu('scenario')">시나리오</button>
    </div>
    <div class="menu-form">
      <div class="uploadImage">
        <div v-if="selectedMenu == 'background'">
          <input type="file" @change="setImage('background')" accept="image/*" id="image">
        </div>
        <div v-else-if="selectedMenu == 'character'">
          <input type="file" @change="setImage('character')" accept="image/*" id="image">
        </div>
        <!-- 시나리오 -->
        <div class="scenario-form2" v-else-if="selectedMenu == 'scenario'">
          <!-- gpt 시나리오 없을 때 -->
          <div v-if="select == false && gpt == true">
            <div class="spinner-border" role="status"></div>
            <p>열심히 작성중입니다.<br>조금만 기다려주세요!ㅠㅠ</p>
          </div>
          <!-- 내가 적은 시나리오 없을 때 -->
          <div v-else-if="select == false && gpt == false && finalScenario[0].length === 0">
            <p>입력된 시나리오가 없습니다.<br>시나리오를 입력해주세요.</p>
            <button class="submit-btn" @click="addScenario()">추가</button>
          </div>
          <!-- 시나리오 선택 완료 -->
          <div v-else-if="select == true">
            <p v-for="(story, index) in selectScenario" :key="index">
              {{ setScenarioLabel(index) }} <br>
              <textarea v-model="selectScenario[index]" class="story-input" :disabled="isDisabled">{{ story }}</textarea>
            </p>
            <button class="submit-btn" v-show="isDisabled" :disabled="isDisabled2"
              @click="editScenario('edit')">수정</button>
            <button class="submit-btn" v-show="!isDisabled" @click="editScenario('save')">저장</button>
          </div>
          <!-- 시나리오 고르는 중 -->
          <div v-else>
            <button class="scenario-btn" :class="{ active: scenarioNum === 0 }" v-show="finalScenario[0][0]"
              @click="setNum(0)">1</button>
            <button class="scenario-btn" :class="{ active: scenarioNum === 1 }" v-show="finalScenario[1][0]"
              @click="setNum(1)">2</button>
            <button class="scenario-btn" :class="{ active: scenarioNum === 2 }" v-show="finalScenario[2][0]"
              @click="setNum(2)">3</button>
            <button class="scenario-btn" :class="{ active: scenarioNum === 3 }" v-show="finalScenario[3][0]"
              @click="setNum(3)">4</button>
            <button class="scenario-btn" :class="{ active: scenarioNum === 4 }" v-show="finalScenario[4][0]"
              @click="setNum(4)">5</button>
            <!-- 다시 작성 -->
            <div v-show="isReScenario">
              <div class="spinner-border" role="status"></div>
              <p>새로운 내용으로 작성중입니다.<br>조금만 기다려주세요!ㅠㅠ</p>
            </div>
            <p v-for="(story, index) in finalScenario[scenarioNum]" :key="index">
              {{ setScenarioLabel(index) }} <br>
              <textarea v-model="finalScenario[scenarioNum][index]" class="story-input"
                :disabled="isDisabled">{{ story }}</textarea>
            </p>
            <button class="submit-btn" :disabled="isDisabled2" @click="reKeyword()">키워드 변경</button>
            <button class="submit-btn" :disabled="isDisabled2" @click="reScenario()">시나리오 다시 받기</button>
            <br>
            <button class="submit-btn select-btn" :disabled="isDisabled2" @click="setScenario()">이 시나리오 선택하기</button>
          </div>
        </div>
      </div>
      <div class="image-list">
        <div class="image-list-char" v-show="selectedMenu == 'character'">
          <img :src="item.src" :draggable="item.draggable" :id="item.id" :data-id="item.dataId"
            :style="{ height: item.height }" v-for="item, index in charList">
        </div>
        <div class="image-list-back" v-show="selectedMenu == 'background'">
          <img :src="item.src" :draggable="item.draggable" :id="item.id" :data-id="item.dataId"
            :style="{ height: item.height }" v-for="item, index in backList">
        </div>
      </div>

    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      bookId: null,
      isDisabled: true, // 시나리오 textarea 비활성화
      isDisabled2: false, // 수정버튼 활성화
      select: false,
      pageNo: 0,
      resultScenario: [],
      finalScenario: [[], [], [], [], []],
      selectScenario: [], // 최종 시나리오
      isReScenario: false,
      scenarioKeyword: {
        who: null,
        when: null,
        where: null,
        event: null
      },

      //리스트 변경 해야함.
      charList: [
        {
          src: '/images/pngwing.com.png',
          id: 'item',
          draggable: "true",
          dataId: 'i1',
          height: "100",
        },
        {
          src: '/images/pngwing2.com.png',
          id: 'item',
          draggable: "true",
          dataId: 'i2',
          height: "100",
        }],
      backList: [
        {
          src: '/images/field.png',
          id: 'item',
          draggable: 'true',
          dataId: 'i3',
          height: '100px',
        }],
      selectedMenu: 'scenario',
      scenarioNum: 0,
      nextId: 1,
      uploadId: 0,
      isUpload: false,
      imageIndex: 0,
    }
  },
  props: {
    currentPageList: Object,
    viewFinalScenario: Array,
    gpt: Boolean,
  },
  mounted() {
    this.$emit('selectedMenu', this.selectedMenu);
    this.existingImageEventDragStart();
    this.bookId = sessionStorage.getItem('book_id');
    this.scenarioKeyword = JSON.parse(sessionStorage.getItem('scenarioKeyword'));
    this.finalScenario = this.viewFinalScenario;
  },
  updated() {
    this.addImageEventDragstart();
  },
  methods: {
    // 시나리오 label 나누는 함수
    setScenarioLabel(index) {
      switch (index) {
        case 0:
          return '[도입]';
        case 1:
          return '[전개]';
        case 2:
          return '[위기]';
        case 3:
          return '[결말]';
        default:
          return '';
      }
    },
    // 시나리오 직접 작성
    addScenario() {
      this.select = true;
      for (let i = 0; i < 4; i++) {
        this.selectScenario.push('');
      }
      this.editScenario('edit');
    },

    // 시나리오 수정
    editScenario(arg) {
      this.isDisabled = !!!this.isDisabled;
      this.resultScenario = '[도입]' + this.selectScenario[0] + '[전개]' + this.selectScenario[1] + '[위기]' + this.selectScenario[2] + '[결말]' + this.selectScenario[3];
      if (arg === 'save') {
        sessionStorage.setItem('scenario', this.resultScenario);
      }
    },
    setNum(num) {
      this.scenarioNum = num;
    },
    // 최종 선택
    setScenario() {
      this.selectScenario = this.finalScenario[this.scenarioNum];
      this.select = true;
    },
    // 키워드 변경
    reKeyword() {
      const popupWidth = 500;
      const popupHeight = 400;
      const popupX = Math.ceil((window.screen.width - popupWidth) / 2);
      const popupY = Math.ceil((window.screen.height - popupHeight) / 2);
      const popup = window.open("", "toolKeyword", ` width=${popupWidth}, height=${popupHeight}, left=${popupX}, top=${popupY}`);

      popup.document.body.innerHTML = `
        <div class="scenario-form">
        <div class="scenario-input">
          <h4>키워드 변경</h4>
          <p>변경할 키워드를 입력하세요.</p>
          <p>사건은 구체적이게 적을수록 좋습니다!</p>
          <p>누가: <input type="text" id="who" value="${this.scenarioKeyword.who}" placeholder="짱구가"></p>
          <p>언제: <input type="text" id="when" value="${this.scenarioKeyword.when}" placeholder="주말 아침에"></p>
          <p>어디서: <input type="text" id="where" value="${this.scenarioKeyword.where}" placeholder="숲에서"></p>
          <p>사건: <input type="text" id="event" value="${this.scenarioKeyword.event}" placeholder="외계인을 만나 당황했지만 재밌게 노는 어린이 이야기"></p>
          <button onclick="setKeyword()">키워드 저장</button>
        </div>
      </div>`;

      popup.setKeyword = () => {
        const who = popup.document.querySelector("#who").value;
        const when = popup.document.querySelector("#when").value;
        const where = popup.document.querySelector("#where").value;
        const event = popup.document.querySelector("#event").value;

        if (who == null || when == null || where == null || event == null) {
          popup.window.alert('빈 내용을 다 채워주세요!');
          return;
        }
        else {
          this.scenarioKeyword.who = who;
          this.scenarioKeyword.when = when;
          this.scenarioKeyword.where = where;
          this.scenarioKeyword.event = event;
          sessionStorage.setItem('scenarioKeyword', JSON.stringify(this.scenarioKeyword));
          popup.window.alert("키워드 저장 후 다시 받기 버튼을 클릭해주세요!");
          popup.window.close();
        }
      }
    },
    // 시나리오 다시 받기
    reScenario() {
      if (this.finalScenario[4].length > 0) {
        alert('시나리오는 작품당 5번만 받을 수 있습니다.');
        return;
      }
      this.isReScenario = true;
      this.isDisabled2 = true;
      console.log(this.scenarioKeyword);
      console.log("axios 통신 요청");
      axios.post("https://api.openai.com/v1/chat/completions",
        {
          "model": "gpt-3.5-turbo",
          "messages": [{
            "role": "user",
            "content": `누가: ${this.scenarioKeyword.who},
                        언제: ${this.scenarioKeyword.when}, 
                        어디서: ${this.scenarioKeyword.where},
                        사건: ${this.scenarioKeyword.event}
                        라는 내용을 가진 동화책을 '도입/전개/위기/결말' 로 나눠서 써줘.
                        내용을 나눌 때 형식은 
                        [도입] 내용
                        [전개] 내용
                        [위기] 내용
                        [결말] 내용 
                        형식으로 나눠서 700자 이내로 써줘.`
          }],
        },
        {
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${process.env.VUE_APP_API_KEY}`,
          }
        }
      )
        .then((res) => {
          // console.log(res.data.choices[0].message.content);
          this.resultScenario = res.data.choices[0].message.content;
          sessionStorage.setItem('scenario', this.resultScenario);
          // console.log(this.finalScenario);
          this.setScenarioArr();
          this.isDisabled2 = false;
        })
        .catch((err) => {
          alert('서버 오류로 시나리오 요청에 실패하였습니다.');
          console.log(err);
        })
        .finally(() => {
          this.isReScenario = false;
        })
    },
    setScenarioArr() {
      // 스토리 도입, 전개, 위기, 결말로 나눠서 배열에 저장(대괄호 글자는 제거)
      const sections = ['[도입]', '[전개]', '[위기]', '[결말]'];
      let num = 0;
      if (this.finalScenario[0].length > 0) {
        num = 1;
      }
      if (this.finalScenario[1].length > 0) {
        num = 2;
      }
      if (this.finalScenario[2].length > 0) {
        num = 3;
      }
      if (this.finalScenario[3].length > 0) {
        num = 4;
      }
      sections.forEach((section, index) => {
        const scenario = this.resultScenario;
        const start = scenario.indexOf(section);
        let end;

        if (index < sections.length - 1) {
          end = scenario.indexOf(sections[index + 1]);
        } else {
          end = scenario.length;
        }
        this.finalScenario[num][index] = scenario.slice(start, end).replace(section, '').trim();
      });
    },

    // 이미지 업로드
    async setImage(menu) {
      console.log(menu);
      try {
        let frm = new FormData();
        let imageFile = document.getElementById("image");
        frm.append("image", imageFile.files[0]);
        frm.append("menu", menu);
        frm.append("bookId", this.bookId);
        const res = await axios.post(`/api/tool/image`, frm, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        this.isUpload = true;
        if (menu === 'background') {
          this.src = "/images/" + res.data;
          this.backList.push({
            src: this.src,
            id: 'item',
            draggable: "true",
            dataId: 'upload' + this.uploadId,
            height: "100px",
          });
        } else if (menu === 'character') {
          this.src = "/images/" + res.data;
          this.charList.push({
            src: this.src,
            id: 'item',
            draggable: "true",
            dataId: 'upload' + this.uploadId,
            height: "100px",
            width: "100px",
          });
        }
        console.log("전송 성공");
        document.getElementById("image").value = "";
      } catch (e) {
        console.log(e);
      }
    },
    setSelectedMenu(menu) {
      this.selectedMenu = menu;
      this.$emit('selectedMenu', this.selectedMenu);
    },
    existingImageEventDragStart() {
      document.querySelectorAll(".menu .image-list #item").forEach((element) => {
        element.addEventListener("dragstart", (e) => {
          const x = e.offsetX;
          const y = e.offsetY;
          e.dataTransfer.setData("text/plain", `${e.target.dataset["id"]}, ${x}, ${y}`);
        });
      });
    },
    addImageEventDragstart() {
      if (this.isUpload) {
        const uploadImage = document.querySelector(`.menu .image-list #item[data-id=upload${this.uploadId}]`);
        if (uploadImage !== null) {
          uploadImage.addEventListener('dragstart', e => {
            const x = e.offsetX;
            const y = e.offsetY;
            e.dataTransfer.setData("text/plain", `${e.target.dataset["id"]}, ${x}, ${y}`);
          });
        }
        this.uploadId++;
      }
      this.isUpload = false;
    },
  },
}
</script>
<style scoped>
.menu {
  height: 80vh;
  background-color: white;
  border-left: 1px solid #dfdfdf;
}

.menu-list {
  padding: 10px 0px;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  border-bottom: 2px solid rgb(236, 236, 236);
}

.image-list-char {
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}

.image-list-char>img {
  /* width: 90%; */
  margin: 5px;
}

input[type=file]::file-selector-button {
  width: 150px;
  height: 30px;
  background: #fff;
  border: 1px solid gray;
  border-radius: 10px;
  cursor: pointer;
  margin-bottom: 20px;
}

input[type=file]::file-selector-button:hover {
  background: rgb(77, 77, 77);
  color: #fff;
}

.image-list {
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-form {
  padding-top: 20px;
  overflow-y: scroll;
  height: 90%;
}

.menu-form::-webkit-scrollbar {
  display: none;
}

.menu-btn {
  padding: 5px 10px;
  /* border-radius: 50%; */
  background-color: white;
  border: none;
}

.menu-btn:hover {
  background-color: rgb(223, 223, 223);
  border: none;
}

.menu-btn.active {
  background-color: gray;
  color: white;
}

.story-input {
  width: 90%;
  height: 170px;
  background-color: white;
  resize: none;
}

.story-input::-webkit-scrollbar {
  width: 10px;
}

.story-input::-webkit-scrollbar-thumb {
  background-color: rgb(223, 223, 223);
  border-radius: 10px;
  background-clip: padding-box;
  border: 2px solid transparent;
}

.story-input::-webkit-scrollbar-track {
  background-color: white;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}

.story-input:disabled {
  color: black;
  border: none;
}

.scenario-form {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.submit-btn {
  border: none;
  padding: 5px 10px;
  margin: 5px;
}

.submit-btn:hover {
  opacity: 0.7;
}

.select-btn {
  background-color: #fceb6e;
  margin-bottom: 20px;
}

.scenario-btn {
  border: none;
  font-weight: 600;
  padding: 5px 10px;
  /* background-color: #2F66FB; */
  color: black;
  margin-bottom: 20px;
  margin-right: 10px;
}

.scenario-btn.active {
  background-color: #50c3fd;
  color: white;
}</style>
  