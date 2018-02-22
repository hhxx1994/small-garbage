<template>
  <div class="section">

    <el-form
      class="regform"
      label-width="0">
      <el-form-item>
        <div>
          <el-checkbox-group v-model="checkboxGroup">
            <el-checkbox-button v-for="label in labels" :label="label" :key="label">{{label}}</el-checkbox-button>
          </el-checkbox-group>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button
          type="success"
          class="submitBtn"
          round
          @click.native.prevent="submit"
          >
          关注标签
        </el-button>
      </el-form-item>
    </el-form>
  </div>


</template>
<script>
  let labels = [];
  export default {
    data() {
      return {
        checkboxGroup: [],
        labels: labels
      };
    },
    mounted() {
      this.$nextTick(function () {
        this.$http.get('/api/user/label').then((response) => {
          response.data.forEach(ele => {
            labels.push(ele)
          })

        });

      })
    }

  }
</script>
<style scoped>
  .top {
    width: 370px;
    margin: 100px auto 0;
    border-radius: 15px;
    box-shadow: 0 0 1px #B4BCCC;
  }

  .regform {
    margin: 20px auto;
    width: 400px;
    height: 400px;
    background: #fff;
    box-shadow: 0 0 10px #B4BCCC;
    padding: 30px 30px 0 30px;
    border-radius: 25px;
  }

  .submitBtn {
    width: 90%;
  }

  .to {
    color: #FA5555;
    cursor: pointer;
  }
</style>
