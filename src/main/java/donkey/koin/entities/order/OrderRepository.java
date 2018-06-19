package donkey.koin.entities.order;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findOrderByOrderTypeOrderByTimestampAsc(OrderType orderType);

}
