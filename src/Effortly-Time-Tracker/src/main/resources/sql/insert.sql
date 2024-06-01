INSERT INTO public.user_app (data_last_log_in, data_sign_in, email, password, user_name, user_secondname, role_id)
VALUES (NOW(), NOW(), 'user@example.com', 'password123', 'FirstName', 'LastName', 1); -- предполагается, что role_id 1 это ADMIN




INSERT INTO public.todo_node (content, priority, status, due_data, user_id)
VALUES ('Complete project report', 'IMPORTANT_URGENTLY', 'ACTIVE', '2024-12-31 23:59:59', 1); -- предполагается, что user_id 1 существует




INSERT INTO public.project (name, description, user_id)
VALUES ('New Project', 'Project to develop new product', 1); -- предполагается, что user_id 1 существует





INSERT INTO public.group_user (name, description, project_id)
VALUES ('Development Team', 'Team responsible for development', 1); -- предполагается, что project_id 1 существует





INSERT INTO public.group_member (group_id, user_id, role)
VALUES (1, 1, 'MANAGER'); -- предполагается, что group_id 1 и user_id 1 существуют





INSERT INTO public.table_app (description, name, status, project_id)
VALUES ('Table for tracking issues', 'Issue Tracker', 'ACTIVE', 1); -- предполагается, что project_id 1 существует




INSERT INTO public.task (description, name, status, sum_timer, start_timer, time_add_task, time_end_task, table_id)
VALUES ('Fix all UI bugs', 'UI Bugfix', 'ACTIVE', 0, NOW(), NOW(), '2024-12-31 23:59:59', 1); -- предполагается, что table_id 1 существует




INSERT INTO public.tag (color, name, project_id)
VALUES ('#FF5733', 'Urgent', 1); -- предполагается, что project_id 1 существует




INSERT INTO public.task_tag (tag_id, task_id)
VALUES (1, 1); -- предполагается, что tag_id 1 и task_id 1 существуют
