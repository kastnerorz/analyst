package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/getItemInfo")
    public Item getItemInfo(@RequestParam String id) {
        return itemRepository.findItemByItemId(id);
    }
}
