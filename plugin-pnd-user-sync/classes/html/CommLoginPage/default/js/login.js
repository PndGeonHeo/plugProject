/**로그인 실패 Notice*/
function showLoginNotice(message) {
  const noticeWrapper = document.querySelector(".login-notice-wrapper");
  const noticeText = noticeWrapper.querySelector(".login-notice");
  /*// 텍스트 업데이트
  noticeText.textContent = message;
  // 표시 애니메이션
  noticeWrapper.classList.add("show");
  noticeWrapper.classList.remove("hide");
  // 일정 시간 후에 사라짐
  setTimeout(() => {
    noticeWrapper.classList.add("hide");
    noticeWrapper.classList.remove("show");
  }, 10000); // 10초 후 사라짐*/

  // 로그인 버튼 아래 생성
  var noticeLoginText = document.querySelector(".bi-label.bi-border-top.error.bi-text");
  document.querySelector(".bi-label.bi-border-top.error.bi-text").innerHTML = message;
}
/** 최초 시작 세팅*/
function init(resultData) {
  initValid(resultData);
  console.log(resultData);
  //웹페이지 문구
  document.title = resultData.title.web;
  //사용자구분 탭영역
  initTab();
  //로그인 로고 밑 텍스트
  document.querySelector("div[class=header] h1").append(resultData.title.page);
}

/**최초 유효성 검사*/
function initValid(resultData){
    initValidLoginLogo(resultData.login.logo);
}

function initValidLoginLogo(login_logo){
    login_logo === 'N'  ? document.querySelector(".loginLogo").remove() : "" ;
}

function initValidLoginTypeTab(login_type_tab){
    login_type_tab === 'N' ? document.querySelector(".tab.active").remove() : "";
}

/** */

function initTab() {
  const tabsContainer = document.getElementById("tabs");
  const contentsContainer = document.getElementById("contents");
  const flags = resultData.tab.flags;
  //최초 tab Active
  const tabInitActive = resultData.tab.init;
  // 동적으로 탭과 내용을 생성
  Object.keys(flags).forEach((key, index) => {
    var activeFlag = "active";
    const flag = flags[key];
    const tabName = flag.name;

    // 탭 버튼 생성
    const tabDiv = document.createElement("div");
    tabDiv.className = `tab `;
    if (tabInitActive === key) {
      tabDiv.className = "tab " + activeFlag;
    }

    tabDiv.textContent = tabName;
    tabDiv.onclick = () => openTab(key);

    tabsContainer.appendChild(tabDiv);

    // 탭 콘텐츠 생성
    const contentDiv = document.createElement("div");
    contentDiv.className = "tab-content ";
    if (tabInitActive == key) {
      contentDiv.className = "tab-content " + activeFlag;
    }
    contentDiv.id = key;

    const loginArea = flag.loginArea;

    if (loginArea.type === "sso") {
      const infoParagraph = document.createElement("p");
      infoParagraph.className = "info";
      infoParagraph.textContent = loginArea.content;
      contentDiv.appendChild(infoParagraph);

      const button = document.createElement("button");
      button.className = "move-button";
      button.textContent = loginArea.button.content;
      button.onclick = () => (window.location.href = loginArea.button.url);
      contentDiv.appendChild(button);

      const notice = document.createElement("p");
      notice.className = "notice";
      notice.innerHTML = loginArea.security.content;
      contentDiv.appendChild(notice);
    }

    if (loginArea.type === "login") {
      const loginDiv = document.createElement("div");
      loginDiv.className = "login-notice-wrapper";
      const loginNotice = document.createElement("p");
      loginNotice.className = "login-notice";
      loginNotice.innerHTML = "";
      loginDiv.appendChild(loginNotice);
      contentDiv.appendChild(loginDiv);
      //                    contentDiv.appendChild(loginNotice);

      const idInput = document.createElement("input");
      idInput.type = "text";
      idInput.className = "input-field";
      idInput.name = "username";
      idInput.placeholder = loginArea.id.content;
      contentDiv.appendChild(idInput);

      const passwordInput = document.createElement("input");
      passwordInput.type = "password";
      passwordInput.name = "password";
      passwordInput.className = "input-field";
      passwordInput.placeholder = loginArea.pw.content;
      passwordInput.onkeyup = () => enterkey();
      contentDiv.appendChild(passwordInput);



      const loginButton = document.createElement("button");
      loginButton.className = "btn btn-primary btn-lg btn-block login-button";
      loginButton.textContent = "로그인";
      loginButton.onclick = () => {
        var username = document.querySelector(".tab-content.active").children
          .username.value; // 사용자 입력값
        //                        document.querySelector('.tab-content.active ')
        var password = document.querySelector(".tab-content.active").children
          .password.value; // 사용자 입력값

        // 서버로 전송할 데이터
        var data = JSON.stringify({
          username: username,
          password: sha256(password),
        });

        // XMLHttpRequest를 사용한 AJAX 요청
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/webroot/decision/loin/account/signin/login", true); // 로그인 API URL
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

      contentDiv.appendChild(loginButton);

      //로그인 실패영역
      const loginError = document.createElement("div");
      loginError.className = "bi-label bi-border-top error bi-text";
      contentDiv.appendChild(loginError);
//      <div class="bi-label bi-border-top error bi-text" style="height: 24px; line-height: 24px; text-align: left; white-space: pre; text-overflow: ellipsis; position: relative;">
      // Login 보안 문구
      const notice = document.createElement("p");
      notice.className = "notice";
      notice.innerHTML = loginArea.security.content;
      contentDiv.appendChild(notice);
    }

    contentsContainer.appendChild(contentDiv);
  });

  // 탭 열기 함수
  function openTab(key) {
    const tabs = document.querySelectorAll(".tab");
    const contents = document.querySelectorAll(".tab-content");

    tabs.forEach((tab, index) => {
      if (tabs[index].textContent === flags[key].name) {
        tab.classList.add("active");
      } else {
        tab.classList.remove("active");
      }
    });

    contents.forEach(content => {
      console.log(key);
      if (content.id === key) {
        content.classList.add("active");
      } else {
        content.classList.remove("active");
      }
    });
  }
}

function enterkey() {
  if (window.event.keyCode == 13) {
    document.querySelector(".login-button").click();
  }
}
