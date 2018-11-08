package com.wchm.website.service;

import com.wchm.website.vo.Block;
import com.wchm.website.vo.Transaction;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

/**
 * 官网区块链浏览器模块所有接口写在此类下
 */

@Service
public interface BlockChainBrowserService {
    /**
     * 查询区块链首页数据
     */
    Result queryIndexData() throws Exception;

    /**
     * 区块链搜索框查询数据
     */
    Result searchData(String hash, Integer pageNum, Integer pageSize) throws Exception;

    /**
     * 区块链首页-最新交易记录
     */
    List<Transaction> queryIndexDataTransaction();

    /**
     * 区块链首页-最新区块
     */
    Set<Block> queryIndexDataBlock() throws Exception;
}
