<template>
    <div class="chargeResult-all">
        <div class="chargeResult-main">
          <ul v-for="result in paymentResult" :key="result.payId">
            <li>{{ result.paySort }} 완료</li>
            <li>일자 : {{ result.payDate }}</li>
            <li>수량 : {{ result.payCount }}개</li>
            <li>결제수단 : {{ result.payType }}</li>
            <li>금액 : {{ Math.abs(result.payCount*100) }}원</li>
          </ul>
        </div>
        <div class="chargeResult-btn">
          <button @click="$router.push('/')">홈으로</button>
          <button @click="$router.push('/mypage/pointmanage')">충전하기</button>
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
          axios.get("/api/point/chargeExecution")
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
    /* display: flex;
    align-items: center;
    justify-content: center; */
    /* border-radius: 25px; */
    border: 2px solid #ccc;
    margin-top: 20px;
    /* padding: 3%; */
   
    /* margin-bottom: 2%;  */
  }
  .chargeResult-all ul{
    margin-right: 5%;
  }
  .chargeResult-all li{
    /* font-size:25px; */
    height:75px;
    list-style:none;
    /* font-weight:bold; */
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .chargeResult-main{
    width: 100%;
    height: 400px;
    background:rgba(235, 235, 235, 0.6); 
    /* overflow: hidden; */
    display: flex;
    align-items: center;
    justify-content: center;
    
    /* border-radius: 25px;
    padding-top: 7%;
    padding-bottom: 1%;
    margin-bottom: 5%; */
  }
  .chargeResult-main li:nth-child(1){
    font-size: 20px;
    font-weight: bold;
    width: 90%;
    /* margin: auto; */
    /* background-color: white; */
    /* border-radius: 20px; */
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
    /* font-size: 22px; */
    border-radius: 3px;
    background-color:#ccc;
    border: none;
    /* color:white; */
    /* font-weight: bold; */
  }

  .chargeResult-btn button:hover{
    opacity: 0.7;
    /* color:black; */
    /* background-color: yellow; */
  }


</style>