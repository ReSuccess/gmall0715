package com.atguigu.gmall0715.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall0715.bean.*;
import com.atguigu.gmall0715.config.RedisUtil;
import com.atguigu.gmall0715.manage.constant.ManageConst;
import com.atguigu.gmall0715.manage.mapper.*;
import com.atguigu.gmall0715.service.ManageService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author sujie
 * @date 2019-12-27-16:47
 */
@Service
public class ManageServiceImpl implements ManageService {
    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(BaseCatalog2 baseCatalog2) {
        return baseCatalog2Mapper.select(baseCatalog2);
    }

    @Override
    public List<BaseCatalog3> getCatalog3(BaseCatalog3 baseCatalog3) {
        return baseCatalog3Mapper.select(baseCatalog3);
    }

    @Override
    public List<BaseAttrInfo> attrInfoList(BaseAttrInfo baseAttrInfo) {
        return baseAttrInfoMapper.select(baseAttrInfo);
    }

    @Override
    @Transactional(rollbackFor = {})
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if(baseAttrInfo.getId()==null){
            //保存平台属性
            baseAttrInfoMapper.insert(baseAttrInfo);
        }else{
            //更新平台属性
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        }
        //删除平台属性值，在重新添加
        BaseAttrValue baseAttrvalue = new BaseAttrValue();
        baseAttrvalue.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrvalue);

        // int i = 1/0;

        //保存平台属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if(attrValueList!=null && attrValueList.size()>0){
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //插入对应的平台属性id
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                //保存
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        return baseAttrValueMapper.select(baseAttrValue);
    }

    @Override
    public BaseAttrInfo     getBaseAttrInfo(String  attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        if(baseAttrInfo!=null){
            baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        }
        return baseAttrInfo;
    }

    @Override
    public List<SpuInfo> spuList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public List<BaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectAll() ;
    }

    @Override
    @Transactional(rollbackFor = {})
    public void saveSpuInfo(SpuInfo spuInfo) {
        //更新或者保存产品信息
        if(spuInfo.getId()==null ||spuInfo.getId().length()==0){
            spuInfo.setId(null);
            spuInfoMapper.insert(spuInfo);
        }else {
            spuInfoMapper.updateByPrimaryKeySelective(spuInfo);
        }
        //保存或者更新图片信息，采用先删除在保存措施，不进行更新操作
        //删除图片信息
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);
        //保存图片信息
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(spuImageList!=null && spuImageList.size()>0){
            for (SpuImage image : spuImageList) {
                image.setId(null);
                image.setSpuId(spuInfo.getId());
                spuImageMapper.insert(image);
            }
        }
        //保存或者更新商品销售属性和商品销售属性值，采用先删除在保存措施，不进行更新操作
        //删除商品销售属性
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);
        //删除商品销售属性值
        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuInfo.getId());
        spuSaleAttrValueMapper.delete(spuSaleAttrValue);

        //保存商品销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if(spuSaleAttrList != null && spuSaleAttrList.size()>0){
            for (SpuSaleAttr saleAttr : spuSaleAttrList) {
                saleAttr.setId(null);
                saleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(saleAttr);
                //保存商品销售属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
                if(spuSaleAttrValueList != null && spuSaleAttrValueList.size()>0){
                    for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                        saleAttrValue.setId(null);
                        saleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insert(saleAttrValue);
                    }
                }
            }
        }
    }

    @Override
    public List<SpuImage> spuImageList(SpuImage spuImage) {
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<BaseAttrInfo> attrInfoList(String catalog3Id) {
        return baseAttrInfoMapper.selectBaseAttrInfoListByCatalog3Id(catalog3Id);
    }

    @Override
    public List<SpuSaleAttr> spuSaleAttrList(String spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListBySpuId(spuId);
    }

    @Override
    @Transactional(rollbackFor = {})
    public void saveSkuInfo(SkuInfo skuInfo) {
        //不考虑更新
        //保存sku基本信息
        skuInfoMapper.insert(skuInfo);
        //保存sku图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(skuImageList != null && skuImageList.size()>0){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setId(null);
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            }
        }
        //保存sku平台属性信息
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(skuAttrValueList != null && skuAttrValueList.size()>0){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setId(null);
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            }
        }
        //保存sku销售属性信息
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(skuSaleAttrValueList != null && skuSaleAttrValueList.size()>0){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setId(null);
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }
    }

    @Override
    public SkuInfo getSkuInfoPage(String skuId) {
        return getSkuInfoByRedisson(skuId);
//        return getSkuInfoByRedisLock(skuId);
    }

    /**
     * 采用redisson解决分布式锁问题
     * tryLock(100, 10, TimeUnit.SECONDS);
     * 第一个参数：等待时间 ； 第二个参数：锁存在时间
     * @param skuId
     * @return
     */
    private SkuInfo getSkuInfoByRedisson(String skuId){
        SkuInfo skuInfo = null;
        RLock lock = null;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //拼接存储页面详情信息的redis中的key
            String skuKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
            String skuInfoJson = jedis.get(skuKey);
            if(skuInfoJson == null){
                //上锁
                //创建redisson配置文件
                Config config = new Config();
                //设置redis节点
                config.useSingleServer().setAddress("redis://192.168.116.130:6379");
                //创建redisson实例
                RedissonClient redissonClient = Redisson.create(config);
                //创建锁
                lock = redissonClient.getLock("myLock");
                System.out.println("redisson 分布式锁！");
                boolean tryLock = lock.tryLock(100, 10, TimeUnit.SECONDS);
                if(tryLock){
                    //查询数据库
                    skuInfo = this.getSkuInfoDB(skuId);
                    //放入缓存
                    jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfo));
                    return skuInfo;
                }
            }else{
                //使用缓存
                skuInfo = JSON.parseObject(skuInfoJson, SkuInfo.class);
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(lock != null){
                //解锁
                lock.unlock();
            }
            if(jedis != null){
                jedis.close();
            }
        }
        return getSkuInfoDB(skuId);
    }
    /**
     * 通过redis机制实现锁功能，解决高并发时，如果redis中没有数据，或者同时失效。
     * 同时查询数据库问题。
     * 通过lua 脚本解决该锁机制存在的问题
     *      如果在锁失效时间内完成查询，为了快速，结束后删除锁。如果用del删除redis中的锁
     *      数据来达到目的的话,会存在另一个问题。就是如果在规定时间内未完成查询，锁释放，
     *      这时另一个进程过来，上锁。当这个进程查询完成后会继续删锁，就造成将新来的查询的
     *      锁给删除，导致之后的全错
     * @param skuId
     * @return
     */
    private SkuInfo getSkuInfoByRedisLock(String skuId) {
        SkuInfo skuInfo = null;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //创建key
            String skuInfoKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
            String skuInfoJson = jedis.get(skuInfoKey);
            if(skuInfoJson == null || skuInfoJson.length() == 0){
                // 没有数据 ,需要加锁！取出完数据，还要放入缓存中，下次直接从缓存中取得即可！
                System.out.println("没有命中缓存");
                //上锁
                String lockKey = ManageConst.SKUKEY_PREFIX + skuId + ManageConst.SKULOCK_SUFFIX;
                String token = UUID.randomUUID().toString().replace("-", "");
                String lockStatus = jedis.set(lockKey, token, "NX", "PX", ManageConst.SKULOCK_EXPIRE_PX);
                //判断是否上锁成功
                if("OK".equals(lockStatus)){
                    System.out.println("获取锁！");
                    // 从数据库中取得数据
                    skuInfo = getSkuInfoDB(skuId);
                    // 存入redis中，setex 是存放有时间限制，不能无限存在
                    jedis.setex(skuInfoKey,ManageConst.SKUKEY_TIMEOUT, JSON.toJSONString(skuInfo));
                    // 解锁：
                    // jedis.del(skuKey); lua 脚本：
                    String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    jedis.eval(script, Collections.singletonList(lockKey),Collections.singletonList(token));
                    return skuInfo;
                }else{
                    System.out.println("等待！");
                    Thread.sleep(1000);
                    return getSkuInfoPage(skuId);
                }

            }else{
                skuInfo = JSON.parseObject(skuInfoJson, SkuInfo.class);
                return skuInfo;
            }
//            //判断是否存在缓存中
//            if(jedis.exists(skuInfoKey)){
//                //取出数据
//                String skuInfoJson = jedis.get(skuInfoKey);
//                if(skuInfoJson != null && skuInfoJson.length() > 0){
//                    //将json字符串转成对象
//                    skuInfo = JSON.parseObject(skuInfoJson,SkuInfo.class);
//                }
//            }else{
//                skuInfo = this.getSkuInfoDB(skuId);
//                //存入redis中，setex 是存放有时间限制，不能无限存在
//                jedis.setex(skuInfoKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfo));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭缓存
            if (jedis!=null){
                jedis.close();
            }
        }
        //如果redis宕机了 查询数据库
        return getSkuInfoDB(skuId);
    }

    /**
     * 从数据库查询 sku详情页面信息
     * @param skuId
     * @return
     */
    private SkuInfo getSkuInfoDB(String skuId) {
        //sku基本信息
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        //sku图片信息
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImageList);
        //sku平台属性值信息
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);
        skuInfo.setSkuAttrValueList(skuAttrValueList);
        return skuInfo;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {

        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuInfo.getSpuId(),skuInfo.getId());
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {

        return skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);
    }
}
