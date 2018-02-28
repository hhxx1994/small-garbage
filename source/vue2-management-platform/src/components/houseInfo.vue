<template>
  <div class="chart-container">
    <span v-for="(item, index) in houseInfo">
      <el-row>
      <el-col :span="24">
        <ul>
          <li>
            <div>
              <a :href="item['link']" target="_blank"
                 data-log_index="1" data-el="ershoufang" data-housecode="FS0003264177" data-is_focus="" data-sl="">
                <img class="img"
                     :src="item['img']"></a>
            </div>
          </li>
          <li>
            <div class="title"> <a :href="item['link']" target="_blank"
                                   data-log_index="1" data-el="ershoufang" data-housecode="FS0003264177"
                                   data-is_focus="" data-sl="">{{item['title']}}</a></div>
          </li>
          <li @click="liClick($event)" :id="item['houseId']" :title="index">
           <el-rate class="title"
                    v-model="houseInfo[index]['score']"
                    show-text>
          </el-rate>
          </li>
        </ul>
      </el-col>
    </el-row>
    <hr>
    </span>
    <el-row class="page">
      <el-col :span="24">
        <el-pagination @current-change="changePage"
                       background
                       layout="prev, pager, next"
                       :total='total'
        >
        </el-pagination>
      </el-col>
    </el-row>

  </div>
</template>
<script>


  export default {
    data() {
      return {
        total: 0,
        houseInfo: [],
      };
    },
    methods: {
      liClick(e) {
        let id = e.currentTarget.id
        let title=e.currentTarget.title
        let score=this.houseInfo[title]['score']
        this.$http.get('/api/houseInfo/score?houseId=' + id + "&score=" + score+"&userId="+this.$store.state.user.id).then((response) => {
        });

      },
      changePage(currentPage) {
        this.getData(currentPage)
      },
      getData(currentPage) {
        this.$http.get('/api/houseInfo/list?currentPage=' + currentPage+"&userId="+this.$store.state.user.id).then((response) => {
          response = response.data;
          this.total = response['total']
          this.houseInfo = response['list']
        });
      }
    },
    mounted() {
      this.$nextTick(function () {
        this.getData(1)
      })
    }
  }

</script>
<style>


  .img {
    float: left;
    position: relative;
    width: 232px;
    height: 174px;
  }

  .title {
    float: left;
    margin-left: 30px;
    margin-top: 75px;
    text-decoration: none;
    font-size: 20px;
    color: #394043;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .page {
    margin-top: 20px;
    text-align: center;
  }


</style>
