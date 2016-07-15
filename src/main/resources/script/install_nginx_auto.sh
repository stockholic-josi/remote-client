#!/bin/bash
function printTitle {
    echo -e "\e[1;97m=======================================\e[0m"
    echo -e "\e[1;97m* $1\e[0m"
    echo -e "\e[1;97m=======================================\e[0m"
}

function printJob {
    echo -e "\e[94m* $1\e[0m"
}

function printSubJob {
    echo -e "\e[96m\t$1\e[0m"
}

printTitle "Nginx 설치"

printJob "httpd.service 종료"
systemctl stop httpd.service

printJob "httpd.service 제거"
yum remove -y httpd
systemctl disable httpd.service

printJob "Nginx 설치"
if [ ! -e /etc/yum.repos.d/nginx.repo ]; then
    printSubJob "nginx repository 추가"
    echo "[nginx]" > /etc/yum.repos.d/nginx.repo
    echo "name=nginx repo" >> /etc/yum.repos.d/nginx.repo
    echo "baseurl=http://nginx.org/packages/mainline/centos/\$releasever/\$basearch/" >> /etc/yum.repos.d/nginx.repo
    #echo "baseurl=http://nginx.org/packages/centos/\$releasever/\$basearch/" >> /etc/yum.repos.d/nginx.repo
    echo "gpgcheck=0" >> /etc/yum.repos.d/nginx.repo
    echo "enabled=1" >> /etc/yum.repos.d/nginx.repo
fi

printSubJob "nginx package 설치"
yum install -y nginx

printJob "Nginx 서비스 활성"
systemctl enable nginx.service

printJob "Nginx 서비스 시작"
systemctl start nginx.service

printJob "방화벽 설정"
if [ `firewall-cmd --zone=public --list-all|grep "services"|grep "http" | wc -l` -eq 0 ]; then
    printSubJob "http/https 개방"
    firewall-cmd --permanent --zone=public --add-service=http
    firewall-cmd --permanent --zone=public --add-service=https

    printSubJob "방화벽 rule reload"
    firewall-cmd --reload
fi