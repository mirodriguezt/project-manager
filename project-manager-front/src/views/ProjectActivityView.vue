<script setup>
    import { onMounted, ref, computed } from 'vue'
    import { useRouter, useRoute } from 'vue-router'
    import ActivityService from '../services/ActivityService'
    import RouterLink from '../components/UI/RouterLink.vue';
    import Heading from '../components/UI/Heading.vue';
    import Activity from '../components/Activity.vue'

    const router = useRouter()
    const route = useRoute()
    const activities = ref([])
    const { project_id } = route.params

    onMounted(() => {
        ActivityService.getActivitiesByProjectId(project_id)
            .then(({data}) => activities.value = data.itemList)
            .catch(error => console.log('An error has ocurred'))
    })

    defineProps({
        title: {
            type: String
        }
    })
    
    const existActivities = computed(() => {
        return activities.value.length > 0 
    })

    const updateStatus = ({id, status}) => {
            ActivityService.changeStatus(id, status == 'OPEN' ? 'FINISHED' : 'OPEN')
            .then(() =>  {
                const i = activities.value.findIndex(activity => activity.id === id)
                activities.value[i].status = (status == 'OPEN') ? 'FINISHED' : 'OPEN';
            })
            .catch(error => console.log(error))
    }
</script>

<template>
    <div>
        <div class="flex justify-end">
            <RouterLink to="list-projects">
                Back
            </RouterLink>
        </div>
        <Heading>{{ title }}</Heading>

        <div v-if="existActivities" class="flow-root mx-auto  mt-10 p-5 bg-white shadow">
            <div class="-my-2 -mx-4 overflow-x-auto sm:-mx-6 lg:-mx-8">
                <div class="min-w-full py-2 align-middle sm:px-6 lg:px-8">
                    <table class="min-w-full divide-y divide-gray-300">
                        <thead>
                        <tr>
                            <th scope="col" class="p-2 text-left text-sm font-extrabold text-gray-600">Project</th>
                            <th scope="col" class="p-2 text-left text-sm font-extrabold text-gray-600">Activity</th>
                            <th scope="col" class="p-2 text-left text-sm font-extrabold text-gray-600">State</th>
                        </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 bg-white">
                            <Activity
                                v-for="activity in activities"
                                :key="activity.id"
                                :activity="activity"
                                @update-status="updateStatus"
                            />
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <p v-else class="text-center mt-10">There are no activities</p>
    </div>
</template>