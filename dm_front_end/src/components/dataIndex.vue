<script>

import {use} from 'echarts/core';
import {CanvasRenderer} from 'echarts/renderers';
import {PieChart} from 'echarts/charts';
import {
    TitleComponent,
    TooltipComponent,
    LegendComponent,
} from 'echarts/components';
import VChart, {THEME_KEY} from 'vue-echarts';
import {defineComponent} from 'vue';

use([
    CanvasRenderer,
    PieChart,
    TitleComponent,
    TooltipComponent,
    LegendComponent,
]);

export default defineComponent({
    name: 'dataIndex',
    components: {
        VChart,
    },
    provide: {
        [THEME_KEY]: 'dark',
    },
    mounted() {
        this.onInitEchart1();
    },
    methods: {
        onInitEchart1(){
    {
        let echarts = require('echarts');
        let myChart = echarts.init(document.getElementById('echart1'), 'walden');

        let option = {
            title: {
                text: 'Traffic Sources',
                left: 'center',
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)',
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['Direct', 'Email', 'Ad Networks', 'Video Ads', 'Search Engines'],
            },
            series: [
                {
                    name: 'Traffic Sources',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: [
                        {value: 335, name: 'Direct'},
                        {value: 310, name: 'Email'},
                        {value: 234, name: 'Ad Networks'},
                        {value: 135, name: 'Video Ads'},
                        {value: 1548, name: 'Search Engines'},
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)',
                        },
                    },
                },

            ]
        };
        myChart.setOption(option);

    }
}}});
</script>

<template>
    <div>
        <v-chart class="chart" :option="option" autoresize/>


        <el-container style="height:100%; border: 1px solid #eee">
        <el-aside width="200px" style="background-color: rgb(238, 241, 246);height: 100%">
            <el-menu :default-openeds="['1', '3']">
                <el-submenu index="1">
                    <template slot="title"><i class="el-icon-message"></i>导航一</template>
                </el-submenu>
                <el-submenu index="2">
                    <template slot="title"><i class="el-icon-message"></i>导航二</template>
                </el-submenu>
                <el-submenu index="3">
                    <template slot="title"><i class="el-icon-message"></i>导航三</template>
                </el-submenu>
                <el-submenu index="4">
                    <template slot="title"><i class="el-icon-message"></i>导航四</template>
                </el-submenu>
                <el-submenu index="3">
                    <template slot="title"><i class="el-icon-message"></i>导航五</template>
                </el-submenu>
            </el-menu>
        </el-aside>
        <el-container>
            <el-header style="text-align: right; font-size: 12px">
                    <i class="el-icon-s-platform" style="margin-right: 15px"></i>
                <span>管理员</span>
            </el-header>
            <el-main>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item>数据展示</el-breadcrumb-item>
                </el-breadcrumb>
                <el-row :gutter="20" class="row-pane">
                    <el-col :span="12" class="col-2">
                        <el-row>
                            <el-col :span="2" style="width: 12px; height: 12px; background: #1890FF;
                        border-radius: 50%; margin: 20px 0 0 35px;"></el-col>
                            <el-col :span="22" style="width: 150px; height: 15px; font-size: 16px;
                        font-family: Alibaba PuHuiTi; font-weight: 400; color: #333333;
                        margin: 15px 0 0 10px;">项目类型汇总</el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 90%; height: 43px; font-size: 16px; font-family: Alibaba PuHuiTi;
                        font-weight: 400; color: #333333; margin: 15px 0 0 40px;">
                                “十三五”期间重点项目完成总投资82.86亿元，其中中央财政2146.56亿元，地方财政0亿元，社会资本0亿元，其他0亿元
                            </el-col>
                        </el-row>
                        <el-row>
                            <div id="echart1"></div>
                        </el-row>
                    </el-col>
                </el-row>

            </el-main>
        </el-container>
    </el-container>
    </div>
</template>

<style scoped>
.el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
}

.el-aside {
    color: #333;
}
#echart1 {
    height: 30vh;
}
</style>