package com.leyou.repository;

import com.leyou.domain.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 9:07
 * @Version 1.0
 */
public interface IGoodRepository extends ElasticsearchRepository<Goods, Long> {
}
