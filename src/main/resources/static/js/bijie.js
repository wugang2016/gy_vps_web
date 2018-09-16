
function logout(){
	if(confirm("确定要注销登录？")){
		window.location.href = "/admin/logout";
	}
}

function umd(){
	$.ajax({
        url: "/system_status",
        type: "POST",
        success: function(data){
            if(data != ""){
            	var obj = JSON.parse(data);
            	$('#cpu').html("CPU: "+obj.cpu+"%");
            	$('#mem').html("MEM: "+obj.mem+"%");
            	$('#disk').html("DISK: "+obj.disk+"%");

        		$('#cpu').removeClass("error");
        		$('#mem').removeClass("error");
        		$('#disk').removeClass("error");
            	var cpu = Number((obj.cpu).split(".")[0]);
            	if(cpu >= 80){
            		$('#cpu').addClass("error");
            	}
            	var mem = Number((obj.mem).split(".")[0]);
            	if(mem >= 80){
            		$('#mem').addClass("error");
            	}
            	var disk = Number((obj.disk).split(".")[0]);
            	if(disk >= 80){
            		$('#cpu').addClass("error");
            	}
            }
        }
    });
	setTimeout("umd()", 30000);
}

function changeURLArg(arg,arg_val){ 
	var url = window.location.href;
	var pattern=arg+'=([^&]*)'; 
	var replaceText=arg+'='+arg_val; 
	if(url.match(pattern)){ 
	    var tmp='/('+ arg+'=)([^&]*)/gi'; 
	    tmp=url.replace(eval(tmp),replaceText); 
	    return tmp; 
	}else{ 
	    if(url.match('[\?]')){ 
			return url+'&'+replaceText; 
		}else{ 
		    return url+'?'+replaceText; 
		}
	}
	return url+'\n'+arg+'\n'+arg_val; 
}
