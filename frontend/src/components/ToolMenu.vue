<template>
  <div class="menu">
    <div class="menu-list">
      <button class="menu-btn" :class="{ active: selectedMenu === 'background' }"
        @click="setSelectedMenu('background')">배경</button>
      <button class="menu-btn" :class="{ active: selectedMenu === 'character' }"
        @click="setSelectedMenu('character')">캐릭터</button>
      <button class="menu-btn" :class="{ active: selectedMenu === 'scenario' }"
        @click="setSelectedMenu('scenario')">시나리오</button>
      <button class="menu-btn" :class="{ active: selectedMenu === 'tts' }"
              @click="setSelectedMenu('tts')">TTS</button>
      <button class="menu-btn" :class="{ active: selectedMenu === 'recode' }"
              @click="setSelectedMenu('recode')">음성녹음</button>
    </div>
    <div class="menu-form">
      <div class="uploadImage">
        <div v-if="selectedMenu == 'background'">
          <input type="file" @change="uploadFile('background')" accept="image/*" id="image" ref="file">
        </div>
        <div v-else-if="selectedMenu == 'character'">
          <input type="file" @change="uploadFile('character')" max accept="image/*" id="image" ref="file">
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
            <button class="submit-btn" :class="{ active: flowMenu == false }" @click="flowMenu = false">선택한 시나리오</button>
            <button class="submit-btn" :class="{ active: flowMenu == true }" @click="checkFlow('menu')">흐름 파악하기</button>

            <!-- 선택한 시나리오 -->
            <div class="scenario-form2" v-if="flowMenu == false">
              <p v-for="(story, index) in selectScenario" :key="index">
                {{ setScenarioLabel(index) }} <br>
                <textarea v-model="selectScenario[index]" class="story-input"
                  :disabled="isDisabled">{{ story }}</textarea>
              </p>
              <button class="submit-btn" v-show="isDisabled" :disabled="isDisabled2"
                @click="editScenario('edit')">수정</button>
              <button class="submit-btn" v-show="!isDisabled" @click="editScenario('save')">저장</button>
            </div>

            <!-- 흐름 파악하기 -->
            <div v-show="flowMenu" class="scenario-form2">
              <div v-if="flowMenu == true && loading == true">
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
        <div v-else-if="selectedMenu == 'tts'">
          <input type="radio" name="myRadio" value="male" @change="handleTtsChange">
          <label for="maleVoice1">남성</label>
          <input type="radio" name="myRadio" value="female" @change="handleTtsChange">
          <label for="femaleVoice1">여성</label>
          <button class="submit-btn" @click="addTts()">추가</button>
<!--          <button class="submit-btn" @click="addTts()">미리 듣기</button>-->
        </div>
        <div v-else-if="selectedMenu == 'recode'">
          <div v-if="!recordingStarted">
            <button @click="startRecording()">녹음 시작</button>
          </div>
          <div v-else>
            <div>{{ timerDisplay }}</div>
            <button @click="stopRecording()">멈춤</button>
          </div>
          <div>
            <div v-for="(audioUrl, index) in this.voiceList" :key="index">
              <audio :src="audioUrl" controls></audio>
              <label>
                <input type="radio" :value="audioUrl" v-model="selectedAudio">선택하기
              </label>
            </div>
            <button @click="saveSelectedAudio">저장</button>
          </div>
        </div>
      </div>
      <div class="image-list">
        <div id="item">
          <div class="uploaded-image-list" v-show="selectedMenu == 'character'">
            <img :src="item.src" crossOrigin="anonymous" :draggable="item.draggable" :id="item.id"
              :style="{ height: '100px', width: '100px' }" v-for="item, index in charList">
          </div>
          <div class="uploaded-image-list" v-show="selectedMenu == 'background'">
            <img :src="item.src" crossOrigin="anonymous" :draggable="item.draggable" :id="item.id"
              :style="{ height: '100px', width: '100px' }" v-for="item, index in backList">
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
      selectedMenu: 'scenario',
      bookId: null,
      pageNo: 0,
      nextId: 1,
      uploadId: 0,
      isUpload: false,
      imageIndex: 0,

      file: null,
      s3: {
        preSignedUrl: null,
        encodedFileName: null,
        uploadedUrl: null,
      },

      scenarioKeyword: {
        who: null,
        when: null,
        where: null,
        event: null
      },
      isDisabled: true, // 시나리오 textarea 비활성화
      isDisabled2: false, // 시나리오 수정버튼 활성화
      select: false, // 시나리오 선택 여부
      scenarioNum: 0, // 시나리오 선택 번호
      flowMenu: false, // 시나리오 or 흐름 파악하기
      loading: false, // gpt 일때 로딩 여부
      flowcnt: 0, // 흐름 파악 횟수
      flowResult: null, // gpt로 받은 흐름 파악하기
      allCaption: [], // 모든 자막
      finalScenario: [[], [], [], [], []], // gpt로 받는 시나리오
      selectScenario: [], // 선택한 시나리오
      resultScenario: [],  // [도입], [전개] 등 다 있는 시나리오 - session 저장용
      isReScenario: false,
      voiceList: [], //TTS나 음성녹음 리스트

      // 업로드되는 이미지 리스트
      uploadBackList: [],
      uploadCharList: [],

      charList: Array.from({ length: 25 }, (_, i) => ({
        src: `${process.env.VUE_APP_S3_DEFAULT_PATH}/character${i}.png`,
        id: `character${i}`,
        draggable: "true",
        height: "100px",
      })),

      // 기본적으로 있는 배경 배열
      backList: Array.from({ length: 18 }, (_, i) => ({
        src: `${process.env.VUE_APP_S3_DEFAULT_PATH}/background${i}.png`,
        id: `background${i}`,
        draggable: "true",
      })),
      recordingStarted: false,
      timer: null,
      elapsedTime: 0,
      selectedAudio: null,

    }
  },
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
  computed: {
    timerDisplay() {
      // 타이머를 분:초 형식으로 표시하기 위한 계산
      const minutes = Math.floor(this.elapsedTime / 60);
      const seconds = this.elapsedTime % 60;
      return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    },
  },
  methods: {
      // S3 presigned url 받아오기
      async uploadFile(menu) {
          const maxSize = 5 * 1024 * 1024;
          const fileSize = document.getElementById("image").files[0].size;
          if (fileSize > maxSize) {
              alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다.");
              return;
          }

          this.file = this.$refs.file.files[0];
          await axios.get("/api/v1/tool/s3/image", {params: {fileName: this.file.name}},)
              .then((res) => {
                  this.s3.preSignedUrl = res.data.preSignedUrl
                  this.s3.encodedFileName = res.data.encodedFileName
                  this.uploadImageToS3(this.s3.preSignedUrl, this.file, menu)
              })
      },
      // S3 업로드
      async uploadImageToS3(preSignedUrl, file, menu) {
          await axios.put(preSignedUrl, file)
              .then((res) => {
                  this.s3.uploadedUrl = `${process.env.VUE_APP_S3_PATH}/${this.s3.encodedFileName}`

                  if (menu === 'background') {
                      this.uploadBackList.push(this.s3.uploadedUrl);
                      sessionStorage.setItem('uploadBackList', JSON.stringify(this.uploadBackList));
                      this.backList.push({
                          src: this.s3.uploadedUrl,
                          id: 'upload' + this.uploadId,
                          draggable: "true",
                          height: "100px",
                      });
                  } else if (menu === 'character') {
                      this.uploadCharList.push(this.s3.uploadedUrl);
                      sessionStorage.setItem('uploadCharList', JSON.stringify(this.uploadCharList));
                      this.charList.push({
                          src: this.s3.uploadedUrl,
                          id: 'upload' + this.uploadId,
                          draggable: "true",
                          height: "100px",
                      });
                  }
                  this.uploadId++;
                  console.log("S3 업로드 성공");
                  document.getElementById("image").value = "";
              })
              .catch((err) => {
                  console.error(err);
                  alert("서버 문제로 파일 업로드에 실패하였습니다. 잠시 후 다시 시도해주세요🙇‍♀️");

              });
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
          sessionStorage.setItem('select', true);
          sessionStorage.removeItem('scenarioKeyword');
      },
      // 기승전결 흐름 파악
      checkFlow(arg) {
          this.flowMenu = true;
          let len = this.pageList.length;

          if (len < 3) {
              alert('3페이지 이상 작업하셔야 흐름을 파악할 수 있습니다!');
              this.flowMenu = false;
              return;
          }
          if (this.flowcnt > 4) {
              alert('흐름 파악은 5번까지만 가능합니다!');
              return;
          }

          for (let i = 0; i < len; i++) {
              this.allCaption[i] = this.pageList[i].caption.content;
          }

          if (arg === 'menu') {
              if (this.flowResult == null) {
                  this.checkFlowGpt();
              }
          } else if (arg === 're') {
              this.checkFlowGpt();
          }
      },

      checkFlowGpt() {
          this.flowcnt++;
          this.loading = true;
          const story = sessionStorage.getItem('scenario');
          const captions = this.allCaption;

          console.log("axios 통신 요청");
          axios.post("/api/v1/tool/scenario/flow", {
              story, captions

          })
              .then((res) => {
                  this.flowResult = res.data;
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
          const popupWidth = 550;
          const popupHeight = 650;
          const popupX = Math.ceil((window.screen.width - popupWidth) / 2);
          const popupY = Math.ceil((window.screen.height - popupHeight) / 2);
          window.open("/keyword", "toolKeyword", ` width=${popupWidth}, height=${popupHeight}, left=${popupX}, top=${popupY}`);
      },
      handleTtsChange(event) {
          const selectedValue = event.target.value;
          this.$emit('ttsValueChange', selectedValue);
      },
      addTts() {
          const text = this.currentPageList.caption.content;
          const voice = this.currentPageList.caption.ttsVoice;
          const language = "ko-KR";
          console.log(text);

          axios.post('/api/v1/tool/tts', {
                  text,
                  language,
                  voice,
              }
          ).then(response => {
              const ttsUrl = response.data.ttsUrl;

              this.currentPageList.caption.ttsName = `${process.env.VUE_APP_S3_PATH}/${response.data.encodedFileName}`;
              console.log(this.currentPageList.caption.ttsName);

              this.voiceList.push(this.currentPageList.caption.ttsName);
              //sessionStorage.setItem('voiceList', JSON.stringify(this.voiceList));
             // console.log(this.voiceList);

              // 작은 인터넷 창을 새로 열어 TTS 음성 재생
              //window.open(ttsUrl, '_blank');
          }).catch(error => {
              console.error(error);
          })
      },
      startRecording() {
          navigator.mediaDevices.getUserMedia({audio: true})
              .then(stream => {
                  this.recordingStarted = true;
                  this.timer = setInterval(() => {
                      this.elapsedTime++;
                  }, 1000);
                  this.currentPageList.caption.ttsVoice = new MediaRecorder(stream);
                  this.currentPageList.caption.ttsVoice.addEventListener('dataavailable', event => {
                      if (event.data.size > 0) {
                          console.log(this.currentPageList);
                          this.currentPageList.caption.recordedChunks.push(event.data);
                      }
                  });
                  this.currentPageList.caption.ttsVoice.start();
              })
              .catch(error => {
                  console.error('녹음을 시작할 수 없습니다:', error);
              });
      },
      stopRecording() {
          if (this.currentPageList.caption.ttsVoice && this.currentPageList.caption.ttsVoice.state === 'recording') {
              this.currentPageList.caption.ttsVoice.addEventListener('stop', () => {
                  const audioBlob = new Blob(this.currentPageList.caption.recordedChunks, {type: 'audio/wav'});
                  this.sendRecording(audioBlob);
                  this.currentPageList.caption.recordedChunks = [];
                  this.recordingStarted = false;
                  clearInterval(this.timer);
                  this.elapsedTime = 0;
              });
              this.currentPageList.caption.ttsVoice.stop();
          }
      },
      sendRecording(audioBlob) {
          const formData = new FormData();
          formData.append('audio', audioBlob, 'recording.wav');

          const config = {
              headers: {
                  'Content-Type': 'multipart/form-data',
              },
          };

          axios.post('/api/v1/tool/audio', formData, config)
              .then(response => {
                  const fileName = `${process.env.VUE_APP_S3_PATH}/` + response.data;
                  this.currentPageList.caption.ttsName = fileName;
                  console.log('음성 녹음이 S3 서버로 전송되었습니다.');
                  console.log(this.currentPageList.caption.ttsName);
                  this.voiceList.push(this.currentPageList.caption.ttsName);
              })
              .catch(error => {
                  console.error('음성 녹음을 S3 서버로 전송하는 중 오류가 발생했습니다:', error);
              });
      },

      saveSelectedAudio() {
          if (this.selectedAudio !== null) {
              this.currentPageList.caption.ttsName = this.selectedAudio;
              console.log(this.currentPageList.caption.ttsName);
          }
      },
      reScenario() {
          if (this.finalScenario[4].length > 0) {
              alert('시나리오는 작품당 5번만 받을 수 있습니다.');
              return;
          }
          this.isReScenario = true;
          this.isDisabled2 = true;
          console.log("axios 통신 요청");
          axios.post("/api/v1/tool/scenario/", {
              who: this.scenarioKeyword.who,
              when: this.scenarioKeyword.when,
              where: this.scenarioKeyword.where,
              event: this.scenarioKeyword.event
          })
              .then((res) => {
                  this.resultScenario = res.data;
                  sessionStorage.setItem('scenario', this.resultScenario);
                  this.setScenarioArr();
                  this.isDisabled2 = false;
              })
              .catch((err) => {
                  alert('서버 오류로 시나리오 요청에 실패하였습니다.');
                  console.error(err);
              })
              .finally(() => {
                  this.isReScenario = false;
              });
      },
      setScenarioArr() {
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
          })
      },
  }

}
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

.scenario-form2>p {
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
