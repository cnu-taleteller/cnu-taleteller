<template>
    <div class="chargeResult-all">
        <div class="chargeResult-main">
          <ul v-for="result in paymentResult" :key="result.payId">
            <li>{{ result.paySort }} 완료</li>
            <li>일자 : {{ result.payDate.replace('T',' ') }}</li>
            <li>수량 : {{ result.payCount }}개</li>
            <li>결제수단 : {{ result.payType }}</li>
            <li>금액 : {{ Math.abs(result.payCount*100) }}원</li>
          </ul>
        </div>
        <div class="chargeResult-btn">
          <button @click="$router.push('/')">홈으로</button>
          <button @click="$router.push('/mypage/pointmanage')">마이페이지</button>
        </div>
    </div>
</template>
<script>
import router from '@/router';
import axios from 'axios';

export default {
    props : {
 
    },
    data(){
        return{
          paymentResult: [],
        }
    },
    created() {
      this.pointCharge()
    },
    computed: {
    },
    methods: {
        pointCharge(){
          axios.get(`${process.env.VUE_APP_API_PATH}/api/point/chargeExecution`)
          .then((res) => {
            console.log(res);
            this.paymentResult = res.data;
          })
          .catch((err) => {
            console.log(err);
          });
        }
    },
}

</script>
<style scoped>
  .chargeResult-all{
    width: 40%;
    height: 100%;
    /* background-color: gainsboro; */
    margin:auto;
    border-radius: 25px;
    border: 2px solid #ccc;
    margin-top: 20px;
    /* padding: 3%; */
  }
  .chargeResult-all ul{
    margin-right: 5%;
  }
  .chargeResult-all li{
    height:75px;
    list-style:none;
    font-weight:bold;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .chargeResult-main{
    width: 100%;
    height: 450px;
    background:rgba(235, 235, 235, 0.6); 
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 25px;
    padding-top: 7%;
    padding-bottom: 7%;
    margin-bottom: 5%;
  }
  .chargeResult-main li:nth-child(1){
    font-size: 25px;
    font-weight: bold;
    width: 90%;
    margin: auto;
    margin-bottom: 5%;
  }
  .chargeResult-btn{
    width: 50%;
    overflow: hidden;
    display: flex;
    margin: auto;

  }
  .chargeResult-btn button{
    width:100%;
    height: 50px;
    margin: 5%;
    border-radius: 3px;
    background-color:#ccc;
    border: none;
    font-weight: bold;
  }

  .chargeResult-btn button:hover{
    opacity: 0.7;
    /* color:black; */
    /* background-color: yellow; */
  }


</style>