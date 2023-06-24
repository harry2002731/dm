<template>
  <div class="app-container">
    <template>
      <div>
        <el-row>
          <el-col :span="4">
            <el-button type="primary" @click="show()">载入数据</el-button>
            <el-button v-if="!isEditing" type="primary" style="margin-bottom: 10px" @click="startEditing">编辑</el-button>
            <el-button v-else type="success" style="margin-bottom: 10px" @click="finishEditing">完成编辑</el-button>
          </el-col>
          <el-col :span="1">
            <el-button v-if="isEditing" type="primary" style="margin-bottom: 10px" @click="addRow">新增一行</el-button>
          </el-col>
        </el-row>
        <el-table :data="tableData" style="width: 100%" stripe border :row-class-name="getRowClassName">
          <el-table-column prop="name" label="姓名">
            <template slot-scope="scope">
              <template v-if="isEditing">
                <el-input v-model="scope.row.name" size="small" />
              </template>
              <template v-else>
                {{ scope.row.name }}
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="age" label="年龄">
            <template slot-scope="scope">
              <template v-if="isEditing">
                <el-input v-model.number="scope.row.age" size="small" />
              </template>
              <template v-else>
                {{ scope.row.age }}
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
          <el-button type="primary" @click="showChart()">分析数据</el-button>
          <el-button type="primary" @click="showChart()">决策树结果</el-button>
        </div>
      </div>
    </template>
    <v-chart class="echart1" :option="option" autoresize />
    <div class="demo-image__placeholder">
      <div class="block">
        <span class="demonstration">默认</span>
        <el-image :src="src" />
      </div>
      <div class="block">
        <span class="demonstration">自定义</span>
        <el-image :src="src">
          <div slot="placeholder" class="image-slot">
            加载中<span class="dot">...</span>
          </div>
        </el-image>
      </div>
    </div>
  </div>
</template>

<script>

import ecStat from 'echarts-stat'

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
      showTable: false, // 控制表格显示的标志
      showErrorDialog: false,
      isFocused: false,
      isEditing: false,
      src: 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
      input1: '',
      input2: ''
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
    showChart() {
      const echarts = require('echarts')
      const myChart = echarts.init(document.getElementById('echart1'), 'walden')
      echarts.registerTransform(ecStat.transform.clustering)

      // prettier-ignore
      const data = [
        [3.275154, 2.957587],
        [-3.344465, 2.603513],
        [0.355083, -3.376585],
        [1.852435, 3.547351],
        [-2.078973, 2.552013],
        [-0.993756, -0.884433],
        [2.682252, 4.007573],
        [-3.087776, 2.878713],
        [-1.565978, -1.256985],
        [2.441611, 0.444826],
        [-0.659487, 3.111284],
        [-0.459601, -2.618005],
        [2.17768, 2.387793],
        [-2.920969, 2.917485],
        [-0.028814, -4.168078],
        [3.625746, 2.119041],
        [-3.912363, 1.325108],
        [-0.551694, -2.814223],
        [2.855808, 3.483301],
        [-3.594448, 2.856651],
        [0.421993, -2.372646],
        [1.650821, 3.407572],
        [-2.082902, 3.384412],
        [-0.718809, -2.492514],
        [4.513623, 3.841029],
        [-4.822011, 4.607049],
        [-0.656297, -1.449872],
        [1.919901, 4.439368],
        [-3.287749, 3.918836],
        [-1.576936, -2.977622],
        [3.598143, 1.97597],
        [-3.977329, 4.900932],
        [-1.79108, -2.184517],
        [3.914654, 3.559303],
        [-1.910108, 4.166946],
        [-1.226597, -3.317889],
        [1.148946, 3.345138],
        [-2.113864, 3.548172],
        [0.845762, -3.589788],
        [2.629062, 3.535831],
        [-1.640717, 2.990517],
        [-1.881012, -2.485405],
        [4.606999, 3.510312],
        [-4.366462, 4.023316],
        [0.765015, -3.00127],
        [3.121904, 2.173988],
        [-4.025139, 4.65231],
        [-0.559558, -3.840539],
        [4.376754, 4.863579],
        [-1.874308, 4.032237],
        [-0.089337, -3.026809],
        [3.997787, 2.518662],
        [-3.082978, 2.884822],
        [0.845235, -3.454465],
        [1.327224, 3.358778],
        [-2.889949, 3.596178],
        [-0.966018, -2.839827],
        [2.960769, 3.079555],
        [-3.275518, 1.577068],
        [0.639276, -3.41284]
      ]
      var CLUSTER_COUNT = 6
      var DIENSIION_CLUSTER_INDEX = 2
      var COLOR_ALL = [
        '#37A2DA',
        '#e06343',
        '#37a354',
        '#b55dba',
        '#b5bd48',
        '#8378EA',
        '#96BFFF'
      ]
      var pieces = []
      for (var i = 0; i < CLUSTER_COUNT; i++) {
        pieces.push({
          value: i,
          label: 'cluster ' + i,
          color: COLOR_ALL[i]
        })
      }
      const option = {
        dataset: [
          {
            source: data
          },
          {
            transform: {
              type: 'ecStat:clustering',
              // print: true,
              config: {
                clusterCount: CLUSTER_COUNT,
                outputType: 'single',
                outputClusterIndexDimension: DIENSIION_CLUSTER_INDEX
              }
            }
          }
        ],
        tooltip: {
          position: 'top'
        },
        visualMap: {
          type: 'piecewise',
          top: 'middle',
          min: 0,
          max: CLUSTER_COUNT,
          left: 10,
          splitNumber: CLUSTER_COUNT,
          dimension: DIENSIION_CLUSTER_INDEX,
          pieces: pieces
        },
        grid: {
          left: 120
        },
        xAxis: {},
        yAxis: {},
        series: {
          type: 'scatter',
          encode: { tooltip: [0, 1] },
          symbolSize: 15,
          itemStyle: {
            borderColor: '#555'
          },
          datasetIndex: 1
        }
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
