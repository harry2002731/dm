<template>
  <div class="app-container">
    <template>
      <div>
        <h1>数据展示与修改</h1>
        <PaginationTable1 :columns="columns1" @custom-event="handleCustomEvent" />
        <PaginationTable2 ref="childRef" :columns="columns2" />
        <v-chart class="echart1" :option="option" autoresize />
      </div>
    </template>
    <el-main>
      <el-row>
        <div id="echart1" />
      </el-row>
    </el-main>
  </div>
</template>

<script>

import axios from 'axios'
import PaginationTable2 from './component/table.vue'
import PaginationTable1 from './component/pagination-table.vue'

export default {
  name: 'PagePermission',
  components: {
    PaginationTable1,
    PaginationTable2
  },

  data() {
    return {
      resources: '',
      tableData: [], // 存储表格数据的数组
      showTable: false, // 控制表格显示的标志
      showErrorDialog: false,
      isFocused: false,
      isEditing: false,
      input1: '',
      input2: '',
      pageNum: 1,
      pageSize: 8,
      receivedData: true,
      columns1: [
        { prop: 'id', label: 'id' },
        { prop: 'sepL', label: 'sepL' },
        { prop: 'sepW', label: 'sepW' },
        { prop: 'petL', label: 'petL' },
        { prop: 'petW', label: 'petW' },
        { prop: 'species', label: 'species' }

      ],
      columns2: [
        { prop: 'data', label: 'data' },
        { prop: 'mean', label: 'mean' },
        { prop: 'min', label: 'min' },
        { prop: 'max', label: 'max' },
        { prop: 'median', label: 'median' },
        { prop: 'quartile25', label: 'quartile25' },
        { prop: 'quartile75', label: 'quartile75' }
      ]
    }
  },

  methods: {
    checkRowData(row) {
      return row.name !== '' && row.age !== null
    },
    loadTableData() {
      // 执行函数
      this.PaginationTable2.loadTable(1)
      // 标记数据已处理
      this.receivedData = false
      // 返回需要显示的内容
    },
    handleCustomEvent(data) {
      this.$refs.childRef.receivedData = data // 获取传递的数据并存储在主文件的数据中
      // this.loadTableData()
    },
    loadTable(num) {
      this.pageNum = num
      axios.get('http://localhost:8080/api/stat' + this.pageNum + '&pageSize=' + this.pageSize).then(res => {
        this.tableData = res.data.list
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
    fetchData() {
      axios.get('/data') // 发起GET请求，获取数据
        .then(response => {
          this.tableData = response.data // 将获取到的数据保存到组件的数据中
        })
        .catch(error => {
          console.error(error)
        })
    },
    showChart() {
      axios.get('http://localhost:8080/api/apri?support=' + this.input1 + '&confidence=' + this.input2).then(res => {
        const echarts = require('echarts')
        const myChart = echarts.init(document.getElementById('echart1'), 'walden')
        // prettier-ignore
        const hours = [
          'milk', 'bread', 'eggs', 'coke', 'cereal'
        ]
        const days = [
          'milk', 'bread', 'eggs', 'coke', 'cereal'
        ]
        const data = res.data
          .map(function(item) {
            return [item[1], item[0], item[2] || '-']
          })
        const option = {
          tooltip: {
            position: 'top'
          },
          grid: {
            left: '0%',
            height: '50%',
            top: '0%'
          },
          xAxis: {
            type: 'category',
            data: hours,
            splitArea: {
              show: true
            }
          },
          yAxis: {
            type: 'category',
            data: days,
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
            bottom: '30%'
          },
          series: [
            {
              name: 'Punch Card',
              type: 'heatmap',
              data: data,
              label: {
                show: true
              },
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }
        myChart.setOption(option)
      })
    }
  }
}

</script>

<style scoped>
.demo-input-suffix {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-left: 0;
  margin-top: 10px;

}

#echart1 {
  margin-top: 10px;
  left: 0;
  height: 100vh;
}
</style>
