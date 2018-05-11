import Vue from 'vue'
import App from './App.vue'
import {store} from './store/store.js'
import VueResource from 'vue-resource'
import VueRouter from 'vue-router'
import Route from './route'

Vue.use(VueResource);
Vue.use(VueRouter);
Vue.config.devtools = true;

const router = new VueRouter({
  routes: Route,
  mode: 'history'
});

new Vue({
  el: '#app',
  store: store,
  render: h => h(App),
  router: router
})
