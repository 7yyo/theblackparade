# noinspection SqlResolveForFile

#SQL region status
select index_name,
       p.store_id,
       count(s.region_id) cnt
from information_schema.tikv_region_status s
         join information_schema.tikv_region_peers p on s.region_id = p.region_id
where s.table_name = "sbtest1"
  and (is_index = 0 or
       index_name in (select distinct key_name from information_schema.tidb_indexes where table_name = "sbtest1"))
group by index_name, p.store_id
order by index_name, cnt
desc;

#shard_row_id_bits=4
create table t1 (
    id bigint not null auto_random,
    c1 text default null,
    c2 text default null,
    primary key  (id)
) pre_split_regions=4;

create table t2 (
    id int not null,
    c1 varchar(20) not null,
    c2 timestamp not null default current_timestamp on update current_timestamp
)
partition by range (unix_timestamp(c2)) (
    partition p0 values less than ( unix_timestamp('2020-09-20 00:00:00') ),
    partition p1 values less than ( unix_timestamp('2020-09-21 00:00:00') ),
    partition p2 values less than ( unix_timestamp('2020-09-22 00:00:00') ),
    partition p3 values less than ( unix_timestamp('2020-09-23 00:00:00') ),
    partition p4 values less than ( unix_timestamp('2020-09-24 00:00:00') ),
    partition p5 values less than ( unix_timestamp('2020-09-25 00:00:00') ),
    partition p6 values less than ( unix_timestamp('2020-09-26 00:00:00') ),
    partition p7 values less than ( unix_timestamp('2020-09-27 00:00:00') ),
    partition p8 values less than ( unix_timestamp('2020-09-28 00:00:00') ),
    partition p9 values less than ( unix_timestamp('2020-09-29 00:00:00') ));

alter table t5 ADD partition (partition p12 values less than (unix_timestamp('2020-10-01 00:00:00')));

