<template>
  <div class="chart-container">
    <el-row>
      <el-col :span="12" class="chart chart_left">
        <div id="allmap3" style="height: 500px">图表加载失败</div>
      </el-col>
    </el-row>


  </div>
</template>
<script>
  import echarts from 'echarts'
  import beijing from 'echarts/map/json/province/beijing'

  export default {
    data() {
      return {};
    },
    methods: {
      getUserChartInit3(response) {
        let chartsData=[];
        response.forEach(ele => {
          chartsData.push({name:ele['name'],value:ele['price']})
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
    },
    mounted() {
      this.$nextTick(function () {
        this.$http.get('/api/chartsData/communityByBj').then((response) => {
          response = response.data;
          this.getUserChartInit3(response);
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
