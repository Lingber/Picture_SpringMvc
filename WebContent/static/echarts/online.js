var totalRecord, currentPage;
	//页面加载完成后，直接去发送一个Ajax请求，要到分页数据
	$(function() {
		to_page(1)
	});
	function to_page(pn) {
		$.ajax({
			url : "/Picture_SpringMvc/count/online",
			data : "",
			type : "GET",
			success : function(result) {
				console.log(result);
				//1.解析并显示员工数据
				build_rough_table(result);
			}
		});
	}
	

function _setWordCloud (cloudData) {
	var arr_name  = Array.from(cloudData.cityName);
	var arr_value = Array.from(cloudData.userNumber);
	var arrvalue=0;//用于存放取出的数组的值
	for(var i=0;i<arr_value.length;i++){
	     arrvalue=arrvalue+arr_value[i];//数组的索引是从0开始的
	}
    var option = {
            title: [{
                text: '全国总计在线人数：  '+arrvalue+'位',
                left: 'center',
                top: 10,
                textStyle: {
                    color: '#000',
                    fontWeight: 'normal',
                    fontSize: 40
                }
            }],
    		xAxis: {
		        type: 'category',
		        data:  arr_name
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [{
		        data: arr_value,
		        type: 'bar'
		    }]
    };
    return option;
};


//初始化
function initWordCloud(wordCloudData) {
    var option = _setWordCloud(wordCloudData.cloudData);
    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption(option);
}
	
function build_rough_table(result) {
	var rough = result;

	var dataCloud = {
		"cloudData":rough,
	 };
	initWordCloud(dataCloud);
};
