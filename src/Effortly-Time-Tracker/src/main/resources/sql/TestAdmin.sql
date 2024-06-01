    -- Подключение от имени admin_user
    \c public.dataBaseEfortly admin_user

    -- Пример вставки записи в таблицу user_app
    INSERT INTO public.user_app (data_last_log_in, data_sign_in, email, password, user_name, user_secondname, role_id)
    VALUES (NOW(), NOW(), 'admisn@example.com', 'admin_password', 'Admin', 'User', 1);

