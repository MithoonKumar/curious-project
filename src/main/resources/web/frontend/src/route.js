import intro from './Intro.vue';
import registration from './Registration.vue';
import user from './User.vue';
import login from './Login.vue';
import chatBox from './chat-box.vue';

export default [
  { path: '/intro', component: intro},
  { path: '/register', component: registration},
  { path: '/user', component: user},
  { path: '/login', component: login},
  { path: '/chat-box', component: chatBox}
]
