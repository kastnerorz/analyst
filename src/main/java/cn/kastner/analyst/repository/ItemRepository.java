package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {

    Item findItemByItemId(String itemId);
}
