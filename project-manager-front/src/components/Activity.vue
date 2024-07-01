<script setup>
    import { computed } from 'vue'
    import { RouterLink } from 'vue-router'

    const props = defineProps({
        activity: {
            type: Object
        }
    })

    defineEmits(['update-status'])

    const statusActivity = computed(() => {
        return props.activity.status
    })
    
</script>

<template>
    <tr>
        <td class="whitespace-nowrap py-4 pl-4 pr-3 text-sm sm:pl-0">
            <p class="font-medium text-gray-900">{{ activity.project.description }}</p>
        </td>
        <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
            <p class="text-gray-900 font-bold">{{ activity.description }}</p>
        </td>
        <td class="whitespace-nowrap px-3 py-4 text-sm">
            <button
                class="inline-flex rounded-full px-2 text-xs font-semibold leading-5"
                :class="[statusActivity == 'OPEN' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800']"
                @click="$emit('update-status', 
                    {id: activity.id, status: activity.status})"
             >
             {{ activity.status }}
           </button>
        </td>
    </tr>
</template>
