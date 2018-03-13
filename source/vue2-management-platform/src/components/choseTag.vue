<template>
  <div class="section">
    <div class="top">
    </div>
    <el-form
      class="regform"
      label-width="0">
      <el-form-item>
        <div>
          <el-checkbox-group v-model="checkboxGroup">
              <span v-for="(label,index) in labels">
                <span v-if="(index+1) % 4 == 0">
                  <el-checkbox-button class="checkbox" :label="label" :key="label">
                    {{label}}
                  </el-checkbox-button>
                  <br/>
                </span>
                <span v-else>
                   <el-checkbox-button class="checkbox" :label="label" :key="label">
                    {{label}}
                  </el-checkbox-button>
                </span>
              </span>
          </el-checkbox-group>
        </div>
      </el-form-item>
      <el-form-item class="form-item">
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
    methods: {
      submit() {
        let param = this.checkboxGroup;
        if (param.length === 0) {
          alert("至少选取一个用户标签");
        } else {
          let params = new URLSearchParams();
          for (let i = 0; i < param.length; i++) {
            params.append("tags", param[i]);
          }
          // console.log(this.$store.state)
          params.append("userId", this.$store.state.user.id);
          this.$http.post('/api/user/tags', params).then((response) => {
            sessionStorage.removeItem('user')
            this.$store.dispatch('logout')
            this.$router.push('/')
          })
        }
      },
    },
    mounted() {
      this.$nextTick(function () {
        this.$http.get('/api/user/label').then((response) => {
          while (labels.length !== 0) {
            labels.pop()
          }
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
    width: 380px;
    height: 300px;
    background: #fff;
    box-shadow: 0 0 10px #B4BCCC;
    padding: 30px 30px 0 30px;
    border-radius: 25px;
  }

  .submitBtn {
    width: 90%;
  }

  .checkbox {
    padding: 15px;
  }

  .form-item {
    padding: 30px;
  }


</style>
