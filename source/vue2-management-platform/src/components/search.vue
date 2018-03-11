<template>
  <div class="chart-container">
    <el-row>
      <el-col :span="24">
        <div class="block">
          <label>每平米价格(万):</label>
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
          <el-slider :range="true" :max="2018" :min="1970" :step="1"  v-model="form.year"></el-slider>
        </div>
      </el-col>

    </el-row>

    <br>
    <el-row>
      <el-col :span="24">
        <div class="block">
          <label>小区名称:</label>
          <el-input v-model="name" placeholder="请输入内容"></el-input>
        </div>
      </el-col>

    </el-row>

  </div>
</template>
<script>
  export default {
    data() {
      return {
        form: {
          unitPrice: [0,20],
          area:[30,200],
          year:[1970,2018],
          name:"",
        }
      }
    },
    methods: {}
  }
</script>
