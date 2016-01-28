package com.caixin.transfer.biz.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caixin.transfer.biz.product.BussProductService;
import com.caixin.transfer.entity.product.BussProductInfo;
import com.caixin.transfer.mapper.product.BussProductInfoMapperExt;

/**
 * 类BussProductServiceImpl.java的实现描述：
 * @author roy.cai 2016年1月28日 上午10:39:02
 */
@Service
public class BussProductServiceImpl implements BussProductService {

    @Autowired
    private BussProductInfoMapperExt bussProductInfoMapperExt;

    public BussProductServiceImpl() {
        
    }

    @Override
    public List<BussProductInfo> selectProductInfo() {
        return null;
    }

}
