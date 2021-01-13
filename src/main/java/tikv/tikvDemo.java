package tikv;

import org.tikv.common.TiConfiguration;
import org.tikv.common.TiSession;
import org.tikv.kvproto.Kvrpcpb;
import org.tikv.raw.RawKVClient;
import shade.com.google.protobuf.ByteString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tikvDemo {

    private static final String PD_ADDRESS = "172.16.4.87:2379,172.16.4.89:2307,172.16.4.91:2379";

    public static void main(String[] args) {

        TiConfiguration conf = TiConfiguration.createRawDefault(PD_ADDRESS);
        TiSession session = TiSession.create(conf);
        /**
         * RawKVClient objects can be reused and are serial.
         * For example, request A to read key A and request B to read key B.
         * you need to wait for request A to complete execution before request B can be executed.
         */
        RawKVClient rawKVClient = session.createRawClient();

        // Define key value
        ByteString key = ByteString.copyFromUtf8("key-1");
        ByteString value = ByteString.copyFromUtf8("a");

        /**
         * Put a raw key-value pair to TiKV
         *
         * @param key raw key
         * @param value raw value
         */
        rawKVClient.put(key, value);

        Map<ByteString,ByteString> kvMap = new HashMap();
        kvMap.put(ByteString.copyFromUtf8("key-2"),ByteString.copyFromUtf8("b"));
        kvMap.put(ByteString.copyFromUtf8("key-3"),ByteString.copyFromUtf8("c"));
        kvMap.put(ByteString.copyFromUtf8("key-4"),ByteString.copyFromUtf8("d"));
        kvMap.put(ByteString.copyFromUtf8("key-5"),ByteString.copyFromUtf8("e"));

        /**
         * Batch put key-value to TiKV
         * @param
         * @return null
         */
        rawKVClient.batchPut(kvMap);

        /**
         * Get a raw key-value pair from TiKV if key exists
         *
         * @param key raw key
         * @return a ByteString value if key exists, ByteString.EMPTY if key does not exist
         */
        ByteString result = rawKVClient.get(key);

        /**
         * Scan raw key-value pairs from TiKV in range [startKey, endKey)
         *
         * @param startKey raw start key, inclusive
         * @param limit limit of key-value pairs scanned, should be less than {@link #MAX_RAW_SCAN_LIMIT}
         * @return list of key-value pairs in range
         */
        List<Kvrpcpb.KvPair> resultList = rawKVClient.scan(ByteString.copyFromUtf8("key-2"), 3);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i).getValue().toStringUtf8() + ",");
        }
        System.out.println("==");

        /**
         * Scan raw key-value pairs from TiKV in range [startKey, endKey)
         *
         * @param startKey raw start key, inclusive
         * @param endKey raw end key, exclusive
         * @param limit limit of key-value pairs scanned, should be less than {@link #MAX_RAW_SCAN_LIMIT}
         * @return list of key-value pairs in range
         */
        resultList = rawKVClient.scan(ByteString.copyFromUtf8("key-2"), ByteString.copyFromUtf8("key-4"), 5);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i).getValue().toStringUtf8() + ",");
        }

        /**
         * Delete a raw key-value pair from TiKV if key exists
         *
         * @param key raw key to be deleted
         */
        rawKVClient.delete(ByteString.copyFromUtf8("key-3"));
        resultList = rawKVClient.scan(ByteString.copyFromUtf8("key-2"), ByteString.copyFromUtf8("key-4"), 5);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i).getValue().toStringUtf8() + ",");
        }

        System.exit(0);
    }
}
