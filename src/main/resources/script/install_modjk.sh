#!/bin/bash

source=/usr/local/source
connectors=tomcat-connectors-1.2.41

if [ ! -d $source ];then
	mkdir $source
fi

if [ ! -f $source/$connectors-src.tar.gz ];then
wget http://ftp.daumkakao.com/apache//tomcat/tomcat-connectors/jk/$connectors-src.tar.gz -O $source/$connectors-src.tar.gz
fi

tar zxvf $source/$connectors-src.tar.gz -C $source	
cd $source/$connectors-src/native && ./configure  -with-apxs=/usr/local/apache/bin/apxs
cd $source/$connectors-src/native && make	
cd $source/$connectors-src/native && make install

rm -rf  $source/$connectors*
