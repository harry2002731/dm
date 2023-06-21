<template>
  <div class="app-container">
    <switch-roles @change="handleRolesChange" />
    <template>
      <div>
        <p /><h1><button type="button" class="btn btn-default btn-primary" @click="show()">载入数据</button></h1>
        <h1><div id="show" style="width: 600px;height:400px;" /></h1>
        <!--        </p>-->
        <!--        <p /><p>input 支持度：</p>-->
        <!--        <h1><input v-model="support" placeholder="0.4"></h1>-->
        <!--        <p>input 置信度：</p>-->
        <!--        <h1><input v-model="confidence" placeholder="0.4"></h1>-->
        <!--        <h1><button type="button" class="btn btn-default btn-primary" @click="result()">分析数据</button></h1>-->
        <!--        <h1><div id="result" style="width: 1000px;height:600px;" /></h1>-->
        <!--        </p>-->
        <v-chart class="chart" :option="option" autoresize />
        <el-main>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>数据展示</el-breadcrumb-item>
          </el-breadcrumb>
          <el-row :gutter="20" class="row-pane">
            <el-col :span="12" class="col-2">
              <el-row>
                <div id="echart1" />
              </el-row>
            </el-col>
          </el-row>

        </el-main>
      </div>

    </template>
  </div>
</template>

<script>
import SwitchRoles from './components/SwitchRoles'
import axios from 'axios'
export default {
  name: 'PagePermission',
  components: { SwitchRoles },
  data() {
    return {
      aa: 'hello',
      support: 0.4,
      confidence: 0.4,
      resources: '',
      days: [],
      hours: [],
      dataHot: []
    }
  },
  created() {
    this.getMessage()
  },
  methods: {
    handleRolesChange() {
      this.$router.push({ path: '/permission/index?' + +new Date() })
    },
    show() {
      const echarts = require('echarts')
      const myChart = echarts.init(document.getElementById('echart1'), 'walden')

      const option = {
        title: {
          text: 'Traffic Sources',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['Direct', 'Email', 'Ad Networks', 'Video Ads', 'Search Engines']
        },
        series: [
          {
            name: 'Traffic Sources',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: [
              { value: 335, name: 'Direct' },
              { value: 310, name: 'Email' },
              { value: 234, name: 'Ad Networks' },
              { value: 135, name: 'Video Ads' },
              { value: 1548, name: 'Search Engines' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }

        ]
      }
      myChart.setOption(option)
      // var myChart = this.$echarts.init(document.getElementById('show'))
      // myChart.setOption({
      //   series: [
      //     {
      //       name: '访问来源',
      //       type: 'pie', // 设置图表类型为饼图
      //       radius: '55%', // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
      //       data: [ // 数据数组，name 为数据项名称，value 为数据项值
      //         { value: 235, name: '视频广告' },
      //         { value: 274, name: '联盟广告' },
      //         { value: 310, name: '邮件营销' },
      //         { value: 335, name: '直接访问' },
      //         { value: 400, name: '搜索引擎' }
      //       ]
      //     }
      //   ]
      // }
      // )
    },
    getMessage() {
      const url = 'http://127.0.0.1:5000/heatmap'
      axios.get(url).then((res) => {
        this.resources = res.data
        this.hours = this.resources.hours
        this.days = this.resources.days
        this.dataHot = this.resources.dataHot.map(function(item) {
          return [item[1], item[0], item[2] || '-']
        })
      })
        .catch((error) => {
          console.error(error)
        })
    },
    result() {
      var myChart = this.$echarts.init(document.getElementById('result'))
      console.log(this.days)
      console.log(this.dataHot)
      var option = {
        tooltip: {
          position: 'top'
        },
        grid: {
          height: '50%',
          top: '10%'
        },
        xAxis: {
          type: 'category',
          data: this.hours,
          splitArea: {
            show: true
          }
        },
        yAxis: {
          type: 'category',
          data: this.days,
          splitArea: {
            show: true
          }
        },
        visualMap: {
          min: 0,
          max: 10,
          calculable: true,
          orient: 'horizontal',
          left: 'center',
          bottom: '15%'
        },
        series: [{
          name: 'Punch Card',
          type: 'heatmap',
          data: this.dataHot,
          label: {
            show: true
          },
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }

      myChart.setOption(option)
    }

  }
}

</script>

<style scoped>
#echart1 {
  height: 30vh;
}
</style>
