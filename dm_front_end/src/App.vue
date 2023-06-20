<template>
  <div id="app">
    <dataIndex></dataIndex>
  </div>
</template>

<script>
import DataIndex from "@/components/dataIndex.vue";

export default {
  name: 'App',
  components: {
      DataIndex,
  }
}
</script>

<style>
#app {
    height: 100%;
}
</style>
