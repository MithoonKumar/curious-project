import Vue  from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state :{
    userData: {
      name: "",
      email: "",
      profilePic: "",
      webSocket: "",
    }
  },
  mutations: {
    changeUserData (state, data) {
      state.userData.name = data.name;
      state.userData.email = data.email;
      state.userData.profilePic = data.imageLink;
    },
    assignWebSocket (state, ws) {
      state.userData.webSocket = ws;
    }
  }
});
