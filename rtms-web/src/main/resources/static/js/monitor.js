var ws = null;
var ws_status = false;
window.data = null;
window.pieData = new Map([['朝阳区', 0], ['东城区', 0], ['西城区', 0], ['海淀区', 0], ['怀柔区', 0], ['顺义区', 0], ['丰台区', 0]]);
window.priceData = [1, 1, 1, 1, 1]
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
	ws.onmessage = function (event) {
		if (event) {
			var data = event.data
			var obj = JSON.parse(data);
			if (obj.neighbourhood != null){
				if (window.pieData.has(obj.neighbourhood)){
					// console.log("查找到了值")
					var value = window.pieData.get(obj.neighbourhood);
					window.pieData.set(obj.neighbourhood,value + 1)
					// for (var x of window.pieData) { // 遍历Map
					// 	console.log(x[0] + '=' + x[1]);
					// }
				}
			}

			if (obj.price != null){
				var price = obj.price;
				if (price < 500){
					window.priceData[0]++;
				}
				if (price >= 500 && price < 1000){
					window.priceData[1]++;
				}
				if (price >= 1000 &&price < 1500){
					window.priceData[2]++;
				}
				if (price >= 1500 && price < 2000){
					window.priceData[3]++;
				}
				if (price >= 2000){
					window.priceData[4]++;
				}
			}
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
// 	// window.location.href="/sendData"
// 	console.log("点击了按钮")
// });



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
	drawLegend(COLOR.MACHINE.TYPE_A,25,'朝阳区');
	drawLegend(COLOR.MACHINE.TYPE_B,50,'东城区');
	drawLegend(COLOR.MACHINE.TYPE_C,75,'西城区');
	drawLegend(COLOR.MACHINE.TYPE_D,100,'海淀区');
	drawLegend(COLOR.MACHINE.TYPE_E,125,'怀柔区');
	drawLegend(COLOR.MACHINE.TYPE_F,150,'顺义区');
	drawLegend(COLOR.MACHINE.TYPE_G,175,'丰台区');
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
// function renderLayer03Right(){
// 	drawLayer03Right($("#layer03_right_chart01 canvas").get(0),"#027825",0.66);
// 	drawLayer03Right($("#layer03_right_chart02 canvas").get(0),"#006DD6",0.52);
// 	drawLayer03Right($("#layer03_right_chart03 canvas").get(0),"#238681",0.34);
// }

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
	// var map = new Map([['朝阳区', 0], ['东城区', 0], ['西城区', 0], ['海淀区', 0], ['怀柔区', 0], ['顺义区', 0], ['丰台区', 0]]);
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
							data:['朝阳区','东城区','西城区','海淀区','怀柔区','顺义区','丰台区']
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
									{value:window.pieData.get('朝阳区'), name:'朝阳区'},
									// {value:1, name:'朝阳区'},
									{value:window.pieData.get('东城区'), name:'东城区'},
									{value:window.pieData.get('西城区'), name:'西城区'},
									{value:window.pieData.get('海淀区'), name:'海淀区'},
									{value:window.pieData.get('怀柔区'), name:'怀柔区'},
									{value:window.pieData.get('顺义区'), name:'顺义区'},
									{value:window.pieData.get('丰台区'), name:'丰台区'}
								]
							}
						]
					}
		);

}


function renderLayer04Left(){
	var myChart = echarts.init(document.getElementById("layer04_left_chart"));

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

	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('layer04_right_chart'));

	// 指定图表的配置项和数据

	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(
		{
			title: {
				text: ''
			},
			tooltip: {},
			legend: {
				data:['销量']
			},
			xAxis: {
				type: 'category',
				data: ["0-500¥","500-1000¥","1000-1500¥","1500-2000¥",">2000¥",],
				axisLabel:{
					textStyle:{
						color:"white",
						fontsize:1
					},
					rotate: 0,
					interval: 0
				},

			},
			yAxis: {
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
						color: '#0175EE',
						width: 1,
						type: 'solid'
					}
				},
				splitLine:{
					show:false
				}
			},
			series: [{
				name: '销量',
				type: 'bar',
				data: window.priceData
			}]
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
setInterval(renderLayer04Left, 1000)
setInterval(renderChartBar01, 1000)
setInterval(renderLayer04Right, 1000)

