import Vue  from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state :{
    userData: {
      name: "",
      email: ""
    }
  },
  mutations: {
    changeUserData (state, data) {
      state.userData.name = data.name;
      state.userData.email = data.email;
      state.userData.profilePic = data.dataImage;
    }
  }
});
