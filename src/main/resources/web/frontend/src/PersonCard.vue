<template>
  <div class="card">
    <p class="person-name">{{person.userName}}</p>
    <button v-if="person.relationStatus === relationStatus.notFriend" class="add-friend-button" v-on:click="sendRequest">Add Friend</button>
    <p v-else-if="person.relationStatus === relationStatus.friendRequestSent" class="relation-text" v-else>Request Sent</p>
    <p v-else-if="person.relationStatus === relationStatus.friendRequestReceived" class="relation-text" v-else>Request Received</p>
    <p v-else class="relation-text">Friend</p>
  </div>
</template>

<script>
  import chatBox from "./chat-box.vue";
  export default {
    props: ['person'],
    methods: {
      sendRequest(){
      //console.log("clicked on add friend", this._props.person.friendRequestSent === true ? "cancel" : "add" );
        this.$http.post("/" + this._props.person.friendRequestSent === true ? "cancel" : "add"  + 'FriendRequest/' + this._props.person.userEmail, {}, {}).then((result)=>{
          this._props.person.relationStatus = this._data.relationStatus.friendRequestSent;
        }).catch((error)=>{
          console.log("Error occurred while processing request to add, cancel and accept friend requests");
        });
      }
    },
    mounted(){
    },
    data() {
      return {
        relationStatus: {
          friend: 1,
          friendRequestSent: 2,
          friendRequestReceived: 3,
          notFriend: 4
        }
      };
    }

  }
</script>

<style scoped>
.card {
  background-color: white;
}
.relation-text {
  margin: 0px;
  font-size: 10px;
}

.person-name {
  margin: 0px;
}

</style>
