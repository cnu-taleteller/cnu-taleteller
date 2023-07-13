<template>
  <div class="point-package-form">
    <p>충전할 엽전 갯수를 선택해주세요!</p>
    <div class="point-package">
      <ul>
        <li>
          <button @click="pointCharge('간편결제', 1)">엽전 1개</button>
        </li>
        <li>
          <button @click="pointCharge('간편결제', 10)">엽전 10개</button>
        </li>
        <li>
          <button @click="pointCharge('간편결제', 50)">엽전 50개</button>
        </li>
        <li>
          <button @click="tossPayments('카드', 100)">카드결제</button>
        </li>
      </ul>
      <ul>
        <li>
          <button @click="pointCharge('간편결제', 100)">엽전 100개</button>
        </li>
        <li>
          <button @click="pointCharge('간편결제', 500)">엽전 500개</button>
        </li>
        <li>
          <button @click="pointCharge('간편결제', 1000)">엽전 1000개</button>
        </li>
        <li>
          <button @click="tossPayments('가상계좌', 100)">무통장입금</button>
        </li>
      </ul>
      <form class="charge-form">
        <select v-model="chargeType">
          <option value="간편결제">카카오페이</option>
          <option value="카드">토스페이-카드</option>
          <option value="가상계좌">토스페이-무통장</option>
        </select>
        <div>
          <input type="number" v-model="chargePoint" />
          <button type="submit" @click="charge">충전</button>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
import axios from "axios"
import { loadTossPayments } from '@tosspayments/payment-sdk'

export default {
  props: {

  },
  data() {
    return {
      kakaopayUrl: "",
      clientKey: process.env.VUE_APP_CHARGE_KEY,
      chargeType: "간편결제",
      chargePoint: null,
    }
  },
  methods: {
    pointCharge(method, amount) {
      this.memberEmail = sessionStorage.getItem('user')
      axios.post(`${process.env.VUE_APP_API_PATH}/api/point/chargeSet/${this.memberEmail}`, {
        chargePoint: amount,
        paymentMethod: method,
      })
        .then((res) => {
          this.kakaopayUrl = res.data;
          console.log(this.kakaopayUrl);
          //window.location.href = `${this.kakaopayUrl}`;
          window.open(this.kakaopayUrl);

        })
        .catch((err) => {
          console.log(err);
        });
    },

    tossPayments(method, amount) {
      this.memberEmail = sessionStorage.getItem('user')
      axios.post(`${process.env.VUE_APP_API_PATH}/api/point/chargeSet/${this.memberEmail}`, {
        chargePoint: amount,
        paymentMethod: method,
      });

      loadTossPayments(this.clientKey).then(tossPayments => {
        tossPayments.requestPayment(method, {
          amount: amount*100,
          orderId: 'xnDIqpt7Dlfdtd99WwXgu',
          orderName: '엽전 충전',
          customerName: '회원',
          successUrl: window.location.origin + '/mypage/chargeResult',
          failUrl: window.location.origin + '/mypage/chargeResult',
          
          cardNumber: '4330123412341234',
        })
          .catch(function (error) {
            if (error.code === 'USER_CANCEL') {
              // 결제 고객이 결제창을 닫았을 때 에러 처리
            } else if (error.code === 'INVALID_CARD_COMPANY') {
              // 유효하지 않은 카드 코드에 대한 에러 처리
            }
          });
      });
    },

    charge(event) {
      event.preventDefault();

        if (this.chargePoint === null) {
          alert("충전할 엽전 개수를 입력해주세요.");
        } else if(this.chargeType == "간편결제") {
          this.pointCharge(this.chargeType,this.chargePoint);
        } else if(this.chargeType == "카드") {
          this.tossPayments(this.chargeType, this.chargePoint);
        } else if(this.chargeType == "가상계좌") {
          this.tossPayments(this.chargeType, this.chargePoint);
        }
         
      },

  },
}

</script>
<style scoped>
.point-package-form {
  width: 100%;
  height: 50vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.point-package {
  width: 600px;
  height: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 10px;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, .1), 0 0 0 1px #ddd;
}

.point-package button {
  width: 150px;
  height: 100%;
  background-color: gray;
  border-width: 0;
  color: white;
  border-radius: 5px;
  font-weight: bold;
  font-size: 20px;
  padding: 10%;

}

.point-package button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #ccc;
  border-radius: 3px;
  border: none;
  color: rgb(58, 58, 58);
  transition: opacity 0.3s ease-in-out;
}

.point-package ul {
  border: none;
  margin-top: 20px;
}

button:hover {
  opacity: 0.7;
}

.point-package li {
  list-style: none;
  margin: 7px;
}

.charge-form {
  padding-right: 15px;
}

.charge-form input {
  margin: 10px;
  width: 150px;
}

.charge-form input,select {
  border-radius: 5px;
  border-width: 2px;
  border-style: solid;
  border-color: #bbbbbb;
}
</style>