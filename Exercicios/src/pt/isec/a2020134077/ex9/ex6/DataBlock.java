package pt.isec.a2020134077.ex9.ex6;

import java.io.Serializable;

public class DataBlock implements Serializable {

    public byte[]block;
    public boolean last = false;

    public int nBytes;

    public DataBlock(){

    }

    public DataBlock (boolean last){
        this.last = last;
    }

    public void setBlock(byte[] data){
        this.block = data;
    }

    public void setBytes(int n){
        this.nBytes = n;
    }
}
