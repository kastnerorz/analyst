package cn.kastner.analyst.crawler;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.CommentRepository;
import cn.kastner.analyst.repository.ItemRepository;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.json.JSONArray;


import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JdCrawler {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CommentRepository commentRepository;

    /**
     * @param url
     * @return itemId
     */
    public String crawItem (String url) {
        Document doc = new Document("");
        String itemCname = "";
        String itemModel = "";
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                    .get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern cnamePattern = Pattern.compile("(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        Matcher cnameMatcher = cnamePattern.matcher(doc.title());
        if (cnameMatcher.find()) {
            itemCname = cnameMatcher.group();
        }

        Pattern modelPattern = Pattern.compile("\\（(.+)\\）");
        Matcher modelMatcher = modelPattern.matcher(doc.title());
        if (modelMatcher.find()) {
            itemModel = modelMatcher.group();
        }

        // check if has item already
        List<Item> items = itemRepository.findItemByCnameContaining(itemCname);
        if (items != null) {
            return items.get(0).getItemId();
        }

        // no item => new item
        Item item = new Item();
        String skuid = "";
        // analyse doc
        // get skuid
        Pattern skuidPattern = Pattern.compile("skuid: (\\d*),");
        Matcher skuidMatcher = skuidPattern.matcher(doc.head().toString());
        if (skuidMatcher.find()) {
            skuid = skuidMatcher.group(1);
        }
        Integer jQueryId = 3456098;
        Long timeStamp = System.currentTimeMillis()/1000;
        // get commentsCount
        Document CommentsCountDoc = new Document("");
        try {
            CommentsCountDoc = Jsoup.connect("https://club.jd.com/comment/productCommentSummaries.action?")
                    .header(":authority", "club.jd.com")
                    .header(":method", "GET")
                    .header(":path", "/comment/productCommentSummaries.action?referenceIds=" + skuid +
                            "&callback=jQuery" + (jQueryId + 1) +
                            "&_=" + System.currentTimeMillis()/1000)
                    .header(":scheme", "https")
                    .header("accept", "*/*")
                    .header("accept-encoding", "gzip, deflate, br")
                    .header("accept-language", "zh-CN,zh;q=0.9")
                    .header("referer", "https://item.jd.com/6577495.html")
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .data("referenceIds", skuid)
                    .data("callback", "jQuery" + jQueryId)
                    .data("_", "" + System.currentTimeMillis() / 1000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject commentsCount;
        Pattern commentsCountPattern = Pattern.compile("^jQuery" + jQueryId + "\\(\\{\\\"CommentsCount\\\":\\[(.+)]\\}\\);$");
        Matcher commentsCountMatcher = commentsCountPattern.matcher(CommentsCountDoc.toString());
        if (commentsCountMatcher.find()) {
            commentsCount = new JSONObject(commentsCountMatcher.group(1));
            int generalCount = commentsCount.getInt("GeneralCount");
        }
        item = itemRepository.saveAndFlush(item);
        return item.getItemId();
    }
}
