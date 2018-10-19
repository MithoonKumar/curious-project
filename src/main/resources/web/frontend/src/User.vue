<template>
  <div class="container">
    <div class="header">
      <span class="user-name">{{userData.name}}</span>
      <img class="profile-img" :src="userData.profilePic">
      <button class="logout" v-on:click="logout">Log Out</button>
    </div>
    <div class="content-div">
      <div class="daily-posts">
      </div>
      <div class="contacts-list">
        <p class="contacts-title">Your Contacts</p>
        <template v-for="user in contacts">
                <Contact v-bind:name=user></Contact>
        </template>
      </div>
    </div>
    <!--div class="other-profile" >
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
    </div-->
  </div>
</template>
<script>
import chatBox from "./chat-box.vue";
import Contact from "./Contact.vue";
export default {
  components: {
          'chatBox': chatBox,
          'Contact': Contact
      },
  mounted () {
    this.userData.name = this.$store.state.userData.name;
    this.userData.email = this.$store.state.userData.email;
    this.userData.profilePic = this.$store.state.userData.profilePic;
    this.ws = new WebSocket("wss://a0d360fe.ngrok.io/message");
    this.$store.commit('assignWebSocket', this.ws);
    window._this = this;
    this.ws.onopen = function(data){
      _this.sendSubscriptionMessage();
    };
    this.ws.onmessage = function(data) {
      var dataObject = JSON.parse(data.data);
      var senderEmail = dataObject.from;
      _this.senderEmail = senderEmail;
      _this.dataObject = dataObject;
      if (!_this.openChats.includes(senderEmail)) {
        _this.createMessageBox(senderEmail);
        setTimeout(function(){
           _this.fetchMessageHistory(_this.senderEmail, _this.userData.email);
        }, 0);
        return;
      }
      if ("history" in dataObject) {
          _this.prependMessageToChatBox(senderEmail, dataObject.chatFrom, dataObject.message);
      } else {
          _this.appendMessageToChatBox(senderEmail, dataObject.message);
      }
    }
  },
  methods: {
    logout(){
      let _this = this;
      this.$http.put('/logout', {}, {}).then(function(data) { //first parameter is address , second parameter is body to be sent and in the third parameter we can send headers
          console.log(data.body);
          _this.ws.close();
          _this.$router.push({path:'/login'});
        }).catch(function (err) {
          console.log("User could not be logged out");
        });
      },
    sendSubscriptionMessage(){
        var message = {"messageType":"subscription","from":this.userData.email};
        this.ws.send(JSON.stringify(message));
    },
    fetchMessageHistory(from, to){
        var message = {"messageType":"history", "from": from, "to": to, "for": from};
        this.ws.send(JSON.stringify(message));
    },
    sendSearchText(){
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
      if (!this.openChats.includes(this.randomEmail)) {
        this.openChats.push(this.randomEmail);
        _this = this;
        setTimeout(function(){
           _this.fetchMessageHistory(_this.randomEmail, _this.userData.email);
        }, 0);
      }
    },
    createMessageBox(email){
      this.openChats.push(email);
    },
    appendMessageToChatBox(email, message){
      var chatContainer = document.getElementById(email).firstChild;
      var pText = document.createElement("p");;
      pText.textContent = email + ":" + message;
      chatContainer.appendChild(pText);
      chatContainer.scrollTop = chatContainer.scrollHeight;
    },
    prependMessageToChatBox(email, chatFrom, message){
      var chatContainer = document.getElementById(email).firstChild;
      var pText = document.createElement("p");;
      pText.textContent = chatFrom + ":" + message;
      if (!chatContainer.firstChild) {
        chatContainer.appendChild(pText);
      } else {
        chatContainer.insertBefore(pText, chatContainer.firstChild);
      }
      chatContainer.scrollTop = chatContainer.scrollHeight;
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
      openChats: [],
      contacts: [ "Sachin", "Dhoni"]
    }
  }

}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.header {
  background: #283e4a;
  display: flex;
  box-sizing: border-box;
  flex-grow: 0;
  padding: 10px 20% 10px 20%;
}

.logout {
  font-size: 20px;
  background: #e1e9ee;
  border-radius: 10px;
  margin-left: 10px;
  cursor: pointer;
  outline: none;
}


.user-name {
  font-size: 30px;
  line-height: 60px;
  color: white;
  flex-grow: 20;
  text-align: right;
  padding-right: 10px
}


.profile-img {
  height: 60px;
  width: 60px;
  border-radius: 10px;
}

.content-div {
  display: flex;
  flex-grow: 1;
}

.daily-posts {
  flex-grow: 3;
}

.contacts-list {
  border-left: solid 1px black;
  flex-grow: 1;
  flex-basis: 200px;
  max-width: 200px;
}

.contacts-title {
  margin: 0px;
  padding: 5px;
  font-weight: 600;
  text-align: center;
  border-bottom: solid 1px black;
}

.contact {
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
