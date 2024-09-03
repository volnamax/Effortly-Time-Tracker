import psycopg2

try:
    conn = psycopg2.connect(
        dbname="dataBaseEfortly",
        user="reg_user",
        password="new_secure_password",  # Убедитесь, что здесь правильный пароль
        host="localhost",
        port="5432"
    )
    cursor = conn.cursor()
    cursor.execute("INSERT INTO public.user_app (data_last_log_in, data_sign_in, email, password, user_name, user_secondname, role_id) VALUES (NOW(), NOW(), 'admin@example.com', 'admin_password', 'Admin', 'User', 1);")


#
#     cursor.execute(""" INSERT INTO public.todo_node (content, priority, status, due_data, user_id)
# VALUES
# ('Завершить отчёт по проекту', 'IMPORTANT_URGENTLY', 'ACTIVE', '2024-06-30 17:00:00', 1);
# """)
    conn.commit

    cursor.close()
    conn.close()
    print("Connected and query executed successfully!")
except Exception as e:
    print("Error:", e)
