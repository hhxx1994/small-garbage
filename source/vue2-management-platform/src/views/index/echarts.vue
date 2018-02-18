<template>
  <div class="chart-container">
    <el-row>
      <el-col :span="12" class="chart chart_left">
        <div id="userChart" style="height: 300px">图表加载失败</div>
      </el-col>
      <el-col :span="12" class="chart">
        <div id="userDoChart" style="height: 300px">图表加载失败</div>
      </el-col>
    </el-row>

  </div>
</template>
<script>
  import echarts from 'echarts'

  export default {
    data() {
      return {};
    },
    methods: {
      // 加载用户来源图
      getUserChartInit(chartsData) {
        const myChart = echarts.init(document.getElementById('userChart'));
        myChart.showLoading();
        var option = {
          title: {
            text: "价格统计图"
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999'
              }
            }
          },
          toolbox: {
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          legend: {
            data: ['最低价', '最高价', '平均价']
          },
          xAxis: [
            {
              type: 'category',
              data: ['北京', '上海', '深圳', '广州'],
              axisPointer: {
                type: 'shadow'
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              name: '每平方米/元',
              max: Math.max(...chartsData['max'])+1000,
              axisLabel: {
                formatter: '{value}'
              }
            },
            {
              type: 'value',
              name: '每平方米/元',
              max: Math.max(...chartsData['max'])+1000,
              axisLabel: {
                formatter: '{value}'
              }
            }
          ],
          series: [
            {
              name: '最低价',
              type: 'bar',
              data: chartsData['min']
            },
            {
              name: '最高价',
              type: 'bar',
              data: chartsData['max']
            },
            {
              name: '平均价',
              type: 'line',
              yAxisIndex: 1,
              data: chartsData['avg']
            }
          ]
        };
        myChart.setOption(option);
        myChart.hideLoading();
      },
      // 加载每日用户行为图
      getUserDoChartInit(chartsData) {
        const myChart = echarts.init(document.getElementById('userDoChart'));
        myChart.showLoading();
        var option = {
          title: {
            text: "房源数量比例统计图"
          },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            x: 'right',
            data:['北京','上海','深圳','广州']
          },
          series: [
            {
              name:'访问来源',
              type:'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: false,
                  position: 'center'
                },
                emphasis: {
                  show: true,
                  textStyle: {
                    fontSize: '30',
                    fontWeight: 'bold'
                  }
                }
              },
              labelLine: {
                normal: {
                  show: false
                }
              },
              data:[
                {value:chartsData['bj'], name:'北京'},
                {value:chartsData['sh'], name:'上海'},
                {value:chartsData['sz'], name:'深圳'},
                {value:chartsData['gz'], name:'广州'}
              ]
            }
          ]
        };
        myChart.setOption(option);
        myChart.hideLoading();
      }
    },
    mounted() {
      this.$nextTick(function () {
        this.$http.get('/api/chartsData/houseInfo').then((response) => {
          response = response.data;
          this.getUserChartInit(response);
        });

        this.$http.get('/api/chartsData/houseRatio').then((response) => {
          response = response.data;
          this.getUserDoChartInit(response)
        });

      })
    }
  };
</script>
<style>
  .chart {
    background-color: white;
    border-radius: 4px;
  }

  .chart_left {
    border-right: #F2F2F2 10px solid;
  }
</style>
