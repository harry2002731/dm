<template>
  <div class="app-container">
    <template>
      <div>
        <el-row>
          <el-col :span="4">
            <el-button type="primary" @click="loadTable(1)">载入数据</el-button>
            <!--            <el-button v-if="!isEditing" type="primary" style="margin-bottom: 10px" @click="startEditing">编辑</el-button>-->
            <!--            <el-button v-else type="success" style="margin-bottom: 10px" @click="finishEditing">完成编辑</el-button>-->
          </el-col>
          <!--          <el-col :span="1">-->
          <!--            <el-button v-if="isEditing" type="primary" style="margin-bottom: 10px" @click="addRow">新增一行</el-button>-->
          <!--          </el-col>-->
        </el-row>
        <el-table :data="tableData" style="width: 100%" stripe border>
          <el-table-column v-for="column in columns" :key="column.prop" :prop="column.prop" :label="column.label">
            <template slot-scope="scope">
              <template v-if="isEditing">
                <el-input v-model="scope.row[column.prop]" size="small" />
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
    </template>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  props: {
    columns: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      showTable: false, // 控制表格显示的标志
      showErrorDialog: false,
      isFocused: false,
      isEditing: false,
      tableData: [], // 存储表格数据的数组
      pageNum: 1,
      pageSize: 8
      // columns: [
      //   { prop: 'id', label: 'id' },
      //   { prop: 'petW', label: 'petW' },
      //   { prop: 'sepL', label: 'sepL' },
      //   { prop: 'species', label: 'species' },
      //   { prop: 'petL', label: 'petL' },
      //   { prop: 'sepW', label: 'sepW' }
      // ]
    }
  },
  methods: {
    loadTable(num) {
      this.pageNum = num
      axios.get('http://localhost:8080/apriori/page?pageNum=' + this.pageNum + '&pageSize=' + this.pageSize).then(res => {
        this.tableData = res.data.list
      })
    },
    startEditing() {
      this.isEditing = true
    },
    finishEditing() {
      this.isEditing = false
      axios.post('http://localhost:8080/api/update', this.tableData)
    },
    deleteRow(index) {
      this.tableData.splice(index, 1)
    },
    addRow() {
      this.tableData.push({ name: '', age: null })
    }
  }
}
</script>
<style scoped lang="scss">

</style>
