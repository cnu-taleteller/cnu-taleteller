import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
       // 배열 인덱스+1 = page_id
      pageArr: [{
        page_number: null,
        page_status: 1, // 페이지 있으면 1, 삭제하면 0
        caption_color: null,
        caption_content: null,
        caption_location: null,
        caption_size: null,
        thumbnail: null,
    }],

    // 배열 인덱스+1 = layer_id
    layerArr: [{
      layer_order: null,
      file_name: null,
      file_origin_name: null,
      file_size: null,
      layer_x: null,
      layer_y: null,
      layer_x_size: null,
      layer_y_size: null,
    }]
  },

  mutations: {
    setPageArr(state, payload) {
     
    }
  }
});

