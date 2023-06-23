<template>
  <div class="tool">
    <div class="tool-header">
      <!-- 전체 페이지 리스트 전달 -->
      <ToolHeader :pageList="this.pageList" :toolState="this.toolState" :currentPageList="this.currentPageList"
        :viewFinalScenario="this.finalScenario" :scenarioKeyword="this.scenarioKeyword" :pageObject="this.pageObjects"></ToolHeader>
    </div>
    <!-- 새로 만드는 작품일 때만 -->
    <div v-if="toolState === 'new'" class="tool-content">
      <div class="scenario-btn">
        <button @click="selectScenarioMenu('gpt')">
          <img src="@/assets/book1.png" width="200">
          <p>시나리오 추천 받기</p>
        </button>
        <button @click="selectScenarioMenu('write')">
          <img src="@/assets/book2.png" width="200">
          <p>시나리오 직접 쓰기</p>
        </button>
      </div>
    </div>
    <!-- gpt가 쓰는 시나리오 -->
    <div v-else-if="toolState === 'gpt'" class="tool-content tool-scenario">
      <div class="scenario-form">
        <h3>시나리오 추천 받기</h3>
        <p class="form-ment">키워드를 입력하세요 ✏️
          <br>사건은 구체적이게 적을수록 좋습니다!
        </p>
        <div class="scenario-input-form">
          <p>1. 주인공은 누구인가요?</p>
          <input type="text" class="scenario-input" v-model="scenarioKeyword.who" placeholder="백설공주가">
          <p>2. 언제 일어난 일인가요?</p>
          <input type="text" class="scenario-input" v-model="scenarioKeyword.when" placeholder="옛날 옛적에">
          <p>3. 어디서 일어난 일인가요?</p>
          <input type="text" class="scenario-input" v-model="scenarioKeyword.where" placeholder="숲에서">
          <p>4. 이 동화책의 주요 사건은 무엇인가요?</p>
          <textarea class="scenario-input" v-model="scenarioKeyword.event"
            placeholder="외계인을 만나 당황했지만 재밌게 놀았던 이야기"></textarea>
          <button class="submit-btn" @click="setGptScenario()">시나리오 받아보기</button>
        </div>
      </div>
      <ToolScenarioExample></ToolScenarioExample>
    </div>
    <!-- 툴 -->
    <div v-else class="tool-content">
      <div class="tool-left">
        <!-- currentPageList 에 handlePageList 메서드로 툴 페이지리스트 컴포넌트에서 $emit으로 받은 pageList(index) 를 넣음, pageList 변경시 -> 썸네일 변경시 새로운 데이터를 전달 -->
        <ToolPageList @currentPageList="handleCurrentPageList" @pageList="handlePageList"></ToolPageList>
      </div>
      <div class="tool-center">
        <!-- toolSelectedPage에 값을 전달해줌 -->
        <ToolSelectedPage :currentPageList="this.currentPageList" :selectedMenu="this.selectedMenu" :pageList="this.pageList" @pageObjects="handlePageObjects" ref="toolSelectedPage"></ToolSelectedPage>
      </div>
      <div class="tool-right">
        <ToolMenu @ttsValueChange="handleTtsValueChanged" @selectedMenu="handleSelectedMenu" :currentPageList="this.currentPageList"
          :viewFinalScenario="this.finalScenario" :gpt="this.gpt" :pageList="this.pageList"></ToolMenu>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

import toolHeader from '@/components/ToolHeader.vue';
import toolPageList from '@/components/ToolPageList.vue';
import toolSelectedPage from '@/components/ToolSelectedPage.vue';
import toolMenu from '@/components/ToolMenu.vue';
import toolScenarioExample from '@/components/ToolScenarioExample.vue'

export default {
  data() {
    return {
      toolState: null,
      gpt: false, // gpt로 시나리오 받을건지.. 받을거면 true

      // gpt로 받을 시나리오 키워드
      scenarioKeyword: {
        who: null,
        when: null,
        where: null,
        event: null
      },
      resultScenario: [], // [도입][전개] 등 태그 전체 있는 배열
      finalScenario: [[], [], [], [], []], // props로 전달할 [도입][전개] 등 태그 없는 순수 텍스트 배열

      // 직접 쓸 때 기승전결 받기
      write1: null,
      write2: null,
      write3: null,
      write4: null,
      selectedMenu: '',
      currentPageList: {}, //pageList[현재 인덱스] 객체가 들어감
      pageList: [],
      bookId: null, // 작품 번호
      pageObjects: null,
    }
  },
  created() {
    if(!sessionStorage.getItem('user')) {
      alert('로그인 후 이용가능합니다!');
      window.location.href = "/";
    }
    if (sessionStorage.getItem('recentlyClickPageNo') != null) {
      this.selectPageNo = sessionStorage.getItem('recentlyClickPageNo');
    }
    this.toolState = sessionStorage.getItem('toolState');

    if (sessionStorage.getItem('scenario')) {
      this.resultScenario = sessionStorage.getItem('scenario');
      this.setScenarioArr();
    }
  },
  mounted() {
    window.addEventListener('beforeunload', this.unLoadEvent);
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.unLoadEvent);
  },
  components: {
    ToolHeader: toolHeader,
    ToolPageList: toolPageList,
    ToolSelectedPage: toolSelectedPage,
    ToolMenu: toolMenu,
    ToolScenarioExample: toolScenarioExample,
  },
  methods: {
    // 새로고침 방지
    unLoadEvent(event) {
      if (this.isLeaveSite) return;
      event.preventDefault();
      event.returnValue = '';
    },
    handleCurrentPageList(currentPageList) {
      this.currentPageList = currentPageList;
    },
    handlePageList(pageList) {
      this.pageList = pageList;
    },
    //매개변수로 selectedMenu (ex) background, character 를 받아서 data에 있는 this.selectedMenu에 넣어주눈 부분
    handleSelectedMenu(selectedMenu) {
      this.selectedMenu = selectedMenu;
    },
    handlePageObjects(pageObjects) {
      this.pageObjects = pageObjects;
    },
    selectScenarioMenu(arg) {
      this.toolState = arg;
      if (arg === 'write') {
        sessionStorage.setItem('select', true); // 임시 저장, 제출에 필요한 데이터
      }
      else {
        sessionStorage.setItem('select', false);
      }
      sessionStorage.setItem('toolState', arg);
    },
    goTool() {
      sessionStorage.removeItem('toolState');
      this.toolState = null;
    },
    setGptScenario() {
      const who = this.scenarioKeyword.who;
      const when = this.scenarioKeyword.when;
      const where = this.scenarioKeyword.where;
      const event = this.scenarioKeyword.event;

      if (who == null || when == null || where == null || event == null) {
        alert('빈 내용을 다 채워주세요!');
        return;
      }
      sessionStorage.setItem('scenarioKeyword', JSON.stringify(this.scenarioKeyword));
      this.gpt = true;
      this.goTool();
      console.log("axios 통신 요청");
      axios.post(`${process.env.VUE_APP_API_PATH}/api/v1/tool/scenario/`, {
        who: this.scenarioKeyword.who,
        when: this.scenarioKeyword.when,
        where: this.scenarioKeyword.where,
        event: this.scenarioKeyword.event
      })
        .then((res) => {
          this.resultScenario = res.data;
          sessionStorage.setItem('scenario', this.resultScenario);
          this.setScenarioArr();
          console.log(this.finalScenario);
          this.gpt = false;
        })
        .catch((err) => {
          this.gpt = false;
          alert('서버 오류로 시나리오 요청에 실패하였습니다.');
          console.log(err);
        })
    },
    setScenarioArr() {
      // 스토리 도입, 전개, 위기, 결말로 나눠서 배열에 저장(대괄호 글자는 제거)
      const sections = ['[도입]', '[전개]', '[위기]', '[결말]'];
      sections.forEach((section, index) => {
        const scenario = this.resultScenario;
        const start = scenario.indexOf(section);
        let end;

        if (index < sections.length - 1) {
          end = scenario.indexOf(sections[index + 1]);
        } else {
          end = scenario.length;
        }
        this.finalScenario[0][index] = scenario.slice(start, end).replace(section, '').trim();
      });
    },
    handleTtsValueChanged(value){
      this.currentPageList.caption.ttsVoice=value;
    },
  },
}


</script>
<style scoped>
button,
textarea,
input[type=text] {
  transition: all ease-in 0.3s;
}

.tool {
  width: 100%;
  height: 90vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #F7F7F7;
}

.tool-header {
  width: 100%;
  height: 5vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool-content {
  height: 90vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #F7F7F7;
}

.tool-left {
  height: 100%;
  width: 15%;
}

.tool-center {
  height: 100%;
  width: 60%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool-right {
  height: 100%;
  width: 25%;
}

.scenario-form {
  display: flex;
  flex-direction: column;
  width: 50%;
  align-items: center;
  justify-content: center;
}

.tool-scenario {
  display: flex;
  flex-direction: row;
  width: 90%;
}

.scenario-input-form>p {
  width: 100%;
  margin-bottom: 5px;
  margin-top: 20px;
  /* font-weight: bold; */
}

h3 {
  font-weight: bold;
  color: #2f66ff;
  text-shadow: 2px 2px 2px #d3d3d3;
}
.form-ment {
  font-weight: bold;
  color: gray;
}

.scenario-input {
  width: 100%;
  outline: none;
  border: none;
  border-bottom: 1px solid #ccc;
}

.scenario-input:focus {
  box-shadow: 0px 0px 5px #cacaca;
  background-color: none;
  background-position: 2%;
}

.submit-btn {
  margin-top: 15px;
  width: 50%;
  padding: 5px 10px;
  border: none;
  background-color: #ccc;
  font-weight: bold;
  color: #353535;
  border-radius: 3px;
}

.submit-btn:hover {
  opacity: 0.7;
}

.scenario-btn {
  display: flex;
  height: 100%;
  width: 80%;
  align-items: center;
  justify-content: space-evenly;
  padding-bottom: 5%;
}

.scenario-btn>button {
  width: 300px;
  height: 300px;
  border-radius: 20px;
  border: none;
  padding-top: 20px;
  background-color: #dfdfdf;
}

.scenario-btn>button>p {
  font-size: 20px;
  font-weight: bold;
  color: rgb(50, 50, 50);
  margin-top: 20px;
}

.scenario-btn>button:hover {
  opacity: 0.7;
}


textarea {
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
}</style>