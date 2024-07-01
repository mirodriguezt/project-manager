# Spring boot example with REST and Spring data JPA / Front Vue.js

### Running

project-manager-api 
  Run docker
  - Execute docker-compose up
  - Postgres work with port 5532
  - To review swagger access: -> http://localhost:8090/swagger-ui/index.html
  - The ClassDiagram.png file contains the class diagram

| Method | Url | Decription |
| ------ | --- | ---------- |
| PUT    |/project/{id} | Modify a project record given its id |
| DELETE |/project/{id} | Delete a project given their id |
| POST   |/project/add/client/{clientid} | Add project
| PATCH  |/project/{id}/status/{status} | Returns all project with a specific status |
| GET    |/project/id/{id} | Returns a project given its id |
| GET    |/project/all | Returns all projects |
| GET    |/project/all/{clientid}/{status} | Returns all projects of a client with a specific status per page |
| GET    |/project/all/status/{status} | Returns all projects with a specific status |
| PUT    |/client/{id} | Modify a client record given its id |
| DELETE |/client/{id} | Delete a client given their id |
| POST   |/client/add | Add client |
| GET    |/client/id/{id} | Returns a client given its id |
| GET    |/client/all | Returns a list with all client per page  |
| PUT    |/activity/{id} | Modify a activity record given its id |
| DELETE |/activity/{id} | Delete a activity given their id |
| POST   |/activity/add/project/{projectid} | Add activity |
| PATCH  |/activity/{id}/status/{status} | Returns all activities with a specific status |
| GET    |/activity/id/{id} | Returns an activity given its id |
| GET    |/activity/all/{projectid} | Returns all activities of a project per page |
| GET    |/activity/all/{projectid}/{status} | Returns all activities of a project with a specific status per page |

