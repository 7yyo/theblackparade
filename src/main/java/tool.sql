# noinspection SqlResolveForFile

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
    desc

