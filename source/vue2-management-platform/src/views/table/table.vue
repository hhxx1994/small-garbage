<template>
  <div class="chart-container">
    <el-row>
      <el-col :span="24" class="chart">
        <div id="container4" style="height: 500px">图表加载失败</div>
      </el-col>

    </el-row>


  </div>
</template>
<script>

  import echarts from 'echarts'
  import bmap from 'echarts/extension/bmap/bmap'


  export default {
    data() {
      return {};
    },
    methods: {
      getUserChartInit2(chartsData) {
        var dom=document.getElementById('container4');

        const myChart = echarts.init(dom);
        myChart.showLoading();
        var data = [];
        var geoCoordMap = {};
        chartsData.forEach(ele=>{
          let obj={name:ele['name'],value:ele['value']};
          data.push(obj);
          geoCoordMap[ele['name']]=ele['coords'];
        });

        var convertData = function (data) {
          var res = [];
          for (var i = 0; i < data.length; i++) {
            var geoCoord = geoCoordMap[data[i].name];
            if (geoCoord) {
              res.push({
                name: data[i].name,
                value: geoCoord.concat(data[i].value)
              });
            }
          }
          return res;
        };


        var option =   {
          title: {
            text: '小区-房价',
            left: 'center'
          },
          toolbox: {
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          tooltip : {
            trigger: 'item'
          },
          bmap: {
            center: [108.824129, 34.600339],
            zoom: 5,
            roam: true,
            mapStyle: {
              styleJson: [{
                'featureType': 'water',
                'elementType': 'all',
                'stylers': {
                  'color': '#d1d1d1'
                }
              }, {
                'featureType': 'land',
                'elementType': 'all',
                'stylers': {
                  'color': '#f3f3f3'
                }
              }, {
                'featureType': 'railway',
                'elementType': 'all',
                'stylers': {
                  'visibility': 'off'
                }
              }, {
                'featureType': 'highway',
                'elementType': 'all',
                'stylers': {
                  'color': '#fdfdfd'
                }
              }, {
                'featureType': 'highway',
                'elementType': 'labels',
                'stylers': {
                  'visibility': 'off'
                }
              }, {
                'featureType': 'arterial',
                'elementType': 'geometry',
                'stylers': {
                  'color': '#fefefe'
                }
              }, {
                'featureType': 'arterial',
                'elementType': 'geometry.fill',
                'stylers': {
                  'color': '#fefefe'
                }
              }, {
                'featureType': 'poi',
                'elementType': 'all',
                'stylers': {
                  'visibility': 'off'
                }
              }, {
                'featureType': 'green',
                'elementType': 'all',
                'stylers': {
                  'visibility': 'off'
                }
              }, {
                'featureType': 'subway',
                'elementType': 'all',
                'stylers': {
                  'visibility': 'off'
                }
              }, {
                'featureType': 'manmade',
                'elementType': 'all',
                'stylers': {
                  'color': '#d1d1d1'
                }
              }, {
                'featureType': 'local',
                'elementType': 'all',
                'stylers': {
                  'color': '#d1d1d1'
                }
              }, {
                'featureType': 'arterial',
                'elementType': 'labels',
                'stylers': {
                  'visibility': 'off'
                }
              }, {
                'featureType': 'boundary',
                'elementType': 'all',
                'stylers': {
                  'color': '#fefefe'
                }
              }, {
                'featureType': 'building',
                'elementType': 'all',
                'stylers': {
                  'color': '#d1d1d1'
                }
              }, {
                'featureType': 'label',
                'elementType': 'labels.text.fill',
                'stylers': {
                  'color': '#999999'
                }
              }]
            }
          },
          series : [
            {
              type: 'scatter',
              coordinateSystem: 'bmap',
              data: convertData(data),
              symbolSize: function (val) {
                return val[2] / 10000;
              },
              label: {
                normal: {
                  formatter: '',
                  position: 'right',
                  show: false
                },
                emphasis: {
                  formatter: '{b}:{@[2]}',
                  position: 'right',
                  show: true
                }
              },
              itemStyle: {
                normal: {
                  color: 'purple'
                }
              }
            },
            {
              type: 'effectScatter',
              coordinateSystem: 'bmap',
              data: convertData(data.sort(function (a, b) {
                return b.value - a.value;
              }).slice(0, 10)),
              symbolSize: function (val) {
                return val[2] / 10000;
              },
              showEffectOn: 'render',
              rippleEffect: {
                brushType: 'stroke'
              },
              label: {
                normal: {
                  formatter: '',
                  position: 'right',
                  show: false
                },
                emphasis:{
                  formatter: '{b}:{@[2]}',
                  position: 'right',
                  show: true
                }
              },
              itemStyle: {
                normal: {
                  color: 'red',
                  shadowBlur: 10,
                  shadowColor: '#333'
                }
              },
              zlevel: 1
            }
          ]
        };

        myChart.setOption(option);

        myChart.hideLoading();

      },
    },
    mounted() {
      this.$nextTick(function () {
        this.$http.get('/api/chartsData/locationData').then((response) => {
          response = response.data;
          this.getUserChartInit2(response);
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
