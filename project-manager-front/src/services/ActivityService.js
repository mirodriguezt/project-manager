import api from "../lib/axios";

export default {
    getActivitiesByProjectId(project_id) {
        return api.get('/activity/all/' + project_id)
    },
    changeStatus(id, status) {
        return api.patch('/activity/' + id + '/status/' + status)
    }
}
