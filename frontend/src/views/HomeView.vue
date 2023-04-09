<template>
  <div>
    homeview
    <button @click="test">연동 확인</button>
  </div>
</template>

<script>
import axios from "axios"

export default { 
  methods:{
    test(){
      axios.post("/api/users/test", {
        data: "test"
      })
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
            });
    }
  }

}
</script>
