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
      <div class="uploadImage">
        <div v-if="selectedMenu == 'background'">
          <input type="file" @change="setImage('background')" accept="image/*" id="image">
        </div>
        <div v-else-if="selectedMenu == 'character'">
          <input type="file" @change="setImage('character')" accept="image/*" id="image">
        </div>
        <!-- 시나리오 -->
        <div v-else-if="selectedMenu == 'scenario'">
          <!-- gpt 시나리오 없을 때 -->
          <div v-if="gpt == true && (!finalScenario || finalScenario.length === 0)">
            <div class="spinner-border" role="status"></div>
            <p>열심히 작성중입니다.<br>조금만 기다려주세요!ㅠㅠ</p>
          </div>
          <!-- 내가 적은 시나리오 없을 때 -->
          <div v-else-if="gpt == false && (!finalScenario || finalScenario.length === 0)">
            <p>입력된 시나리오가 없습니다.<br>시나리오를 입력해주세요.</p>
            <button>추가</button>
          </div>
          <!-- 시나리오 있을 때 -->
          <div v-else>
            <!-- 다시 작성 -->
            <div v-show="isReScenario">
              <div class="spinner-border" role="status"></div>
              <p>새로운 내용으로 작성중입니다.<br>조금만 기다려주세요!ㅠㅠ</p>
            </div>
            <!-- 내용 보여주기 -->
            <p v-for="(story, index) in finalScenario" :key="index">
              {{ setScenarioLabel(index) }} <br>
              <textarea v-model="finalScenario[index]" class="story-input" :disabled="isDisabled">{{ story }}</textarea>
            </p>
            <button :disabled="isDisabled2" @click="reScenario()">시나리오 다시 받기</button>
            <button v-show="isDisabled" :disabled="isDisabled2" @click="editScenario('edit')">수정</button>
            <button v-show="!isDisabled" @click="editScenario('save')">저장</button>
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
      isDisabled: true, // 시나리오 textarea 비활성화
      isDisabled2: false, // 수정버튼 활성화
      pageNo: 0,
      resultScenario: [],
      finalScenario: [],
      isReScenario: false,
      scenarioKeyword: {},

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
      nextId: 1,
      uploadId: 0,
      isUpload: false,
      imageIndex: 0,
    }
  },
  props: {
    currentPageList: Object,
    finalScenario2: Array,
    gpt: Boolean,
  },
  mounted() {
    const toolSelectedPageDrag = document.querySelector('.page-form .selected-page .drag-image');
    this.existingImageEventDragStart();
    this.imageEventDragOver(toolSelectedPageDrag);
    this.imageEventDrop(toolSelectedPageDrag);
    this.scenarioKeyword = JSON.parse(sessionStorage.getItem('scenarioKeyword'));
    this.finalScenario = this.finalScenario2;
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
    // 시나리오 수정
    editScenario(arg) {
      this.isDisabled = !!!this.isDisabled;
      this.resultScenario = '[도입]' + this.finalScenario[0] + '[전개]' + this.finalScenario[1] + '[위기]' + this.finalScenario[2] + '[결말]' + this.finalScenario[3];
      if (arg === 'save') {
        sessionStorage.setItem('scenario', this.resultScenario);
      }
    },

    // 시나리오 다시 받기
    reScenario() {
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
          this.finalScenario = [];
          this.setScenarioArr();
          console.log(this.finalScenario);
          
          this.gpt = false;
          this.isDisabled2 = false;
        })
        .catch((err) => {
          this.gpt = false;
          alert('서버 오류로 시나리오 요청에 실패하였습니다.');
          console.log(err);
        })
        .finally(()=>{
          this.isReScenario = false;
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
    },
    async setImage(menu) {
      console.log(menu);
      try {
        let frm = new FormData();
        let imageFile = document.getElementById("image");
        frm.append("image", imageFile.files[0]);
        const res = await axios.post(`/api/users/image?menu=${menu}`, frm, {
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
    imageEventDragOver(element) {
      element.addEventListener("dragover", (e) => {
        e.preventDefault();
        e.stopPropagation();
      });
    },
    imageEventDrop(element) {
      let nextId = this.nextId;
      let layerListRemove;
      element.addEventListener("drop", (e) => {
        e.preventDefault();
        e.stopPropagation();
        let rX = e.pageX - document.querySelector('.page-form').offsetLeft;
        let rY = e.pageY - document.querySelector('.page-form').offsetTop;
        let [data, x, y] = e.dataTransfer.getData("text/plain").split(',');
        let imageElement = document.querySelector(`.menu .image-list #item[data-id=${data}]`);
        let cloneImageElement = imageElement.cloneNode();
        cloneImageElement.setAttribute("draggable", "false");
        let imageId = this.selectedMenu + nextId++;
        cloneImageElement.dataset.layerId = imageId;
        if (this.selectedMenu == 'background') {
          const toolSelectedPageDrag = document.querySelector('.page-form');
          const dragImageWidth = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('width');
          const dragImageHeight = window.getComputedStyle(toolSelectedPageDrag).getPropertyValue('height');
          cloneImageElement.style.left = "0px";
          cloneImageElement.style.top = "0px";
          cloneImageElement.style.width = dragImageWidth;
          cloneImageElement.style.height = dragImageHeight;
          cloneImageElement.style.position = "absolute";
          cloneImageElement.style.zIndex = 1;
          let newImage = {
            fileId: cloneImageElement.src,
            id: 'item',
            layerId: String(imageId),
            menu: this.selectedMenu,
            style: {
              left: cloneImageElement.style.left,
              top: cloneImageElement.style.top,
              position: cloneImageElement.style.position,
              width: cloneImageElement.style.width,
              height: cloneImageElement.style.height,
            },
          };
          let elementToRemove = Array.from(document.querySelectorAll('.object #item[data-layer-id]'))
            .find(el => el.dataset.layerId.includes('background'));
          if (elementToRemove) {
            layerListRemove = elementToRemove.dataset.layerId;
            this.currentPageList.layerList.splice(0, 1, newImage);
            elementToRemove.parentNode.removeChild(elementToRemove);
          } else {
            this.currentPageList.layerList.unshift(newImage);
          }
          document.querySelector('.object').insertBefore(cloneImageElement, document.querySelector('.object').firstChild);
        } else {
          cloneImageElement.style.left = (rX - x) + "px";
          cloneImageElement.style.top = (rY - y) + "px";
          cloneImageElement.style.position = "absolute";
          cloneImageElement.style.width = cloneImageElement.width;
          cloneImageElement.style.height = cloneImageElement.height;
          cloneImageElement.style.zIndex = 1;
          let newImage = {
            fileId: cloneImageElement.src,
            id: 'item',
            layerId: String(imageId),
            menu: this.selectedMenu,
            style: {
              left: cloneImageElement.style.left,
              top: cloneImageElement.style.top,
              position: cloneImageElement.style.position,
              width: cloneImageElement.style.width,
              height: cloneImageElement.style.height,
            },
          };
          document.querySelector('.object').appendChild(cloneImageElement);
          this.imageIndex = this.currentPageList.layerList.length;
          this.currentPageList.layerList[this.imageIndex] = newImage;
        }
      });
    },
  },
}
</script>
<style scoped>
.menu {
  margin: 10px;
  height: 80vh;
  border: 1px solid gray;
}

.menu-list {
  text-align: left;
}

.image-list {
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-form {
  margin-top: 10px;
  overflow-y: scroll;
  height: 90%;
}

.menu-form::-webkit-scrollbar {
  display: none;
}

.menu-btn {
  border: 1px solid gray;
  background-color: white;
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

.story-input:disabled {
  color: black;
  border: none;
}</style>
  