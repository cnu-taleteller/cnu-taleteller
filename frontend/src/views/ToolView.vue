<template>
  <div class="tool">
    <div class="tool-header">
      <ToolHeader></ToolHeader>
    </div>
    <!-- 새로 만드는 작품일 때만 -->
    <div v-if="toolState === 'new'" class="tool-contnet">
      <button @click="selectScenarioMenu('gpt')">시나리오 추천 받기</button>
      <button @click="selectScenarioMenu('write')">시나리오 직접 쓰기</button>
    </div>
    <!-- gpt가 쓰는 시나리오 -->
    <div v-else-if="toolState === 'gpt'" class="tool-contnet">
      <div class="scenario-form">
        <h4>시나리오</h4>
        <p>키워드를 입력하세요.</p>
        <p>사건은 구체적이게 적을수록 좋습니다!</p>
        <p>누가: <input type="text" class="scenario-input" v-model="scenario.who" placeholder="짱구가"></p>
        <p>언제: <input type="text" class="scenario-input" v-model="scenario.when" placeholder="주말 아침에"></p>
        <p>어디서: <input type="text" class="scenario-input" v-model="scenario.where" placeholder="숲에서"></p>
        <p>사건: <textarea class="scenario-input" v-model="scenario.event"
            placeholder="외계인을 만나 당황했지만 재밌게 노는 어린이 이야기"></textarea></p>
        <button @click="setScenario()">시나리오 받아보기</button>
      </div>
    </div>
    <!-- 내가 쓰는 시나리오 -->
    <div v-else-if="toolState === 'write'" class="tool-contnet">
      <div class="scenario-form">
        <h4>시나리오</h4>
        <p>시나리오를 입력해주세요.</p>
        <p>도입: <input type="text" class="scenario-input" v-model="write1"></p>
        <p>전개: <input type="text" class="scenario-input" v-model="write2"></p>
        <p>위기: <input type="text" class="scenario-input" v-model="write3"></p>
        <p>결말: <input type="text" class="scenario-input" v-model="write4"></p>
        <button @click="saveScenario()">시나리오 저장</button>
        <button @click="goTool()">나중에 쓸게요</button>
      </div>
    </div>
    <!-- 툴 -->
    <div v-else class="tool-content">
      <div class="tool-left">
        <ToolPageList @currentPageList="handlePageList"></ToolPageList>
      </div>
      <div class="tool-center">
        <ToolSelectedPage :currentPageList="this.currentPageList" :selectedMenu="this.selectedMenu"></ToolSelectedPage>
      </div>
      <div class="tool-right">
        <ToolMenu @selectedMenu="handleSelectedMenu" :currentPageList="this.currentPageList" :finalScenario="this.finalScenario" :gpt="this.gpt"></ToolMenu>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

import toolHeader from '@/components/ToolHeader.vue';
import toolPageList from '@/components/ToolPageList.vue';
import toolSelectedPage from '@/components/ToolSelectedPage.vue';
import toolLayer from '@/components/ToolLayer.vue';
import toolMenu from '@/components/ToolMenu.vue';

export default {
  data() {
    return {
      toolState: null,
      gpt: false, // gpt로 시나리오 받을건지.. 받을거면 true
      API_KEY: '',

      // gpt로 받을 시나리오 키워드
      scenario: {
        who: null,
        when: null,
        where: null,
        event: null
      },  
      resultScenario: [], // [도입][전개] 등 태그 전체 있는 배열
      finalScenario: [], // props로 전달할 [도입][전개] 등 태그 없는 순수 텍스트 배열

      // 직접 쓸 때 기승전결 받기
      write1: null,
      write2: null,
      write3: null,
      write4: null,
      selectedMenu : '',
      currentPageList: {},
      bookId: null, // 작품 번호
      pageList: [ // 작품안에 있는 페이지 여러 개 배열로 - 인덱스가 order
          {
          pageId : 1, // 작품마다 페이지 고유한 번호 
          pageStatus: 1, // 페이지 있으면 1, 삭제하면 0
          // 자막 관련
          caption : {
            size: 10,
            content: null,
            location: null,
          },
          thumbnail: null,
          // 페이지 안에 있는 파일들(레이어)
          layerList : [
            {
              id : 'item',
              layerId : '0',
              fileId : '/images/field.png', 
              menu: 'background', 
              draggable : 'true', 
              style : { 
                width: '1200px', // 가로사이즈
                height: '800px', // 세로사이즈
                left : "0px", // x 좌표
                top : "0px", // y 좌표
                position : "absolute",
              },
            },
          ]
        }
      ],
    }
  },
  created() {
    if (sessionStorage.getItem('recentlyClickPageNo') != null) {
      this.selectPageNo = sessionStorage.getItem('recentlyClickPageNo');
    }
    this.toolState = sessionStorage.getItem('toolState');

    if (sessionStorage.getItem('scenario')) {
      this.resultScenario = sessionStorage.getItem('scenario');
      this.setScenarioArr();
    }
  },
  components: {
    ToolHeader: toolHeader,
    ToolPageList: toolPageList,
    ToolSelectedPage: toolSelectedPage,
    ToolLayer: toolLayer,
    ToolMenu: toolMenu,
  },
  methods: {
    handlePageList(currentPageList) {
      this.currentPageList = currentPageList;
    },
    handleSelectedMenu(selectedMenu) {
      this.selectedMenu = selectedMenu;
    },
    selectScenarioMenu(arg) {
      this.toolState = arg;
      sessionStorage.setItem('toolState', arg);
    },
    saveScenario(){
      if(this.write1==null || this.write2==null || this.write3==null || this.write4==null){
        alert('빈 내용을 다 채워주세요!');
        return;
      }
      else {
        this.resultScenario='[도입]'+this.write1 + '[전개]'+this.write2+ '[위기]'+this.write3 +'[결말]'+this.write4;
        sessionStorage.setItem('scenario', this.resultScenario);
        this.setScenarioArr();
        console.log(this.finalScenario);
        alert('시나리오가 저장되었습니다!');
      }
      this.goTool();
    },
    goTool(){
      sessionStorage.removeItem('toolState');
      this.$emit('finalScenario', this.finalScenario);
      this.$emit('gpt', this.gpt);
      this.toolState = null;
    },
    setScenario() {
      this.gpt = true;
      this.goTool();
      console.log("axios 통신 요청");
      axios.post("https://api.openai.com/v1/chat/completions",
        {
          "model": "gpt-3.5-turbo",
          "messages": [{
            "role": "user",
            "content": `누가: ${this.scenario.who},
                        언제: ${this.scenario.when}, 
                        어디서: ${this.scenario.where},
                        사건: ${this.scenario.event}
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
            "Authorization": `Bearer ${this.API_KEY}`,
          }
        }
      )
        .then((res) => {
          console.log(res.data.choices[0].message.content);
          this.resultScenario = res.data.choices[0].message.content;
          sessionStorage.setItem('scenario', this.resultScenario);
          this.setScenarioArr();
          this.gpt = false;
          this.$emit('gpt', this.gpt);
          this.$emit('finalScenario', this.finalScenario);
        })
        .catch((err) => {
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
        this.finalScenario.push(scenario.slice(start, end).replace(section, '').trim());
      });
    }
  },
}


</script>
<style scoped>
.tool {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.tool-header {
  height: 10%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool-content {
  height: 90%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool-left {
  height: 100%;
  width: 15%;
}

.tool-center {
  height: 100%;
  width: 60%;
}

.tool-right {
  height: 100%;
  width: 25%;
}

.scenario-form {
  display: flex;
  flex-direction: column;
  width: 100%;
}

/* .scenario-input {
    width: 40%;
}

.scenario-content {
    width: 60%;
} */

.scenario-input {
  width: 250px;
}
</style>