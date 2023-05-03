import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
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
            fileId : '', 
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
  },
  mutations: {
    setPageArr(state, payload) {},
  },
});
