<template>
  <div class="contact" v-on:click="openChat">{{contact.userName}}</div>
</template>

<script>
  export default {
    props: ['contact'],
    mounted() {
    },
    methods: {
      openChat() {
        //console.log("clicked on a chat name");
        this.$emit("openChat", this.contact);
      }
    }

  }
</script>

<style scoped>
  .contact {
    text-align: center;
    cursor: pointer;
    background-color: lightgray;
    margin-bottom: 2px;
  }
  .contact:hover {
    background: rgb(230, 230, 230);
  }

</style>
