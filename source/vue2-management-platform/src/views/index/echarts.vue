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
    <hr>
    <el-row>
      <el-col :span="12" class="chart chart_left">
        <div id="peopleChart" style="height: 300px">图表加载失败</div>
      </el-col>
      <el-col :span="12" class="chart">
        <div id="areaPrice" style="height: 300px">图表加载失败</div>
      </el-col>
    </el-row>
    <hr>
    <el-row>
      <el-col :span="12" class="chart chart_left">
        <div id="subWayCharts" style="height: 300px">图表加载失败</div>
      </el-col>
      <el-col :span="12" class="chart">
        <div id="peoplePrice" style="height: 300px">图表加载失败</div>
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
              max: Math.max(...chartsData['max']) + 1000,
              axisLabel: {
                formatter: '{value}'
              }
            },
            {
              type: 'value',
              name: '每平方米/元',
              max: Math.max(...chartsData['max']) + 1000,
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
          toolbox: {
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          legend: {
            orient: 'vertical',
            x: 'right',
            data: ['北京', '上海', '深圳', '广州']
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
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
              data: [
                {value: chartsData['bj'], name: '北京'},
                {value: chartsData['sh'], name: '上海'},
                {value: chartsData['sz'], name: '深圳'},
                {value: chartsData['gz'], name: '广州'}
              ]
            }
          ]
        };
        myChart.setOption(option);
        myChart.hideLoading();
      },
      getPeopleChartInit(chartsData) {
        const myChart = echarts.init(document.getElementById('peopleChart'));
        myChart.showLoading();
        var option = {
          backgroundColor: '#2c343c',

          title: {
            text: '用户分布',
            left: 'center',
            top: 20,
            textStyle: {
              color: '#ccc'
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

          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },

          visualMap: {
            show: true,
            min: 5000,
            max: 600000,
            inRange: {
              colorLightness: [0, 1]
            }
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
              radius: '55%',
              center: ['50%', '50%'],
              data: [
                {value: chartsData['focus'], name: '关注人数'},
                {value: chartsData['sell'], name: '购买人数'},
                {value: chartsData['watch'], name: '看房人数'},
                {value: chartsData['rent'], name: '租房人数'}
              ].sort(function (a, b) {
                return a.value - b.value;
              }),
              roseType: 'radius',
              label: {
                normal: {
                  textStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                  }
                }
              },
              labelLine: {
                normal: {
                  lineStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                  },
                  smooth: 0.2,
                  length: 10,
                  length2: 20
                }
              },
              itemStyle: {
                normal: {
                  color: '#c23531',
                  shadowBlur: 200,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },

              animationType: 'scale',
              animationEasing: 'elasticOut',
              animationDelay: function (idx) {
                return Math.random() * 200;
              }
            }
          ]
        };
        myChart.setOption(option);
        myChart.hideLoading();
      },
      getAreaPriceInit(chartsData) {
        const myChart = echarts.init(document.getElementById('areaPrice'));
        myChart.showLoading();
        var option = {

          title: {
            text: '房价-面积比',
            x: 'center',
            y: 0,
            textStyle: {
              color: '#3259B8',
              fontSize: 16,
              fontWeight: 'normal',
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
          visualMap: {
            min: 15202,
            max: 159980,
            dimension: 1,
            left: 'right',
            top: 'top',
            text: ['HIGH', 'LOW'], // 文本，默认为数值文本
            calculable: true,
            itemWidth: 18,
            itemHeight: 160,
            textStyle: {
              color: '#3259B8',
              height: 56,
              fontSize: 11,
              lineHeight: 60,
            },
            inRange: {
              color: ['#3EACE5', '#F02FC2']
            },
            padding: [50, 20],
            orient: 'horizontal',
          },
          grid: {
            left: '5%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          tooltip: {
            trigger: 'item',
            showDelay: 0,
            formatter: function (params) {
              if (params.value.length > 1) {
                return 'Area: ' +
                  params.value[0] + '㎡<br/> ' + 'House price: ' +
                  params.value[1] + ' CNY/㎡ ';
              } else {
                return params.seriesName + ' :<br/>' +
                  params.name + ' : ' +
                  params.value + ' CNY/㎡ ';
              }
            },
            axisPointer: {
              show: true,
              type: 'cross',
              lineStyle: {
                type: 'dashed',
                width: 1
              }
            }
          },

          xAxis: [{
            type: 'value',
            scale: true,
            axisLabel: {
              formatter: '{value} ㎡'
            },
            nameTextStyle: {
              color: '#3259B8',
              fontSize: 14,
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                color: '#3259B8',
              }
            },
            splitLine: {
              show: false,
            }
          }],
          yAxis: [{
            type: 'value',
            scale: true,
            axisLabel: {
              formatter: '{value} CNY/㎡'
            },
            nameTextStyle: {
              color: '#3259B8',
              fontSize: 14
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                color: '#3259B8',
              }
            },
            splitLine: {
              show: false,
            }
          }],
          series: [{
            name: 'price-area',
            type: 'scatter',
            data: chartsData,
            symbolSize: 4,

          },

          ]
        };
        myChart.setOption(option);
        myChart.hideLoading();
      },
      getSubWayInit(chartsData) {
        const myChart = echarts.init(document.getElementById('subWayCharts'));
        myChart.showLoading();
        let option = {
          title: {
            text: '地铁与非地铁房价分布',
            x: 'center',
            y: 20,
            textStyle: {
              color: '#3259B8',
              fontSize: 16,
              fontWeight: 'normal',
            },
          },
          toolbox: {
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: [{
            height: '40%',
            left: '20%',
            right: '10%',
          },
            {
              top: '40%',
              height: '40%',
              left: '20%',
              right: '10%',
            }],
          legent: {
            data: ['有地铁', '无地铁'],
            textStyle: {
              color: '#3259B8',
            },
            top: 20,
          },
          yAxis: [{
            type: 'category',
            gridIndex: 0,
            data: ['有地铁'],
            nameTextStyle: {
              color: '#3259B8',
              fontSize: 16,
            },
            axisLine: {
              lineStyle: {
                color: '#3259B8',
              }
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              rotate: 0,
            },
            splitLine: {
              show: false
            }
          },
            {
              type: 'category',
              gridIndex: 1,
              data: ['无地铁'],
              nameTextStyle: {
                color: '#3259B8',
                fontSize: 16,
              },
              axisLine: {
                lineStyle: {
                  color: '#3259B8',
                }
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                rotate: 0,
              },
              splitLine: {
                show: false
              }
            }],
          xAxis: [{
            type: 'value',
            gridIndex: 0,
            nameTextStyle: {
              color: '#3259B8',
              fontSize: 14,
            },
            axisLabel: {
              show: false,
              formatter: '{value}'
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
              lineStyle: {
                color: '#3259B8',
              }
            },
            splitLine: {
              lineStyle: {
                color: '#A7BAFA',
              },
            }

          },
            {
              type: 'value',
              gridIndex: 1,
              nameTextStyle: {
                color: '#3259B8',
                fontSize: 14,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                formatter: '{value}'
              },
              axisLine: {
                lineStyle: {
                  color: '#3259B8',
                }
              },
              splitLine: {
                lineStyle: {
                  color: '#A7BAFA',
                },
              }

            }],
          series: [{
            name: '有地铁',
            type: 'boxplot',
            xAxisIndex: 0,
            yAxisIndex: 0,
            padding: [20, 0],
            boxWidth: ['10%', '20%'],
            data: [
              chartsData['subway']
            ],
            itemStyle: {
              normal: {
                borderColor: {
                  type: 'linear',
                  x: 1,
                  y: 0,
                  x2: 0,
                  y2: 0,
                  colorStops: [{
                    offset: 0,
                    color: '#F02FC2' // 0% 处的颜色
                  }, {
                    offset: 1,
                    color: '#956FD4' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                },
                borderWidth: 2,
                color: {
                  type: 'linear',
                  x: 1,
                  y: 0,
                  x2: 0,
                  y2: 0,
                  colorStops: [{
                    offset: 0,
                    color: 'rgba(240,47,194,0.7)'// 0% 处的颜色
                  }, {
                    offset: 1,
                    color: 'rgba(149,111,212,0.7)'  // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                },
              }
            },


            tooltip: {
              formatter: function (param) {
                return [
                  'upper: ' + param.data[5],
                  'Q3: ' + param.data[4],
                  'median: ' + param.data[3],
                  'Q1: ' + param.data[2],
                  'lower: ' + param.data[1]
                ].join('<br/>')
              }
            }
          },
            {
              name: '无地铁',
              xAxisIndex: 1,
              yAxisIndex: 1,
              type: 'boxplot',
              data: [
                chartsData['noSubway']
              ],
              boxWidth: ['10%', '20%'],
              itemStyle: {
                normal: {
                  borderColor: {
                    type: 'linear',
                    x: 1,
                    y: 0,
                    x2: 0,
                    y2: 0,
                    colorStops: [{
                      offset: 0,
                      color: '#3EACE5' // 0% 处的颜色
                    }, {
                      offset: 1,
                      color: '#956FD4' // 100% 处的颜色
                    }],
                    globalCoord: false // 缺省为 false
                  },
                  borderWidth: 2,
                  color: {
                    type: 'linear',
                    x: 1,
                    y: 0,
                    x2: 0,
                    y2: 0,
                    colorStops: [{
                      offset: 0,
                      color: 'rgba(62,172,299,0.7)'  // 0% 处的颜色
                    }, {
                      offset: 1,
                      color: 'rgba(149,111,212,0.7)'  // 100% 处的颜色
                    }],
                    globalCoord: false // 缺省为 false
                  },
                }
              },


              tooltip: {
                formatter: function (param) {
                  return [

                    'upper: ' + param.data[5],
                    'Q3: ' + param.data[4],
                    'median: ' + param.data[3],
                    'Q1: ' + param.data[2],
                    'lower: ' + param.data[1]
                  ].join('<br/>')
                }
              }
            },

          ]
        };
        myChart.setOption(option);
        myChart.hideLoading();
      },
      getPeoplePriceInit(chartsData) {
        const myChart = echarts.init(document.getElementById('peoplePrice'));
        myChart.showLoading();
        var dataBJ = chartsData['bjData'];

        var dataGZ = chartsData['gzData'];

        var dataSH = chartsData['shData'];

        var dataSZ = chartsData['szData'];

        var schema = [
          {name: 'date', index: 0, text: '户数'},
        ];

        var itemStyle = {
          normal: {
            opacity: 0.8,
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowOffsetY: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        };
        let option = {
          title: {
            text: "建年-均价比"
          },
          toolbox: {
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          backgroundColor: '#404a59',
          color: [
            '#dd4444', '#fec42c', '#80F1BE', '#243880'
          ],
          legend: {
            y: 'top',
            data: ['北京', '上海', '深圳', '广州'],
            textStyle: {
              color: '#fff',
              fontSize: 16
            }
          },
          grid: {
            x: '10%',
            x2: 150,
            y: '18%',
            y2: '10%'
          },
          tooltip: {
            padding: 10,
            backgroundColor: '#222',
            borderColor: '#777',
            borderWidth: 1,
            formatter: function (obj) {
              var value = obj.value;
              return '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'
                + obj.seriesName + ' ' + value[0] + '年'
                + '</div>'
                + schema[0].text + '：' + value[1] + '<br>'
                ;
            }
          },
          xAxis: {
            type: 'value',
            name: '年',
            nameGap: 16,
            nameTextStyle: {
              color: '#fff',
              fontSize: 14
            },
            min:1999,
            max:2017,
            splitLine: {
              show: false
            },
            axisLine: {
              lineStyle: {
                color: '#eee'
              }
            }
          },
          yAxis: {
            type: 'value',
            name: 'CNY/㎡',
            nameLocation: 'end',
            nameGap: 20,
            min:15000,
            nameTextStyle: {
              color: '#fff',
              fontSize: 16
            },
            axisLine: {
              lineStyle: {
                color: '#eee'
              }
            },
            splitLine: {
              show: false
            }
          },
          visualMap: [
            {
              left: 'right',
              top: '10%',
              dimension: 1,
              min:20000,
              max:80000,
              itemHeight: 60,
              calculable: true,
              precision: 0.1,
              text: ['圆形大小：CNY/㎡'],
              textGap: 30,
              textStyle: {
                color: '#fff'
              },
              inRange: {
                symbolSize: [1, 50]
              },
              outOfRange: {
                symbolSize: [10, 70],
                color: ['rgba(255,255,255,.2)']
              },
              controller: {
                inRange: {
                  color: ['#c23531']
                },
                outOfRange: {
                  color: ['#444']
                }
              }
            },
            // {
            //   left: 'right',
            //   bottom: '5%',
            //   dimension: 1,
            //   itemHeight: 60,
            //   min:0,
            //   max:80000,
            //   calculable: true,
            //   precision: 0.1,
            //   text: ['明暗：二氧化硫'],
            //   textGap: 30,
            //   textStyle: {
            //     color: '#fff'
            //   },
            //   inRange: {
            //     colorLightness: [1, 0.5]
            //   },
            //   outOfRange: {
            //     color: ['rgba(255,255,255,.2)']
            //   },
            //   controller: {
            //     inRange: {
            //       color: ['#c23531']
            //     },
            //     outOfRange: {
            //       color: ['#444']
            //     }
            //   }
            // }
          ],
          series: [
            {
              name: '北京',
              type: 'scatter',
              itemStyle: itemStyle,
              data: dataBJ
            },
            {
              name: '上海',
              type: 'scatter',
              itemStyle: itemStyle,
              data: dataSH
            },
            {
              name: '深圳',
              type: 'scatter',
              itemStyle: itemStyle,
              data: dataSZ
            },
            {
              name: '广州',
              type: 'scatter',
              itemStyle: itemStyle,
              data: dataGZ
            }
          ]
        };
        myChart.setOption(option);
        myChart.hideLoading();
      },
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
        this.$http.get('/api/chartsData/userStats').then((response) => {
          response = response.data;
          this.getPeopleChartInit(response)
        });
        this.$http.get('/api/chartsData/houseAreaRatio').then((response) => {
          response = response.data;
          let chartsData = [];
          response.forEach(ele => chartsData.push([ele['area'], ele['price']]));
          this.getAreaPriceInit(chartsData)
        });
        this.$http.get('/api/chartsData/subWayData').then((response) => {
          response = response.data;
          let trueJson = response['true'];
          let falseJson = response['false'];
          let subway = [trueJson['lower'], trueJson['q1'], trueJson['median'], trueJson['q3'], trueJson['upper']];
          let noSubway = [falseJson['lower'], falseJson['q1'], falseJson['median'], falseJson['q3'], falseJson['upper']];
          let chartsData = {'subway': subway, 'noSubway': noSubway};
          this.getSubWayInit(chartsData)
        });
        this.$http.get('/api/chartsData/yearPrice').then((response) => {
          response = response.data;
          let gzData = [];
          let szData = [];
          let shData = [];
          let bjData = [];
          response['gz'].forEach(ele => {
            gzData.push([ele['year'], ele['price'], ele['price']])
          });
          response['sz'].forEach(ele => {
            szData.push([ele['year'], ele['price'], ele['price']])
          });
          response['sh'].forEach(ele => {
            shData.push([ele['year'], ele['price'], ele['price']])
          });
          response['bj'].forEach(ele => {
            bjData.push([ele['year'], ele['price'], ele['price']])
          });
          let chartsData = {
            gzData,
            shData,
            szData,
            bjData
          };
          this.getPeoplePriceInit(chartsData)
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
