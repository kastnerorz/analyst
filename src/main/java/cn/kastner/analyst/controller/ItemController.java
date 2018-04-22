package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.ItemRepository;
import cn.kastner.analyst.util.NetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/getItemInfoByItemId")
    public NetResult getItemInfoByItemId(@RequestParam String itemId) {
        NetResult netResult = new NetResult();
        Item item = itemRepository.findByItemId(itemId);
        if (item != null) {
            netResult.result = item;
            netResult.status = 0;
            return netResult;
        }
        netResult.result = "No such Item!";
        netResult.status = -1;
        return netResult;

    }

    @RequestMapping(value = "/getItemInfoByCname")
    public NetResult getItemInfoByCname(@RequestParam String zhName) {
        NetResult netResult = new NetResult();
        List<Item> items = itemRepository.findAllByZhName(zhName);
        if (items != null) {
            netResult.result = items;
            netResult.status = 0;
            return netResult;
        }
        netResult.result = "No such Items!";
        netResult.status = -1;
        return netResult;
    }

    @RequestMapping(value = "/getImageListByItemId")
    public NetResult getImageListByItemId(@RequestParam String itemId) {
        NetResult netResult = new NetResult();
        Item item = itemRepository.findByItemId(itemId);
        if (item != null) {
            String imageList = item.getImageList();
            netResult.result = imageList;
            netResult.status = 0;
            return netResult;
        }
        netResult.result = "No such Item!";
        netResult.status = -1;
        return netResult;
    }

//    @RequestMapping(value = "/test")
//    public NetResult test(@RequestParam String cname) {
//        NetResult netResult = new NetResult();
//        List<String> cnames = itemRepository.findAllByCnameContaining(cname);
//        if (cnames != null) {
//            netResult.result = cnames;
//            netResult.status = 0;
//            return netResult;
//        }
//        netResult.result = "No such Cnames!";
//        netResult.status = -1;
//        return netResult;
//    }
}
