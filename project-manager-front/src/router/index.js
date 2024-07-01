import { createRouter, createWebHistory } from 'vue-router'
import Init from '../views/ProjectsView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'list-projects',
      component: Init,
      props: { title: 'Projects List'}
    },
    {
      path: '/project-activity/:project_id',
      name: 'project-activity',
      component: () => import('../views/ProjectActivityView.vue'),
      props: { title: 'Activities' }
    },
  ]
})

export default router
