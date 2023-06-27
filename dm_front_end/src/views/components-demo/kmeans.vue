<template>
  <div>
    <h2>Table 1:</h2>
    <PaginationTable :columns="columns" />
  </div>
</template>

<script>
import PaginationTable from './pagination-table.vue'

export default {
  components: {
    PaginationTable
  },
  data() {
    return {
      columns: [
        { prop: 'id', label: 'id' },
        { prop: 'petW', label: 'petW' },
        { prop: 'sepL', label: 'sepL' },
        { prop: 'species', label: 'species' },
        { prop: 'petL', label: 'petL' },
        { prop: 'sepW', label: 'sepW' }
      ]
    }
  }
}
</script>
