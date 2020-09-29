date=`mysql -uroot -P4000 -p'yuyang@123' -h172.16.4.104 -N -e "select CONCAT((select current_date),' 00:00:00')";`
echo "today is date ======== "${date}

old_parition_name=`mysql -uroot -P4000 -p'yuyang@123' -h172.16.4.104 -N -e"use information_schema;select PARTITION_NAME from PARTITIONS where table_name = '$1' order by PARTITION_ORDINAL_POSITION desc limit 1;"`
echo "Now table '$1' newest partition ======== "$old_parition_name

parition_index=${old_parition_name:1}
echo "table '$1' parition_index ======== "$parition_index

new_parition_name="p"$(($parition_index+1))
echo "table '$1' new_parition_name ======== "$new_parition_name

sql="ALTER TABLE "$1" ADD PARTITION (PARTITION "$new_parition_name" VALUES LESS THAN (UNIX_TIMESTAMP('"$date"')));"
echo "begin to execute sql ========= "$sql
`mysql -uroot -P4000 -p'yuyang@123' -h172.16.4.104 -N -e "use test;$sql";`

now_parition_name=`mysql -uroot -P4000 -p'yuyang@123' -h172.16.4.104 -N -e"use information_schema;select PARTITION_NAME from PARTITIONS where table_name = '$1' order by PARTITION_ORDINAL_POSITION desc limit 1;"`
echo "Now table '$1' newest partition ======== "$now_parition_name