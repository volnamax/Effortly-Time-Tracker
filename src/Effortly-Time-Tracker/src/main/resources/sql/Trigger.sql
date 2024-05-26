CREATE TABLE public.activity_log
(
    id         SERIAL PRIMARY KEY,
    table_name VARCHAR(255),
    operation  VARCHAR(255),
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
VALUES ('Sample Description', 'Sample Table', 'ACTIVE', 7);



INSERT INTO public.task (description, name, status, start_timer, time_add_task, time_end_task, table_id)
VALUES ('Test task', 'Test', 'NO_ACTIVE', NOW(), NOW(), NOW() + INTERVAL '1 day', 3);
UPDATE public.task SET status = 'ACTIVE' WHERE name = 'Test';
DELETE FROM public.task WHERE name = 'Test';


SELECT * FROM public.activity_log;


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

UPDATE public.table_app
SET status = 'NO_ACTIVE'
where id_table = 16;
