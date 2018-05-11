<template>
  <div class="container">
    <div class="header">
      <button class="logout" v-on:click="logout">Log Out</button>
      <span class="user-name">{{userData.name}}</span>
    </div>
    <img class="profile-img" :src="userData.profilePic">
    <div>
  </div>
</template>
<script>
export default {
  mounted () {
    this.userData.name = this.$store.state.userData.name;
    this.userData.email = this.$store.state.userData.email;
    this.userData.profilePic = this.$store.state.userData.profilePic;
    console.log("user vue is mounted");
  },
  methods: {
    logout(){
      document.cookie = "sessionId" + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
      this.$router.push({path:'/login'});
    }
  },
  data() {
    return {
      userData : {
        name: "",
        email: "",
        profilePic: ""
      }
    }
  }
}
</script>

<style scoped>
.container {
  height: 100%;
  width: 100%;
}

.logout {
  font-size: 20px;
  padding: 10px;
  display: inline-block;
  float: right;
  background: #e1e9ee;
  margin-right: 100px;
}

.header {
  background: #283e4a;
  padding-top: 10px;
  padding-bottom: 10px;
  width: 100%;
  display: inline-block;
}

.user-name {
  margin-right: 20px;
  font-size: 30px;
  color: white;
  float: right;
}

.profile-img {
  height: 100px;
  width: 100px;
}
</style>
