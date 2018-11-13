<template>
  <div class="request-card">
    <p class="user-name">{{person.userName}}</p>
    <div style="display: flex">
      <button style="flex-grow: 1" v-on:click="acceptFriendRequest">Accept</button>
      <button style="flex-grow: 1" v-on:click="cancelFriendRequest">Cancel</button>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['person'],
    mounted() {
      //console.log("The component has been mounted");
    },
    methods: {
      acceptFriendRequest() {
        //console.log("clicked on accept friend request");
        this.$http.post("/acceptFriendRequest/" + this._props.person.userEmail, {}, {},).then(()=>{
          //console.log("successfully added friend");
        }).catch(()=>{
          console.log("request failed");
        });
      },
      cancelFriendRequest() {
        //console.log("clicked on cancel friend request");
        this.$http.post("/cancelFriendRequest/" + this._props.person.userEmail, {}, {},).then(()=>{
          //console.log("successfully cancelled friend request");
        }).catch(()=>{
          console.log("request failed");
        });
      }
    }
  }
</script>

<style scoped>
  .user-name {
    margin: 0;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .request-card {

  }
</style>
