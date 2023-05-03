<template>
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
                <td>{{ Math.abs(result.payCount*100) }}</td>
            </tr>
        </table>
    </div>
</template>
<script>
import axios from 'axios';

export default {
    props : {
 
    },
    data(){
        return{
            detailsResult: [],
        }
    },
    created() {
        this.getDetails()
    },
    computed: {
        amount(){
            if(result.payCount<0){

            }
        }

    },
    methods: {
        getDetails(){
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
    .point-details{
        margin: auto;
        width: 80%;
        height: 60%;
        display: flex;
        justify-content: center;
        touch-action: none;
        position: relative;
        border: 1px solid gray;
        overflow-y: scroll;
    }
    table th{
        background-color: gray;
        border: 1px solid darkgray;
        color: white;
        position: sticky;
        top: 0;
    }
    table td{
        border: 1px solid darkgray;
    }
    table{
        width: 90%;
        margin-bottom: 1%;
    }

</style>