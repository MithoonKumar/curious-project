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
      listOfRequestsReceived: []
    },
    contacts: []
  },
  mutations: {
    changeUserData (state, data) {
      state.userData.name = data.name;
      state.userData.email = data.email;
      state.userData.profilePic = data.imageLink;
      state.userData.listOfRequestsReceived = data.listOfRequestsReceived;
      state.contacts = data.contacts;
    },
    assignWebSocket (state, ws) {
      state.userData.webSocket = ws;
    }
  }
});
