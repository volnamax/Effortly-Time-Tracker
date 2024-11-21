
# Инструкция по настройке и запуску сервиса с использованием Podman и Systemd на VK Cloud

Эта инструкция поможет вам настроить и запустить сервис с использованием Podman и Systemd на виртуальной машине в VK Cloud. Предполагается, что у вас есть SSH-доступ к серверу и вы уже загрузили SSH-ключ.

## Шаги для подключения к серверу

1. Подключитесь к серверу по SSH, используя загруженный ключ:

    ```bash
    ssh -i <путь к ключу> centos@<IP-адрес сервера>
ssh -i D:/service/src/Effortly-Time-Tracker/src/main/resources/certs/centos-STD2-1-1-10GB-mGkqAcOF.pem centos@90.156.219.228

    ```

    ```bash
    ssh -i <путь к ключу> centos@<IP-адрес сервера>
    ```
   
2. После подключения выполните команду для получения прав root:

    ```bash
    sudo bash
    ```

## Установка Podman

Podman может быть уже установлен на сервере по умолчанию, но если он отсутствует, выполните следующие команды:

    ```bash
    dnf install -y podman
    ```

Проверьте версию Podman, чтобы убедиться, что установка прошла успешно:

    ```bash
    podman --version
    ```

## Запуск контейнера с Podman

1. Запустите контейнер с вашим приложением, используя Podman. Замените `your-dockerhub-username/your-image` на имя вашего Docker-образа в Docker Hub:

    ```bash
    podman run -d -p 80:8080 --name effortly-time-tracker docker.io/your-dockerhub-username/effortly-time-tracker:latest
    ```

## Генерация systemd-файла для автоматического запуска

1. Сгенерируйте systemd-файл с помощью Podman:

    ```bash
    podman generate systemd --name effortly-time-tracker --files --new
    ```

    Это создаст файл в домашней директории, например: `/home/centos/container-effortly-time-tracker.service`.

2. Переместите этот файл в директорию Systemd:

    ```bash
    mv /home/centos/container-effortly-time-tracker.service /etc/systemd/system/
    ```

## Настройка SELinux

1. Если при попытке запустить службу возникает ошибка SELinux, выполните следующие команды для создания временного разрешения:

    ```bash
    ausearch -c 'systemd' --raw | audit2allow -M my-systemd
    semodule -i my-systemd.pp
    ```

2. Отключите SELinux временно, чтобы убедиться, что проблема действительно в нём:

    ```bash
    setenforce 0
    ```

## Запуск и активация службы

1. Перезагрузите демона `systemd`:

    ```bash
    systemctl daemon-reload
    ```

2. Запустите службу и проверьте её статус:

    ```bash
    systemctl start container-effortly-time-tracker.service
    systemctl status container-effortly-time-tracker.service
    ```

3. Если служба запустилась корректно, активируйте её для автозапуска при старте системы:

    ```bash
    systemctl enable container-effortly-time-tracker.service
    ```

## Проверка работы сервиса

Теперь ваш сервис должен быть доступен на IP-адресе вашего сервера через порт 80. Вы можете открыть браузер и перейти по адресу `http://<IP-адрес сервера>`, чтобы проверить работу приложения.

## Полезные команды для управления службой

- Остановить службу:

    ```bash
    systemctl stop container-effortly-time-tracker.service
    ```

- Перезапустить службу:

    ```bash
    systemctl restart container-effortly-time-tracker.service
    ```

- Отключить автозапуск службы:

    ```bash
    systemctl disable container-effortly-time-tracker.service
    ```

## Дополнительные замечания

- **SELinux**: Если вы хотите оставить SELinux включённым, рекомендуется настроить SELinux политику, чтобы избежать временного отключения.
- **Podman**: Подходит для случаев, когда Docker недоступен. Команды аналогичны Docker, но иногда требуется дополнительная настройка.

Это завершает инструкцию по настройке и запуску сервиса с использованием Podman и Systemd на VK Cloud.
