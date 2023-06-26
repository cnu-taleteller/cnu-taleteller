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
    canSaveThumbNail: false,
    currentPageIndexNo: 0,
    isChange : false,
    isFinishDrop : true,
    isCaptionInput : false,
    bookName : '',
    isVoiceInput: false,
  },
  getters: {
    getBookId(state) {
      return state.bookId;
    },
    getBookName(state) {
      return state.bookName;
    },
    getPageList(state) {
      return state.pageList;
    },
    getSaveState(state) {
      return state.saveState;
    },
    getCanvasCompleted(state) {
      return state.canvasCompleted;
    },
    getCanSaveThumbNail(state) {
      return state.canSaveThumbNail;
    },
    getCurrentPageIndexNo(state) {
      return state.currentPageIndexNo;
    },
    getIsChange(state) {
      return state.isChange;
    },
    getIsFinishDrop(state) {
      return state.isFinishDrop;
    },
    getIsCaptionInput(state) {
      return state.isCaptionInput;
    },
    getIsVoiceInput(state) {
      return state.isVoiceInput;
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
    setCanSaveThumbNail(state, newCanSaveThumbNail) {
      state.canSaveThumbNail = newCanSaveThumbNail;
    },
    setCurrentPageIndexNo(state, newCurrentPageIndexNo) {
      state.currentPageIndexNo = newCurrentPageIndexNo;
    },
    setIsChange(state, isChange) {
      state.isChange = isChange;
    },
    setIsFinishDrop(state, newIsFinishDrop) {
      state.isFinishDrop = newIsFinishDrop;
    },
    setIsCaptionInput(state, newIsCaptionInput) {
      state.isCaptionInput = newIsCaptionInput;
    },
    setBookName(state, newBookName) {
      state.bookName = newBookName;
    },
    setIsVoiceInput(state, newIsVoiceInput) {
      state.isVoiceInput = newIsVoiceInput;
    }
  },
  actions: {
    clearSession() {
      sessionStorage.removeItem('scenario');
      sessionStorage.removeItem('uploadBackList');
      sessionStorage.removeItem('uploadCharList');
      sessionStorage.removeItem('select');
      sessionStorage.removeItem('toolState');
      sessionStorage.removeItem('bookName');
    }
  },
  plugins: [
    createPersistedState({
      path: ['bookId', 'saveState']
    })
  ]
});
