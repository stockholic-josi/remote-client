#!/bin/bash

source=/usr/local/source

if [ ! -d $source ];then
	mkdir $source
fi

apr=apr-1.5.2
apr_util=apr-util-1.5.4
httpd=httpd-2.4.23
path=/usr/local/apache

if [  -d $path ];then
	exit 0
fi


if [ ! -f $source/$apr.tar.gz ];then
wget http://mirror.apache-kr.org/apr/$apr.tar.gz -O $source/$apr.tar.gz
fi

tar zxvf $source/$apr.tar.gz -C $source
cd $source/$apr && ./configure --prefix=/usr/local/apr
cd $source/$apr && make
cd $source/$apr && make install

if [ ! -f $source/$apr_util.tar.gz ];then
wget http://mirror.apache-kr.org/apr/$apr_util.tar.gz -O $source/$apr_util.tar.gz
fi

tar zxvf $source/$apr_util.tar.gz -C $source
cd $source/$apr_util && ./configure --with-apr=/usr/local/apr --prefix=/usr/local/apr-util
cd $source/$apr_util && make
cd $source/$apr_util && make install

yum -y install pcre-devel

if [ ! -f $source/$httpd.tar.gz ];then
wget http://mirror.apache-kr.org/httpd/$httpd.tar.gz -O $source/$httpd.tar.gz
fi

tar zxvf $source/$httpd.tar.gz -C $source
cd $source/$httpd && ./configure --prefix=$path --with-apr=/usr/local/apr --with-apr-util=/usr/local/apr-util --enable-module=so --enable-so --enable-rewrite
cd $source/$httpd && make
cd $source/$httpd && make install

cp $path/bin/apachectl /etc/rc.d/init.d/httpd

if [ ! -d /usr/local/www ];then
	mkdir -p /usr/local/www
fi

rm -rf $source/$apr*
rm -rf $source/$apr_util*
rm -rf $source/$httpd*