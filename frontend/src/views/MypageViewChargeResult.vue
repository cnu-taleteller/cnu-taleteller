<template>
    <div>
        <h2>엽전 충전 완료</h2>
    </div>
</template>
<script>
import axios from 'axios';

export default {
    props : {
 
    },
    data(){
        return{
        }
    },
    created() {
        this.pointCharge()
    },
    computed: {
    },
    methods: {
        pointCharge(){
          axios.post("/api/point/chargeExecution")
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

</style>