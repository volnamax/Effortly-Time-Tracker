CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public, pg_catalog;

CREATE TYPE public.role_t AS ENUM ('ADMIN', 'USER', 'GUEST');
CREATE TYPE public.status_t AS ENUM ('NO_ACTIVE', 'ACTIVE');
CREATE TYPE public.priority_t AS ENUM ('IMPORTANT_URGENTLY', 'NO_IMPORTANT_URGENTLY', 'IMPORTANT_NO_URGENTLY', 'NO_IMPORTANT_NO_URGENTLY');


CREATE TABLE public.roles
(
    id_role SERIAL NOT NULL,
    PRIMARY KEY (id_role),

    name    VARCHAR(255) CHECK (name IN ('ADMIN', 'USER', 'GUEST'))
);

ALTER TABLE public.roles
    ADD CONSTRAINT chk_roles_name CHECK (name IN ('ADMIN', 'USER', 'GUEST'));

INSERT INTO public.roles ("name")
VALUES ('ADMIN'),
       ('USER'),
       ('GUEST');


CREATE TABLE public.user_app
(
    id_user          SERIAL       NOT NULL,
    PRIMARY KEY (id_user),

    data_last_log_in TIMESTAMP(6),
    data_sign_in     TIMESTAMP(6),
    email            VARCHAR(255) NOT NULL,
    password         VARCHAR(255) NOT NULL,
    user_name        VARCHAR(255) NOT NULL,
    user_secondname  VARCHAR(255) NOT NULL,

    role_id          INTEGER,
    FOREIGN KEY (role_id) REFERENCES public.roles (id_role)
);

ALTER TABLE public.user_app
    ADD CONSTRAINT uniq_user_app_email UNIQUE (email);

ALTER TABLE public.user_app
    ADD CONSTRAINT chk_user_app_email_length CHECK (CHAR_LENGTH(email) <= 255);

ALTER TABLE public.user_app
    ADD CONSTRAINT chk_user_app_password_length CHECK (CHAR_LENGTH(password) <= 255);

ALTER TABLE public.user_app
    ADD CONSTRAINT chk_user_app_user_name_length CHECK (CHAR_LENGTH(user_name) <= 255);

ALTER TABLE public.user_app
    ADD CONSTRAINT chk_user_app_user_secondname_length CHECK (CHAR_LENGTH(user_secondname) <= 255);


CREATE TABLE public.todo_node
(
    id_todo  SERIAL NOT NULL,
    PRIMARY KEY (id_todo),

    content  VARCHAR(255),
    priority VARCHAR(255),
    status   VARCHAR(255),
    due_data TIMESTAMP(6),

    user_id  INTEGER,
    FOREIGN KEY (user_id) REFERENCES public.user_app (id_user)
);

ALTER TABLE public.todo_node
    ADD CONSTRAINT chk_todo_node_content_length CHECK (CHAR_LENGTH(content) <= 255);

ALTER TABLE public.todo_node
    ADD CONSTRAINT chk_todo_node_priority CHECK (priority IN ('IMPORTANT_URGENTLY', 'NO_IMPORTANT_URGENTLY',
                                                              'IMPORTANT_NO_URGENTLY', 'NO_IMPORTANT_NO_URGENTLY'));

ALTER TABLE public.todo_node
    ADD CONSTRAINT chk_todo_node_status CHECK (status IN ('NO_ACTIVE', 'ACTIVE'));


CREATE TABLE public.project
(
    id_project  SERIAL       NOT NULL,
    PRIMARY KEY (id_project),

    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),

    user_id     INTEGER,
    FOREIGN KEY (user_id) REFERENCES public.user_app (id_user)
);

ALTER TABLE public.project
    ADD CONSTRAINT chk_project_name_length CHECK (CHAR_LENGTH(name) <= 255);
ALTER TABLE public.project
    ADD CONSTRAINT chk_project_description_length CHECK (CHAR_LENGTH(description) <= 255);



CREATE TABLE public.group_user
(
    id_group    SERIAL       NOT NULL,
    PRIMARY KEY (id_group),

    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),

    project_id  INTEGER UNIQUE,
    FOREIGN KEY (project_id) REFERENCES public.project (id_project)
);

ALTER TABLE public.group_user
    ADD CONSTRAINT chk_group_name_length CHECK (CHAR_LENGTH(name) <= 255);

ALTER TABLE public.group_user
    ADD CONSTRAINT chk_group_description_length CHECK (CHAR_LENGTH(description) <= 255);


CREATE TABLE public.group_member
(
    id       SERIAL  NOT NULL,
    PRIMARY KEY (id),

    group_id INTEGER NOT NULL,
    user_id  INTEGER NOT NULL,
    role     VARCHAR(255),

    FOREIGN KEY (group_id) REFERENCES public.group_user (id_group),
    FOREIGN KEY (user_id) REFERENCES public.user_app (id_user)
);

ALTER TABLE public.group_member
    ADD CONSTRAINT chk_group_member_role CHECK (role IN ('ADMIN', 'USER', 'GUEST'));



CREATE TABLE public.table_app
(
    id_table    SERIAL       NOT NULL,
    PRIMARY KEY (id_table),

    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    status      VARCHAR(255),

    project_id  INTEGER,
    FOREIGN KEY (project_id) REFERENCES project (id_project)
);

ALTER TABLE public.table_app
    ADD CONSTRAINT chk_table_name_length CHECK (CHAR_LENGTH(name) <= 255);

ALTER TABLE public.table_app
    ADD CONSTRAINT chk_table_description_length CHECK (CHAR_LENGTH(description) <= 255);

ALTER TABLE public.table_app
    ADD CONSTRAINT chk_table_status CHECK (status IN ('NO_ACTIVE', 'ACTIVE'));


CREATE TABLE public.task
(
    id_task       SERIAL       NOT NULL,
    PRIMARY KEY (id_task),

    description   VARCHAR(255),
    name          VARCHAR(255) NOT NULL,
    status        VARCHAR(255) NOT NULL,

    sum_timer     BIGINT,
    start_timer   TIMESTAMP(6),
    time_add_task TIMESTAMP(6),
    time_end_task TIMESTAMP(6),

    table_id      INTEGER,
    FOREIGN KEY (table_id) REFERENCES public.table_app (id_table)
);

ALTER TABLE public.task
    ADD CONSTRAINT chk_task_name_length CHECK (CHAR_LENGTH(name) <= 255);

ALTER TABLE public.task
    ADD CONSTRAINT chk_task_description_length CHECK (CHAR_LENGTH(description) <= 255);

ALTER TABLE public.task
    ADD CONSTRAINT chk_table_status CHECK (status IN ('NO_ACTIVE', 'ACTIVE'));



CREATE TABLE public.tag
(
    id_tags    SERIAL       NOT NULL,
    PRIMARY KEY (id_tags),

    color      VARCHAR(255),
    name       VARCHAR(255) NOT NULL,

    project_id INTEGER,
    FOREIGN KEY (project_id) REFERENCES project (id_project)
);

ALTER TABLE public.tag
    ADD CONSTRAINT chk_tag_name_length CHECK (CHAR_LENGTH(name) <= 255);

ALTER TABLE public.tag
    ADD CONSTRAINT chk_tag_color_length CHECK (CHAR_LENGTH(color) <= 255);


CREATE TABLE public.task_tag
(
    id      SERIAL  NOT NULL,
    PRIMARY KEY (id),

    tag_id  INTEGER NOT NULL,
    task_id INTEGER NOT NULL,
    FOREIGN KEY (tag_id) REFERENCES public.tag (id_tags),
    FOREIGN KEY (task_id) REFERENCES public.task (id_task)
);

