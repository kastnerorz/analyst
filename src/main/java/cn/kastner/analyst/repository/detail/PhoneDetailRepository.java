package cn.kastner.analyst.repository.detail;

import cn.kastner.analyst.domain.detail.PhoneDetail;
import cn.kastner.analyst.repository.core.ItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PhoneDetailRepository extends JpaRepository<PhoneDetail, Long>,
        JpaSpecificationExecutor<ItemRepository> {


}
