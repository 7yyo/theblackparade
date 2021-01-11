package tikv;

import com.flipkart.lois.channel.exceptions.ChannelClosedException;
import org.tikv.common.TiConfiguration;
import org.tikv.common.TiSession;
import org.tikv.kvproto.Kvrpcpb;
import org.tikv.raw.RawKVClient;
import shade.com.google.protobuf.ByteString;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class tikvDemo {

    private static final String PD_ADDRESS = "172.16.4.89:2307";
    private static final int NUM_COLLECTIONS = 10;
    private static final Logger logger = Logger.getLogger("Main");

    public static void main(String[] args) throws ChannelClosedException, InterruptedException {
        TiConfiguration conf = TiConfiguration.createRawDefault(PD_ADDRESS);
        TiSession session = TiSession.create(conf);
        RawKVClient rawKVClient = session.createRawClient();
        Random random = new Random(System.nanoTime());

        // write
        ByteString key = ByteString.copyFromUtf8(String.format("collection-%d", random.nextInt(NUM_COLLECTIONS)));
        ByteString value = ByteString.copyFromUtf8(String.format("%d", random.nextInt(NUM_COLLECTIONS)));
        rawKVClient.put(key, value);
        System.out.println("=================================================================");
        System.out.println("write to TiKV success!\n[Key]   = " + key + " = " + key.toStringUtf8() + "\n[Value] = " + value + "  = " + value.toStringUtf8());
        System.out.println("=================================================================");

        // select
        System.out.println("select from TiKV where key = " + key.toStringUtf8() + "[" + key + "]");
        ByteString result = rawKVClient.get(key);
        System.out.println("result = " + result.toStringUtf8());
        System.out.println("=================================================================");

        // scan limit
        List<Kvrpcpb.KvPair> resultList = rawKVClient.scan(ByteString.copyFromUtf8("collection-1"), 5);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i).getValue().toStringUtf8() + ",");
        }

        // scan where
        System.out.println("=================================================================");
        resultList = rawKVClient.scan(ByteString.copyFromUtf8("collection-1"), ByteString.copyFromUtf8("collection-3"), 10);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i).getValue().toStringUtf8() + ",");
        }
    }
}
