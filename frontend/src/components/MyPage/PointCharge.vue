<template>
    <div class="point-package">
      <ul>
        <li>
          <button @click="pointCharge(1)">엽전 1개</button>
        </li>
        <li>
          <button @click="pointCharge(10)">엽전 10개</button>
        </li>
        <li>
          <button @click="pointCharge(50)">엽전 50개</button>
        </li>
      </ul>
      <ul>
        <li>
          <button @click="pointCharge(100)">엽전 100개</button>
        </li>
        <li>
          <button @click="pointCharge(500)">엽전 500개</button>
        </li>
        <li>
          <button @click="pointCharge(1000)">엽전 1000개</button>
        </li>
      </ul>
    </div>
</template>
<script>
import axios from "axios";

export default {
    props : {
 
    },
    data(){
        return{
          kakaopayUrl: "",
        }
    },
    methods: {
        pointCharge(amount){
          axios.post("/api/point/chargeSet", {
            chargePoint: amount,
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
        }

    },
}

</script>
<style scoped>
  .point-package{
    margin: auto;
    width: 80%;
    height: 60%;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    touch-action: none;
    position: relative;
    border: 1px solid gray;
  }
  .point-package button{
    width: 150px;
    height: 40px;
  }
  .point-package li{
    list-style:none;
    margin:7px;
  }

  


</style>