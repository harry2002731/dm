<template>
  <div class="app-container">
    <template>
      <PaginationTable :columns="columns" />
      <div>
        <div class="demo-input-suffix">
          <h1 class="support">
            <el-input
              v-model="input1"
              size="medium"
              placeholder="请输入 决策树高度"
              clearable
            />
          </h1>
          <h1 class="support">
            <el-input
              v-model="input2"
              size="medium"
              placeholder="请输入 叶子节点样本数"
              clearable
            />
          </h1>
          <el-button type="primary" @click="showChart()">决策树结果</el-button>
          <div v-if="show_chart" class="demo-image__placeholder">
            <div class="block">
              <el-image :src="src" />
            </div>
          </div>
        </div>

        <div class="demo-input-suffix2">
          <h1 class="support">
            <el-input
              v-model="input3"
              size="medium"
              placeholder="请输入 petW"
              clearable
            />
          </h1>
          <h1 class="support">
            <el-input
              v-model="input4"
              size="medium"
              placeholder="请输入 sepL"
              clearable
            />
          </h1>
          <h1 class="support">
            <el-input
              v-model="input5"
              size="medium"
              placeholder="请输入 species"
              clearable
            />
          </h1>
          <h1 class="support">
            <el-input
              v-model="input6"
              size="medium"
              placeholder="请输入 petL"
              clearable
            />
          </h1>
          <el-button type="primary" @click="post()">计算结果</el-button>
          <h1 class="support">
            <el-input
              v-model="input7"
              size="medium"
              placeholder="结果"
              clearable
            />
          </h1>
        </div>
        <v-chart class="echart1" :option="option" autoresize />
        <el-main>
          <el-row>
            <div id="echart1" />
          </el-row>
        </el-main>
      </div>
    </template>
  </div>
</template>

<script>

// import ecStat from 'echarts-stat'
import PaginationTable from './component/pagination-table.vue'

import axios from 'axios'
// import echarts from 'echarts'
export default {
  name: 'PagePermission',
  components: { PaginationTable },
  // components: { SwitchRoles },
  data() {
    return {
      aa: 'hello',
      support: 0.4,
      confidence: 0.4,
      days: [],
      hours: [],
      dataHot: [],
      tableData: [], // 存储表格数据的数组
      showTable: false, // 控制表格显示的标志
      showErrorDialog: false,
      isFocused: false,
      isEditing: false,
      input1: '',
      input2: '',
      src: 'http://localhost:8080/images/tree?height=' + this.input1 + '&leaves=' + this.input2,
      input3: '',
      input4: '',
      input5: '',
      input6: '',
      input7: '',
      show_chart: false,
      a: [['category', 'precision', 'recall']],
      columns: [
        { prop: 'id', label: 'id' },
        { prop: 'petW', label: 'petW' },
        { prop: 'sepL', label: 'sepL' },
        { prop: 'species', label: 'species' },
        { prop: 'petL', label: 'petL' },
        { prop: 'sepW', label: 'sepW' }
      ],
      charts: '',
      score: '100',
      source: [
        ['category', 'precision', 'recall'],
        ['cart decision tree', 45, 12],
        ['logistic regression', 30, 15],
        ['F1-measure', 45, 8]
      ]
    }
  },
  mounted() {
    if (this.show_chart) { this.draw('report') }
  },

  methods: {
    draw(id) {
      this.$axios.get('http://localhost:8080/classification/classify_vali')
        .then((res) => {
          console.log('访问后台')
          // console.log(res.data)
          this.source = res.data
          for (var i = 0; i < res.data.length; i++) {
            this.a.push([res.data[i].name, res.data[i].precision, res.data[i].recall])
          }
          const echarts = require('echarts')
          this.charts = echarts.init(document.getElementById('echart1'), 'walden')
          this.charts.setOption({
            title: {
              text: 'Comparison of Decision Tree'
            },
            legend: {},
            tooltip: {},
            dataset: {
              source: this.a // 连接数据
            },
            xAxis: { type: 'category' },
            yAxis: {},
            grid: { bottom: 30 },
            series: [
              {
                type: 'bar',
                barCategoryGap: '40%',
                itemStyle: { color: '#999' },
                tooltip: {
                  formatter: params => {
                    // console.log(params)
                    return ` ${params.value[0]} <br/>
                         ${params.seriesName}:${params.value[1]}`
                  }
                }
              },
              {
                type: 'bar',
                barCategoryGap: '40%',
                itemStyle: { color: '#81cebe' },
                tooltip: {
                  formatter: params => {
                    return ` ${params.value[0]} <br/>
                         ${params.seriesName}:${params.value[2]}`
                  }
                }
              }
            ]

          })
        })
    },
    checkRowData(row) {
      return row.name !== '' && row.age !== null
    },

    post() {
      axios.post('http://localhost:8080/classification/new_data_vali', {
        sepL: parseFloat(this.input3),
        sepW: parseFloat(this.input4),
        petL: parseFloat(this.input5),
        petW: parseFloat(this.input6)
      },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(res => {
          console.log(res.data)
          this.input7 = res.data
        })
        .catch(error => {
          console.error(error)
        })
    },
    startEditing() {
      this.isEditing = true
    },
    finishEditing() {
      this.isEditing = false
    },
    getRowClassName(row) {
      return this.isEditing ? 'editable-row' : ''
    },
    deleteRow(index) {
      this.tableData.splice(index, 1)
    },
    addRow() {
      this.tableData.push({ name: '', age: null })
    },
    showChart() {
      this.src = 'http://localhost:8080/images/tree?height=' + this.input1 + '&leaves=' + this.input2
      this.draw('report')
      this.show_chart = true
    }
  }
}
</script>

<style scoped>
.demo-input-suffix {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-left: 100px;
  margin-top: 10px;

}
.demo-input-suffix2 {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-left: 0px;
  margin-top: 10px;

}
#echart1 {
  margin-top: 0px;
  left: 0;
  height: 60vh;
}
.center{
  margin-left: 100px;
  margin-top: 0px;

}
.block {
  margin-left: 100px;
  margin-top: 0px;

}
</style>
