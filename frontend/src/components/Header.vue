<template>
    <header>
        <div class="bar">
        <h1 id="logo"><a href="/" class="log"><img src="@/assets/logo.png"></a></h1>
            <div class="search">
            </div>
      </div>
    </header>
</template>

<script>
export default {}
</script>

<style>
#logo img{
  width: 40%;
  height: auto;
}
.bar {
    display: flex;
    align-items: center;
  }

  .bar input {
    width: 200px;
    height: 30px;
    border: none;
    border-radius: 20px;
    padding: 0 10px;
    margin-right: 10px;
  }

  .bar button {
    height: 30px;
    border: none;
    border-radius: 20px;
    background-color: #bdbdbee4;
    color: #ffffff;
    padding: 0 20px;
  }
</style>