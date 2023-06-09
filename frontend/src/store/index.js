import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    bookId: null, // 작품 번호
    pageList: [], // 작품 페이지 리스트
    saveState : false,
    canvasCompleted: false,
  },
  getters: {
    getBookId(state) {
      return state.bookId;
    },
    getPageList(state) {
      return state.pageList;
    },
    getSaveState(state) {
      return state.saveState;
    },
    getCanvasCompleted(state) {
      return state.canvasCompleted;
    }
  },
  mutations: {
    setBookId(state, newBookId) {
      state.bookId = newBookId;
    },
    setPageList(state, newPageList) {
      state.pageList = newPageList;
    },
    setSaveState(state, newSaveState) {
      state.saveState = newSaveState;
    },
    setCanvasCompleted(state, canvasCompleted) {
      state.canvasCompleted = canvasCompleted;
    },
  },
  actions: {
    
  },
  plugins: [
    createPersistedState({
      path: ['bookId', 'pageList', 'saveState']
    })
  ]
});
