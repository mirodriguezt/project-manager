import api from "../lib/axios";

export default {
    getProjects() {
        return api.get('/project/all')
    },
    getProjectsByStatus(status) {
        return api.get('/project/all/status/' + status)
    },
    changeStatus(id, status) {
        return api.patch('/project/' + id + '/status/' + status)
    }
}
