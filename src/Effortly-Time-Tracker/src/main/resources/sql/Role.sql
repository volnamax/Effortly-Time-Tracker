CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public, pg_catalog;

-- Создание пользователей
CREATE USER admin_user WITH PASSWORD 'admin_password';
CREATE USER regular_user WITH PASSWORD 'user_password';
CREATE USER guest_user WITH PASSWORD 'guest_password';

-- Создание ролей
CREATE ROLE admin_role;
CREATE ROLE user_role;
CREATE ROLE guest_role;

-- Привилегии для admin_role
GRANT ALL PRIVILEGES ON SCHEMA public TO admin_role;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin_role;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin_role;


-- Привилегии для user_role
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO user_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO user_role;

-- Исключения для user_role: отсутствие всех операций над таблицей user_app
REVOKE ALL PRIVILEGES ON TABLE public.user_app FROM user_role;



-- Привилегии для guest_role
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO guest_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO guest_role;

-- Исключения для guest_role: отсутствие всех операций над таблицами user_app и group_user
REVOKE ALL PRIVILEGES ON TABLE public.user_app FROM guest_role;
REVOKE ALL PRIVILEGES ON TABLE public.group_user FROM guest_role;

-- Назначение ролей пользователям
GRANT admin_role TO admin_user;
GRANT user_role TO regular_user;
GRANT guest_role TO guest_user;
