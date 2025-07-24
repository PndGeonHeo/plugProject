/**로그인 실패 Notice*/
function showLoginNotice(message) {
  const noticeWrapper = document.querySelector(".login-notice-wrapper");
  const noticeText = noticeWrapper.querySelector(".login-notice");
  // 텍스트 업데이트
  noticeText.textContent = message;

  // 로그인 버튼 아래 생성
 // var noticeLoginText = document.querySelector(".bi-label.bi-border-top.error.bi-text");
 // document.querySelector(".bi-label.bi-border-top.error.bi-text").innerHTML = message;
}

function sanitizeTextWithLineBreaks(text) {
  if (typeof text !== 'string') return '';

  // 1. HTML 특수문자 escape 처리
  const escaped = text
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");

  // 2. 줄바꿈 문자(\n, \r\n, \r, \\n) → <br>로 치환
  const withBreaks = escaped.replace(/\\n|(?:\r\n|\r|\n)/g, "<br>");

  return withBreaks;
}


function init(resultData){
   // document.title = resultData.title.web;
    if(resultData?.title?.web){
        document.title = resultData.title.web;
    }

    /*CommLogin.properties.topd.login.type = SSO */
    if (resultData?.topd?.login?.type === "sso") {
      addSSOButton();
    }

    /* 로고 텍스트*/
    if(resultData?.title?.page){
       document.querySelector(".login-wrapper .login-title").innerHTML = sanitizeTextWithLineBreaks(resultData.title.page);
    }
    // 로고 이미지 상단/텍스트 상단
    if(resultData?.topd?.login?.logo?.top === "Y"){
           const leftContainer = document.querySelector('.login-wrapper');
           const loginTitle = leftContainer.querySelector('.login-title');
           const logo = leftContainer.querySelector('.logo');

           // h1 다음 형제를 기준으로 insert 위치를 보존
           const loginTitleNext = loginTitle.nextSibling;
           const logoNext = logo.nextSibling;
           // h1을 logo 자리에 이동
           leftContainer.insertBefore(loginTitle, logoNext);
           // logo를 원래 h1 자리에 이동
           leftContainer.insertBefore(logo, loginTitleNext);
    }

}

function addSSOButton(){
    const loginButton = document.getElementById("loginBtn");

    const ssoBtn = document.createElement("button");
    ssoBtn.id = "ssoBtn";
    ssoBtn.classList.add("button");
    if (resultData?.topd?.login?.sso?.button?.content) {
      ssoBtn.innerText = resultData.topd.login.sso.button.content;
    }

    // 클릭 이벤트 (예: 다른 URL로 이동)
    ssoBtn.onclick = function () {
      window.location.href = resultData.topd.login.button.url;
    };
    loginButton.parentNode.insertBefore(ssoBtn, loginButton.nextSibling);
}



const loginButton = document.getElementById("loginBtn");

loginButton.onclick = () => {
        var username = document.querySelector("#login-id").value; // 사용자 입력값
        //                        document.querySelector('.tab-content.active ')
        var password = document.querySelector("#pw").value; // 사용자 입력값

        // 서버로 전송할 데이터
        var data = JSON.stringify({
          username: username,
          password: sha256(password),
        });

        // XMLHttpRequest를 사용한 AJAX 요청
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/webroot/decision/loin/type/account/signin/login", true); // 로그인 API URL
        xhr.setRequestHeader("Content-Type", "application/json");
        console.log(data);
        // 서버 응답 처리
        xhr.onload = () => {
          console.log(xhr);
          if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.success) {
              window.location.href = resultData.login.redirect.url;
            } else {
              if (response.message === "USER_NOT_FOUND_MESSAGE") {
                //사용자 정보가 틀릴경우
                showLoginNotice(response.data);

              }else{
                showLoginNotice(response.data);
              }
            }
          } else {
            showLoginNotice('서버 통신이 불가합니다. 관리자에게 문의하세요.');
            console.error("Server error:", xhr.statusText);// 서버 오류 처리
          }
        };

        // 요청 오류 처리
        xhr.onerror = () => {
          showLoginNotice('서버 통신이 불가합니다. 관리자에게 문의하세요.');
        };
        // 요청 전송
        xhr.send(data);
      };
