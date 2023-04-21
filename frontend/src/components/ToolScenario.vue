<template>
    <div class="scenario-form">
        <div class="scenario-input">
            <h4>시나리오</h4>
            <p>키워드를 입력하세요.</p>
            <p>사건은 구체적이게 적을수록 좋습니다!</p>
            <p>누가: <input type="text" v-model="scenario.who" placeholder="짱구가" ></p>
            <p>언제: <input type="text" v-model="scenario.when" placeholder="주말 아침에" ></p>
            <p>어디서: <input type="text"  v-model="scenario.where" placeholder="숲에서"></p>
            <p>사건: <input type="text"  v-model="scenario.event"  placeholder="외계인을 만나 당황했지만 재밌게 노는 어린이 이야기"></p>
            <button @click="setScenario()">시나리오 받아보기</button>
        </div>
        <div class="scenario-content">
            <p>{{ resultScenario }}</p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data(){
        return{
            API_KEY : '',
            scenario: {
                who: null,
                when: null,
                where: null,
                event: null
            },
            resultScenario: null
        }
    },
    methods: {
        setScenario(){
            axios.post("https://api.openai.com/v1/chat/completions",
                {
                    "model": "gpt-3.5-turbo",
                    "messages": [{
                        "role": "user",
                        "content": `누가: ${this.scenario.who},
                                    언제: ${this.scenario.when}, 
                                    어디서: ${this.scenario.where},
                                    사건: ${this.scenario.event}
                                    라는 내용을 가진 동화책을 '도입/전개/위기/결말' 로 나눠서 써줘.
                                    내용을 나눌 때 형식은 
                                    [도입] 내용
                                    [전개] 내용
                                    [위기] 내용
                                    [결말] 내용 
                                    형식으로 나눠서 700자 이내로 써줘`}],
                },
                {
                headers: { 
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${this.API_KEY}`,   
                }}
            )
            .then((res) => {
                console.log(res.data.choices[0].message.content);
                this.resultScenario = res.data.choices[0].message.content;
                sessionStorage.setItem('scenario',  this.resultScenario);
            })
            .catch((err) => {
                console.log(err);
            })
        }
    }
}
</script>
<style>
.scenario-form {
    display: flex;
    width: 100%;
}
.scenario-input {
    width: 40%;
}

.scenario-content {
    width: 60%;
}

</style>