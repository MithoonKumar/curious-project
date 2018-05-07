<template>
  <div class="container">
    <div class="login-form-container">
      <p class="title">My Page</p>
      <form>
        <input class="input-box" type="text" name="emailId" placeholder="Email Address">
        <input class="input-box" type="text" name="password" placeholder="Password">
        <button class="submit-button" v-on:click="sendAuthentication">Sign in</button>
      </form>
      <span class ="membership-text">Not a member ?</span>
      <router-link to="/register" class="redirection">Join Now</router-link>
    </div>
  </div>
</template>
<script>
export default {
  methods: {
    sendAuthentication: function() {
      console.log("Ready for sending details");
    }
  }
}
</script>

<style scoped>
.container {
  height: 100%;
  width: 100%;
  background-image: url(./assets/loginPage.jpg);
  background-size: 100% 100%;
  background-position: 0px 0px;
}
.login-form-container {
  display: inline-block;
  background-color:rgba(255,255,255,0.7);
  height: 300px;
  width: 400px;
  border-radius: 5px;
  margin-left: calc((100% - 400px)/ 2);
  margin-top: 200px;
}
.title {
  margin-top: 30px;
  margin-left: 160px;
  font-size: 20px;
  font-weight: 500;
}
.input-box {
  font-size: 20px;
  display: block;
  width: 300px;
  margin-left: calc((100% - 300px)/ 2);
  font-family: Helvetica,Arial,sans-serif;
  outline: 0px;
  border: 1px solid grey;
  border-radius: 4px;
  padding-left: 10px;
  padding-top: 5px;
  padding-bottom: 0px;
  margin-top: 20px;
  box-sizing: border-box;
}
.submit-button {
  font-weight: bold;
  border-width: 1px;
  border-style: solid;
  cursor: pointer;
  border-radius: 3px;
  padding: 0 15px;
  line-height: 32px;
  box-sizing: border-box;
  font-size: 16px;
  color: #fff;
  background-color: #287bbc;
  border-color: #1b5480;
  background-image: -webkit-linear-gradient(top, #287bbc 0%,#23639a 100%);
  margin-top: 15px;
  width: 300px;
  outline: 0px;
  margin-left: calc((100% - 300px)/ 2);
  margin-bottom: 10px;
}
.submit-button:active {
  background-image: -webkit-linear-gradient(top, #287bbc 100%,#23639a 100%);
}
.membership-text {
  margin-left: 100px;
}
.redirection {
  text-decoration: none;
}

</style>
