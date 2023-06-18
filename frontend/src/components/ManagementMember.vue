<template>
    <div class="management-member">
      <table>
        <thead>
        <tr>
          <th>회원번호</th>
          <th>이름</th>
          <th>이메일</th>
          <th>전화번호</th>
          <th>상태</th>
          <th>정지</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="member in members" :key="member.memberId">
          <td>{{ member.memberId }}</td>
          <td>{{ member.memberName }}</td>
          <td>{{ member.memberEmail }}</td>
          <td>{{ member.memberPhone }}</td>
          <td v-if="member.memberAuth !== 'Suspended'">회원</td>
          <td v-else>정지</td>
          <td>
            <button v-if="member.memberAuth !== 'Suspended'" class="suspend-button" @click="suspendUser(member.memberId)">정지</button>
            <button v-if="member.memberAuth === 'Suspended'" class="release-button" @click="suspendUser(member.memberId)">해제</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      members: []
    };
  },
  mounted() {
    this.fetchMembers();
  },
  methods: {
    fetchMembers() {
      axios
          .get("/api/admin/member")
          .then((response) => {
            this.members = response.data;
            console.log(response.data);
          })
          .catch(error => {
            console.error(error);
          });
    },
    suspendUser(memberId) {
      const member = this.members.find((m) => m.memberId === memberId);
      if (member) {
        if (member.memberAuth === "User") {
          if (confirm("해당 회원을 활동 정지시키겠습니까?")) {
            axios
                .post(`/api/admin/member/suspend/${memberId}`, {
                  memberAuth: "Suspended",
                })
                .then((response) => {
                  const updatedAuth = response.data;
                  const index = this.members.findIndex(
                      (m) => m.memberId === updatedAuth.memberId
                  );
                  if (index !== -1) {
                    this.members.splice(index, 1, updatedAuth);
                  }
                })
                .catch((error) => {
                  console.error(error);
                });
          }
        } else {
          if (confirm("활동 정지를 해제하시겠습니까?")) {
            axios
                .post(`/api/admin/member/suspend/${memberId}`, {
                  memberAuth: "User",
                })
                .then((response) => {
                  const updatedAuth = response.data;
                  const index = this.members.findIndex(
                      (m) => m.memberId === updatedAuth.memberId
                  );
                  if (index !== -1) {
                    this.members.splice(index, 1, updatedAuth);
                  }
                })
                .catch((error) => {
                  console.error(error);
                });
          }
        }
      }
    },
  },
};
</script>

<style scoped>
.management-member {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 100vh;
}

table {
  width: 100%;
  border-collapse: collapse;
  border: none;
  margin-top: 20px;
}

thead th {
  background-color: #f0f0f0;
  font-weight: bold;
  border: none;
}

th, td {
  padding: 10px;
  text-align: center;
  border: none;
}

tbody tr:nth-child(even) {
  background-color: #f5f5f5;
}

.suspend-button, .release-button {
  padding: 8px 12px;
  font-size: 14px;
}

.suspend-button {
  background-color: #d32f2f;
  color: white;
  border: none;
  border-radius: 5px;
}

.release-button {
  background-color: #43a047;
  color: white;
  border: none;
  border-radius: 5px;
}
</style>
