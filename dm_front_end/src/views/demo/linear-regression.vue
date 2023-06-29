<template>
  <div class="app-container">
    <template>
      <div>
        <PaginationTable :columns="columns" />
        <div>
          <el-row>
            <el-col :span="2">
              <el-button type="primary" @click="showChart()">散点图</el-button>
              <div v-for="option in options" :key="option.value" style="margin-top: 10px">
                <el-checkbox v-model="option.checked" :label="option.label" class="custom-checkbox" />
              </div>
            </el-col>
            <el-col :span="22">
              <v-chart class="echart1" :option="option" autoresize />
              <el-main>
                <div id="echart1" />
              </el-main>
            </el-col>
          </el-row>
        </div>
      </div>
    </template>

  </div>
</template>

<script>

// import ecStat from 'echarts-stat'
import axios from 'axios'
import PaginationTable from '@/views/components-demo/pagination-table.vue'
// import echarts from "echarts";
// import echarts from "echarts";
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
      data1: [],
      datus: [],
      data2: [],
      pieces_raw_data: '',
      checkList: ['选中且禁用', '复选框 A'],
      columns: [
        { prop: 'id', label: 'id' },
        { prop: 'petW', label: 'petW' },
        { prop: 'sepL', label: 'sepL' },
        { prop: 'species', label: 'species' },
        { prop: 'petL', label: 'petL' },
        { prop: 'sepW', label: 'sepW' }
      ],
      options: [
        { label: 'first-order', value: 1, checked: false, url: '/linear_test', param: [] },
        { label: 'second-order', value: 2, checked: false, url: '/ransac', param: [] },
        { label: 'third-order', value: 3, checked: false, url: '/poly_res/two', param: [] },
        { label: 'fourth-order', value: 4, checked: false, url: '/poly_res/three', param: [] },
        { label: 'fifth-order', value: 5, checked: false, url: '/poly_res/four', param: [] },
        { label: 'ransac', value: 6, checked: false, url: '/poly_res/five', param: [] }
      ]
    }
  },

  methods: {

    checkRowData(row) {
      return row.name !== '' && row.age !== null
    },
    show() {
      this.tableData = [
        { name: '张三', age: 20, editingFields: [] },
        { name: '李四', age: 25, editingFields: [] },
        { name: '王五', age: 30, editingFields: [] }
      ]
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
    // showSelectedOptions() {
    //   const selectedOptions = this.options
    //     .filter(option => option.checked)
    //     .map(option => option.url)
    //   const selectedOptions2 = this.options
    //     .filter(option => option.checked)
    //     .map(option => option.value)
    //   for (var i = 0; i < selectedOptions.length; i++) {
    //     axios.get('http://localhost:8080/regression' + selectedOptions[i]).then(res => {
    //       this.options[selectedOptions2[i]].param = this.generateFunction(res.data)
    //       this.options[selectedOptions2[i]].param = this.generateFunction(res.data)
    //     })
    //   }
    // },
    // 生成一元函数
    showSelectedOptions() {
      var a = []
      axios.get('http://localhost:8080/regression/get_data').then(res => {
        for (var i = 0; i < res.data.length; i++) {
          this.data2.push([res.data[i].x, res.data[i].y])
        }
      })
      axios.get('http://localhost:8080/regression/all').then(res => {
        for (var i = 0; i < res.data.length; i++) {
          a.push(this.generateFunction(res.data[i]))
        }
        this.datus = a
      })
    },
    generateFunction(data) {
      // 获取数据的长度
      const n = data.length

      // 生成函数字符串
      let functionString = 'return '
      var data1 = []
      // 生成函数体
      for (let i = 0; i < n; i++) {
        const coefficient = data[i]
        functionString += `${coefficient} * Math.pow(x, ${i})`
        if (i < n - 1) {
          functionString += ' + '
        }
      }
      const generatedFunction = new Function('x', functionString)
      for (let i = -5; i <= 5; i += 0.1) {
        data1.push([i, generatedFunction(i)])
      }
      return data1
    },

    showChart() {
      // this.showSelectedOptions()
      var a = []
      axios.get('http://localhost:8080/regression/get_data').then(res => {
        for (var i = 0; i < res.data.length; i++) {
          this.data2.push([res.data[i].x, res.data[i].y])
        }
      })
      axios.get('http://localhost:8080/regression/all').then(res => {
        for (var i = 0; i < res.data.length; i++) {
          a.push(this.generateFunction(res.data[i]))
        }
        this.datus = a
        const echarts = require('echarts')
        const chart = echarts.init(document.getElementById('echart1'))
        const option = {
          animation: false,
          grid: {
            top: 0,
            left: 50,
            right: 40,
            bottom: 50
          },
          xAxis: {
            name: 'x',
            minorTick: {
              show: true
            },
            minorSplitLine: {
              show: true
            }
          },
          yAxis: {
            name: 'y',
            min: -100,
            max: 100,
            minorTick: {
              show: true
            },
            minorSplitLine: {
              show: true
            }
          },
          legend: {
            data: ['first-order', 'second-order', 'third-order', 'fourth-order', 'fifth-order', 'ransac']
          },
          dataZoom: [
            {
              show: true,
              type: 'inside',
              filterMode: 'none',
              xAxisIndex: [0],
              startValue: -20,
              endValue: 20
            },
            {
              show: true,
              type: 'inside',
              filterMode: 'none',
              yAxisIndex: [0],
              startValue: -20,
              endValue: 20
            }
          ],
          series: [
            {
              type: 'scatter',
              showSymbol: false,
              clip: true,
              data: this.data2
            },
            {
              type: 'line',
              showSymbol: false,
              clip: true,
              name: 'first-order',
              lineStyle: {
                opacity: this.options[0].checked ? 1 : 0
              },
              data: this.datus[0]
            },
            {
              type: 'line',
              showSymbol: false,
              clip: true,
              name: 'second-order',
              lineStyle: {
                opacity: this.options[1].checked ? 1 : 0
              },
              data: this.datus[1]
            },
            {
              type: 'line',
              showSymbol: false,
              clip: true,
              name: 'third-order',
              lineStyle: {
                opacity: this.options[2].checked ? 1 : 0
              },
              data: this.datus[2]
            },
            {
              type: 'line',
              showSymbol: false,
              clip: true,
              name: 'fourth-order',
              lineStyle: {
                opacity: this.options[3].checked ? 1 : 0
              },
              data: this.datus[3]
            },
            {
              type: 'line',
              showSymbol: false,
              clip: true,
              name: 'fifth-order',
              lineStyle: {
                opacity: this.options[4].checked ? 1 : 0
              },
              data: this.datus[4]
            },
            {
              type: 'line',
              showSymbol: false,
              clip: true,
              name: 'ransac',
              lineStyle: {
                opacity: this.options[5].checked ? 1 : 0
              },
              data: this.datus[5]
            }
          ]
        }
        chart.setOption(option)
      })
    }
  }
}

</script>

<style scoped>
.custom-checkbox {
  transform: scale(1.5); /* 调整复选框的大小 */
  margin-right: 10px; /* 调整复选框与文本之间的间距 */
  display: flex;
  align-items: center;
}

#echart1 {
  margin-top: 0px;
  left: 0;
  height: 100vh;
}
</style>
