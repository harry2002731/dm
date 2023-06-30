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
          <!--          <el-col :span="1">-->
          <!--            <el-button v-if="isEditing" type="primary" style="margin-bottom: 10px" @click="addRow">新增一行</el-button>-->
          <!--          </el-col>-->
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
      </div>
      <div class="demo-input-suffix">
        <el-select v-model="value" class="select-component" clearable placeholder="请选择" @change="test()">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-button type="primary" class="select-component" @click="showChart()">数据 聚类</el-button>
        <el-input
          v-if="value==='选项1'"
          v-model="input_k"
          size="medium"
          placeholder="请输入k"
          clearable
          class="outer-container"
        />
        <el-input
          v-if="value==='选项2'"
          v-model="input_epsilon"
          size="medium"
          placeholder="请输入 epsilon"
          clearable
          class="outer-container"
        />
        <el-input
          v-if="value==='选项2'"
          v-model="input_minPts"
          size="medium"
          placeholder="请输入 minPts"
          clearable
          class="outer-container"
        />
      </div>
      <div>
        <!--        <el-col :span="12">-->
        <!--          <el-slider-->
        <!--            v-model="input_k"-->
        <!--            :step="1"-->
        <!--            max="30"-->
        <!--            height="200px"-->
        <!--            show-stops-->
        <!--          />-->
        <!--        </el-col>-->
        <el-col :span="24">
          <el-row>
            <el-col :span="8">
              <div class="chart-container ">
                <div id="1" class="chart" />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="chart-container ">
                <div id="2" class="chart" />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="chart-container ">
                <div id="3" class="chart" />
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
      input_k: '',
      input_epsilon: '',
      input_minPts: '',
      pageNum: 1,
      pageSize: 8,
      value: '',
      options: [{
        value: '选项1',
        label: 'kmeans'
      }, {
        value: '选项2',
        label: 'dbscan'
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
    this.renderChart('1', 'Dataset(Iris)')
    this.renderChart('2', 'Dataset(Two clusters)')
    this.renderChart('3', 'Dataset(Three clusters)')
  },
  methods: {
    loadTable(num) {
      this.pageNum = num
      axios.get('/user/page?pageNum=' + this.pageNum + '&pageSize=' + this.pageSize).then(res => {
        this.tableData = res.data.list
      })
    },
    test() {
      this.renderChart('1', 'Dataset(Iris)')
      this.renderChart('2', 'Dataset(Two clusters)')
      this.renderChart('3', 'Dataset(Three clusters)')
    },
    checkRowData(row) {
      return row.name !== '' && row.age !== null
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
    renderChart(containerId, chart_name) {
      var url
      if (this.value === '选项1') {
        url = 'http://localhost:8080/clustering/kmeans?K_num=' + this.input_k + '&dataset=' + containerId
      } else {
        url = 'http://localhost:8080/clustering/dbscan?dataset=' + containerId + '&epsilon=' + this.input_epsilon + '&minPts=' + this.input_minPts
      }
      axios.get(url).then(res => {
        this.pieces_raw_data = res.data
        const echarts = require('echarts')
        const chart = echarts.init(document.getElementById(containerId))
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
          data.push({
            value: [this.pieces_raw_data[i].x, this.pieces_raw_data[i].y],
            category: this.pieces_raw_data[i].clu_res
          })
        }
        const option = {
          xAxis: {

          },
          yAxis: {},
          title: [
            {
              left: 'center',
              text: chart_name
            }
          ],
          series: data.map(item => ({
            type: 'scatter',
            data: [item.value],
            itemStyle: {
              color: colors[item.category] // 根据类别设置颜色
            }
          }))

        }
        chart.setOption(option)
      })
    },
    showChart() {
      this.renderChart('1', 'Dataset(Iris)')
      this.renderChart('2', 'Dataset(Two clusters)')
      this.renderChart('3', 'Dataset(Three clusters)')
    }
  }
}

</script>

<style scoped>
.demo-input-suffix {
  //border: 1px solid #ccc;
  gap: 10px;
  align-items: center;
  margin-right: 0;
  margin-top: 50px;

}

.chart-container {
  height: 50vh;
  width: 50vh;
  margin: -1px; /* 为了消除列之间的间距 */
  align-items: center;
  justify-content: center;
}
.outer-container {
  margin-top: 5px;
  width: 315px;

}
.chart {
  width: 100%;
  height: 100%;
}
.select-component {
  margin-right: 10px; /* 调整组件之间的水平间距 */
}
</style>
