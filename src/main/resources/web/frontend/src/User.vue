<template>
  <div class="container" v-on:click="onUserContainerClick">
    <div class="header">
      <div class="search">
      <input class="search-input-box" v-on:keyup.enter="sendSearchRequest" v-model="searchText"  placeholder="Search For Person">
        <div class="card-container" v-on:click="onCardContainerClick" v-if="showDropDown">
          <template v-if="searchResult.length>0" v-for="result in searchResult">
            <PersonCard v-bind:person="result"></PersonCard>
          </template>
          <p class="not-found" v-if="searchResult.length===0">No Person Found</p>
        </div>
      </input>
      </div>
      <img class="search-image" src="./assets/search-image.jpg" v-on:click="sendSearchRequest"></img>
      <span class="user-name">{{userData.name}}</span>
      <img class="profile-img" :src="userData.profilePic">
      <div class="notification-container">
        <img v-bind:class="{haveNotifications: userData.listOfRequestsReceived.length > 0}" class="notification-img" src="./assets/bell-icon.png" v-on:click="showNotifications">
        <div class="request-container" v-if="userData.listOfRequestsReceived.length>0 && this.showRequests">
          <template v-for="request in userData.listOfRequestsReceived">
            <RequestCard v-bind:person="request"></RequestCard>
          </template>
        </div>
      </div>
      <button class="logout" v-on:click="logout">Log Out</button>
    </div>
    <div class="content-div">
      <div class="daily-posts">
      </div>
      <div class="contacts-list">
        <p class="contacts-title">Your Contacts</p>
        <div class="chat-list">
          <template v-for="contact in contacts">
                  <Contact v-bind:contact=contact v-on:openChat="openChat"></Contact>
          </template>
        </div>

        <div class="chat-box-container">
          <template v-for="liveChat in liveChatsOpened">
            <ChatBox v-on:closeChat="closeChat(liveChat)" v-bind:chats="findChats(liveChat)" v-bind:contact="liveChat" v-on:sendWebSocketMessage="sendWebSocketMessage"></ChatBox>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import ChatBox from "./ChatBox.vue";
import Contact from "./Contact.vue";
import PersonCard from "./PersonCard.vue";
import RequestCard from "./RequestCard.vue";
export default {
  components: {
          'ChatBox': ChatBox,
          'Contact': Contact,
          'PersonCard': PersonCard,
          'RequestCard': RequestCard
      },
  mounted () {
    this.userData.name = this.$store.state.userData.name;
    this.userData.email = this.$store.state.userData.email;
    this.userData.profilePic = this.$store.state.userData.profilePic;
    this.userData.listOfRequestsReceived = this.$store.state.userData.listOfRequestsReceived;
    this.contacts = this.$store.state.contacts;
    this.ws = new WebSocket("ws://localhost:8185/message");
    this.$store.commit('assignWebSocket', this.ws);
    window._this = this;
    this.ws.onopen = function(data){
      console.log("websocket connection opened");
    };
    this.ws.onmessage = (data) => {
      let jsonData = JSON.parse(data.data);
      if (jsonData.topic === "chat") {
        let contactEmail = jsonData.from === this.userData.email ? jsonData.to : jsonData.from;
        let contact = this.contacts.find((contact)=>{
          return contact.userEmail === contactEmail;
        })
        let liveMessage = {};
        liveMessage["sender"] = jsonData.from;
        liveMessage["receiver"] = jsonData.to;
        liveMessage["message"] = jsonData.message;
        this.openChat(contact, liveMessage);
      } else if (jsonData.topic === "friendRequestSent") {
        var request = {
                        userEmail : jsonData.from,
                        userName : jsonData.requestSentFromUserName
                      }
        this.userData.listOfRequestsReceived.unshift(request);
      } else if (jsonData.topic === "friendRequestCancelled") {
        let requestCancelledEmail = jsonData.requestCancelledForUser;
        this.userData.listOfRequestsReceived = this.userData.listOfRequestsReceived.filter((request)=>{
          return request.userEmail != requestCancelledEmail;
        });
        if (this.userData.listOfRequestsReceived.length === 0) {
          this._data.showRequests = false;
        }
      } else if (jsonData.topic === "friendRequestAccepted") {
        let requestAcceptedEmail = jsonData.requestAcceptedForUserEmail;
        this.userData.listOfRequestsReceived = this.userData.listOfRequestsReceived.filter((request)=>{
          return request.userEmail != requestAcceptedEmail;
        });
        if (this.userData.listOfRequestsReceived.length === 0) {
          this._data.showRequests = false;
        }
        this.contacts.push({userEmail: requestAcceptedEmail, userName: jsonData.requestAcceptedForUserName});
      } else if(jsonData.topic === "addContact"){
        this.contacts.push({userEmail: jsonData.requestAcceptedBy, userName: jsonData.requestAcceptedByUserName})
      }else {

      }
    }
  },
  methods: {
    logout(){
      let _this = this;
      this.$http.put('/logout', {}, {}).then(function(data) { //first parameter is address , second parameter is body to be sent and in the third parameter we can send headers
          _this.ws.close();
          _this.$router.push({path:'/login'});
        }).catch(function (err) {
          console.log("User could not be logged out");
        });
      },
    sendSearchRequest() {
      this.$http.get('/searchPerson/' + this._data.searchText, {}, {}).then((result)=>{
        this._data.showDropDown = true;
        this._data.searchResult = result.body;
      }).catch((error)=>{
        console.log("Error occurred while fetching results");
      });
    },
    showNotifications() {
      this.showRequests = this.showRequests === true ? false : true;
    },
    onCardContainerClick(event) {
      event.stopPropagation();
    },
    onUserContainerClick(event) {
      this._data.showDropDown = false;
    },
    openChat(contact, liveMessage) {
      let liveChatOpened = this.liveChatsOpened.find((liveChats)=>{
        return liveChats.userEmail === contact.userEmail;
      });
      if (liveChatOpened === undefined) {
        if (Object.keys(this.chatDataByUserEmail).indexOf(contact.userEmail) === -1) {
          this.fetchMessageHistoryAsynchronously(this.userData.email, contact.userEmail).then((data)=>{
            this.chatDataByUserEmail[contact.userEmail] = {};
            this.chatDataByUserEmail[contact.userEmail].chats = data.body;
            this.liveChatsOpened.push(contact);
            setTimeout(()=>{
              var chatDiv = document.getElementById(contact.userEmail);
              chatDiv.scrollTop = chatDiv.scrollHeight;
            },0);
          }).catch((error)=>{
            console.log("consoling the error", error);
          });
        } else {
          this.liveChatsOpened.push(contact);
          if (liveMessage) {
            this.chatDataByUserEmail[contact.userEmail].chats.push(liveMessage);
          }
          setTimeout(()=>{
            var chatDiv = document.getElementById(contact.userEmail);
            chatDiv.scrollTop = chatDiv.scrollHeight;
          },0);
        }
      } else {
        if (liveMessage) {
          this.chatDataByUserEmail[contact.userEmail].chats.push(liveMessage);
        }
        setTimeout(()=>{
          var chatDiv = document.getElementById(contact.userEmail);
          chatDiv.scrollTop = chatDiv.scrollHeight;
        },0);
      }
    },
    fetchMessageHistoryAsynchronously(from, to) {
      return this.$http.get("/fetchMessageHistory/" + from + "/" + to, {}, {});
    },
    sendWebSocketMessage(data) {
      var payload = {
        from: this.userData.email,
        to: data.userEmail,
        topic: "chat",
        message: data.message
      };
      this.ws.send(JSON.stringify(payload));
    },
    findChats(contact) {
      let chats = this.findChatsByUserEmail(contact.userEmail);
      return chats;
    },
    findChatsByUserEmail(email) {
      let userChatData = this.chatDataByUserEmail[email];
      return userChatData.chats;
    },
    closeChat(contact) {
      var index = this.liveChatsOpened.indexOf(contact);
      if (index > -1) {
        this.liveChatsOpened.splice(index, 1);
      }
    }

  },
  data() {
    return {
      userData : {
        name: "",
        email: "",
        profilePic: "",
        chatInput: "",
        listOfRequestsReceived: []
      },
      client: "",
      destination: "",
      ws: "",
      searchResult: false,
      searchText: "",
      randomPic: "",
      randomEmail: "",
      openChats: [],
      searchResult: [],
      contacts: [],
      showDropDown: false,
      showRequests: false,
      liveChatsOpened: [],
      chatDataByUserEmail: {}
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
  background: #777;
  display: flex;
  box-sizing: border-box;
  flex-grow: 0;
  padding: 10px 10% 10px 10%;
  align-items: center;
  min-height: 80px;
  max-height: 80px;
}

.search {
  position: relative;
}

.search-input-box {
  height: 40px;
  border-radius: 2px;
  border: none;
  outline: none;
  font-size: 15px;
  box-sizing: border-box;
  padding: 10px;
  min-width: 100px;
}

.card-container {
  position: absolute;
  width: 100%;
  background-color: white;
  padding: 5px;
  box-sizing: border-box;
  white-space: nowrap;
  border: solid 1px black;
  overflow: hidden;
  text-overflow: ellipsis;
}

.search-image {
  height: 30px;
  width: 30px;
  margin-left: 10px;
  cursor: pointer;
}

.notification-img{
  height: 30px;
  min-width: 30px;
  position : relative;
  margin-left: 10px;
  cursor: pointer;
  border-radius: 5px;
  position: relative;
}

.haveNotifications {
  background-color: yellow;
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
  padding-right: 10px;
}


.profile-img {
  height: 40px;
  width: 40px;
  border-radius: 10px;
}

.not-found {
  margin: 0px;
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
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
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


.request-container {
  position: absolute;
  border: solid 1px black;
  border-radius: 4px;
  left: 10px;
  padding: 5px;
  background-color: yellow;
  width: 350%;
  box-sizing: border-box;
}

.notification-container {
  position: relative;
  height: 30px;
  width: 30px;
  margin-right: 10px;
}

.contacts-title {
  margin: 0px;
  padding: 5px;
  font-weight: 600;
  text-align: center;
  background-color: darkgrey;
}


.chat-list {
  overflow: scroll;
  box-sizing: border-box;
  max-height: 150px;
  min-height: 150px;
}

.chat-box-container {
  flex-grow: 1;
  overflow: scroll;
}
</style>
