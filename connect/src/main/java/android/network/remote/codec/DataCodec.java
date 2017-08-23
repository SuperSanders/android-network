package android.network.remote.codec;

import android.network.protocol.Packet;
import android.network.protocol.Protocol;
import android.network.remote.binder.RemoteBinder;

import com.dream.socket.codec.Codec;
import com.dream.socket.codec.Decode;
import com.dream.socket.codec.Encode;
import com.dream.socket.codec.Handle;

import java.nio.ByteBuffer;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class DataCodec extends Codec<Packet, Packet> implements Decode<Packet>, Encode<Packet> {

    @Override
    public Decode<Packet> getDecode() {
        return this;
    }

    @Override
    public Encode<Packet> getEncode() {
        return this;
    }

    @Override
    public Packet decode(ByteBuffer buffer) {
        int limit = buffer.limit();
        if (limit < Protocol.HEADER_LENGTH) {
            return null;
        }
        char start = (char) buffer.get();
        byte version = buffer.get();
        int length = buffer.getInt();//包的总长度 包括头
        buffer.get(Protocol.RETAIN);
        char xy = (char) buffer.get();
        if (limit < length) {
            return null;
        }
        byte[] bytes = new byte[length - Protocol.HEADER_LENGTH];
        buffer.get(bytes);
        char end = (char) buffer.get();
        return new Packet(bytes);
    }

    @Override
    public void encode(Packet packet, ByteBuffer buffer) {
        buffer.put(Protocol.START_TAG);
        buffer.put(Protocol.VERSION);
        buffer.putInt(packet.getBody().length + Protocol.HEADER_LENGTH);
        buffer.put(Protocol.RETAIN);
        buffer.put(Protocol.VERIFY_TAG);
        buffer.put(packet.getBody());
        buffer.put(Protocol.END_TAG);
    }
}