<template>
  <div class="admin">
    <nav>
      <button class="nav-button" :class="{ active: activeComponent === 'ManagementMember' }" @click="showMemberManagement">회원 관리</button>
      <button class="nav-button" :class="{ active: activeComponent === 'ManagementBook' }" @click="showBookManagement">작품 관리</button>
    </nav>
    <component :is="activeComponent"></component>
  </div>
</template>

<script>
import ManagementBook from '../components/ManagementBook.vue';
import ManagementMember from '../components/ManagementMember.vue';

export default {
  components: {
    ManagementBook,
    ManagementMember,
  },
  data() {
    return {
      activeComponent: 'ManagementMember',
    };
  },
  created() {
    if(!sessionStorage.getItem('user')) {
      alert('비정상적인 접근입니다.');
      this.$router.push("/");
    }
  },
  methods: {
    showMemberManagement() {
      this.activeComponent = 'ManagementMember';
    },
    showBookManagement() {
      this.activeComponent = 'ManagementBook';
    },
  },
}
</script>

<style scoped>
.admin {
  max-width: 100%;
  padding: 20px;
}

nav {
  display: flex;
  justify-content: center;
  padding: 10px;
  font-size: 20px;
  font-weight: bold;
  background-color: transparent;
}

.nav-button {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 15px;
  border: none;
  border-radius: 10px;
  flex: 1;
  text-align: center;
  cursor: pointer;
}

.active {
  background-color: #4AAEE2;
  color: white;
}
</style>