
-- Подключение от имени regular_user
\c dataBaseEfortly regular_user


-- Пример вставки записи в таблицу project (разрешено)
INSERT INTO public.project (name, description, user_id) VALUES ('User Project', 'User project description', 1);

-- Пример вставки записи в таблицу group_user (разрешено)
INSERT INTO public.group_user (name, description, project_id) VALUES ('User Group', 'User group description', 1);

-- Пример вставки записи в таблицу user_app (запрещено, ожидаем ошибку)
INSERT INTO public.user_app (data_last_log_in, data_sign_in, email, password, user_name, user_secondname, role_id)
VALUES (NOW(), NOW(), 'user@example.com', 'user_password', 'Regular', 'User', 1);