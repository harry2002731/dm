/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const componentsRouter = {
  path: '/components',
  component: Layout,
  redirect: 'noRedirect',
  alwaysShow: true, // will always show the root menu
  name: 'ComponentDemo',
  meta: {
    title: '聚类分析',
    icon: 'component'
  },
  children: [
    {
      path: 'kmeans',
      component: () => import('@/views/demo/kmeans.vue'),
      name: 'K-means',
      meta: { title: 'K-means' }
    }
  ]
}

export default componentsRouter
