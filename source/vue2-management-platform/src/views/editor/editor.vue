<template>
  <div class="chart-container">
    <el-row>
      <el-col :span="12" class="chart chart_left">
        <div id="allmap3" style="height: 500px"></div>
      </el-col>
      <el-col :span="12" class="chart chart_left">
        <div id="allmap4" style="height: 500px"></div>
      </el-col>
    </el-row>
    <hr>
    <el-row>
      <el-col :span="12" class="chart chart_left">
        <div id="allmap5" style="height: 500px"></div>
      </el-col>
      <el-col :span="12" class="chart chart_left">
        <div id="allmap6" style="height: 500px"></div>
      </el-col>
    </el-row>


  </div>
</template>
<script>
  import echarts from 'echarts'
  import beijing from 'echarts/map/json/province/beijing'
  import shanghai from 'echarts/map/json/province/shanghai'
  import guangzhou from '../../map/guangzhou'
  import shenzhen from '../../map/shenzhen'

  export default {
    data() {
      return {};
    },
    methods: {
      getUserChartInit3(response) {
        let chartsData = [];
        response.forEach(ele => {
          chartsData.push({name: ele['name'], value: ele['price']})
        });
        echarts.registerMap('bj', beijing);
        var dom = document.getElementById('allmap3');
        const myChart = echarts.init(dom);
        myChart.showLoading();
        var option = {
          backgroundColor: '#ffffff',
          title: {
            text: "北京市各区房价",
            left: 'center',
            textStyle: {
              color: '#000'
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
            min: 0,
            max: 125000,
            dimension: 0,
            left: 'left',
            top: 'top',
            text: ['HIGH', 'LOW'], // 文本，默认为数值文本
            calculable: true,
            inRange: {
              color: ['#3EACE5', '#F02FC2']
            }
          },
          series: [{
            type: 'map',
            mapType: 'bj',
            label: {
              normal: {
                show: false,
              },
              emphasis: {
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.8)'
                }
              }
            },
            itemStyle: {

              normal: {
                borderColor: '#fff',
                borderWidth: 1,
                areaColor: '#000',
              },
              emphasis: {
                areaColor: '#EABBFF',
                borderColor: 'rgb(255,222,254)',
                borderWidth: 1,
              }
            },
            animation: false,
            data: chartsData
            // animationDurationUpdate: 1000,
            // animationEasingUpdate: 'quinticInOut'
          }]
        };

        myChart.setOption(option);

        myChart.hideLoading();

      },
      getUserChartInit4(response) {
        let chartsData = [];
        response.forEach(ele => {
          chartsData.push({name: ele['name'], value: ele['price']})
        });
        echarts.registerMap('sh', shanghai);
        var dom = document.getElementById('allmap4');
        const myChart = echarts.init(dom);
        myChart.showLoading();
        var option = {
          backgroundColor: '#ffffff',
          title: {
            text: "上海市各区房价",
            left: 'center',
            textStyle: {
              color: '#000'
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
            min: 0,
            max: 125000,
            dimension: 0,
            left: 'left',
            top: 'top',
            text: ['HIGH', 'LOW'], // 文本，默认为数值文本
            calculable: true,
            inRange: {
              color: ['#3EACE5', '#F02FC2']
            }
          },
          series: [{
            type: 'map',
            mapType: 'sh',
            label: {
              normal: {
                show: false,
              },
              emphasis: {
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.8)'
                }
              }
            },
            itemStyle: {

              normal: {
                borderColor: '#fff',
                borderWidth: 1,
                areaColor: '#000',
              },
              emphasis: {
                areaColor: '#EABBFF',
                borderColor: 'rgb(255,222,254)',
                borderWidth: 1,
              }
            },
            animation: false,
            data: chartsData
            // animationDurationUpdate: 1000,
            // animationEasingUpdate: 'quinticInOut'
          }]
        };

        myChart.setOption(option);

        myChart.hideLoading();

      },
      getUserChartInit5(response) {
        let chartsData = [];
        response.forEach(ele => {
          chartsData.push({name: ele['name'], value: ele['price']})
        });
        echarts.registerMap('gz', guangzhou);
        var dom = document.getElementById('allmap5');
        const myChart = echarts.init(dom);
        myChart.showLoading();
        var option = {
          backgroundColor: '#ffffff',
          title: {
            text: "广州市各区房价",
            left: 'center',
            textStyle: {
              color: '#000'
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
            min: 0,
            max: 125000,
            dimension: 0,
            left: 'left',
            top: 'top',
            text: ['HIGH', 'LOW'], // 文本，默认为数值文本
            calculable: true,
            inRange: {
              color: ['#3EACE5', '#F02FC2']
            }
          },
          series: [{
            type: 'map',
            mapType: 'gz',
            label: {
              normal: {
                show: false,
              },
              emphasis: {
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.8)'
                }
              }
            },
            itemStyle: {

              normal: {
                borderColor: '#fff',
                borderWidth: 1,
                areaColor: '#000',
              },
              emphasis: {
                areaColor: '#EABBFF',
                borderColor: 'rgb(255,222,254)',
                borderWidth: 1,
              }
            },
            animation: false,
            data: chartsData
            // animationDurationUpdate: 1000,
            // animationEasingUpdate: 'quinticInOut'
          }]
        };

        myChart.setOption(option);

        myChart.hideLoading();

      },
      getUserChartInit6(response) {
        let chartsData = [];
        response.forEach(ele => {
          chartsData.push({name: ele['name'], value: ele['price']})
        });
        echarts.registerMap('sz', shenzhen);
        var dom = document.getElementById('allmap6');
        const myChart = echarts.init(dom);
        myChart.showLoading();
        var option = {
          backgroundColor: '#ffffff',
          title: {
            text: "深圳市各区房价",
            left: 'center',
            textStyle: {
              color: '#000'
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
            min: 0,
            max: 125000,
            dimension: 0,
            left: 'left',
            top: 'top',
            text: ['HIGH', 'LOW'], // 文本，默认为数值文本
            calculable: true,
            inRange: {
              color: ['#3EACE5', '#F02FC2']
            }
          },
          series: [{
            type: 'map',
            mapType: 'sz',
            label: {
              normal: {
                show: false,
              },
              emphasis: {
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.8)'
                }
              }
            },
            itemStyle: {

              normal: {
                borderColor: '#fff',
                borderWidth: 1,
                areaColor: '#000',
              },
              emphasis: {
                areaColor: '#EABBFF',
                borderColor: 'rgb(255,222,254)',
                borderWidth: 1,
              }
            },
            animation: false,
            data: chartsData
            // animationDurationUpdate: 1000,
            // animationEasingUpdate: 'quinticInOut'
          }]
        };

        myChart.setOption(option);

        myChart.hideLoading();

      },
    },
    mounted() {
      this.$nextTick(function () {
        this.$http.get('/api/chartsData/community?area=bj').then((response) => {
          let data = response.data;
          this.getUserChartInit3(data);
        });
        this.$http.get('/api/chartsData/community?area=sh').then((response) => {
          let data = response.data;
          this.getUserChartInit4(data);
        });
        this.$http.get('/api/chartsData/community?area=gz').then((response) => {
          let data = response.data;
          this.getUserChartInit5(data);
        });
        this.$http.get('/api/chartsData/community?area=sz').then((response) => {
          let data = response.data;
          this.getUserChartInit6(data);
        });


      })
    }
  }
</script>
<style>
  .form-section {
    padding: 10px;
    width: 500px;
  }
</style>
