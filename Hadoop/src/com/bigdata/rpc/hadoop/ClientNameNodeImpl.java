package com.bigdata.rpc.hadoop;

public class ClientNameNodeImpl implements ClientNameNode {
	@Override
	public String getDataNode() {
		return "{'/home/test.data':'blk_1,blk_2,blk_3'}";
	}
}
