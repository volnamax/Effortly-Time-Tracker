# Импорт необходимых библиотек
import psycopg2
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from time import time
import random
import string
import logging

# Параметры подключения к базе данных
conn_params = {
    "dbname": "dataBaseEfortly",
    "user": "postgres",
    "password": "1564",
    "host": "localhost"
}

# Настройка логирования
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Функция для генерации уникальных случайных данных пользователей
def generate_random_user_data(n, existing_emails=set()):
    """Генерация случайных данных пользователей с уникальными электронными адресами"""
    users = []
    while len(users) < n:
        email = ''.join(random.choices(string.ascii_lowercase, k=10)) + '@example.com'
        if email in existing_emails:
            continue
        existing_emails.add(email)
        password = ''.join(random.choices(string.ascii_letters + string.digits, k=12))
        name = ''.join(random.choices(string.ascii_lowercase, k=5))
        surname = ''.join(random.choices(string.ascii_lowercase, k=5))
        role_id = random.randint(1, 3)
        users.append((email, password, name, surname, role_id))
    return users

# Функция для вставки пользователей в базу данных
def insert_random_users(conn, users):
    """Вставка данных о пользователях в базу данных"""
    with conn.cursor() as cur:
        cur.executemany("""
            INSERT INTO public.user_app (email, password, user_name, user_secondname, role_id)
            VALUES (%s, %s, %s, %s, %s);
        """, users)
        conn.commit()

# Функция для создания индекса
def create_index(conn, index_type, index_name, table, column):
    """Создание индекса указанного типа на таблице"""
    with conn.cursor() as cur:
        cur.execute(f"DROP INDEX IF EXISTS {index_name};")
        cur.execute(f"CREATE INDEX {index_name} ON {table} USING {index_type} ({column});")
        conn.commit()

# Функция для удаления индекса
def drop_index(conn, index_name):
    """Удаление индекса из таблицы"""
    with conn.cursor() as cur:
        index_name = index_name.replace("-", "_")
        cur.execute(f"DROP INDEX IF EXISTS {index_name};")
        conn.commit()

# Функция для получения плана запроса
def fetch_explain_plan(conn, query, params):
    """Извлечение плана выполнения SQL запроса с анализом и буферами"""
    with conn.cursor() as cur:
        cur.execute("EXPLAIN (ANALYZE, BUFFERS) " + query, params)
        plan = cur.fetchall()
        return plan

# Функция для удаления таблиц
def drop_tables(conn):
    """Удаление всех таблиц в схеме 'public' для очистки базы данных"""
    with conn.cursor() as cur:
        try:
            cur.execute("DROP schema public cascade;")
            conn.commit()
            logging.info("Tables dropped successfully.")
        except Exception as e:
            logging.error(f"Failed to drop tables: {e}")
            conn.rollback()

# Функция для пересоздания таблиц из SQL-скрипта
def recreate_tables(conn, script_path):
    """Пересоздание таблиц из файла SQL-скрипта"""
    with conn.cursor() as cur, open(script_path, 'r') as file:
        sql_script = file.read()
        try:
            cur.execute(sql_script)
            conn.commit()
            logging.info("Tables recreated successfully.")
        except Exception as e:
            logging.error(f"Failed to recreate tables from script {script_path}: {e}")
            conn.rollback()

# Функция для вставки данных в таблицу
def insert_data(conn, data, query):
    """Общая функция для вставки данных в таблицу"""
    with conn.cursor() as cur:
        cur.executemany(query, data)
        conn.commit()

# Генерация случайных данных для таблицы 'table_app'
def generate_random_table_data(n):
    """Генерация случайных данных для таблицы 'table_app'"""
    tables = []
    for _ in range(n):
        name = ''.join(random.choices(string.ascii_letters, k=15))
        description = ''.join(random.choices(string.ascii_letters + string.digits, k=50))
        status = random.choice(['ACTIVE', 'NO_ACTIVE'])
        project_id = random.randint(1, n)
        tables.append((name, description, status, project_id))
    return tables

# Генерация случайных данных для таблицы 'project'
def generate_random_project_data(n):
    """Генерация случайных данных для таблицы 'project'"""
    projects = []
    for _ in range(n):
        name = ''.join(random.choices(string.ascii_lowercase, k=10))
        description = ''.join(random.choices(string.ascii_letters + string.digits, k=20))
        user_id = random.randint(1, n)
        projects.append((name, description, user_id))
    return projects

# Генерация случайных данных для таблицы 'task'
def generate_random_task_data(n):
    """Генерация случайных данных для таблицы 'task'"""
    tasks = []
    for _ in range(n):
        name = ''.join(random.choices(string.ascii_lowercase, k=10))
        description = ''.join(random.choices(string.ascii_letters + string.digits, k=20))
        status = random.choice(['NO_ACTIVE', 'ACTIVE'])
        sum_timer = random.randint(1, 1000)
        start_timer = None  # Set to None if not applicable
        time_add_task = None  # Set to None if not applicable
        time_end_task = None  # Set to None if not applicable
        table_id = random.randint(1, n)  # Assuming projects are pre-generated up to 1000
        tasks.append((description, name, status, sum_timer, start_timer, time_add_task, time_end_task, table_id))
    return tasks

# Функция для замера времени выполнения запроса и получения плана EXPLAIN
def measure_query_time_and_explain(conn, email, users, index_type="No Index", num_records=0):
    query = """        
    SELECT   
    ua.user_name, ua.email,
    p.name AS project_name, p.description AS project_description,
    ta.name AS table_name, ta.description AS table_description,
    t.name AS task_name, t.description AS task_description, t.status AS task_status
        FROM
        public.user_app ua
            JOIN
            public.project p ON ua.id_user = p.user_id
            JOIN
            public.table_app ta ON p.id_project = ta.project_id
            JOIN
            public.task t ON ta.id_table = t.table_id
                WHERE email  = %s;
    """

    """Замер времени выполнения запроса и получение плана EXPLAIN"""
    n = 3
    times = []
    explain_plan = None
    avg_time = 0
    with conn.cursor() as cur:
        for _ in range(n):
            start = time()
            cur.execute("BEGIN;")
            cur.execute(query, [email])
            cur.execute("COMMIT;")
            conn.commit()
            end = time()
            query_time = (end - start ) * 1_000  # Преобразование в микросекунды
            times.append(query_time)
            if explain_plan is None:
                cur.execute("EXPLAIN (ANALYZE, BUFFERS) " + query, [email])
                explain_plan = cur.fetchall()
        avg_time = np.mean(times)
        logging.info(f"Index Type: {index_type}, Number of Records: {num_records}, Average Query Time: {avg_time:.4f} seconds")
    return avg_time, explain_plan

# Основная функция программы
def main():
    conn = psycopg2.connect(**conn_params)  # Подключение к базе данных
    index_types = ["BTREE", "HASH"]  # Типы индексов для тестирования
    results = []  # Список результатов для анализа производительности
    num_records = [100, 500, 1000, 5000, 10000]  # Различные объемы данных для тестирования

    for n in range(10000, 110000, 10000):
        users = generate_random_user_data(n)
        projects = generate_random_project_data(n)
        tables = generate_random_table_data(n)
        tasks = generate_random_task_data(n)

        user_insert_query = """
        INSERT INTO public.user_app (email, password, user_name, user_secondname, role_id)
        VALUES (%s, %s, %s, %s, %s);
        """
        project_insert_query = """
        INSERT INTO public.project (name, description, user_id)
        VALUES (%s, %s, %s);
        """
        table_insert_query =  """
        INSERT INTO public.table_app (name, description, status, project_id)
        VALUES (%s, %s, %s, %s);
        """
        task_insert_query = """
        INSERT INTO public.task (description, name, status, sum_timer, start_timer, time_add_task, time_end_task, table_id)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s);
        """

        insert_random_users(conn, users)
        insert_data(conn, projects, project_insert_query)
        insert_data(conn, tables, table_insert_query)
        insert_data(conn, tasks, task_insert_query)

        random_id_user = random.choice(users)[4]
        random_email = random.choice(users)[0]

        time_without_index, explain_no_index = measure_query_time_and_explain(conn, random_email, users, "No Index", n)
        results.append((n, "No Index", time_without_index, str(explain_no_index)))
        drop_tables(conn)
        recreate_tables(conn, 'CreateTables.sql')

        for index_type in index_types:
            index_name = f"idx_user_email_{index_type.lower()}"
            insert_random_users(conn, users)
            insert_data(conn, projects, project_insert_query)
            insert_data(conn, tables, table_insert_query)
            insert_data(conn, tasks, task_insert_query)
            create_index(conn, index_type, index_name, "public.user_app", "email")

            time_with_index, explain_with_index = measure_query_time_and_explain(conn, random_email, users, index_type, n)
            results.append((n, index_type, time_with_index, str(explain_with_index)))

            drop_index(conn, index_name)
            drop_tables(conn)
            recreate_tables(conn, 'CreateTables.sql')

    conn.close()

    # Анализ результатов и создание графиков
    df = pd.DataFrame(results, columns=["Number of Records", "Index Type", "Query Time (s)", "EXPLAIN Plan"])
    df.to_csv("index_performance_by_record_count_and_explain.csv", index=False)

    plt.figure(figsize=(14, 8))
    markers = {'No Index': 'o', 'BTREE': 's', 'HASH': '^'}
    for index_type in ["No Index", "BTREE", "HASH"]:
        subset = df[df["Index Type"] == index_type]
        plt.plot(subset["Number of Records"], subset["Query Time (s)"], label=index_type, marker=markers[index_type], markersize=8)

    plt.xlabel("Кол-во записей в БД")
    plt.ylabel("Среднее время выполнения запроса (мс)")
    plt.title("Сравнение времени выполнения запросов с индексами HASH и B-TREE и без них.")
    plt.legend()
    plt.grid(True)
    plt.savefig("query_performance_by_record_count.png")
    plt.show()

# Точка входа в программу
if __name__ == "__main__":
    main()
