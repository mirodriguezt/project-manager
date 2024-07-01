<script setup>
    import { computed } from 'vue'
    import { RouterLink } from 'vue-router'

    const props = defineProps({
        project: {
            type: Object
        }
    })

    defineEmits(['update-status'])

    const statusProject = computed(() => {
        return props.project.status
    })
    
</script>

<template>
    <tr>
        <td class="whitespace-nowrap py-4 pl-4 pr-3 text-sm sm:pl-0">
            <p class="font-medium text-gray-900">{{ project.client.name }}</p>
        </td>
        <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            <p class="text-gray-900 font-bold">{{ project.description }}</p>
        </td>
        <td class="whitespace-nowrap px-3 py-4 text-sm">
            <!-- <p class="font-medium text-gray-900">{{ project.status }}</p> -->

            <button
                class="inline-flex rounded-full px-2 text-xs font-semibold leading-5"
                :class="[statusProject == 'OPEN' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800']"
                @click="$emit('update-status', 
                    {id: project.id, status: project.status})"
             >
             {{ project.status }}
           </button>


        </td>
        <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500 ">
            <RouterLink 
                :to="{ name: 'project-activity', params: { project_id: project.id } }"
                class="text-indigo-900 hover:text-indigo-900 mr-5"
            >View Activities</RouterLink>
        </td>
    </tr>
</template>
