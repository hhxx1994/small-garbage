<template>
  <div class="chart-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="房源浏览" name="first"><house-info></house-info></el-tab-pane>
      <el-tab-pane label="猜你喜欢" name="second"><houseRecommend></houseRecommend></el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import houseInfo from './houseInfo'
  import houseRecommend from './houseRecommend'
  export default {
    data() {
      return {
        activeName: 'first'
      };
    },
    components: {
      houseInfo:houseInfo,
      houseRecommend:houseRecommend
    },
    methods: {
      handleClick(tab, event) {

      }
    }
  };

</script>
<style>

</style>
