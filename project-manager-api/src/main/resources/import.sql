insert into tb_client(id, name, creation_date, update_date) values('f6888a87-b49f-49a1-b3db-56998ee81657', 'client2', '2024-06-30 19:47:22.911911', '2024-06-30 19:47:22.912917')

insert into tb_project(id, client_id, description, status, creation_date, update_date) values('5b3cb947-bff0-49f0-a332-4f5fae93e5be', 'f6888a87-b49f-49a1-b3db-56998ee81657', 'Project 1', 'O', '2024-06-30 19:47:48.794942', '2024-06-30 19:47:48.794942');
insert into tb_project(id, client_id, description, status, creation_date, update_date) values('cb32ad0e-92e6-4f78-9d1b-46b84f7d3bec', 'f6888a87-b49f-49a1-b3db-56998ee81657', 'Project 2', 'O', '2024-06-30 19:48:17.453898', '2024-06-30 19:48:17.453898');

insert into tb_activity(id, project_id, description, status, creation_date, update_date) values('7d4032e0-8ecf-4b2e-ac2d-597953a29701', '5b3cb947-bff0-49f0-a332-4f5fae93e5be', 'Activity 1-1', 'O', '2024-06-30 19:47:48.794942', '2024-06-30 19:47:48.794942');
insert into tb_activity(id, project_id, description, status, creation_date, update_date) values('7d4032e0-8ecf-4b2e-ac2d-597953a29702', '5b3cb947-bff0-49f0-a332-4f5fae93e5be', 'Activity 1-2', 'O', '2024-06-30 19:48:17.453898', '2024-06-30 19:48:17.453898');
insert into tb_activity(id, project_id, description, status, creation_date, update_date) values('7d4032e0-8ecf-4b2e-ac2d-597953a29703', 'cb32ad0e-92e6-4f78-9d1b-46b84f7d3bec', 'Activity 2-1', 'O', '2024-06-30 19:50:48.794942', '2024-06-30 19:50:48.794942');
insert into tb_activity(id, project_id, description, status, creation_date, update_date) values('7d4032e0-8ecf-4b2e-ac2d-597953a29704', 'cb32ad0e-92e6-4f78-9d1b-46b84f7d3bec', 'Activity 2-2', 'O', '2024-06-30 19:51:17.453898', '2024-06-30 19:50:17.453898');