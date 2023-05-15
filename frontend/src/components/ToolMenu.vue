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
          <input type="file" @change="setImage('character')" max accept="image/*" id="image">
        </div>
        <!-- 시나리오 -->
        <div class="scenario-form2" v-else-if="selectedMenu == 'scenario'">
          <!-- gpt 시나리오 없을 때 -->
          <div class="scenario-form2" v-if="select == false && gpt == true">
            <div class="spinner-border" role="status"></div>
            <p>열심히 작성중입니다.<br>조금만 기다려주세요!😥</p>
          </div>
          <!-- 내가 적은 시나리오 없을 때 -->
          <div class="scenario-form2" v-else-if="select == false && gpt == false && finalScenario[0].length === 0">
            <p>입력된 시나리오가 없습니다.<br>시나리오를 입력해주세요.</p>
            <button class="submit-btn" @click="addScenario()">추가</button>
          </div>
          <!-- 시나리오 선택 완료 -->
          <div class="scenario-form2" v-else-if="select == true">
            <button class="submit-btn" :class="{ active: flowMenu == false }" @click="flowMenu=false">선택한 시나리오</button>
            <button class="submit-btn" :class="{ active: flowMenu == true }" @click="checkFlow('menu')">흐름 파악하기</button>

            <!-- 선택한 시나리오 -->
            <div class="scenario-form2" v-if="flowMenu==false">
              <p v-for="(story, index) in selectScenario" :key="index">
              {{ setScenarioLabel(index) }} <br>
              <textarea v-model="selectScenario[index]" class="story-input" :disabled="isDisabled">{{ story }}</textarea>
              </p>
              <button class="submit-btn" v-show="isDisabled" :disabled="isDisabled2"
                @click="editScenario('edit')">수정</button>
              <button class="submit-btn" v-show="!isDisabled" @click="editScenario('save')">저장</button>
            </div>
            
            <!-- 흐름 파악하기 -->
            <div v-show="flowMenu" class="scenario-form2">
              <div v-if="flowMenu==true && loading == true">
                <br>
                <div class="spinner-border" role="status"></div>
                <p>흐름 파악 중입니다.<br>조금만 기다려주세요!😥</p>
              </div>
              <p>{{ flowResult }}</p>
              <button class="submit-btn" v-show="!loading" @click="checkFlow('re')">다시 받기</button>
            </div>

          </div>
          <!-- 시나리오 고르는 중 -->
          <div v-else class="scenario-form2">
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
        <div id="item">
          <div class="uploaded-image-list" v-show="selectedMenu == 'character'">
            <img :src="item.src" :draggable="item.draggable" :id="item.id" :style="{ height: '100px', width: '100px' }"
              v-for="item, index in charList">
          </div>
          <div class="uploaded-image-list" v-show="selectedMenu == 'background'">
            <img :src="item.src" :draggable="item.draggable" :id="item.id" :style="{ height: '100px', width: '100px' }"
              v-for="item, index in backList">
          </div>
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
      pageNo: 0,
      selectedMenu: 'scenario',
      isDisabled: true, // 시나리오 textarea 비활성화
      isDisabled2: false, // 수정버튼 활성화
      select: false, // 시나리오 선택여부
      scenarioNum: 0,
      flowMenu: false, // 시나리오 or 흐름 파악하기
      loading: false, // gpt 일때 로딩 여부
      flowcnt: 0,
      flowResult: null, // gpt로 받은 흐름 파악하기
      resultScenario: [],
      finalScenario: [[], [], [], [], []],
      selectScenario: [],
      isReScenario: false,
      scenarioKeyword: {
        who: null,
        when: null,
        where: null,
        event: null
      },
      allCaption: [],
      //리스트 변경 해야함
      // charList:[
      // {
      //     src: '/images/character/pngwing.com.png',
      //     id: 'character13',
      //     draggable: "true",
      //     height: "100px",
      //   },
      //   {
      //     src: '/images/character/pngwing2.com.png',
      //     id: 'character14',
      //     draggable: "true",
      //     height: "100px",
      //   },
      //   {
      //     src: 'https://taleteller.s3.ap-northeast-2.amazonaws.com/static/C_71f13106-6e3f-4cdc-9cec-fc923c85ef4d_47508966-5575-4f4f-8aeb-df68b9d52a86_img.jpg',
      //     id: 'character15',
      //     draggable: "true",
      //     height: "100px",
      //   },
      // ]
       
      // 기본적으로 있는 이미지 배열. 반복되는 부분 많아서 방식 변경
      charList: Array.from({length: 25}, (_, i) => ({
        src: `/images/character/character${i}.png`,
        id: `character${i}`,
        draggable: "true",
        height: "100px",
      })),
      // 기본적으로 있는 배경 배열
      backList: [
      ...Array.from({ length: 18}, (_, i) => ({
        src: `/images/background/background${i}.png`,
        id: `background${i}`,
        draggable: "true",
        height: "100px",
      })),
      {
        src:
          "https://taleteller.s3.ap-northeast-2.amazonaws.com/static/C_71f13106-6e3f-4cdc-9cec-fc923c85ef4d_47508966-5575-4f4f-8aeb-df68b9d52a86_img.jpg",
        id: "background",
        draggable: "true",
        height: "100px",
      },
    ],
      nextId: 1,
      uploadId: 0,
      isUpload: false,
      imageIndex: 0,
    }
  },
  //props로 toolView에서 보낸 데이터를 받음
  props: {
    currentPageList: Object,
    viewFinalScenario: Array,
    gpt: Boolean,
    pageList: Array
  },
  mounted() {
    this.$emit('selectedMenu', this.selectedMenu);
    this.imageEventDragStart();
    this.scenarioKeyword = JSON.parse(sessionStorage.getItem('scenarioKeyword'));
    this.finalScenario = this.viewFinalScenario;
  },
  methods: {
    // 이미지 업로드
    async setImage(menu) {
      const maxSize = 5 * 1024 * 1024;
      const fileSize = document.getElementById("image").files[0].size;
      // console.log(fileSize);

      if (fileSize > maxSize) {
        alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다.");
        return;
      }

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
        if (menu === 'background') {
          this.backList.push({
            src: res.data,
            id: 'upload' + this.uploadId,
            draggable: "true",
            height: "100px",
          });
        } else if (menu === 'character') {
          this.charList.push({
            src: res.data,
            id: 'upload' + this.uploadId,
            draggable: "true",
            height: "100px",
          });
        }
        this.uploadId++;
        console.log("S3 업로드 성공");
        document.getElementById("image").value = "";
      } catch (e) {
        console.log(e);
      }
    },
    setSelectedMenu(menu) {
      this.selectedMenu = menu;
      this.$emit('selectedMenu', this.selectedMenu);
    },

    //기존 이미지 배열에 있는 이미지들에게 drag이벤트 추가
    imageEventDragStart() {
      document.querySelectorAll(".menu .image-list #item").forEach((element) => {
        element.addEventListener("dragstart", (e) => {
          const x = e.offsetX;
          const y = e.offsetY;
          //기본적으로 e.target.id -> img<id> 클릭했을 때 해당이미지의 x 좌표 y 좌표를 setData해줌
          e.dataTransfer.setData("text/plain", `${e.target.id}, ${x}, ${y}`);
        });
      });
    },
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
      this.select = true; // 임시 저장, 제출에 필요한 데이터
      sessionStorage.setItem('select',true);
    },
    // 기승전결 흐름 파악
    checkFlow(arg){
      this.flowMenu = true;
      let len = this.pageList.length;

      if(len < 3) {
        alert('3페이지 이상 작업하셔야 흐름을 파악할 수 있습니다!');
        this.flowMenu = false;
        return;
      }
      if(this.flowcnt > 4) {
        alert('흐름 파악은 5번까지만 가능합니다!');
        return;
      }

      for(let i=0; i<len; i++){
        this.allCaption[i]=this.pageList[i].caption.content;
        console.log(this.allCaption[i]);
      }

      if(arg === 'menu') {
        if (this.flowResult == null) {
          this.checkFlowGpt();
        }
      }
    
      else if(arg === 're') {
        this.checkFlowGpt();
      }

    },
    
    checkFlowGpt(){
      this.flowcnt++;
      this.loading = true;
      const story = sessionStorage.getItem('scenario');
      const caption = this.allCaption;
      console.log(story);

      console.log("axios 통신 요청");
      axios.post("https://api.openai.com/v1/chat/completions",
        {
          "model": "gpt-3.5-turbo",
          "messages": [{
            "role": "user",
            "content": `${story} 라는 내용을 가진 동화책을 만드려고 하는데,
            초반 내용: ${caption[0]}, ${caption[1]}, ${caption[2]}, ...,
            후반 내용: ${caption[caption.length-2]}, ${caption[caption.length-1]}...
            까지 제작이 진행된 상황이라면,
            현재 만들고 있는 내 동화책은
            [도입], [전개], [위기], [결말] 중 어디까지 진행된 거고, 어떤 내용을 더 추가해야할까?
            처음에 말한 동화책 내용이랑 내가 제작하고 있는 내용이 상관없는 얘기라면 상관없는 내용이라고 말해줘.
            `
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
          console.log(res.data.choices[0].message.content);
          this.flowResult = res.data.choices[0].message.content;
          this.loading = false;
        })
        .catch((err) => {
          // this.gpt = false;
          alert('서버 오류로 시나리오 요청에 실패하였습니다.');
          console.log(err);
        })
    },
     // 키워드 변경
     reKeyword() {
      const popupWidth = 600;
      const popupHeight = 650;
      const popupX = Math.ceil((window.screen.width - popupWidth) / 2);
      const popupY = Math.ceil((window.screen.height - popupHeight) / 2);
      const popup = window.open("", "toolKeyword", ` width=${popupWidth}, height=${popupHeight}, left=${popupX}, top=${popupY}`);

      popup.document.body.innerHTML = `
      <div class="scenario-form">
        <h2>키워드 변경하기</h2>
        <p>변경할 키워드를 입력하세요 ✏️
          <br>사건은 구체적이게 적을수록 좋습니다!
        </p>
        <div class="scenario-input-form">
        <p>1. 주인공은 누구인가요?</p>
        <input type="text" class="scenario-input" id="who" value="${this.scenarioKeyword.who}" placeholder="짱구가">
        <p>2. 언제 일어난 일인가요?</p>
        <input type="text" class="scenario-input" id="when" value="${this.scenarioKeyword.when}" placeholder="주말 아침에">
        <p>3. 어디서 일어난 일인가요?</p> 
        <input type="text" class="scenario-input" id="where" value="${this.scenarioKeyword.where}" placeholder="숲에서">
        <p>4. 이 동화책의 주요 사건은 무엇인가요?</p>
        <input type="text" class="scenario-input" id="event" value="${this.scenarioKeyword.event}" placeholder="외계인을 만나 당황했지만 재밌게 놀았던 이야기">
        <button class="submit-btn" @click="setGptScenario()">키워드 변경하기</button>
        </div>
      </div>
      `;
      popup.document.head.innerHTML = `
        <style>
        body{
          margin: 0;
        }
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
        .scenario-form{
          width: 100%;
          height: 100%;         
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          text-align: center;
        }
        .scenario-input-form{
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
          color: #3b3b3b;
          text-shadow: 2px 2px 2px #d3d3d3;
        }
        .scenario-input {
          width: 100%;
          height: 30px;
          outline: none;
          border: none;
          border-bottom: 1px solid #ccc;
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
        .submit-btn:hover{
          opacity: 0.7;
        }
        </style>
        `;

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

  },
}
</script>
<style scoped>
button {
  border-radius: 3px;
}
.menu {
  /* height: 100%; */
  height: 90vh;
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

.image-list {
  display: flex;
  align-items: center;
  justify-content: center;
}

.uploaded-image-list {
  width: 100%;
  height: 70vh;
  flex-wrap: wrap;
  overflow-y: scroll;
}

.uploaded-image-list>img {
  /* width: 90%; */
  margin: 10px;
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

.menu-form {
  padding-top: 20px;
  overflow-y: scroll;
  /* height: 90vh; */
  height: 80vh;
}

.menu-form::-webkit-scrollbar {
  display: none;
}

.menu-btn {
  padding: 5px 10px;
  /* border-radius: 50%; */
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

.story-input {
  width: 90%;
  height: 170px;
  background-color: white;
  resize: none;
}

.story-input::-webkit-scrollbar,
.uploaded-image-list::-webkit-scrollbar {
  width: 10px;
}

.story-input::-webkit-scrollbar-thumb,
.uploaded-image-list::-webkit-scrollbar-thumb {
  background-color: rgb(223, 223, 223);
  border-radius: 10px;
  background-clip: padding-box;
  border: 2px solid transparent;
}

.story-input::-webkit-scrollbar-track,
.uploaded-image-list::-webkit-scrollbar-track {
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
  height: 100%;
}
.scenario-form2 {
  width: 100%;
  /* height: 90vh; */
  height: 80vh;
}

.scenario-form2 > p {
  margin: 20px;
}
.submit-btn {
  border: none;
  padding: 5px 10px;
  margin: 5px;
}

.submit-btn:hover {
  opacity: 0.7;
}
.submit-btn.active {
  background-color: #fceb6e;
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
  margin-left: 10px;
}

.scenario-btn.active {
  background-color: #50c3fd;
  color: white;
}
</style>
  