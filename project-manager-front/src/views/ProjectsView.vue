<script setup>
    import { onMounted, ref, computed } from 'vue'
    import ProjectService from '../services/ProjectService'
    import RouterLink from '../components/UI/RouterLink.vue';
    import Heading from '../components/UI/Heading.vue';
    import Project from '../components/Project.vue'

    const projects = ref([])

    onMounted(() => {
        ProjectService.getProjectsByStatus('OPEN')
            .then(({data}) => projects.value = data.itemList)
            .catch(error => console.log('An error has ocurred'))
    })

    defineProps({
        title: {
            type: String
        }
    })
    
    const existProjects = computed(() => {
        return projects.value.length > 0 
    })

    const updateStatus = ({id, status}) => {
            ProjectService.changeStatus(id, status == 'OPEN' ? 'FINISHED' : 'OPEN')
            .then(() =>  {
                const i = projects.value.findIndex(project => project.id === id)
                projects.value[i].status = (status == 'OPEN') ? 'FINISHED' : 'OPEN';
            })
            .catch(error => console.log(error))
    }
    
    const onChange = ($event) => {
        ProjectService.getProjectsByStatus($event.target.value)
            .then(({data}) => projects.value = data.itemList)
            .catch(error => console.log('An error has ocurred'))
        return true;
    }
</script>

<template>
    <div>
        <Heading>{{ title }}</Heading>
        <div class="mx-auto mt-5 ">
            <FormKit type="form" #default="{ value }" :actions="false">
                <FormKit
                    type="select"
                    label="Status"
                    name="status_filter"
                    :options="[
                        'OPEN',
                        'FINISHED'
                    ]"
                    @change="onChange($event)"
                />
            </FormKit>
        </div>
        
        <div v-if="existProjects" class="flow-root mx-auto  mt-10 p-5 bg-white shadow">
            <div class="-my-2 -mx-4 overflow-x-auto sm:-mx-6 lg:-mx-8">
                <div class="min-w-full py-2 align-middle sm:px-6 lg:px-8">
                    <table class="min-w-full divide-y divide-gray-300">
                        <thead>
                        <tr>
                            <th scope="col" class="p-2 text-left text-sm font-extrabold text-gray-600">Client</th>
                            <th scope="col" class="p-2 text-left text-sm font-extrabold text-gray-600">Description</th>
                            <th scope="col" class="p-2 text-left text-sm font-extrabold text-gray-600">State</th>
                            <th scope="col" class="p-2 text-left text-sm font-extrabold text-gray-600">Actions</th>
                        </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 bg-white">
                            <Project
                                v-for="project in projects"
                                :key="project.id"
                                :project="project"
                                @update-status="updateStatus"
                            />
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <p v-else class="text-center mt-10">There are no projects</p>
    </div>
</template>