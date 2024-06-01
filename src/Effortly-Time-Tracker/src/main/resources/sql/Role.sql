CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public, pg_catalog;



-- Создание пользователей
CREATE USER admin_user WITH PASSWORD 'admin_password';
CREATE USER manager_user WITH PASSWORD 'manager_password';
CREATE USER reg_user WITH PASSWORD 'reg_password';

-- Создание ролей
CREATE ROLE admin_role;
CREATE ROLE manager_role;
CREATE ROLE user_role;

-- Привилегии для admin_role
GRANT ALL PRIVILEGES ON SCHEMA public TO admin_role;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin_role;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin_role;


-- Привилегии для manager_role
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO manager_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO manager_role;

-- Исключения для manager_role: отсутствие всех операций над таблицей user_app
REVOKE ALL PRIVILEGES ON TABLE public.user_app FROM manager_role;



-- Привилегии для user_role
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO user_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO user_role;

-- Исключения для user_role: отсутствие всех операций над таблицами user_app и group_user
REVOKE ALL PRIVILEGES ON TABLE public.user_app FROM user_role;
REVOKE ALL PRIVILEGES ON TABLE public.group_user FROM user_role;

-- Назначение ролей пользователям
GRANT admin_role TO admin_user;
GRANT manager_role TO manager_user;
GRANT user_role TO reg_user;



ALTER USER reg_user WITH PASSWORD 'new_secure_password';
SELECT has_schema_privilege('reg_user', 'public', 'usage');
GRANT USAGE ON SCHEMA public TO reg_user;
SELECT has_schema_privilege('reg_user', 'public', 'usage');
