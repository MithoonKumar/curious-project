<template>
  <div class="container">
    <div class="header">
      <button class="logout" v-on:click="logout">Log Out</button>
      <span class="user-name">{{userData.name}}</span>
    </div>
    <div class="personal-details">
      <p class="profile-name">{{userData.name}}</p>
      <img class="profile-img" :src="userData.profilePic">
    </div>
    <div class="other-profile" >
      <input class="search-box" placeholder="Search Result" v-on:keyup.enter="sendSearchText" v-model="searchText"></input>
      <img class="random-img" :src="randomPic" v-bind:class="{ hide: !searchResult, show: searchResult }">
      <div class="buttons" v-bind:class="{ hide: !searchResult, show: searchResult }">
        <button>Add Friend</button>
        <button>Ignore</button>
        <button v-on:click="message">Message</button>
      </div>
    </div>
    <div class="chat-container">
      <template v-for="user in openChats">
        <chatBox v-bind:id="user">{{user}}</chatBox>
      </template>
    </div>
  </div>
</template>
<script>
import chatBox from "./chat-box.vue";
export default {
  components: {
          'chatBox': chatBox
      },
  mounted () {
    this.userData.name = this.$store.state.userData.name;
    this.userData.email = this.$store.state.userData.email;
    this.userData.profilePic = this.$store.state.userData.profilePic;
    console.log("user vue is mounted");
    this.ws = new WebSocket("wss://5b193ad3.ngrok.io/message");
    this.$store.commit('assignWebSocket', this.ws);
    console.log("consoling websocket connection", this.ws);
    window._this = this;
    this.ws.onopen = function(data){
      console.log("websocket connection opened", data);
      _this.sendSubscriptionMessage();
    };
    this.ws.onmessage = function(data) {
      console.log("message received", data);
      var dataObject = JSON.parse(data.data);
      var senderEmail = dataObject.from;
      if (!_this.openChats.includes(senderEmail)) {
        _this.createMessageBox(senderEmail);
      }
      process.nextTick(function(){
        var chatContainer = document.getElementById(senderEmail).firstChild;
        var pText = document.createElement("p");;
        pText.textContent = senderEmail + ":" + dataObject.message;
        chatContainer.appendChild(pText);
        chatContainer.scrollTop = chatContainer.scrollHeight;
      });
    }
  },
  methods: {
    logout(){
      document.cookie = "sessionId" + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
      this.$router.push({path:'/login'});
      this.ws.close();
    },
    sendSubscriptionMessage(){
        var message = {"messageType":"subscription","from":this.userData.email};
        this.ws.send(JSON.stringify(message));
    },
    sendSearchText(){
      console.log("text sent");
      var _this = this;
      var postData = {
        name: this.searchText
      };
      this.$http.post('/search', postData, {}).then(function(data) { //first parameter is address , second parameter is body to be sent and in the third parameter we can send headers
        _this.randomPic = data.body.imageLink;
        _this.randomEmail = data.body.email;
        _this.searchResult = true;
      }).catch(function (err) {
        console.log("user could not sign up");
      });
    },
    message(){
      console.log("clicked on message button");
      if (!this.openChats.includes(this.randomEmail)) {
        this.openChats.push(this.randomEmail);
      }
    },
    createMessageBox(email){
      this.openChats.push(email);
    }
  },
  data() {
    return {
      userData : {
        name: "",
        email: "",
        profilePic: "",
        chatInput: ""
      },
      client: "",
      destination: "",
      ws: "",
      searchResult: false,
      searchText: "",
      randomPic: "",
      randomEmail: "",
      openChats: []
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

.profile-name {
  margin-left: 20px;
}

.personal-details {
  margin-left:10px;
  display: inline-block;
  float: left;
}

.profile-img {
  height: 200px;
  width: 200px;
}

.other-profile {
  padding: 20px;
  border: solid 1px black;
  display: inline-block;
  margin-left: calc((100% - 650px) / 2);
  vertical-align: top;
  float: left;
}

.hide {
  display: none;
}

.show {
  display: block;
}

.showInline {
  display: inline-block!important;
}

.inner-div {
  height: 200px;
  width: 500px;
  background-color: green;
}

.buttons {
  margin-top: 20px;
}

button {
  padding: 10px 10px;
  margin-right: 30px;
}

.random-img {
  height: 400px;
  width: 400px;
}

.search-box {
  width: 80%;
  font-size: 20px;
  outline: none;
  margin-bottom: 10px;
}

.message-box {
  width: 100%;
  box-sizing: border-box;
  outline: none;
  font-size: 15px;
}

.chat-history {
  width: 440px;
  height: 150px;
  background-color: grey;
}

.chat-container {
  width: 100%;
  display: inline-block;
}

</style>
