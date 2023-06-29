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
          <el-button type="primary" @click="showChart()">分析数据</el-button>
          <el-button type="primary" @click="showChart()">决策树结果</el-button>
        </div>
        <div>
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

// import ecStat from 'echarts-stat'
import PaginationTable from '@/views/components-demo/pagination-table.vue'

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
      src: 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
      input1: '',
      input2: '',
      input3: '',
      input4: '',
      input5: '',
      input6: '',
      input7: '',
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
    this.draw('report')
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
    show() {
      this.tableData = [
        { name: '张三', age: 20, editingFields: [] },
        { name: '李四', age: 25, editingFields: [] },
        { name: '王五', age: 30, editingFields: [] }
      ]
    },
    post() {
      axios.post('http://localhost:8080/api/post_test', {
        SepL: parseFloat(this.input3),
        SepW: parseFloat(this.input4),
        PetL: parseFloat(this.input5),
        PetW: parseFloat(this.input6)
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
      axios.get('http://localhost:8080/api/visualization?K_num=' + this.input1).then(res => {
        this.src = res.data
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
  margin-top: 100px;
  left: 0;
  height: 100vh;
}
</style>
