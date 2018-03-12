<template>
  <div class="chart-container">
    <el-row>
      <el-col :span="18" class="chart">
        <div id="container4" style="height: 500px"></div>
      </el-col>
      <el-col :span="6" class="chart">
        <div id="container5" style="height: 500px">
          <el-row>
            <el-col :span="24">
              <div class="block">
                <label>每平米价格:</label>
                <el-slider :range="true" :max="20" :step="0.1" v-model="form.unitPrice"></el-slider>
              </div>
            </el-col>

          </el-row>
          <br>
          <el-row>
            <el-col :span="24">
              <div class="block">
                <label>面积大小:</label>
                <el-slider :range="true" :max="200" :min="30" :step="1" v-model="form.area"></el-slider>
              </div>
            </el-col>

          </el-row>
          <br>
          <el-row>
            <el-col :span="24">
              <div class="block">
                <label>建造时间(年):</label>
                <el-slider :range="true" :max="2018" :min="1970" :step="1" v-model="form.year"></el-slider>
              </div>
            </el-col>

          </el-row>

          <br>
          <el-row>
            <el-col :span="24">
              <div class="block">
                <label>小区名称:</label>
                <el-input v-model="form.name" placeholder="请输入内容"></el-input>
              </div>
            </el-col>

          </el-row>

          <br>
          <el-row>
            <el-col :span="24">
              <div class="block">
                <el-button @click="submit" type="primary">提交</el-button>
              </div>
            </el-col>

          </el-row>
        </div>
      </el-col>

    </el-row>


  </div>
</template>
<script>

  import echarts from 'echarts'
  import bmap from 'echarts/extension/bmap/bmap'


  export default {
    data() {
      return {
        form: {
          unitPrice: [0, 20],
          area: [30, 200],
          year: [1970, 2018],
          name: "",
          dom: {},
          myChart: {},
        },

      };
    },

    methods: {
      getUserChartInit2(chartsData) {


        var data = [];
        var geoCoordMap = {};
        chartsData.forEach(ele => {
          let obj = {name: ele['name'], value: ele['value']};
          data.push(obj);
          geoCoordMap[ele['name']] = ele['coords'];
        });

        var convertData = function (data) {
          var res = [];
          for (var i = 0; i < data.length; i++) {
            var geoCoord = geoCoordMap[data[i].name];
            if (geoCoord) {
              res.push({
                name: data[i].name,
                value: geoCoord.concat(data[i].value),
                tooltip: {
                  formatter: '{b}'
                }
              });
            }
          }
          return res;
        };

        let realData=convertData(data);
        console.log(realData);


        var option = {
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
          tooltip: {
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
          series: [
            {
              type: 'scatter',
              coordinateSystem: 'bmap',
              data: realData,
              symbolSize: function (val) {
                return val[2] / 10000;
              },
              label: {
                normal: {
                  show: false,
                  formatter: '{b}',
                  position: 'right',
                },
                emphasis: {
                  show: true,
                  formatter: '{b}',
                  position: 'right',
                }
              },
              itemStyle: {
                normal: {
                  color: 'purple'
                },
                emphasis: {
                  color: 'purple'
                }
              }
            },

          ]
        };
        this.myChart.setOption(option);



      },
      submit: function () {
        let param = {
          unitPrice: [this.form.unitPrice[0] * 10000, this.form.unitPrice[1] * 10000],
          area: this.form.area,
          year: this.form.year,
          name: this.form.name,
        }
        this.$http.post('/api/recommend/search', param).then((response) => {
          response = response.data;
          this.getUserChartInit2(response);
        });
      },
      init: function () {
        this.dom = document.getElementById('container4');
        this.myChart = echarts.init(this.dom);
      }
    },
    mounted() {
      this.$nextTick(function () {
        this.init();
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
