package com.wchm.website.controller;

import com.wchm.website.service.BlockChainBrowserService;
import com.wchm.website.util.Result;
import com.wchm.website.vo.BlockChainVo;
import com.wchm.website.vo.DetailsVo;
import com.wchm.website.vo.Transaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 官网区块链浏览器模块所有请求接口写在此类下
 */

@Api(tags = "区块链浏览器")
@RestController
@RequestMapping("/browser")
public class BlockChainBrowserController {

    @Autowired
    BlockChainBrowserService blockChainBrowserService;

    /**
     * 查询区块链首页数据
     *
     * @return
     */
    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "区块链首页查询", response = BlockChainVo.class)
    public Result queryIndexData() {
        try {
            return blockChainBrowserService.queryIndexData();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.create().fail("查询数据失败，请稍后重试");
        }
    }

    /**
     * 区块链首页-最新交易记录
     *
     * @return
     */
    @GetMapping("/trans")
    @ResponseBody
    @ApiOperation(value = "区块链首页-最新交易记录", response = Transaction.class)
    public Result queryIndexDataTransaction() {
        try {
            List<Transaction> trans = blockChainBrowserService.queryIndexDataTransaction();
            return Result.create().success(trans);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.create().fail("查询数据失败，请稍后重试");
        }
    }

//     TODO 查询最新区块，第一版暂时不要
//    /**
//     * 区块链首页-最新区块
//     * @return
//     */
//    @GetMapping("/block")
//    @ResponseBody
//    @ApiOperation(value = "区块链首页-最新区块", response = Block.class)
//    public Result queryIndexDataBlock() {
//        try {
//            Set<Block> block = blockChainBrowserService.queryIndexDataBlock();
//            return Result.create().success(block);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.create().fail("查询数据失败，请稍后重试");
//        }
//    }


    /**
     * https://api.etherscan.io/api?module=account&action=tokentx&contractaddress=0x183630c3AfA08957E588eaa26b748cc5C2D42DC6&startblock=0&endblock=99999999&page=1&offset=10&sort=desc&address=0xbd2fd77d6c2f4ba55d39825fe24b4993dbd79913&apikey=RAUWFA7NCA5H2IMDJ4V5GYK6ARVTA7Y23U
     *
     * contractaddress: 代币地址-固定
     * address:         钱包地址
     *
     *
     */

    /**
     * 区块链搜索框查询数据
     *
     * @return
     */
    @GetMapping("/search")
    @ResponseBody
    @ApiOperation(value = "区块链搜索框查询数据", response = DetailsVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hash", value = "区块哈希/钱包地址", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页，默认1", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小，默认20", required = true, paramType = "query"),
    })
    public Result searchData(String hash, Integer pageNum, Integer pageSize) {
        try {
            return blockChainBrowserService.searchData(hash, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.create().fail("查询数据失败，请稍后重试");
        }
    }


}