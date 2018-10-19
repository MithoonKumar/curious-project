<template>
  <div class="contact">{{name}}</div>
</template>

<script>
  export default {
    props: ['name']

  }
</script>

<style scoped>
  .contact {
    text-align: center;
    cursor: pointer;
  }
  .contact:hover {
    background: rgb(230, 230, 230);
  }

</style>
