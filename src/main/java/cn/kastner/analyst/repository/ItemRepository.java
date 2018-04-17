package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    Item findItemByItemId(String itemId);
    List<Item> findItemByCnameContaining(String Cname);
    List<String> findAllByCnameContaining(String Cname);
}
