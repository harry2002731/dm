<template>
  <div class="app-container">
    <template>
      <div class="demo-input-suffix">
        <el-row>
          <el-select v-model="value" clearable placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <el-button type="primary" @click="showChart()">数据 聚类</el-button>
        </el-row>
        <el-row>
          <el-input
            v-model="input1"
            size="medium"
            placeholder="请输入k"
            clearable
            class="outer-container"
          />
        </el-row>
      </div>
    </template>
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
      <div>
        <!--        <el-col :span="1">-->
        <!--        </el-col>-->
        <el-col :span="20">
          <el-row>
            <el-col :span="8">
              <div class="chart-container ">
                <div id="chart1" class="chart" />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="chart-container ">
                <div id="chart2" class="chart" />
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <div class="chart-container ">
                <div id="chart3" class="chart" />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="chart-container ">
                <div id="chart4" class="chart" />
              </div>
            </el-col>
          </el-row>
        </el-col>

      </div>
    </template>
  </div>
</template>

<script>

// import ecStat from 'echarts-stat'
import axios from 'axios'
// import echarts from "echarts";
// import echarts from "echarts";

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
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
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
  mounted() {
    this.renderChart('chart1', [
      [1, 2],
      [3, 4],
      [5, 6]
    ])
    this.renderChart('chart2', [
      [7, 8],
      [9, 10],
      [11, 12]
    ])
    this.renderChart('chart3', [
      [13, 14],
      [15, 16],
      [17, 18]
    ])
    this.renderChart('chart4', [
      [19, 20],
      [21, 22],
      [23, 24]
    ])
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
    renderChart(containerId, data) {
      axios.get('http://localhost:8080/api/clu_test?K_num=' + this.input1).then(res => {
        this.pieces_raw_data = res.data
        const echarts = require('echarts')
        const chart = echarts.init(document.getElementById(containerId))
        // var colors = [
        //   '#37A2DA',
        //   '#e06343',
        //   '#37a354',
        //   '#b55dba',
        //   '#b5bd48',
        //   '#8378EA',
        //   '#96BFFF'
        // ]
        var data = []
        for (var i = 0; i < this.pieces_raw_data.length; i++) {
          data.push({ value: [this.pieces_raw_data[i].x, this.pieces_raw_data[i].y], category: this.pieces_raw_data[i].clu_res })
        }
        const option = {
          xAxis: {
            type: 'value',
            min: -4, // 设置 x 轴的最小值
            max: 4 // 设置 x 轴的最大值},
          },
          yAxis: {},
          title: [
            {
              left: 'center',
              text: 'Gradient along the y axis'
            }
          ],
          series: [{
            type: 'scatter',
            data: data,
            symbolSize: 10 // 设置散点的大小
          }]
        }
        chart.setOption(option)
      })
    },
    showChart() {
      axios.get('http://localhost:8080/api/clu_test?K_num=' + this.input1).then(res => {
        this.pieces_raw_data = res.data
        const echarts = require('echarts')
        document.getElementById('echart1').removeAttribute('_echarts_instance_')
        const myChart = echarts.init(document.getElementById('echart1'), 'walden')
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
          xAxis: {
            type: 'value',
            min: -4, // 设置 x 轴的最小值
            max: 4 // 设置 x 轴的最大值},
          },
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
      })
    }
  }
}

</script>

<style scoped>
.demo-input-suffix {
  gap: 10px;
  align-items: center;
  margin-left: 0;
  margin-top: 10px;

}

#echart1 {
  margin-top: 0px;
  left: 0;
  height: 100vh;
}
.chart-container {
  height: 40vh;
  width: 50vh;
  margin: -1px; /* 为了消除列之间的间距 */
//display: flex;
  align-items: center;
  justify-content: center;
}
.outer-container {
  margin-top: 5px;
  width: 300px;

}
.chart {
  width: 100%;
  height: 100%;
}
</style>
