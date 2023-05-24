<template>
  <div class="point-details-form">
    <p>현재 회원님의 엽전 거래내역입니다.</p>
    <div class="point-details">
      <table>
        <thead>
          <th>구분</th>
          <th>일자</th>
          <th>수량</th>
          <th>결제수단</th>
          <th>금액</th>
        </thead>
        <tr v-for="result in detailsResult" :key="result.payId">
          <td>{{ result.paySort }}</td>
          <td>{{ result.payDate }}</td>
          <td>{{ result.payCount }}</td>
          <td>{{ result.payType }}</td>
          <td>{{ Math.abs(result.payCount * 100) }}</td>
        </tr>
      </table>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  props: {

  },
  data() {
    return {
      detailsResult: [],
    }
  },
  created() {
    this.getDetails()
  },
  computed: {

  },
  methods: {
    getDetails() {
      axios.get("/api/point/details")
        .then((res) => {
          console.log(res);
          this.detailsResult = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
}

</script>
<style scoped>
.point-details-form {
  width: 100%;
  height: 50vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.point-details {
  overflow-y: scroll;
  font-size: 20px;
  width: 70%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 10px;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, .1), 0 0 0 1px #ddd;
}

table th {
  background-color: gray;
  border: 1px solid darkgray;
  color: white;
  position: sticky;
  top: 0;
}

table td {
  border: 1px solid darkgray;
}

table {
  width: 90%;
  margin-bottom: 1%;
}
</style>