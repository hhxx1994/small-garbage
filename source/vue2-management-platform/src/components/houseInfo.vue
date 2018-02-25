<template>
  <div class="chart-container">
    <span v-for="item in houseInfo">
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
          <li @click="liClick($event)" :id="item['houseId']">
           <el-rate class="title"
                    v-model="scores[item['houseId']]"
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
        scores: {}
      };
    },
    methods: {
      liClick(e) {
        let id=e.currentTarget.id
        console.log(id)
        console.log(this.scores[id])
        console.log(this.scores)
      },
      changePage(currentPage) {
        this.getData(currentPage)
      },
      getData(currentPage) {
        this.$http.get('/api/houseInfo/list?currentPage=' + currentPage).then((response) => {
          response = response.data;
          this.total = response['total']
          response['list'].forEach(ele => {
            this.houseInfo.push(ele)
            this.scores["${ele['houseId']}"]=ele['score']
          })

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
