var ws = null;
var ws_status = false;
window.data = null;
// var receiveData = 1;
function openWebSocket(){
	//判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
		ws = new WebSocket("ws://localhost:8080");
		console.log("开启8080端口服务成功")
		console.log("初始化websocket服务器成功" )
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("ws://"+window.location.host+"/websocket");
	} else {
		ws = new SockJS("http://"+window.location.host+"/websocket");
		console.log("该浏览器不支持websocket服务器")
	}

	ws.onopen = function () {

	};
	// 接收到从后端发来的数据
	ws.onmessage = function  (event) {
		//根据业务逻辑解析数据
		// console.log("Server:");
		// document.getElementById("data").innerHTML = event.data
		// console.log(event.data);
		if (event) {
			// console.log("websocket收到" + event.data)
			window.data = event.data
			console.log("window.data的值为" + window.data)
			// receiveData = event.data
			// renderLayer03Right(event.data);
		} else {
			console.log("websocket没有收到消息")
		}

	};
	ws.onclose = function (event) {
		console.log("Connection closed!");
	};
	ws.onopen = function (event){
		ws_status = true;
		console.log("Connected!");
	};
	ws.onerror = function (event){
		console.log("Connect error!");
	};
}
//如果连接失败，每隔两秒尝试重新连接
setInterval(function(){
	if(!ws_status){
		openWebSocket();
	}
}, 2000);
// $("#sendData").click(function(){
// 	// ws.send("Hello, server, I am browser.");
// 	window.location.href="/sendData"
// });

// document.write("<script language=javascript src='js/websocket.js'></script>");
function drawLayer02Label(canvasObj,text,textBeginX,lineEndX){
	var colorValue = '#04918B';

	var ctx = canvasObj.getContext("2d");

	ctx.beginPath();
	ctx.arc(35,55,2,0,2*Math.PI);
	ctx.closePath();
	ctx.fillStyle = colorValue;
	ctx.fill();

	ctx.moveTo(35,55);
	ctx.lineTo(60,80);
	ctx.lineTo(lineEndX,80);
	ctx.lineWidth = 1;
	ctx.strokeStyle = colorValue;
	ctx.stroke();

	ctx.font='12px Georgia';
	ctx.fillStyle = colorValue;
	ctx.fillText(text,textBeginX,92);
}

//接入机型占比

var COLOR = {
	MACHINE:{
		TYPE_A:'#0175EE',
		TYPE_B:'#D89446',
		TYPE_C:'#373693',
		TYPE_D:'#25AE4F',
		TYPE_E:'#06B5C6',
		TYPE_F:'#009E9A',
		TYPE_G:'#AC266F'
	}
};

function renderLegend(){
	drawLegend(COLOR.MACHINE.TYPE_A,25,'A机型');
	drawLegend(COLOR.MACHINE.TYPE_B,50,'B机型');
	drawLegend(COLOR.MACHINE.TYPE_C,75,'C机型');
	drawLegend(COLOR.MACHINE.TYPE_D,100,'D机型');
	drawLegend(COLOR.MACHINE.TYPE_E,125,'E机型');
	drawLegend(COLOR.MACHINE.TYPE_F,150,'F机型');
	drawLegend(COLOR.MACHINE.TYPE_G,175,'G机型');
}

function drawLegend(pointColor,pointY,text){
	var ctx = $("#layer03_left_01 canvas").get(0).getContext("2d");
	ctx.beginPath();
	ctx.arc(20,pointY,6,0,2*Math.PI);
	ctx.fillStyle = pointColor;
	ctx.fill();
	ctx.font='20px';
	ctx.fillStyle = '#FEFFFE';
	ctx.fillText(text,40,pointY+3);
}


//存储
function renderLayer03Right(){
	drawLayer03Right($("#layer03_right_chart01 canvas").get(0),"#027825",0.66);
	drawLayer03Right($("#layer03_right_chart02 canvas").get(0),"#006DD6",0.52);
	drawLayer03Right($("#layer03_right_chart03 canvas").get(0),"#238681",0.34);
}

function drawLayer03Right(canvasObj,colorValue,rate){
	var ctx = canvasObj.getContext("2d");
    
	var circle = {
        x : 65,    //圆心的x轴坐标值
        y : 80,    //圆心的y轴坐标值
        r : 60      //圆的半径
    };

	//画扇形
	//ctx.sector(circle.x,circle.y,circle.r,1.5*Math.PI,(1.5+rate*2)*Math.PI);
	//ctx.fillStyle = colorValue;
	//ctx.fill();

	ctx.beginPath();
	ctx.arc(circle.x,circle.y,circle.r,0,Math.PI*2)
	ctx.lineWidth = 10;
	ctx.strokeStyle = '#052639';
	ctx.stroke();
	ctx.closePath();

	ctx.beginPath();
	ctx.arc(circle.x,circle.y,circle.r,1.5*Math.PI,(1.5+rate*2)*Math.PI)
	ctx.lineWidth = 10;
	ctx.lineCap = 'round';
	ctx.strokeStyle = colorValue;
	ctx.stroke();
	ctx.closePath();
    
	ctx.fillStyle = 'white';
	ctx.font = '20px Calibri';
	ctx.fillText(rate*100+'%',circle.x-15,circle.y+10);

}


function renderChartBar01(){
	var myChart = echarts.init(document.getElementById("layer03_left_02"));
		myChart.setOption(
					 {
						title : {
							text: '',
							subtext: '',
							x:'center'
						},
						tooltip : {
							trigger: 'item',
							formatter: "{b} : {c} ({d}%)"
						},
						legend: {
							show:false,
							x : 'center',
							y : 'bottom',
							data:['A机型','B机型','C机型','D机型','E机型','F机型','G机型']
						},
						toolbox: {
						},
						label:{
							normal:{
								show: true, 
								formatter: "{b} \n{d}%"
							} 
						},
						calculable : true,
						color:[COLOR.MACHINE.TYPE_A,COLOR.MACHINE.TYPE_B,COLOR.MACHINE.TYPE_C,COLOR.MACHINE.TYPE_D,COLOR.MACHINE.TYPE_E,COLOR.MACHINE.TYPE_F,COLOR.MACHINE.TYPE_G],
						series : [
							{
								name:'',
								type:'pie',
								radius : [40, 80],
								center : ['50%', '50%'],
								//roseType : 'area',
								data:[
									{value:4600, name:'A机型'},
									{value:4600, name:'B机型'},
									{value:15600, name:'C机型'},
									{value:6600, name:'D机型'},
									{value:5700, name:'E机型'},
									{value:7600, name:'F机型'},
									{value:3500, name:'G机型'}
								]
							}
						]
					}
		);

}

/*
function renderChartBar02(){
	var myChart = echarts.init(document.getElementById("layer03_left_03"));
		myChart.setOption(
					{
						title : {
							text: '',
							subtext: '',
							x:'center'
						},
						tooltip : {
							show:true,
							trigger: 'item',
							formatter: "上线率<br>{b} : {c} ({d}%)"
						},
						legend: {
							show:false,
							orient: 'vertical',
							left: 'left',
							data: ['A机型','B机型','C机型','D机型','E机型','F机型','G机型']
						},
						series : [
							{
								name: '',
								type: 'pie',
								radius : '50%',
								center: ['50%', '60%'],
								data:[
									{value:7600, name:'A机型'},
									{value:6600, name:'B机型'},
									{value:15600, name:'C机型'},
									{value:5700, name:'D机型'},
									{value:4600, name:'E机型'},
									{value:4600, name:'F机型'},
									{value:3500, name:'G机型'}
								],
								itemStyle: {
									emphasis: {
										shadowBlur: 10,
										shadowOffsetX: 0,
										shadowColor: 'rgba(0, 0, 0, 0.5)'
									}
								}
							}
						],
						color:[COLOR.MACHINE.TYPE_A,COLOR.MACHINE.TYPE_B,COLOR.MACHINE.TYPE_C,COLOR.MACHINE.TYPE_D,COLOR.MACHINE.TYPE_E,COLOR.MACHINE.TYPE_F,COLOR.MACHINE.TYPE_G]
					}
		);
}*/

// ws = new WebSocket("ws://"+window.location.host);
function renderLayer04Left(){
	if (window.data) {
		var parseData = JSON.parse(window.data);
		console.log(parseData.province)
	}else {
		// console.log("data is null！！！！")
	}
	var myChart = echarts.init(document.getElementById("layer04_left_chart"));
	// if (ws.onmessage){
	// 	var myData = ws.onmessage()
	// 	console.log(myData)
	// }
// 	var parseData = null;
// 	// window.onload =
// 	if (ws.onmessage()){
// 		ws.onmessage = function (event) {
// //'{"action":0,"ageRange":0,"brandId":2661,"catId":833,"day":29,"gender":1,"itemId":323294,"merchantId":2882,"month":8,"province":"内蒙古","userId":328862}'
// 			var data = event.data;
// 			parseData = JSON.parse(data);
// 			console.log("dashboard收到了消息" + parseData.province)
// 			// data.par
// 		}
// 	}

	myChart.setOption(
		{
			title: {
				text: ''
			},
			tooltip : {
				trigger: 'axis'
			},
			legend: {
				data:[]
			},
			grid: {
				left: '3%',
				right: '4%',
				bottom: '5%',
				top:'4%',
				containLabel: true
			},
			xAxis :
			{
				type : 'category',
				boundaryGap : false,
				data : getLatestDays(31),
				axisLabel:{
					textStyle:{
						color:"white", //刻度颜色
						fontSize:8  //刻度大小
					},
					rotate:45,
					interval:2
				},
				axisTick:{show:false},
				axisLine:{
					show:true,
					lineStyle:{
						color: '#0B3148',
						width: 1,
						type: 'solid'
					}
				}
			},
			yAxis : 
			{
				type : 'value',
				axisTick:{show:false},
				axisLabel:{
					textStyle:{
						color:"white", //刻度颜色
						fontSize:8  //刻度大小
						}
				},
				axisLine:{
					show:true,
					lineStyle:{
						color: '#0B3148',
						width: 1,
						type: 'solid'
					}
				},
				splitLine:{
					show:false
				}
			},
			tooltip:{
				formatter:'{c}',
				backgroundColor:'#FE8501'
			},
			series : [
				{
					name:'',
					type:'line',
					smooth:true,
					areaStyle:{
						normal:{
							color:new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: '#026B6F'}, {offset: 1, color: '#012138' }], false),
							opacity:0.2
						}
					},
					itemStyle : {  
                            normal : {  
                                  color:'#009991'
                            },
							lineStyle:{
								normal:{
								color:'#009895',
								opacity:1
							}
						}
                    },
					symbol:'none',
					// data:[48, 52, 45, 46, 89, 120, 110,100,88,96,88,45,78,67,89,103,104,56,45,104,112,132,120,110,89,95,90,89,102,110,110]
					data:(function(){
						if (window.data != null){
							var res = [];
							var len = Math.round(Math.random() * 1000);
							while (len--) {
								res.push(len);
							}
							return res;
						}else {
							return [48, 52, 45, 46, 89, 120, 110,100,88,96,88,45,78,67,89,103,104,56,45,104,112,132,120,110,89,95,90,89,102,110,110]
						}
					})()
					// data:(function (){
					// 	var res = [];
					// 	var len = Math.round(Math.random() * 1000);
					// 	while (len--) {
					// 		res.push(len);
					// 	}
					// 	return res;
					// })()
				}
			]
		}
	);

}


function renderLayer04Right(){
	var myChart = echarts.init(document.getElementById("layer04_right_chart"));
	myChart.setOption({
			title: {
				text: ''
			},
			tooltip: {
				trigger: 'axis'
			},
			legend: {
				top:20,
				right:5,
				textStyle:{
					color:'white'
				},
				orient:'vertical',
				data:[
						{name:'网络',icon:'circle'},
						{name:'内存',icon:'circle'},
						{name:'CPU',icon:'circle'}
					]
			},
			grid: {
				left: '3%',
				right: '16%',
				bottom: '3%',
				top:'3%',
				containLabel: true
			},
			xAxis: {
				type: 'category',
				boundaryGap: false,
				axisTick:{show:false},
				axisLabel:{
					textStyle:{
						color:"white", //刻度颜色
						fontSize:8  //刻度大小
						}
				},
				axisLine:{
					show:true,
					lineStyle:{
						color: '#0B3148',
						width: 1,
						type: 'solid'
					}
				},
				data: get10MinutesScale()
			},
			yAxis: {
				type: 'value',
				axisTick:{show:false},
				axisLabel:{
					textStyle:{
						color:"white", //刻度颜色
						fontSize:8  //刻度大小
						}
				},
				axisLine:{
					show:true,
					lineStyle:{
						color: '#0B3148',
						width: 1,
						type: 'solid'
					}
				},
				splitLine:{
					show:false
				}
			},
			series: [
						{
							name:'网络',
							type:'line',
							itemStyle : {  
									normal : {  
									color:'#F3891B'
								},
								lineStyle:{
									normal:{
									color:'#F3891B',
									opacity:1
										}
								}
							},  
							data:[120, 132, 101, 134, 90, 230, 210]
						},
						{
							name:'内存',
							type:'line',
							itemStyle : {  
									normal : {  
									color:'#006AD4'
								},
								lineStyle:{
									normal:{
									color:'#F3891B',
									opacity:1
										}
								}
							},
							data:[220, 182, 191, 234, 290, 330, 310]
						},
						{
							name:'CPU',
							type:'line',
							itemStyle : {  
									normal : {  
									color:'#009895'
								},
								lineStyle:{
									normal:{
									color:'#009895',
									opacity:1
										}
								}
							},
							data:[150, 232, 201, 154, 190, 330, 410]
						}
					]
		}	
	);
}

function get10MinutesScale() {
	var currDate = new Date();
	var odd = currDate.getMinutes()%10;
	var returnArr = new Array();
	currDate.setMinutes(currDate.getMinutes()-odd);
	for(var i = 0; i <7; i++){
		returnArr.push(currDate.getHours()+":"+(currDate.getMinutes()<10?("0"+currDate.getMinutes()):currDate.getMinutes()));
		currDate.setMinutes(currDate.getMinutes()-10);
	}
	return returnArr;
}


function getLatestDays(num) {
	var currentDay = new Date();
	var returnDays = [];
	for (var i = 0 ; i < num ; i++)
	{
		currentDay.setDate(currentDay.getDate() - 1);
		returnDays.push((currentDay.getMonth()+1)+"/"+currentDay.getDate());
	}
	return returnDays;
}
setInterval(renderLayer04Left, 2000)

