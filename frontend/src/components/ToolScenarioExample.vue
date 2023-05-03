<template>
    <div class="example-scenario">
        <h4>예제1</h4>
        <div class="example-input">
            누가: {{ example1.who }}<br>
            언제: {{ example1.when }}<br>
            어디서: {{ example1.where }}<br>
            사건: {{ example1.event }}<br>
            <hr>
            {{ example1.story1 }}<br><br>
            {{ example1.story2 }}<br><br>
            {{ example1.story3 }}<br><br>
            {{ example1.story4 }}
        </div>
    </div>
</template>
<script>
export default {
    data(){
        return {
            example1: {
                who: '철수가',
                when: '5살때',
                where: '유치원 놀이터에서',
                event: '친구들과 미끄럼틀을 타다가 외계인에게 납치되었지만 외계인과 친구가 되어 무사히 살아 돌아온 이야기',
                story1: '[도입] 어린 철수는 유치원에서 친구들과 함께 놀이터에서 즐거운 시간을 보내고 있었다. 그러던 그 날, 철수와 친구들은 놀이터를 한 바퀴 돌았을 때 갑자기 들떴다. 그 대신, 외계인들이 놀이터에 찾아와 철수를 납치해 버렸다. 복잡한 우주선 안에서 사라진 철수는 꽤나 두려워하고 있었지만, 그렇게 몇 시간 후, 철수는 친구로서 이러한 외계인을 만났다는 것을 알게 되었다.',
                story2: '[전개] 외계인들은 철수에 대해 매우 친근하게 대했고, 그들은 철수를 그들의 우주선 안에서 놀이터에서 했던 것과 비슷한 게임들을 즐기게 해주었다. 그러나 예상치 못한 일이 벌어졌다. 외계인들이 어떠한 이유로 인해 지구로 되돌아 가야 했을 때, 철수는 자신이 다시 지구로 돌아가지 못할지두려워했다. 그러나 그들은 철수의 안전과 행복을 보장하는 약속을 했다.',
                story3: '[위기] 그럼에도 불구하고, 돌아가는 길은 위험했다. 작은 호기심 때문에 철수는 외계인들이 가지고 있는 "위험한 방향"으로 가고 말았다. 그들은 치명적인 괴물에게 공격을 받았다. 외계인들은 안전한 곳으로 철수를 끌고가기 위해 발로 해골을 공격해 괴물을 물리쳤다.',
                story4: '[결말] 미션을 성공적으로 마친 뒤, 철수는 모든것이 끝나고 친구들을 다시 만날 기회를 가졌다. 이제 한쪽은 지구, 한쪽은 외계인의 우주선 안에서의 경험을 한 철수는 이전보다 훨씬 열린 마음으로 살아갈 수 있었다. 철수는 우주선 안에서의 그들의 친구와 놀이터에서의 친구들을 사랑하고, 이 두 가지 간의 깊은 연결이 철수의 인생에 있는 매우 의미 있는 부분이 되었다.'
            }
        }
        
    }
}
</script>
<style scoped>
.example-scenario{
    width: 100%;
    border-left: 2px solid #aaaaaa;
    padding-left: 30px;
}
.example-input{
    height: 500px;
    overflow-y: scroll;
}

</style>