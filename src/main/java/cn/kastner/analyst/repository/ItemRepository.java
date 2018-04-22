package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    Item findByItemId(String itemId);
    List<Item> findALlByItemId(String itemId);
    List<Item> findByZhName(String zhName);
    List<Item> findAllByZhName(String zhName);
}
