<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1조 홈페이지</title>

<style>
.menuBoxGroup{
	width: calc(200px*5 + 1*10);
	height: auto;
	margin-left: calc((100% - 1050px )/2);
}


.menuBoxGroup>li {
	width: 200px;
	height: 200px;
	display: inline-block;
	cursor: pointer;
}

.menuBoxGroup > li:nth-child(1){
	background-color:#24315b; 

}
.menuBoxGroup > li:nth-child(2){
	background-color:#304da0; 

}
.menuBoxGroup > li:nth-child(3){
	background-color:#f3f3f3 ; 

}
.menuBoxGroup > li:nth-child(4){
	background-color:#62cfd3; 

}
.menuBoxGroup > li:nth-child(5){
	background-color:#2565b0; 

}




.menuBoxGroup > li > p{
	font-weight:bold;
	color: white;
	text-align: center;
	font-size: 1em;
	line-height: 2em;
}
.menuBoxGroup > li:nth-child(3) > p{
	color: gray;

}

.slideGroup{
	height: 800px;
	width: auto;
}
.slideGroup > img{
	display:none;
	width: 90%;
	margin-left:5%;
	height:100%;
}
.slideGroup > img:nth-child(1){
	display:block;
}

</style>

</head>
<script src="./lib/jquery-3.6.1.min.js"></script>
<script>
	var imgRange = 5;//슬라이드 할 사진 갯수(범위)
	var nowImgNum = 1; //현재 노출중인 사진 넘버링
	var yetNum;// 이미지를 숨길때 사용하는 임시변수
	
	function changeImg(){
		yetNum = nowImgNum; //임시 변수에 현재 노출된 사진 번호 입력
		nowImgNum++; //현재 이미지 번호 상승
		if (nowImgNum > imgRange) { //현재 이미지 카운트가 이미지 갯수 범위보다 많다면
			nowImgNum = 1; //이미지 카운트를 1로 돌림
		}
		$(".slideGroup > img:nth-child("+ nowImgNum+ ")").fadeIn(2000); //n번째 사진을 노출시키고
		$(".slideGroup > img:nth-child("+ yetNum+ ")").css('display', 'none'); //기존의 사진을 숨김

	}
	
	var slideTimer = setInterval(function(){
		changeImg(); //인터벌로 10초마다 changeImg() 함수 호출
		
	}, 10000);

	window.addEventListener('DOMContentLoaded', function() {
		$('.bannerImg').css('display', 'none');
		//header.jsp에 있는 사진을 숨기기 위한 기능, dom이 전부 로딩 된 후 실행
	});
</script>


<body>
	<%@ include file="./header.jsp"%>
	<div class="slideGroup">
		<img src="/member_1조/images/member01.PNG" />
		<img src="/member_1조/images/member02.PNG" />
		<img src="/member_1조/images/member03.PNG" />
		<img src="/member_1조/images/member04.PNG" />
		<img src="/member_1조/images/member05.PNG" />
	</div>

	<ul class="menuBoxGroup">
		<li onclick="location.href ='/member_1조/mem_views/member01.jsp'"><p>회의사진01</p>
		<li onclick="location.href ='/member_1조/mem_views/member02.jsp'"><p>회의사진02</p>
		<li onclick="location.href ='/member_1조/mem_views/member03.jsp'"><p>회의사진03</p>
		<li onclick="location.href ='/member_1조/mem_views/member04.jsp'"><p>회의사진04</p>
		<li onclick="location.href ='/member_1조/member_U&D_pwCheck.jsp'"><p>회원정보수정</p>
	</ul>

	<%@ include file="./footer.jsp"%>
</body>
</html>