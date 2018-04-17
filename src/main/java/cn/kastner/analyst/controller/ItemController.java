package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.ItemRepository;
import cn.kastner.analyst.util.NetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/getItemInfoByItemId")
    public NetResult getItemInfoByItemId(@RequestParam String itemId) {
        NetResult netResult = new NetResult();
        Item item = itemRepository.findItemByItemId(itemId);
        if (item != null) {
            netResult.result = item;
            netResult.status = 1;
            return netResult;
        }
        netResult.result = "No such Item!";
        netResult.status = 0;
        return netResult;

    }

//    @RequestMapping(value = "/getItemInfoByCname")
//    public NetResult getItemInfoByCname(@RequestParam String cname) {
//
//    }
}
