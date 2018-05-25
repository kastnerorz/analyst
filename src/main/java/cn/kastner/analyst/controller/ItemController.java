package cn.kastner.analyst.controller;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.repository.core.CommentRepository;
import cn.kastner.analyst.repository.core.ItemRepository;
import cn.kastner.analyst.service.core.CommentService;
import cn.kastner.analyst.service.core.ItemService;
import cn.kastner.analyst.service.crawler.JdCrawlerService;
import cn.kastner.analyst.util.NetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
public class ItemController {

    private final
    ItemService itemService;

    private final
    CommentService commentService;

    private final
    NetResult netResult;

    private final
    JdCrawlerService jdCrawlerService;

    @Autowired
    public ItemController(ItemService itemService, CommentService commentService, NetResult netResult, JdCrawlerService jdCrawlerService) {
        this.itemService = itemService;
        this.commentService = commentService;
        this.netResult = netResult;
        this.jdCrawlerService = jdCrawlerService;
    }

    @RequestMapping(value = "/getItemInfoByItemId")
    public NetResult getItemInfoByItemId(@RequestParam Long itemId) {
        NetResult netResult = new NetResult();
        Item item = itemService.findById(itemId);
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
        List<Item> items = itemService.findByZhName(zhName);
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
    public NetResult getImageListByItemId(@RequestParam Long itemId) {
        Item item = itemService.findById(itemId);
        if (item != null) {
            netResult.result = item.getImageList();
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
//        List<String> cnames = itemService.findAllByCnameContaining(cname);
//        if (cnames != null) {
//            netResult.result = cnames;
//            netResult.status = 0;
//            return netResult;
//        }
//        netResult.result = "No such Cnames!";
//        netResult.status = -1;
//        return netResult;
//    }
    @RequestMapping(value = "/getItemComments")
    public NetResult getItemComments (@RequestParam Long itemId){
        Item item = itemService.findById(itemId);
        if (item.getCrawDate() == null) {
            try {
                jdCrawlerService.crawItemComment(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (LocalDate.now().minusWeeks(1).isAfter(item.getCrawDate())) {
            try {
                jdCrawlerService.crawItemComment(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        netResult.status = 0;
        netResult.result = commentService.findByItemAndCrawDateAfter(item, LocalDate.now().minusWeeks(1));


        return netResult;
    }
}
