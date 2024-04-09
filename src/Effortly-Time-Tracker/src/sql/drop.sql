-- Удаление таблиц, начиная с тех, на которые ссылаются внешние ключи
DROP TABLE IF EXISTS task_tag CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS tag CASCADE;
DROP TABLE IF EXISTS project_table CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS todo_node CASCADE;
DROP TABLE IF EXISTS user_app CASCADE;
DROP TABLE IF EXISTS group_member CASCADE;
DROP TABLE IF EXISTS group_user CASCADE;

-- Удаление пользовательских типов данных
DROP TYPE IF EXISTS role_t CASCADE;
DROP TYPE IF EXISTS status_t CASCADE; -- Предполагая, что также существует пользовательский тип status_t
DROP TYPE IF EXISTS priority_t CASCADE;

