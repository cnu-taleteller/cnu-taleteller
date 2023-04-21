<template>
    <div class="point-package">
        <h1>충전 패키지</h1>
        <button @click="payTest()">충전 테스트</button>
    </div>
</template>
<script>
import axios from "axios";

export default {
    props : {
 
    },
    data(){
        return{
        }
    },
    methods: {
        async payTest(){
          await axios.post("/api/point/charge", {
                point: 20,
          })
          .then((res) => {
            console.log(res);
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
    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    touch-action: none;
    position: relative;
    border: 1px solid gray;
}

</style>