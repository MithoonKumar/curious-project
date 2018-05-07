<template>
  <div class="container">
    <div class="form-container">
      <form action="">
        <p class="text">First name</p>
        <input class="input-box" type="text" name="firstName">
        <p class="text">Last name</p>
        <input class="input-box" type="text" name="lastName">
        <p class="text">Email</p>
        <input class="input-box" type="email" name="emailId">
        <p class="text">Password</p>
        <input class="input-box" type="password" name="password">
        <input class="submit-button" type="button" value="Agree & Join">
      </form>
    </div>
  </div>
</template>

<script>
export default {
}
</script>

<style scoped>
.container {
  background: -webkit-linear-gradient(45deg, #0077b5,#008891);
  height: 100%;
  width: 100%;
}
.form-container {
  padding: 40px;
  height: 400px;
  width: 400px;
  background-color:#EEE;
  margin-left: calc((100% - 400px)/ 2);
  margin-top: 100px;
  box-sizing: border-box;
  display: inline-block;
  border-radius: 4px;
}
.text {
  margin: 0px;
  font-size: 12px;
  color: #66696a;
}
.input-box {
  font-size: 16px;
  font-weight: normal;
  line-height: 18px;
  color: #434649;
  padding-left: 6px;
  padding-right: 0px;
  padding-top: 0px;
  padding-bottom: 0px;
  border-radius: 2px;
  -webkit-transition: border-color 300ms ease-in-out;
  background: #FFF;
  border: 1px solid #BFBFBF;
  height: 32px;
  outline: 0;
  box-sizing: border-box;
  width: 100%;
  margin-bottom: 10px;
}
.submit-button {
  font-weight: bold;
  border-width: 1px;
  border-style: solid;
  cursor: pointer;
  border-radius: 3px;
  padding: 0 15px;
  line-height: 32px;
  box-sizing: border-box;
  font-size: 16px;
  color: #fff;
  background-color: #287bbc;
  border-color: #1b5480;
  background-image: -webkit-linear-gradient(top, #287bbc 0%,#23639a 100%);
  margin-top: 15px;
  width: 100%;
  outline: 0px;
}
.submit-button:active {
  background-image: -webkit-linear-gradient(top, #287bbc 100%,#23639a 100%);
}
</style>
