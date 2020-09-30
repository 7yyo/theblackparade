ip=$1
port=$2
user=$3
password=$4
table=$5
db=$6

echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
url="mysql -u"$user" -p"$password" -h"$ip" -P"$port" -N -e "
echo "1.url ========" $url

date=`$url"select date_sub(curdate(),interval -1 day)";`" 00:00:00"
echo "2.Tomorrow ======== "$date

old_parition_name=`$url"USE INFORMATION_SCHEMA;SELECT PARTITION_NAME FROM PARTITIONS WHERE TABLE_NAME = '"$table"' ORDER BY PARTITION_ORDINAL_POSITION DESC LIMIT 1;"`

if [ ! -n "$old_parition_name" ]; then
echo "3.table is not exists!"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
exit
fi

echo "3.now table "$table" partition ======== "$old_parition_name

parition_index=${old_parition_name:1}
echo "4.now table "$table" parition_index ======== "$parition_index

new_parition_name="p"$(($parition_index+1))
echo "5.now table "$table" new_parition_name ======== "$new_parition_name

sql="ALTER TABLE "$db"."$table" ADD PARTITION (PARTITION "$new_parition_name" VALUES LESS THAN (UNIX_TIMESTAMP('"$date"')));"
echo "6.begin to execute sql ========= "$sql
`$url" $sql";`
echo "7.Execute success"

now_parition_name=`$url "USE INFORMATION_SCHEMA;SELECT PARTITION_NAME FROM PARTITIONS WHERE TABLE_NAME = '"$table"' ORDER BY PARTITION_ORDINAL_POSITION DESC LIMIT 1;"`
echo "8..now table "$table" partition ======== "$now_parition_name
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"