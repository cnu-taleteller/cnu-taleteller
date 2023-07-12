<template>
    <div class="content-error">
      <strong class="tit-error">권한이 없거나 존재하지 않는<br>페이지입니다.</strong>
      <p class="desc-error">문의사항이 있으시면 고객센터로 연락주세요.</p>
      <div class="wrap-btn">
        <a class="btn-basic" href="javascript:window.history.back();">이전화면</a>
      </div>
    </div>
</template>

<style scoped>
.content-error {
    height: 75vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
}
.tit-error {
    font-size: 32px;
    line-height: 40px;
}
.content-error .btn-basic {
    width: 162px;
}
.wrap-btn .btn-basic {
    margin: 0 4px;
    vertical-align: middle;
    text-align: center;
}
.btn-basic {
    display: inline-block;
    min-width: 120px;
    height: 40px;
    padding: 0 23px;
    border-radius: 20px;
    border: 1px solid #e0e0e0;
    font-size: 13px;
    color: #333;
    line-height: 38px;
    box-sizing: border-box;
    background-color: #fff;
    -webkit-transition: background-color .1s linear,border-color .1s linear;
    -moz-transition: background-color .1s linear,border-color .1s linear;
    transition: background-color .1s linear,border-color .1s linear;
}
</style>