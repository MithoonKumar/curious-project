<template>
    <p v-bind:class="{left: chat.sender === this.$store.state.userData.email, right: chat.sender != this.$store.state.userData.email}">{{chat.message}}</p>
</template>
<script>
  export default {
    props: ['chat'],
    mounted() {
    }
  }

</script>

<style>
  .right {
    text-align: right;
    color: green;
    margin: 3px;
    background-color: gainsboro;
    padding: 0px 10px;
    box-sizing: border-box;
  }
  .left {
    text-align: left;
    color: blue;
    margin: 3px;
    background-color: gainsboro;
    padding: 0px 10px;
    box-sizing: border-box;
  }
</style>
