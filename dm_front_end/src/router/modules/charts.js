/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const chartsRouter = {
  path: '/charts',
  component: Layout,
  redirect: 'noRedirect',
  name: 'Charts',
  alwaysShow: true, // will always show the root menu
  meta: {
    title: '分类分析',
    icon: 'chart'
  },
  children: [
    {
      path: 'decision_tree',
      component: () => import('@/views/demo/decisionTree.vue'),
      name: '决策树',
      meta: { title: '决策树', noCache: true }
    }
  ]
}

export default chartsRouter
