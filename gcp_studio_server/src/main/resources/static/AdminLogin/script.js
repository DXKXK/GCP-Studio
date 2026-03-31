let URL

const BG = document.getElementById("BG");
const lock = document.getElementById("lock");
const logo = document.getElementById("logo");
const inputBox = document.getElementById("inputBox");
const password = document.getElementById("password");

window.onload = function(){
    changeLogoclass('U');
    getUrl();
}

logo.addEventListener('click', function(){changeLogoclass('C')});
lock.addEventListener('click', function(){changeLogoclass('C')});
BG.addEventListener('click', function(e){
    if(e.target.id === 'BG'){
        changeLogoclass('U');
    }
});

// 修改状态
function changeLogoclass(CName){
    const regex = /logo/;
    // 控制输入框的显示和隐藏
    password.className = CName === 'W'|| CName === 'C'? '' : 'show';;
    // 移除处logo之外的所有类名
    logo.className = 'logo';
    inputBox.className = 'input-box';
    logo.classList.add(CName);
    inputBox.classList.add('box-' + CName);
}

// 监控回车被按下
password.addEventListener('keyup', function(e){
    e.key === 'Enter' ? checkPassword(password) : '';
});

// 密码判断
async function checkPassword(passwordInput) {
    const response = await fetch(`/api/adminlogin`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ password: passwordInput.value })
    });

    const data = await response.json();
    console.log(data);

    if (data.code === 200) {
        // 跳转到管理页
        changeLogoclass('R');
        setTimeout(() => {
            window.location.href = data.data;
        }, 1500);
    } else {
        changeLogoclass('W');
    }
    passwordInput.value = '';
}

// 获取当前网站的url
function getUrl() {
    return window.location.href;
    console.log(window.location.href);
}