<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.1.min.js">  </script>
    <script type="text/javascript">
        var url = "http://localhost:37799/webroot/decision";

        $(function () {
            loginFR(goto_report());
        });

        function loginFR(callback) {
            var username = "admin";
            var password = "123456";
            jQuery.ajax({
                url: url + "/login/cross/domain?fine_username=" + username + "&fine_password=" + password + "&validity=-1",
                type: "GET",
                dataType:"jsonp",
                timeout:10000,
                success:function(data) {
                    // alert(JSON.stringify(data));
                    if (data.status === "success") {
                        // alert("successful!");
                    } else if (data.status === "fail"){
                        alert("login fail!");
                    }
                    callback && callback(data.accessToken);
                },
                error:function(){
                    alert("login error!");
                }
            });
        }

        function goto_report(token) {
            // window.location.href = "http://localhost:37799/webroot/decision/v5/design/report/2c8b43ce791049a5af3f4b68a1c66154/view&fine_auth_token=" + token;
            window.location.href = "http://localhost:37799/webroot/decision/v5/design/report/2c8b43ce791049a5af3f4b68a1c66154/view";
        }

        function GetList(token) {
            var i=0;
            jQuery.ajax({
                url: url + "/v5/api/dashboard/user/info?op=api&cmd=get_all_reports_data&fine_auth_token=" + token,
                type: "GET",
                timeout: 10000,
                dataType: "jsonp",
                success:function(res) {
                    for(;i < res.data.dashboards.length;i++) {
                        document.write(JSON.stringify(res.data.dashboards[i].name)+"<br>");
                    }
                },
                error:function(XMLHttpRequest,textStatus,errorThrown){
                    alert(XMLHttpRequest+"/"+textStatus+"/"+errorThrown);
                }
            });
        }
        function NewDemo(token) {
            var name = prompt("name","");
            var id = "";
            var dir = {name: name, catalog: []}
            var flag = 0;
            jQuery.ajax({
                url: url + "/v5/api/platform/dashboard/reports?dir=" + window.encodeURI(JSON.stringify(dir)) + "&fine_auth_token=" + token,
                type: "GET",
                dataType:"jsonp",
                timeout:10000,
                success:function(res) {
                    if(name != "") {
                        for (var key in res) {
                            if(key=="error") {
                                flag=1;
                            }
                        }
                        if(flag==0) {
                            window.location.href = url + "/v5/design/report/" + res.data.id + "/edit";
                        } else {
                            alert("error!");
                        }
                    } else {
                        alert("name is empty");
                    }
                },
                error:function(XMLHttpRequest,textStatus,errorThrown){
                    //alert(JSON.stringify(data));
                    alert(XMLHttpRequest+"/"+textStatus+"/"+errorThrown);
                }
            });
        }
    </script>
</head>
<body>

</body>
</html>