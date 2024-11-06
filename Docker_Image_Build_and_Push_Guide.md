
# Docker Image Build and Push Guide

This guide explains how to build a Docker image from a specific Dockerfile and push it to Docker Hub.

## Prerequisites
- Docker installed on your machine.
- A Docker Hub account (sign up at [Docker Hub](https://hub.docker.com/)).

## Steps

### 1. Log in to Docker Hub

First, log in to Docker Hub from the terminal to authenticate.

```bash
docker login
```

- Enter your Docker Hub **username** and **password** when prompted.

### 2. Build the Docker Image

Navigate to the directory containing your Dockerfile, then build your Docker image with the following command:

```bash
docker build -t your-username/your-image-name:tag -f path/to/Dockerfile .
```

- Replace `your-username` with your Docker Hub username.
- Replace `your-image-name` with the name you want to give the image.
- Replace `tag` with a version tag (e.g., `latest` or `v1.0`).
- Replace `path/to/Dockerfile` with the relative path to your Dockerfile.

**Example:**

```bash
docker build -t volnamax1/effortly-time-tracker:latest -f docker/Dockerfile .
```

### 3. Verify the Image

To confirm the image was built successfully, list your local Docker images:

```bash
docker images
```

You should see your image in the list, with the name and tag you specified.

### 4. Push the Image to Docker Hub

Once your image is built and tagged, push it to Docker Hub using:

```bash
docker push your-username/your-image-name:tag
```

**Example:**

```bash
docker push volnamax1/effortly-time-tracker:latest
```

### 5. Verify on Docker Hub

- Log in to [Docker Hub](https://hub.docker.com/) and navigate to your profile.
- You should see the image under "Repositories" with the tag you specified.

## Troubleshooting

If you see an error like `denied: requested access to the resource is denied`, make sure:
- You are logged in to Docker Hub (`docker login`).
- The image tag matches your Docker Hub username (e.g., `volnamax1/effortly-time-tracker:latest` if your username is `volnamax1`).

---

With these steps, you can now build and push Docker images to Docker Hub successfully. Happy Dockerizing! 🚀
