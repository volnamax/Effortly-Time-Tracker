
-- Подключение от имени guest_user
\c dataBaseEfortly guest_user



-- Пример вставки записи в таблицу project (разрешено)
INSERT INTO public.project (name, description, user_id) VALUES ('Guest Project', 'Guest project description', 1);

-- Пример вставки записи в таблицу user_app (запрещено, ожидаем ошибку)
INSERT INTO public.user_app (data_last_log_in, data_sign_in, email, password, user_name, user_secondname, role_id)
VALUES (NOW(), NOW(), 'guest@example.com', 'guest_password', 'Guest', 'User', 1);

-- Пример вставки записи в таблицу group_user (запрещено, ожидаем ошибку)
INSERT INTO public.group_user (name, description, project_id) VALUES ('Guest Group', 'Guest group description', 1);
