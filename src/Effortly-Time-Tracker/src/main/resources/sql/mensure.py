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

def generate_random_user_data(n):
    """ Generate random user data """
    users = []
    for _ in range(n):
        email = ''.join(random.choices(string.ascii_lowercase, k=10)) + '@example.com'
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

def clear_data(conn):
    with conn.cursor() as cur:
        try:
            cur.execute("DELETE FROM public.user_app;")
            conn.commit()
            logging.info("Data cleared successfully from user_app.")
        except Exception as e:
            logging.error(f"Failed to clear data: {e}")
            conn.rollback()



def measure_query_time_and_explain(conn, query, users, n=10, index_type="No Index", num_records=0):
    """ Measure the execution time of a query over 'n' repetitions and fetch the EXPLAIN (ANALYZE, BUFFERS) plan """
    times = []
    explain_plan = None
    with conn.cursor() as cur:  # Create the cursor within the function
        for _ in range(n):
            random_user = random.choice(users)  # Choose a random user each time
            email_to_search = random_user[0]    # Email is the first element in the tuple
            params = [email_to_search]          # Params must be a list or tuple
            start = time()
            cur.execute(query, params)
            conn.commit()
            end = time()
            query_time = end - start
            times.append(query_time)
            if explain_plan is None:  # Fetch the EXPLAIN plan only once to avoid redundancy
                cur.execute("EXPLAIN (ANALYZE, BUFFERS) " + query, params)
                explain_plan = cur.fetchall()
        avg_time = np.mean(times)
        logging.info(f"Index Type: {index_type}, Number of Records: {num_records}, Average Query Time: {avg_time:.4f} seconds")
    return avg_time, explain_plan

def main():
    conn = psycopg2.connect(**conn_params)
    num_records = [100, 500, 1000, 5000, 10000]  # Adjust as necessary
    index_types = ["BTREE", "HASH"]
    results = []

    for n in range(1000, 10000, 100):
        users = generate_random_user_data(n)
        insert_random_users(conn, users)
        query = "SELECT * FROM public.user_app WHERE email = %s;"

        # No Index Test
        time_without_index, explain_no_index = measure_query_time_and_explain(conn, query, users, 10, "No Index", n)
        results.append((n, "No Index", time_without_index, str(explain_no_index)))
        clear_data(conn)

        for index_type in index_types:
            index_name = f"idx_email_{index_type.lower()}"
            create_index(conn, index_type, index_name, "public.user_app", "email")
            insert_random_users(conn, users)  # Reinsert users for each index test

            time_with_index, explain_with_index = measure_query_time_and_explain(conn, query, users, 10, index_type, n)
            results.append((n, index_type, time_with_index, str(explain_with_index)))

            drop_index(conn, index_name)
            clear_data(conn)  # Clear data after each index test

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
