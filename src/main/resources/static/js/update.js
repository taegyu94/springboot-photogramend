// (1) 회원정보 수정
function update(userId,event) {
	event.preventDefault();		// 폼태그 액션 막기!
	
	let data = $("#profileUpdate").serialize();	// MiME type : x-www-form-urlencoded, id : profileUpdate 인 form태그의 데이터를 가져온다. 직렬
	
	//console.log(data);
	
	$.ajax({
		type : "put",
		url : `/api/user/${userId}`,
		data : data,
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		dataType : "json"	// 응답받는 데이터의 타입 : json -> res : json 타입.  
	}).done(res=>{	//	Http Status 상태코드가 200번대라면 done() 실행 
		//console.log("성공",res);
		alert("수정 성공!!");
		location.href = `/user/${userId}`;
	}).fail(error=>{		//	Http Status 상태코드가 200번대가 아니라면 fail() 실행 
		//console.log("실패",error.responseJSON.data);	console 에 data만 띄우기
		//alert(JSON.stringify(error.responseJSON.data));	//data 는 Object 이고 이것을 alert 하려면 Json 문자열로 변환해야한다.(JSON.stringify(오브젝트))
		if(error.responseJSON.data == null){
			alert(error.responseJSON.message);
		}
		else{
			alert(JSON.stringify(error.responseJSON.data));
		}
	});
}