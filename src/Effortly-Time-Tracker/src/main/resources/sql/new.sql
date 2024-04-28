CREATE SCHEMA public;


CREATE TABLE group_user

(
    goup_id     SERIAL       NOT NULL,
    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    PRIMARY KEY (goup_id)
);
CREATE TABLE roles
(
    id_role SERIAL NOT NULL,
    name    VARCHAR(255) CHECK (name IN ('ADMIN', 'USER', 'GUEST')),
    PRIMARY KEY (id_role)
);


CREATE TABLE user_app
(
    id_user          SERIAL       NOT NULL,
    data_last_log_in TIMESTAMP(6),
    data_sign_in     TIMESTAMP(6),
    email            VARCHAR(255) NOT NULL,
    password         VARCHAR(255) NOT NULL,
    user_name        VARCHAR(255) NOT NULL,
    user_secondname  VARCHAR(255) NOT NULL,
    role_id          INTEGER,
    PRIMARY KEY (id_user),
    UNIQUE (email),
    FOREIGN KEY (role_id) REFERENCES roles (id_role)
);

CREATE TABLE group_member
(
    id       SERIAL NOT NULL,
    group_id INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (group_id) REFERENCES group_user (goup_id),
    FOREIGN KEY (user_id) REFERENCES user_app (id_user)
);



CREATE TABLE project
(
    id_project  SERIAL       NOT NULL,
    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    group_id    INTEGER,
    user_id     INTEGER,
    PRIMARY KEY (id_project),
    FOREIGN KEY (group_id) REFERENCES group_user (goup_id),
    FOREIGN KEY (user_id) REFERENCES user_app (id_user),
    UNIQUE (group_id)
);

CREATE TABLE tag
(
    id_tags    SERIAL       NOT NULL,
    color      VARCHAR(255),
    name       VARCHAR(255) NOT NULL,
    project_id INTEGER,
    PRIMARY KEY (id_tags),
    FOREIGN KEY (project_id) REFERENCES project (id_project)
);

CREATE TABLE "table"
(
    id_table    SERIAL       NOT NULL,
    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    status      VARCHAR(255) CHECK (status IN ('NO_ACTIVE', 'ACTIVE')),
    project_id  INTEGER,
    PRIMARY KEY (id_table),
    FOREIGN KEY (project_id) REFERENCES project (id_project)
);


CREATE TABLE task
(
    id_task       SERIAL       NOT NULL,
    description   VARCHAR(255),
    name          VARCHAR(255) NOT NULL,
    start_timer   TIMESTAMP(6),
    status        VARCHAR(255) NOT NULL CHECK (status IN ('NO_ACTIVE', 'ACTIVE')),
    sum_timer     BIGINT,
    time_add_task TIMESTAMP(6),
    time_end_task TIMESTAMP(6),
    table_id      INTEGER,
    PRIMARY KEY (id_task),
    FOREIGN KEY (table_id) REFERENCES "table" (id_table)
);


CREATE TABLE task_tag
(
    id      BIGSERIAL NOT NULL,
    tag_id  INTEGER,
    task_id INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id_tags),
    FOREIGN KEY (task_id) REFERENCES task (id_task)
);

CREATE TABLE todo_node
(
    id_todo  SERIAL NOT NULL,
    content  VARCHAR(255),
    priority VARCHAR(255) CHECK (priority IN ('IMPORTANT_URGENTLY', 'NO_IMPORTANT_URGENTLY', 'IMPORTANT_NO_URGENTLY',
                                              'NO_IMPORTANT_NO_URGENTLY')),
    status   VARCHAR(255) CHECK (status IN ('NO_ACTIVE', 'ACTIVE')),
    PRIMARY KEY (id_todo)
);
