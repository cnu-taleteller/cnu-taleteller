<template>
    <div>
        <h4>시나리오</h4>
        <div>
            <p>키워드를 입력하세요</p>
            <p>언제: <input type="text" v-model="scenario.when" placeholder="주말 아침에" ></p>
            <p>어디서: <input type="text" placeholder="숲에서"></p>
            <p>사건: <input type="text" placeholder="외계인을 만나 당황했지만 재밌게 노는 어린이 이야기"></p>
            <button>시나리오 받아보기</button>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return{
            scenario: {
                when: null,
                where: null,
                event: null
            }
        }
    }
}
</script>