/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const nestedRouter = {
  path: '/nested',
  redirect: 'noRedirect',
  name: 'Nested',
  component: Layout,
  alwaysShow: true, // will always show the root menu
  meta: {
    title: '回归分析',
    icon: 'nested'
  },
  children: [
    {
      path: '/linear_regression',
      component: () => import('@/views/nested/linear-regression'),
      name: '线性回归',
      meta: { title: '线性回归' }
    }
  ]
}

export default nestedRouter
