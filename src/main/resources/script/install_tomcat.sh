#!/bin/bash

source=/usr/local/source
tomcat=apache-tomcat-8.5.3

if [ ! -d $source ];then
	mkdir $source
fi


if [ ! -f $source/$tomcat.tar.gz ];then
wget http://mirror.apache-kr.org/tomcat/tomcat-8/v8.5.3/bin/$tomcat.tar.gz -O $source/$tomcat.tar.gz
fi

tar zxvf $source/$tomcat.tar.gz -C /usr/local/


if [ -s /usr/local/tomcat ];then
rm -f /usr/local/tomcat
fi

ln -s /usr/local/$tomcat  /usr/local/tomcat

useradd -M tomcat -u 28 >& /dev/null

cd /usr/local && chown -R tomcat:tomcat $tomcat

if [ ! -d /usr/local/www ];then
	mkdir -p /usr/local/www
fi

if [ -d /usr/local/www ];then
	cd /usr/local && chown -R tomcat:tomcat www
fi


rm -f $source/$tomcat.tar.gz


