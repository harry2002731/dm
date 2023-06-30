<template>
  <div class="app-container">
    <template>
      <div>
        <el-table :data="tableData" style="width: 100%" stripe border>
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
      pageSize: 8,
      label_list: ['sepL', 'sepW', 'petL', 'petW']
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
  mounted() {
    setInterval(this.loadTable, 500) // 每秒执行一次 yourFunction
  },
  methods: {
    loadTable(num) {
      this.pageNum = num
      axios.get('http://localhost:8080/api/stat').then(res => {
        for (var i = 0; i < res.data.length; i++) {
          res.data[i].data = this.label_list[i]
        }

        this.tableData = res.data
      })
    },
    startEditing() {
      this.isEditing = true
    },
    finishEditing() {
      this.isEditing = false
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
