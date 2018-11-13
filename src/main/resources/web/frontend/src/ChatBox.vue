<template>
  <div class="chat-box">
    <div class="chat-info">
      <p class="chat-name">{{this.contact.userName}} </p>
      <button class="close-button" v-on:click="closeChat">x</button>
    </div>

    <div v-bind:id="contact.userEmail" class="chats">
      <template v-for="chat in userChats">
        <Chat v-bind:chat="chat"></Chat>
      </template>
    </div>
    <input class="input-message" v-model:message="message" v-on:keyup.enter="submit">
  </div>
</template>

<script>
  import Chat from "./Chat.vue";
  export default {
    components: {
      'Chat': Chat
    },
    props: ['contact', 'chats'],
    mounted() {
    },
    data() {
      return {
        userChats: this._props.chats,
        message: ""
      };
    },
    methods: {
      submit() {
        this.$emit("sendWebSocketMessage", {message: this.message, userEmail: this.contact.userEmail});
        this.message = "";
      },
      closeChat() {
        this.$emit("closeChat");
      }
    }
  }
</script>

<style>
  .chat-box {
    border-radius: 3px;
    width: 100%;
    height: 30%;
  }

  .chat-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin: 0px;
    padding: 5px;
    background-color: lightsteelblue;
    font-size: 15px;
    font-weight: 600;
    flex-grow: 1;
  }
  .chats {
    height: 200px;
    overflow: scroll;
    background-color: aliceblue;
  }

  .input-message {
    width: 100%;
    outline: none;
    font-size: 13px;
    padding: 3px;
    box-sizing: border-box;
  }

  .chat-info {
    display: flex;
  }

  .close-button {
    cursor: pointer;
  }
</style>
