<template>
  <img class="loading-gif" src="./assets/tenor.gif">
</template>

<script>
export default {
  mounted () {
    this.$http.post('/home', {}, {}).then(function(data) { //first parameter is address , second parameter is body to be sent and in the third parameter we can send headers
      this.$store.commit('changeUserData', data.body);
      this.$router.push({path:'/user'});
    }).catch(function (err) {
      console.log("could not get any user");
      this.$router.push({path:'/login'});
    });
  }
}
</script>

<style scoped>
.loading-gif {
  height:320px;
  width: 480px;
  margin-left: calc((100% - 480px)/ 2);
  margin-top: 200px;
}
</style>
