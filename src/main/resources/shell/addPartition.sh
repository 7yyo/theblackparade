ip=$1
port=$2
user=$3
password=$4
table=$5
db=$6

echo "#####################################################################"
url="mysql -u"$user" -p"$password" -h"$ip" -P"$port" -N -e "
echo "Url ========" $url

date=`$url"SELECT CONCAT((SELECT CURRENT_DATE),' 00:00:00')";`
echo "Today ======== "$date

old_parition_name=`$url"USE INFORMATION_SCHEMA;SELECT PARTITION_NAME FROM PARTITIONS WHERE TABLE_NAME = '"$table"' ORDER BY PARTITION_ORDINAL_POSITION DESC LIMIT 1;"`
echo "Now table "$table" partition ======== "$old_parition_name

parition_index=${old_parition_name:1}
echo "Now table "$table" parition_index ======== "$parition_index

new_parition_name="p"$(($parition_index+1))
echo "Now table "$table" new_parition_name ======== "$new_parition_name

sql="ALTER TABLE "$db"."$table" ADD PARTITION (PARTITION "$new_parition_name" VALUES LESS THAN (UNIX_TIMESTAMP('"$date"')));"
echo "Begin to execute sql ========= "$sql
`$url" $sql";`
echo "Execute success"

now_parition_name=`$url "USE INFORMATION_SCHEMA;SELECT PARTITION_NAME FROM PARTITIONS WHERE TABLE_NAME = '"$table"' ORDER BY PARTITION_ORDINAL_POSITION DESC LIMIT 1;"`
echo "Now table "$table" partition ======== "$now_parition_name
echo "#####################################################################"