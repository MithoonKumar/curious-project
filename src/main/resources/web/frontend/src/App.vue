<template>
  <div id="app" v-on:click="show">
    <router-view></router-view>
  </div>
</template>

<script>
import intro from './Intro.vue'; //For showing loading
import login from './Login.vue';//Through this page user can login and click on register tab
import register from './Registration.vue'//Registration page
import user from './User.vue'//User specific page
export default {
  name: 'app',
  data () {
    return {
    }
  },
  methods: {
    show () {
      console.log("consoling the state", this.$store.state);
    }
  },
  computed: {
    state () {
      return this.$store.state;
    }
  },
  mounted () {
    this.$router.push({path:'/intro'});
  }
}
</script>

<style>
html {
  height: 100%;
  width: 100%;
}
body {
  margin: 0px;
  height: 100%;
  width: 100%
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
  width: 100%;
}
</style>
