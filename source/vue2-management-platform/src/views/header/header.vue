<template>
  <div>
    <header>
      <ul class="header-operations">
        <li>
          <el-button class="button" @click.native="tologin" type="danger" size="small" round>登录</el-button>
          <el-button class="button" @click.native="toRegin" type="success" size="small" round>注册</el-button>
        </li>
        <li></li>
      </ul>
    </header>
  </div>
</template>
<script>
  import 'font-awesome/css/font-awesome.min.css';

  export default {
    props: {
      user: {}
    },
    data() {
      return {};
    },
    methods: {


      tologin() {
        this.$router.push('/login')
      },
      toRegin() {
        this.$router.push('/regin')
      },


    }
  };
</script>
<style>
  header {
    background-color: rgb(32, 160, 255);
    height: 80px;
    position: absolute;
    width: 100%;
    top: 0;
    left: 0;
    padding: 0 20px;
    z-index: 999;
    box-sizing: border-box;
    position: fixed;
  }

  .header-logo {
    display: inline-block;
    vertical-align: middle;
  }

  .header-operations {
    display: inline-block;
    float: right;
    padding-right: 30px;
    height: 100%;
  }

  .header-operations li {
    color: #fff;
    display: inline-block;
    vertical-align: middle;
    padding: 0 10px;
    margin: 0 10px;
    line-height: 80px;
    cursor: pointer;
  }

  .header-operations li:first-child {
    cursor: default
  }

  .header-opacity {
    opacity: .7;
  }

  .header-operations:after, header:after {
    display: inline-block;
    content: "";
    height: 100%;
    vertical-align: middle;
  }
</style>
