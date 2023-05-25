<template>
  <div id="app">
    <Header v-if="ShowHeader"></Header>
    <SearchHeader v-if="ShowSearchHeader"></SearchHeader>
    <router-view/>
  </div>
</template>

<script>
import Header from './components/Header.vue';
import SearchHeader from './components/SearchHeader.vue';

export default {
  name: 'App',
  components: {
    Header,
    SearchHeader
  },
  computed: {
    ShowSearchHeader() {
      return this.$route.path != '/tool' &&  this.$route.path !== '/preview' && this.$route.path !== '/keyword';
    },
    ShowHeader() {
      return this.$route.path !== '/preview' && this.$route.path !== '/keyword';
    }
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Sans+KR&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap');

#app {
  font-family: 'IBM Plex Sans KR', Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.wrap .tab {
  font-family: 'Do Hyeon', sans-serif;
}
</style>
