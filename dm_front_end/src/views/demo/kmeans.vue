<template>
  <div class="app-container">
    <template>
      <div>
        <el-row>
          <el-col :span="4">
            <el-button type="primary" @click="loadTable(1)">载入数据</el-button>
            <el-button v-if="!isEditing" type="primary" style="margin-bottom: 10px" @click="startEditing">编辑</el-button>
            <el-button v-else type="success" style="margin-bottom: 10px" @click="finishEditing">完成编辑</el-button>
          </el-col>
          <el-col :span="1">
            <el-button v-if="isEditing" type="primary" style="margin-bottom: 10px" @click="addRow">新增一行</el-button>
          </el-col>
        </el-row>
        <el-table :data="tableData" style="width: 100%" stripe border :row-class-name="getRowClassName">
          <el-table-column v-for="column in columns" :key="column.prop" :prop="column.prop" :label="column.label">
            <template slot-scope="scope">
              <template v-if="isEditing">
                <el-input v-model.number="scope.row[column.prop]" size="small" />
              </template>
              <template v-else>
                {{ scope.row[column.prop] }}
              </template>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <template v-if="isEditing">
                <el-button type="text" size="small" @click="deleteRow(scope.$index)">删除</el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>
        <el-row type="flex" justify="center" style="margin-top: 10px">
          <el-pagination
            layout="prev, pager, next"
            :page-size="pageSize"
            :current-page="pageNum"
            @prev-click="loadTable"
            @current-change="loadTable"
            @next-click="loadTable"
          />
        </el-row>
        <div class="demo-input-suffix">
          <h1 class="support">
            <el-input
              v-model="input1"
              size="medium"
              placeholder="请输入k"
              clearable
            />
          </h1>
          <el-button type="primary" @click="showChart()">散点图</el-button>
        </div>
      </div>
    </template>
    <v-chart class="echart1" :option="option" autoresize />
    <el-main>
      <el-row>
        <div id="echart1" />
      </el-row>
    </el-main>
  </div>
</template>

<script>

import ecStat from 'echarts-stat'
import axios from 'axios'

export default {
  name: 'PagePermission',
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
      pieces_raw_data: [],
      showTable: false, // 控制表格显示的标志
      showErrorDialog: false,
      isFocused: false,
      isEditing: false,
      input1: '',
      input2: '',
      pageNum: 1,
      pageSize: 8,
      columns: [
        { prop: 'id', label: 'id' },
        { prop: 'petW', label: 'petW' },
        { prop: 'sepL', label: 'sepL' },
        { prop: 'species', label: 'species' },
        { prop: 'petL', label: 'petL' },
        { prop: 'sepW', label: 'sepW' }
      ]
    }
  },
  methods: {
    loadTable(num) {
      this.pageNum = num
      axios.get('/user/page?pageNum=' + this.pageNum + '&pageSize=' + this.pageSize).then(res => {
        this.tableData = res.data.list
      })
    },
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
    showChart() {
      axios.get('http://localhost:8080/api/clu_test?K_num=' + this.input1).then(res => {
        this.pieces_raw_data = res.data
      })
      const echarts = require('echarts')
      const myChart = echarts.init(document.getElementById('echart1'), 'walden')
      echarts.registerTransform(ecStat.transform.clustering)
      var colors = [
        '#37A2DA',
        '#e06343',
        '#37a354',
        '#b55dba',
        '#b5bd48',
        '#8378EA',
        '#96BFFF'
      ]
      var data = []
      for (var i = 0; i < this.pieces_raw_data.length; i++) {
        data.push({ value: [this.pieces_raw_data[i].x, this.pieces_raw_data[i].y], category: this.pieces_raw_data[i].clu_res })
      }
      const option = {
        xAxis: {},
        yAxis: {},
        series: data.map(item => ({
          type: 'scatter',
          data: [item.value],
          itemStyle: {
            color: colors[item.category] // 根据类别设置颜色
          }
        }))
      }
      myChart.setOption(option)
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
  margin-top: 100px;
  left: 0;
  height: 100vh;
}
</style>
