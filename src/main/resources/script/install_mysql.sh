#!/bin/bash

source=/usr/local/source

if [ ! -d $source ];then
 mkdir $source
fi

boost=boost_1_59_0
mysql=mysql-5.7.13
path=/usr/local/mysql

if [  -d $path ];then
	exit 0
fi


yum -y install cmake
yum -y install ncurses-devel

if [ ! -f $source/$boost.tar.gz ];then
wget  http://downloads.sourceforge.net/project/boost/boost/1.59.0/$boost.tar.gz -O $source/$boost.tar.gz
fi

if [ ! -d $source/$boost ];then
tar zxvf $source/$boost.tar.gz -C $source
fi
	
if [ ! -f $source/$mysql.tar.gz ];then
wget http://dev.mysql.com/get/Downloads/MySQL-5.7/$mysql.tar.gz -O $source/$mysql.tar.gz
fi

tar zxvf $source/$mysql.tar.gz -C $source	

cd $source/$mysql && cmake -DCMAKE_INSTALL_PREFIX=$path -DWITH_INNOBASE_STORAGE_ENGINE=1 -DDEFAULT_CHARSET=utf8 -DDEFAULT_COLLATION=utf8_general_ci -DDOWNLOAD_BOOST=1 -DWITH_BOOST=$source/$boost
cd $source/$mysql && make	
cd $source/$mysql && make install

useradd -M mysql -u 27 >& /dev/null

cd $path/bin && ./mysql_install_db --user=mysql --datadir=$path/data

chown -R mysql:mysql $path
cp $path/support-files/mysql.server /etc/rc.d/init.d/mysqld


rm -rf $source/$mysql


#root 패스워드 확인 
#cat /root/.mysql_secret

#/usr/local/mysql/bin/mysqladmin -u root -p<이전거> password '뉴패스워드'
# grant all privileges on *.* to merong@localhost identified by 'Wkwkdaus' 

#패스월드 까먹으면 안전모드 실행 후 패스워드 없이 들어감
#./mysqld_safe --skip-grant-tables &
# update user set authentication_string=password('qkrtjqkddhkTsi') where user='root';
# FLUSH PRIVILEGES;

#ERROR 1820 (HY000): You must reset your password using ALTER USER statement before executing this statement.
#SET PASSWORD = PASSWORD('qkrtjqkddhkTsi');


