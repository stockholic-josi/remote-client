#!/bin/bash

source=/usr/local/source
nginx=nginx-1.10.1

if [ ! -d $source ];then
 mkdir $source
fi

if [ ! -f $source/$nginx.tar.gz ];then
wget http://nginx.org/download/$nginx.tar.gz -O $source/$nginx.tar.gz
fi

tar -zxvf $source/$nginx.tar.gz  -C $source

cd $source/$nginx && ./configure --prefix=/usr/local/nginx

cd $source/$nginx && make
cd $source/$nginx && make install

if [ ! -d /usr/local/www ];then
	mkdir -p /usr/local/www
fi

rm -rf $source/$nginx