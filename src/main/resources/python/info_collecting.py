#!/usr/bin/env python
# -*- coding: utf-8 -*-

# tiup - tidb-ansible pwd，execute`python info_gathering.py`
# The script needs to be executed by the central controller under the tidb-ansible directory
# eg：cd /home/tidb/tidb-ansible & python info_gathering.py

import os
os.system('sudo pip install prettytable >/dev/null 2>&1')
os.system('sudo pip install -U PyYAML==5.1 >/dev/null 2>&1')
import json
import urllib2
import yaml
import re
from prettytable import PrettyTable


with open('./inventory.ini', 'r') as f:
    info = f.readlines()


URL_IP_PORT_DIC = dict()

for item in info:
    if 'monitoring_servers' in item:
        monitor_ip = info[info.index(item) + 1].replace('\n', '')
        if len(re.split('[, ]+', monitor_ip)) > 1 and 'prometheus_port' in re.split('[, ]+', monitor_ip):
            for i in re.split('[, ]+', monitor_ip):
                if 'ansible_host' in i:
                    URL_IP_PORT_DIC['monitor_ip'] = i.split('=')[-1]
                if 'prometheus_port' in i:
                    URL_IP_PORT_DIC['monitor_port'] = i.split('=')[-1]
                else:
                    URL_IP_PORT_DIC['monitor_ip'] = re.split('[, ]+', monitor_ip)[0]
        else:
            URL_IP_PORT_DIC['monitor_ip'] = re.split('[, ]+', monitor_ip)[0]
            with open('./group_vars/monitoring_servers.yml', 'r') as f:
                info_port = yaml.load(f, Loader=yaml.FullLoader)
                URL_IP_PORT_DIC['monitor_port'] = info_port['prometheus_port']
    if 'tidb_servers' in item:
        tidb_server = info[info.index(item) + 1].replace('\n', '')
        if len(re.split('[, ]+', tidb_server)) > 1 and 'tidb_status_port' in re.split('[, ]+', tidb_server):
            for i in re.split('[, ]+', tidb_server):
                if 'ansible_host' in i:
                    URL_IP_PORT_DIC['tidb_ip'] = i.split('=')[-1]
                if '=' not in i:
                    URL_IP_PORT_DIC['tidb_ip'] = i
                if 'tidb_status_port' in i:
                    URL_IP_PORT_DIC['tidb_status_port'] = i.split('=')[-1]
        else:
            URL_IP_PORT_DIC['tidb_ip'] = re.split('[, ]+', tidb_server)[0]
            with open('./group_vars/tidb_servers.yml', 'r') as f:
                info_port = yaml.load(f, Loader=yaml.FullLoader)
                print(info_port)
                URL_IP_PORT_DIC['tidb_status_port'] = info_port['tidb_status_port']

tidb_url = "http://{tidb_ip}:{tidb_status_port}/{expression}"
pd_url = "http://{pd_ip_client_port}/{expression}"

NODE_GROUP_LIST = list()
INS_LIST = list()
SET_INS_DICT = dict()
step = '&step=15'
MAX_REPLICA = 'N/A'

expression_info = {
    'status': 'status',
    'pd_config': 'pd/api/v1/config',
    'hot_read': 'pd/api/v1/hotspot/regions/read',
    'hot_write': 'pd/api/v1/hotspot/regions/write'

}

METRICS_INFO = {
    'node_uname': 'node_uname_info',
    'node_count': 'probe_success{group=~"pd|tidb|tikv"}',
    'size': 'pd_cluster_status',
    'region_count': 'pd_cluster_status',
    'clu_qps': 'sum(rate(tidb_executor_statement_total[1m]))',
    'duration_999_ms': 'histogram_quantile(0.999,sum(rate(tidb_server_handle_query_duration_seconds_bucket[1m]))by(le,instance))',
    'duration_99_ms': 'histogram_quantile(0.99,sum(rate(tidb_server_handle_query_duration_seconds_bucket[1m]))by(le,instance))',
    'hot_region': 'pd_hotspot_status{type=~"hot_read_region_as_leader|hot_write_region_as_leader"}',
    'disk_read': 'irate(node_disk_read_time_seconds_total[5m])/irate(node_disk_reads_completed_total[5m])',
    'disk_write': 'irate(node_disk_write_time_seconds_total[5m])/irate(node_disk_writes_completed_total[5m])'

}

tidb_status_url = tidb_url.format(tidb_ip=URL_IP_PORT_DIC.get('tidb_ip'), tidb_status_port=URL_IP_PORT_DIC.get('tidb_status_port'), expression=expression_info.get('status'))
pro_url = "http://{ip}:{port}/api/v1/query?query=".format(ip=URL_IP_PORT_DIC.get('monitor_ip'), port=URL_IP_PORT_DIC.get('monitor_port'))


def collector_api_data(url):
    req = urllib2.Request(url)
    res = urllib2.urlopen(req)
    data = json.loads(res.read())
    return data


def collector_pro_data(url):
    # try:
    req = urllib2.Request(url)
    res = urllib2.urlopen(req)
    data_info = json.loads(res.read())
    assert('result' in data_info['data'])
    data = data_info['data']['result']
    # except Exception as e:
    #     raise e
    return data


def collect_uname(url, tb):
    print 'system info'
    url = url + METRICS_INFO['node_uname'] + step
    data = collector_pro_data(url)
    for item in data:
        node_name = item['metric']['nodename']
        node_release = item['metric']['release']
        row = [node_name, node_release]
        tb.add_row(row)
    return tb


def collect_node_count(url, tb):
    print 'TiDB cluster info'
    tidb = list()
    pd = list()
    tikv = list()
    node = list()
    global MAX_REPLICA
    url = url + METRICS_INFO['node_count'] + step
    data = collector_pro_data(url)
    tidb_version = collector_api_data(tidb_status_url)['version']
    for item in data:
        INS_LIST.append(item['metric']['instance'])
        NODE_GROUP_LIST.append(item['metric']['instance'])
        NODE_GROUP_LIST.append(item['metric']['group'])
        if item['metric']['group'] == 'tidb':
            tidb.append('tidb')
        if item['metric']['group'] == 'pd':
            pd.append('pd')
            MAX_REPLICA = collector_api_data(pd_url.format(pd_ip_client_port=item['metric']['instance'], expression=expression_info['pd_config']))['replication']['max-replicas']
        if item['metric']['group'] == 'tikv':
            tikv.append('tikv')
    row = [tidb_version, MAX_REPLICA, len(tidb), len(pd), len(tikv)]
    tb.add_row(row)
    return tb


def collect_clu_node(url, tb):
    print "cluster node info"
    set_ins_list = list(set([i.split(':')[0] for i in INS_LIST]))
    for index, item in enumerate(set_ins_list):
        SET_INS_DICT[item] = 'instance_%s' % index
    node_group_list_to_dict = dict(zip(NODE_GROUP_LIST[0::2], NODE_GROUP_LIST[1::2]))
    tmp_dict = dict()
    for i in range(len(set_ins_list)):
        tmp_dict.update({set_ins_list[i]: []})
    for k, v in node_group_list_to_dict.items():
        ip = k.split(':')[0]
        if tmp_dict.has_key(ip):
            tmp_dict[ip].append(v)
        else:
            tmp_dict[ip] = []
    for k, v in tmp_dict.items():
        row = [SET_INS_DICT[k], "+".join(v)]
        tb.add_row(row)
    return tb


def collect_capacity(url, tb):
    print "storage & region count"
    url = url + METRICS_INFO['size'] + step
    data = collector_pro_data(url)
    for item in data:
        if item['metric']['type'] == 'storage_size':
            size = format(float(item['value'][-1])/1024/1024/1024, '.2f')
        if item['metric']['type'] == 'region_count':
            region_count = item['value'][-1]
        if item['metric']['type'] == 'storage_capacity':
            capacity = format(float(item['value'][-1])/1024/1024/1024, '.2f')
    row = [capacity, size, region_count]
    tb.add_row(row)
    return tb


def collect_qps(url, tb):
    print "QPS"
    qps_url = url + METRICS_INFO['clu_qps'] + step
    duration999_url = url + METRICS_INFO['duration_999_ms'] + step
    duration99_url = url + METRICS_INFO['duration_99_ms'] + step
    qps = format(float(collector_pro_data(qps_url)[-1]['value'][-1]), '.2f')
    duration999 = format(float(collector_pro_data(duration999_url)[-1]['value'][-1])*1000, '.2f')
    duration99 = format(float(collector_pro_data(duration99_url)[-1]['value'][-1])*1000, '.2f')
    row = [qps, duration99, duration999]
    tb.add_row(row)
    return tb


def collect_hot_region(url, tb):
    print "hot region info"
    hot_region_url = url + METRICS_INFO['hot_region'] + step
    data = collector_pro_data(hot_region_url)
    hot_read_region_dict = dict()
    hot_write_region_dict = dict()
    for item in data:
        if item['metric']['type'] == 'hot_read_region_as_leader':
            hot_read_region_dict[item['metric']['store']] = item['value'][-1]
        if item['metric']['type'] == 'hot_write_region_as_leader':
            hot_write_region_dict[item['metric']['store']] = item['value'][-1]
    for read in hot_read_region_dict:
        for write in hot_write_region_dict:
            if read == write:
                row = ['store-%s' % write, hot_read_region_dict[read], hot_write_region_dict[write]]
                tb.add_row(row)
    return tb


def collect_disk_lat(url, tb):
    print "disk info"
    disk_read_url = url + METRICS_INFO['disk_read'] + step
    disk_write_url = url + METRICS_INFO['disk_write'] + step
    read_data = collector_pro_data(disk_read_url)
    write_data = collector_pro_data(disk_write_url)
    for read in read_data:
        for write in write_data:
            if read['metric']['device'] == write['metric']['device'] and read['metric']['instance'] == write['metric']['instance']:
                ip = read['metric']['instance'].split(':')[0]
                if SET_INS_DICT.has_key(ip):
                    row = [read['metric']['device'], SET_INS_DICT[ip], format(float(read['value'][-1])*1000, '.2f'), format(float(write['value'][-1])*1000, '.2f')]
                    tb.add_row(row)
    return tb


def table_init(tbl):
    table = PrettyTable()
    cols_map = {
        'uname': ["Host", "Release"],
        'node_count': ["TiDB_version", "Clu_replicas", "TiDB", "PD", "TiKV"],
        'clu_node': ['Node_IP', 'Server_info'],
        'capacity': ["Storage_capacity_GB", "Storage_uesd_GB", "Region_count"],
        'qps': ["Clu_QPS", "Duration_99_MS", "Duration_999_MS"],
        'hot_region': ["Store", "Hot_read", "Hot_write"],
        'disk_lat': ["Device", "Instance", "Read_lat_MS", "Write_lat_MS"]
    }
    table.field_names = cols_map[tbl]
    return table


def execute(collect, url, tb):
    try:
        tables = eval('collect_%s' % collect)(url, tb)
    except Exception as e:
        raise e
    return tables


if __name__ == '__main__':
    table_info = ['uname', 'node_count', 'clu_node', 'capacity', 'qps', 'hot_region', 'disk_lat']
    print('```')
    for item in table_info:
        tbl = table_init(item)
        table = execute(item, pro_url, tbl)
        print table
    print('```')

