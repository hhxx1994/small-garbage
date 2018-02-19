<template>
  <div class="chart-container">
    <el-row>
      <el-col :span="24" class="chart chart_left">
        <div id="allmap" style="height: 600px">图表加载失败</div>
      </el-col>

    </el-row>


  </div>
</template>
<script>


  export default {
    data() {
      return {};
    },
    methods: {},
    mounted() {
      this.$nextTick(function () {
        // 百度地图API功能
        var map = new BMap.Map("allmap");    // 创建Map实例
        map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);  // 初始化地图,设置中心点坐标和地图级别
        //添加地图类型控件
        map.addControl(new BMap.MapTypeControl({
          mapTypes:[
            BMAP_NORMAL_MAP,
            BMAP_HYBRID_MAP
          ]}));
        map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
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
