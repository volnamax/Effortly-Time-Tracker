import psycopg2
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from time import time
import random
import string
import  logging
# Connection parameters
conn_params = {
    "dbname": "dataBaseEfortly",
    "user": "postgres",
    "password": "1564",
    "host": "localhost"
}
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def generate_random_user_data(n, existing_emails=set()):
    """ Generate unique random user data """
    users = []
    while len(users) < n:
        email = ''.join(random.choices(string.ascii_lowercase, k=10)) + '@example.com'
        if email in existing_emails:
            continue  # Skip this email and generate a new one
        existing_emails.add(email)
        password = ''.join(random.choices(string.ascii_letters + string.digits, k=12))
        name = ''.join(random.choices(string.ascii_lowercase, k=5))
        surname = ''.join(random.choices(string.ascii_lowercase, k=5))
        role_id = random.randint(1, 3)
        users.append((email, password, name, surname, role_id))
    return users


def insert_random_users(conn, users):
    """ Insert users into the database """
    with conn.cursor() as cur:
        cur.executemany("""
            INSERT INTO public.user_app (email, password, user_name, user_secondname, role_id)
            VALUES (%s, %s, %s, %s, %s);
        """, users)
        conn.commit()

def create_index(conn, index_type, index_name, table, column):
    """ Create an index of a specified type """
    with conn.cursor() as cur:
        cur.execute(f"DROP INDEX IF EXISTS {index_name};")
        cur.execute(f"CREATE INDEX {index_name} ON {table} USING {index_type} ({column});")
        conn.commit()

def drop_index(conn, index_name):
    """ Drop an index """
    with conn.cursor() as cur:
        index_name = index_name.replace("-", "_")
        cur.execute(f"DROP INDEX IF EXISTS {index_name};")
        conn.commit()

def fetch_explain_plan(conn, query, params):
    """ Fetch the EXPLAIN (ANALYZE, BUFFERS) plan for a given query """
    with conn.cursor() as cur:
        cur.execute("EXPLAIN (ANALYZE, BUFFERS) " + query, params)
        plan = cur.fetchall()
        return plan

def drop_tables(conn):
    """ Drop tables in the database to ensure a clean slate """
    with conn.cursor() as cur:
        try:
            cur.execute("DROP schema public cascade;")
            conn.commit()
            logging.info("Tables dropped successfully.")
        except Exception as e:
            logging.error(f"Failed to drop tables: {e}")
            conn.rollback()

def recreate_tables(conn, script_path):
    """ Execute SQL script to recreate tables """
    with conn.cursor() as cur, open(script_path, 'r') as file:
        sql_script = file.read()
        try:
            cur.execute(sql_script)
            conn.commit()
            logging.info("Tables recreated successfully.")
        except Exception as e:
            logging.error(f"Failed to recreate tables from script {script_path}: {e}")
            conn.rollback()


def insert_data(conn, data, query):
    """ Generic function to insert data into the database """
    with conn.cursor() as cur:
        cur.executemany(query, data)
        conn.commit()

def generate_random_table_data(n):
    """ Generate random table data for 'table_app' """
    tables = []
    for _ in range(n):
        name = ''.join(random.choices(string.ascii_letters, k=15))
        description = ''.join(random.choices(string.ascii_letters + string.digits, k=50))
        status = random.choice(['ACTIVE', 'NO_ACTIVE'])
        project_id = random.randint(1, n)
        tables.append((name, description, status, project_id))
    return tables


def generate_random_project_data(n):
    """ Generate random project data """
    projects = []
    for _ in range(n):
        name = ''.join(random.choices(string.ascii_lowercase, k=10))
        description = ''.join(random.choices(string.ascii_letters + string.digits, k=20))
        user_id = random.randint(1, n)  # Assuming users are pre-generated up to 1000
        projects.append((name, description, user_id))
    return projects

def generate_random_task_data(n):
    """ Generate random task data """
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

def measure_query_time_and_explain(conn, email, users, n=100, index_type="No Index", num_records=0):
    """ Measure the execution time of a query over 'n' repetitions and fetch the EXPLAIN (ANALYZE, BUFFERS) plan """
    """ Run a complex query involving multiple joins and conditions """


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
        WHERE
        ua.id_user = %s;
    """

    times = []
    explain_plan = None
    with conn.cursor() as cur:  # Create the cursor within the function
        for _ in range(n):
            user_id = random.choice(users)[4]
            start = time()
            cur.execute(query, [user_id])
            conn.commit()
            end = time()
            query_time = end - start
            times.append(query_time)
            if explain_plan is None:  # Fetch the EXPLAIN plan only once to avoid redundancy
                cur.execute("EXPLAIN (ANALYZE, BUFFERS) " + query,  [user_id])
                explain_plan = cur.fetchall()
        avg_time = np.mean(times)
        logging.info(f"Index Type: {index_type}, Number of Records: {num_records}, Average Query Time: {avg_time:.4f} seconds")
    return avg_time, explain_plan

def main():
    conn = psycopg2.connect(**conn_params)
    index_types = ["BTREE", "HASH"]
    results = []
    # Number of records for testing at different scales
    num_records = [100, 500, 1000, 5000, 10000]
    for n in range(10000, 60000, 10000):
        # Generate random data for users, projects, and tasks
        users = generate_random_user_data(n)
        projects = generate_random_project_data(n)
        tables = generate_random_table_data(n)  # Assuming project IDs are sequential
        tasks = generate_random_task_data(n)


        # Insert data into the database
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

        random_email = random.choice(users)[0]


        # No Index Test
        time_without_index, explain_no_index = measure_query_time_and_explain(conn, random_email, users, 10, "No Index", n)
        results.append((n, "No Index", time_without_index, str(explain_no_index)))
        drop_tables(conn)
        recreate_tables(conn, 'CreateTables.sql')

        for index_type in index_types:
            index_name = f"idx_user_id_{index_type.lower()}"
            create_index(conn, index_type, index_name, "public.user_app", "id_user")
            insert_random_users(conn, users)
            insert_data(conn, projects, project_insert_query)
            insert_data(conn, tables, table_insert_query)
            insert_data(conn, tasks, task_insert_query)  # Reinsert users for each index test

            time_with_index, explain_with_index = measure_query_time_and_explain(conn, random_email, users, 10, index_type, n)
            results.append((n, index_type, time_with_index, str(explain_with_index)))

            drop_index(conn, index_name)
            drop_tables(conn)  # Clear data after each index test
            recreate_tables(conn, 'CreateTables.sql')


    conn.close()

    df = pd.DataFrame(results, columns=["Number of Records", "Index Type", "Query Time (s)", "EXPLAIN Plan"])
    df.to_csv("index_performance_by_record_count_and_explain.csv", index=False)


    # Create the plot
    plt.figure(figsize=(14, 8))
    markers = {'No Index': 'o', 'BTREE': 's', 'HASH': '^'}  # Define markers for each index type
    for index_type in ["No Index", "BTREE", "HASH"]:
        subset = df[df["Index Type"] == index_type]
        plt.plot(subset["Number of Records"], subset["Query Time (s)"], label=index_type, marker=markers[index_type], markersize=8)

    plt.xlabel("Number of Records")
    plt.ylabel("Average Query Time (seconds)")
    plt.title("Query Performance vs Number of Records for Different Index Types")
    # plt.yscale('log')  # Optional: Set y-axis to logarithmic scale
    plt.legend()
    plt.grid(True)
    plt.savefig("query_performance_by_record_count.png")
    plt.show()

if __name__ == "__main__":
    main()
