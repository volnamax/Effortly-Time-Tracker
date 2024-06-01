CREATE TABLE public.activity_log
(
    id         SERIAL PRIMARY KEY not null ,
    table_name VARCHAR(255) not null ,
    operation  VARCHAR(255) not null ,
    data       JSONB,
    logged_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE OR REPLACE FUNCTION log_activity() RETURNS trigger AS
$$
DECLARE
    log_data JSONB;
BEGIN
    -- Формирование JSON данных для логирования
    IF TG_OP = 'INSERT' THEN
        log_data := jsonb_build_object(
                'operation', TG_OP,
                'new_data', row_to_json(NEW),
                'changed_at', current_timestamp
                    );
    ELSIF TG_OP = 'UPDATE' THEN
        log_data := jsonb_build_object(
                'operation', TG_OP,
                'old_data', row_to_json(OLD),
                'new_data', row_to_json(NEW),
                'changed_at', current_timestamp
                    );
    ELSIF TG_OP = 'DELETE' THEN
        log_data := jsonb_build_object(
                'operation', TG_OP,
                'old_data', row_to_json(OLD),
                'changed_at', current_timestamp
                    );
    END IF;

    -- Вставка логов в таблицу activity_log
    INSERT INTO public.activity_log (table_name, operation, data)
    VALUES (TG_TABLE_NAME, TG_OP, log_data);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Триггеры для таблицы project
CREATE TRIGGER log_project_changes
    AFTER INSERT OR UPDATE OR DELETE ON public.project
    FOR EACH ROW EXECUTE FUNCTION log_activity();

-- Триггеры для таблицы task
CREATE TRIGGER log_task_changes
    AFTER INSERT OR UPDATE OR DELETE ON public.task
    FOR EACH ROW EXECUTE FUNCTION log_activity();


INSERT INTO public.project (name, description, user_id) VALUES ('Test Project', 'This is a test project', 1);
UPDATE public.project SET description = 'Updated description' WHERE name = 'Test Project';
DELETE FROM public.project WHERE name = 'Test Project';


INSERT INTO public.table_app (description, name, status, project_id)
VALUES ('Sample Description', 'Sample Table', 'ACTIVE', 1);

INSERT INTO public.task (description, name, status, start_timer, time_add_task, time_end_task, table_id)
VALUES ('Test task', 'Test', 'NO_ACTIVE', NOW(), NOW(), NOW() + INTERVAL '1 day', 1);
UPDATE public.task SET status = 'ACTIVE' WHERE name = 'Test';
DELETE FROM public.task WHERE name = 'Test';

SELECT * FROM public.activity_log;
--
-- 1,project,INSERT,"{""new_data"": {""name"": ""Test Project"", ""user_id"": 1, ""id_project"": 2, ""description"": ""This is a test project""}, ""operation"": ""INSERT"", ""changed_at"": ""2024-06-01T14:29:21.516512+00:00""}",2024-06-01 14:29:21.516512
--               2,project,UPDATE,"{""new_data"": {""name"": ""Test Project"", ""user_id"": 1, ""id_project"": 2, ""description"": ""Updated description""}, ""old_data"": {""name"": ""Test Project"", ""user_id"": 1, ""id_project"": 2, ""description"": ""This is a test project""}, ""operation"": ""UPDATE"", ""changed_at"": ""2024-06-01T14:29:21.533529+00:00""}",2024-06-01 14:29:21.533529
--                             3,project,DELETE,"{""old_data"": {""name"": ""Test Project"", ""user_id"": 1, ""id_project"": 2, ""description"": ""Updated description""}, ""operation"": ""DELETE"", ""changed_at"": ""2024-06-01T14:29:21.550195+00:00""}",2024-06-01 14:29:21.550195
--                                           4,task,INSERT,"{""new_data"": {""name"": ""Test"", ""status"": ""NO_ACTIVE"", ""id_task"": 3, ""table_id"": 1, ""sum_timer"": null, ""description"": ""Test task"", ""start_timer"": ""2024-06-01T14:29:30.652147"", ""time_add_task"": ""2024-06-01T14:29:30.652147"", ""time_end_task"": ""2024-06-02T14:29:30.652147""}, ""operation"": ""INSERT"", ""changed_at"": ""2024-06-01T14:29:30.652147+00:00""}",2024-06-01 14:29:30.652147
--                                                      5,task,UPDATE,"{""new_data"": {""name"": ""Test"", ""status"": ""ACTIVE"", ""id_task"": 3, ""table_id"": 1, ""sum_timer"": null, ""description"": ""Test task"", ""start_timer"": ""2024-06-01T14:29:30.652147"", ""time_add_task"": ""2024-06-01T14:29:30.652147"", ""time_end_task"": ""2024-06-02T14:29:30.652147""}, ""old_data"": {""name"": ""Test"", ""status"": ""NO_ACTIVE"", ""id_task"": 3, ""table_id"": 1, ""sum_timer"": null, ""description"": ""Test task"", ""start_timer"": ""2024-06-01T14:29:30.652147"", ""time_add_task"": ""2024-06-01T14:29:30.652147"", ""time_end_task"": ""2024-06-02T14:29:30.652147""}, ""operation"": ""UPDATE"", ""changed_at"": ""2024-06-01T14:29:30.682839+00:00""}",2024-06-01 14:29:30.682839
--                                                                 6,task,DELETE,"{""old_data"": {""name"": ""Test"", ""status"": ""ACTIVE"", ""id_task"": 3, ""table_id"": 1, ""sum_timer"": null, ""description"": ""Test task"", ""start_timer"": ""2024-06-01T14:29:30.652147"", ""time_add_task"": ""2024-06-01T14:29:30.652147"", ""time_end_task"": ""2024-06-02T14:29:30.652147""}, ""operation"": ""DELETE"", ""changed_at"": ""2024-06-01T14:29:30.701649+00:00""}",2024-06-01 14:29:30.701649


CREATE OR REPLACE FUNCTION public.func_update_task_table_status()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the project status is updated to 'NO_ACTIVE'
    IF NEW.status = 'NO_ACTIVE' THEN
        -- Update all tasks associated with the project
        UPDATE public.task
        SET status = 'NO_ACTIVE'
        WHERE NEW.id_table = public.task.table_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER trg_update_task_table_status ON public.table_app;

CREATE TRIGGER trg_update_task_table_status
AFTER UPDATE ON public.table_app
FOR EACH ROW
EXECUTE FUNCTION public.func_update_task_table_status();

--table
-- 1,Table for tracking issues,Issue Tracker,ACTIVE,1
-- 2,Sample Description,Sample Table,ACTIVE,1



    --task
-- 1,Fix all UI bugs,UI Bugfix,ACTIVE,0,2024-06-01 14:28:53.818965,2024-06-01 14:28:53.818965,2024-12-31 23:59:59.000000,1

UPDATE public.table_app
SET status = 'NO_ACTIVE'
where id_table = 1;

--1,Fix all UI bugs,UI Bugfix,NO_ACTIVE,0,2024-06-01 14:28:53.818965,2024-06-01 14:28:53.818965,2024-12-31 23:59:59.000000,1
UPDATE public.table_app
SET status = 'ACTIVE'
where id_table = 1;
