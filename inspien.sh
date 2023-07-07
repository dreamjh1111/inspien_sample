docker build -f /home/ubuntu/inspien_sample/Dockerfile.dev -t weatherapi . && docker run -itd -p 8080:8080 --restart=always --name inspien weatherapi
