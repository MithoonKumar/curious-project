<template>
  <div class="container">
    <div class="form-container">
        <p class="text">First name</p>
        <input class="input-box" v-model="firstName" type="text" name="firstName">
        <p class="text">Last name</p>
        <input class="input-box" v-model="lastName" type="text" name="lastName">
        <p class="text">Email</p>
        <input class="input-box" v-model="userEmail" type="email" name="emailId">
        <p class="text">Password</p>
        <input class="input-box" v-model="userPwd" type="password" name="password">
        <p class="text">Upload Your Profile Pic</p>
        <input id="profile-pic" type="file" @change="onFileChange($event.target.files)" name="profilePic" accept="image/*">
        <input class="submit-button" type="button" v-on:click="signUp" value="Agree & Join">
    </div>
    <Toast v-bind:message=message v-if="showToast" ></Toast>
  </div>
</template>

<script>
import Toast from "./Toast.vue";
export default {
  components: {
    'Toast': Toast
  },
  methods: {
    signUp: function() {
      if (this.firstName === "" || this.lastName === "" || this.userEmail === "" || this.userPwd === "" || this.formData === "")
      {
        this.showToast = true;
        setTimeout(()=>{ this.showToast = false;}, 3000);
        return;
      }

      if (this.userEmail != "" && this.userEmail.indexOf('@') === -1) {
        this.message = "Email is not in proper format";
        this.showToast = true;
        setTimeout(()=>{ this.showToast = false; this.message = "All fields are mandatory"}, 3000);
        return;
      }

      var postData = {
        firstName: this.firstName,
        lastName: this.lastName,
        userEmail: this.userEmail,
        userPwd: this.userPwd
      }
      this.$http.post('/signUp', postData, {}).then(function(data) { //first parameter is address , second parameter is body to be sent and in the third parameter we can send headers
        this.$http.post('/uploadProfilePic/'+this.userEmail, this.formData, {}).then(function(data) { //first parameter is address , second parameter is body to be sent and in the third parameter we can send headers
          this.$router.push({path:'/login'});
        }).catch(function (err) {
          console.log("user could not upload image");
        });
      }).catch(function (err) {
        console.log("user could not sign up");
      });

    },
    onFileChange: function(files) {
      this.formData = new FormData();
      this.formData.append("profilePic", files[0], files[0].name);
    }
  },
  data: function(){
    return {
      userEmail: "",
      userPwd: "",
      firstName: "",
      lastName: "",
      formData: "",
      showToast: false,
      message: "All fields are mandatory"
    }
  }
}
</script>

<style scoped>
.container {
  background: -webkit-linear-gradient(45deg, #0077b5,#008891);
  background: -webkit-linear-gradient(45deg, white,black);
  height: 100%;
  width: 100%;
}
.form-container {
  padding: 40px;
  height: 400px;
  width: 400px;
  background-color:#EEE;
  margin-left: calc((100% - 400px)/ 2);
  margin-top: 100px;
  box-sizing: border-box;
  display: inline-block;
  border-radius: 4px;
}
.text {
  margin: 0px;
  font-size: 12px;
  color: #66696a;
}
.input-box {
  font-size: 16px;
  font-weight: normal;
  line-height: 18px;
  color: #434649;
  padding-left: 6px;
  padding-right: 0px;
  padding-top: 0px;
  padding-bottom: 0px;
  border-radius: 2px;
  -webkit-transition: border-color 300ms ease-in-out;
  background: #FFF;
  border: 1px solid #BFBFBF;
  height: 32px;
  outline: 0;
  box-sizing: border-box;
  width: 100%;
  margin-bottom: 10px;
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
  width: 100%;
  outline: 0px;
}
.submit-button:active {
  background-image: -webkit-linear-gradient(top, #287bbc 100%,#23639a 100%);
}
</style>
