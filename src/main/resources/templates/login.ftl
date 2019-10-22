<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no, user-scalable=no">
    <meta name="author" content="">
    <meta name="keyword" content="">
    <meta name="description" content="">
    <title>ThinkerAdmin - 登录</title>
    <!-- Icons-->
    <link href="/plugins/custom-login/vendors/simple-line-icons/css/simple-line-icons.css" rel="stylesheet">
    <!-- Main styles for this application-->
    <link href="/plugins/custom-login/css/style.min.css" rel="stylesheet">
    <link href="/plugins/custom-login/css/login.css" rel="stylesheet">
    <link href="/plugins/custom-login/vendors/pace-progress/css/pace.min.css" rel="stylesheet">
    <link href="/plugins/toastr/toastr.min.css" rel="stylesheet">
</head>
<body class="app flex-row align-items-center">
<div class="mask-layer"></div>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card-group">
                <div class="card p-4 card-wrapper">
                    <div class="card-body">
                        <form id="login-form" action="/auth" method="post" autocomplete="off">
                            <#if _csrf?? && _csrf.parameterName??>
                                <input type="hidden" name="${_csrf.parameterName?default('_csrf')}" value="${_csrf.token?default('')}"/>
                            </#if>
                            <!-- r1 -->
                            <h1>ThinkerAdmin平台</h1>
                            <!-- r2 -->
                            <p class="text-muted">ThinkerAdmin platform</p>
                            <!-- r3 -->
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text w-40">
                                      <i class="icon-user"></i>
                                    </span>
                                </div>
                                <input class="form-control text" type="text" id="username" name="username" placeholder="账号" value="admin">
                            </div>
                            <!-- r4 -->
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text w-40">
                                      <i class="icon-lock"></i>
                                    </span>
                                </div>
                                <input class="form-control text" type="password" id="password" name="password" placeholder="密码" value="123456">
                            </div>
                            <!-- r5 -->
                            <div class="input-group mb-4">
                                <div class="input-group-prepend">
                                    <span class="input-group-text w-40">
                                      <i class="icon-flag"></i>
                                    </span>
                                </div>
                                <input class="form-control text" type="text" id="captcha" name="captcha" maxlength="4" placeholder="验证码">
                                <div class="input-group-append">
                                    <button type="button" class="btn p-0 btn-v-code" title="点击更换验证码">
                                        <img class="v-code" src="/captcha/image" onclick="this.src='/captcha/image?'+Math.random();"/>
                                    </button>
                                </div>
                            </div>
                            <!-- r6 -->
                            <div class="row mb-4">
                                <div class="col-6">
                                    <button class="btn btn-primary px-4 w-75" type="button" onclick="loginHandler()">登录</button>
                                </div>
                                <div class="col-6 text-right">
                                    <button onclick="javascript:alert('请联系管理员进行密码重置！');" class="btn btn-link px-0" type="button">忘记密码？</button>
                                </div>
                            </div>
                            <!-- r7 -->
                            <p class="text-muted text-right mb-0">© 2008 - 2019 <a href="http://www.jyou.com" target="_blank">**科技</a> 版权所有</p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Plugins -->
<script src="/plugins/custom-login/vendors/jquery/jquery.min.js"></script>
<script src="/plugins/custom-login/vendors/popper/popper.min.js"></script>
<script src="/plugins/custom-login/vendors/bootstrap/bootstrap.min.js"></script>
<script src="/plugins/toastr/toastr.min.js"></script>
<script>
    $(function () {
        focusHandler();

        // 捕捉回车事件
        document.onkeydown = function (e) {
            var ev = (typeof event != 'undefined') ? window.event : e;
            if (ev.keyCode == 13) {
                loginHandler();
            }
        };

        // 绑定提示工具
        $('.btn-v-code').tooltip({
            placement: "top",
            trigger: "hover"
        });

        // 设置提示样式
        if (toastr != undefined) {
            toastr.options = {
                "closeButton":false,//显示关闭按钮
                "debug":false,//启用debug
                "newestOnTop": false,
                "progressBar": true,
                "positionClass":"toast-top-center",//弹出的位置
                "preventDuplicates": false,
                "onclick": null,
                "showDuration":"300",       //显示的时间
                "hideDuration":"1000",      //消失的时间
                "timeOut":"2000",           //停留的时间
                "extendedTimeOut":"1000",   //控制时间
                "showEasing":"swing",       //显示时的动画缓冲方式
                "hideEasing":"linear",      //消失时的动画缓冲方式
                "showMethod":"fadeIn",      //显示时的动画方式
                "hideMethod":"fadeOut"      //消失时的动画方式
            };
        }
    });
    <#if error??>
        toastr.error("${error? default("")}","提示信息");
    </#if>

    // 登录处理
    function loginHandler() {
        var username = $("#username");
        var password = $("#password");
        var captcha = $("#captcha");

        if ($.trim(username.val()) === "") {
            toastr.warning("请输入用户名", "提示信息");
            username.focus();
            return false;
        }
        if ($.trim(password.val()) === "") {
            toastr.warning("请输入密码", "提示信息");
            password.focus();
            return false;
        }
        if ($.trim(captcha.val()) === "") {
            toastr.warning("请输入验证码", "提示信息");
            captcha.focus();
            return false;
        }

        $('#login-form').submit();
    }

    function focusHandler() {
        setTimeout(function () {
            var username = $("#username");
            var password = $("#password");
            var captcha = $("#captcha");

            if ($.trim(username.val) === "") {
                username.focus();
                return;
            }
            if ($.trim(password.val()) === "") {
                password.focus();
                return;
            }
            if ($.trim(captcha.val()) === "") {
                captcha.focus();
            }
        }, 100);
    }
</script>
</body>
</html>
