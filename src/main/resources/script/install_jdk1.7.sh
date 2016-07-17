#!/bin/bash

source=/usr/local/source

if [ ! -d $source ];then
 mkdir $source
fi

if [ ! -f $source/jdk-7u79-linux-x64.tar.gz ];then
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-x64.tar.gz"  -O $source/jdk-7u79-linux-x64.tar.gz
fi

tar zxvf $source/jdk-7u79-linux-x64.tar.gz -C /usr/local/

if [ -s /usr/local/java ];then
rm -f /usr/local/java
fi

ln -s /usr/local/jdk1.7.0_79  /usr/local/java

rm -f $source/jdk-7u79-linux-x64.tar.gz 

/usr/local/java/bin/java -version

