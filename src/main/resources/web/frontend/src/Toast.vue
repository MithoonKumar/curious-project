<template>
  <p class="toast">{{message}}</p>
</template>

<script>
  export default {
    props: ['message']
  }
</script>

<style scoped>
  .toast{
    text-align: center;
    background: lightyellow;
    display: inline-block;
    border-radius: 10px;
    position: absolute;
    padding: 10px;
    bottom: 100px;
    transform: translateX(-50%);
    left: 50%;
  }

</style>
