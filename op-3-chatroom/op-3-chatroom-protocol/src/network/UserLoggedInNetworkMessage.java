package network;

public class UserLoggedInNetworkMessage extends NetworkMessage {

    public static final byte code = 0x06;

    public UserLoggedInNetworkMessage(String username) {
        super(new Object[]{ username });
    }

    public UserLoggedInNetworkMessage(byte[] bytes) {
        this( unpack(bytes) );
    }

    public String getUsername() {
        return (String) data[0];
    }

    @Override
    public byte[] pack() {
        Packer packer = new Packer();
        packer.packByte(code);
        packer.packString(getUsername());
        return packer.getArray();
    }

    private static String unpack(byte[] bytes) {
        Unpacker unpacker = new Unpacker(bytes);
        unpacker.skip(1);
        return unpacker.unpackString();
    }
}