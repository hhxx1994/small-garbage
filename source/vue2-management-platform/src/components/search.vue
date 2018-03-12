<template>
  <div class="chart-container">
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
</template>
<script>
  export default {
    data() {
      return {
        form: {
          unitPrice: [0, 20],
          area: [30, 200],
          year: [1970, 2018],
          name: "",
        }
      }
    },
    methods: {
      submit: function () {
        let param = {
          unitPrice: [this.form.unitPrice[0] * 10000, this.form.unitPrice[1] * 10000],
          area: this.form.area,
          year: this.form.year,
          name: this.form.name,
        }
        this.$http.post('/api/recommend/search', param).then((response) => {
          response = response.data;
          sessionStorage.setItem('searchData', JSON.stringify(response))
          this.$store.dispatch('searchDataAction')
          this.$parent.getUserChartInit2();

        });
      }

    }
  }
</script>
