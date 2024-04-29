CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public, pg_catalog;

-- Создание ENUM типов
CREATE TYPE public.role_t AS ENUM ('ADMIN', 'USER', 'GUEST');
CREATE TYPE public.status_t AS ENUM ('NO_ACTIVE', 'ACTIVE');
CREATE TYPE public.priority_t AS ENUM ('IMPORTANT_URGENTLY', 'NO_IMPORTANT_URGENTLY', 'IMPORTANT_NO_URGENTLY', 'NO_IMPORTANT_NO_URGENTLY');

-- Таблица ролей
CREATE TABLE public.roles
(
    id_role SERIAL NOT NULL,
    name    role_t,  -- Использование ENUM типа для определения ролей
    PRIMARY KEY (id_role)
);

-- Таблица пользователей
CREATE TABLE public.user_app
(
    id_user          SERIAL       NOT NULL,
    data_last_log_in TIMESTAMP(6),
    data_sign_in     TIMESTAMP(6),
    email            VARCHAR(255) NOT NULL,
    password         VARCHAR(255) NOT NULL,
    user_name        VARCHAR(255) NOT NULL,
    user_secondname  VARCHAR(255) NOT NULL,
    role             role_t, -- Использование ENUM типа
    PRIMARY KEY (id_user),
    UNIQUE (email)
);

-- Таблица todo-записей
CREATE TABLE public.todo_node
(
    id_todo  SERIAL NOT NULL,
    content  VARCHAR(255),
    priority priority_t, -- Использование ENUM типа
    status   status_t,   -- Использование ENUM типа
    due_data TIMESTAMP(6),
    user_id  INTEGER,
    PRIMARY KEY (id_todo),
    FOREIGN KEY (user_id) REFERENCES public.user_app (id_user)
);

-- Таблица групп
CREATE TABLE public.group_user
(
    goup_id     SERIAL       NOT NULL,
    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    PRIMARY KEY (goup_id)
);

-- Таблица участников группы
CREATE TABLE public.group_member
(
    id       SERIAL NOT NULL,
    group_id INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (group_id) REFERENCES public.group_user (goup_id),
    FOREIGN KEY (user_id) REFERENCES public.user_app (id_user)
);

-- Таблица проектов
CREATE TABLE public.project
(
    id_project  SERIAL       NOT NULL,
    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    group_id    INTEGER,
    user_id     INTEGER,
    PRIMARY KEY (id_project),
    FOREIGN KEY (group_id) REFERENCES public.group_user (goup_id),
    FOREIGN KEY (user_id) REFERENCES public.user_app (id_user),
    UNIQUE (name, group_id) -- Уникальность на комбинацию имени проекта и группы
);

-- Таблица тэгов
CREATE TABLE public.tag
(
    id_tags    SERIAL       NOT NULL,
    color      VARCHAR(255),
    name       VARCHAR(255) NOT NULL,
    project_id INTEGER,
    PRIMARY KEY (id_tags),
    FOREIGN KEY (project_id) REFERENCES public.project (id_project)
);

-- Таблица задач
CREATE TABLE public.table_app
(
    id_table    SERIAL       NOT NULL,
    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    status      status_t,    -- Использование ENUM типа
    project_id  INTEGER,
    PRIMARY KEY (id_table),
    FOREIGN KEY (project_id) REFERENCES public.project (id_project)
);

-- Таблица задач
CREATE TABLE public.task
(
    id_task       SERIAL       NOT NULL,
    description   VARCHAR(255),
    name          VARCHAR(255) NOT NULL,
    start_timer   TIMESTAMP(6),
    status        status_t,    -- Использование ENUM типа
    sum_timer     BIGINT,
    time_add_task TIMESTAMP(6),
    time_end_task TIMESTAMP(6),
    table_id      INTEGER,
    PRIMARY KEY (id_task),
    FOREIGN KEY (table_id) REFERENCES public.table_app (id_table)
);

-- Таблица связей между задачами и тэгами
CREATE TABLE public.task_tag
(
    id      BIGSERIAL NOT NULL,
    tag_id  INTEGER,
    task_id INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (tag_id) REFERENCES public.tag (id_tags),
    FOREIGN KEY (task_id) REFERENCES public.task (id_task)
);
-- Создание таблицы ролей и вставка начальных данных
CREATE TABLE public.roles
(
    id_role SERIAL NOT NULL,
    name    role_t,  -- Использование ENUM типа для определения ролей
    PRIMARY KEY (id_role)
);

-- Вставка значений в таблицу ролей
INSERT INTO public.roles (name)
VALUES ('ADMIN'),
       ('USER'),
       ('GUEST');