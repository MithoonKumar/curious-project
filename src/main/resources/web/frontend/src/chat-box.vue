<template>
  <div class="chat-box">
    <div class="message-container"></div>
    <input class="message-box" placeholder="Enter Message" v-on:keyup.enter="sendMessage($event)" v-model="chatInput"></input>
  </div>
</template>

<script>
export default {
  methods :{
    sendMessage(event){
      this.destination = event.currentTarget.parentElement.id;
      var message = {userEmail: this.$store.state.userData.email, "messageType": "message", "destinationEmail":this.destination, message: this.chatInput};
      this.$store.state.userData.webSocket.send(JSON.stringify(message));
      var chatContainer = event.currentTarget.parentElement.firstChild;
      var pText = document.createElement("p");
      pText.textContent = this.$store.state.userData.email + ":" + this.chatInput;
      chatContainer.appendChild(pText);
      chatContainer.scrollTop = chatContainer.scrollHeight;
      this.chatInput = "";
    }
  },
  data(){
    return {
      chatInput: "",
      destination: ""
    }
  }
}
</script>

<style scoped>
.chat-box {
    display: inline-block!important;
    border: solid 1px black;
    box-sizing: border-box;
    margin: 10px;
}

.message-container {
  width: 200px!important;
  height: 150px;
  box-sizing: border-box;
  font-size: 15px;
  width: 100%;
  box-sizing: border-box;
  overflow-y: overlay;
  word-wrap: break-word;
  padding: 10px;
}

.message-box {
  width: 200px;
  box-sizing: border-box;
  outline: none;
  border-radius: 10px;
}
</style>
