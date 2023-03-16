package com.github.prgrms.products;


import static java.util.Optional.ofNullable;

import com.github.prgrms.utils.DateTimeUtils;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Product> findById(long id) {
        List<Product> results = jdbcTemplate.query(
                "SELECT * FROM products WHERE seq=?",
                mapper,
                id
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM products ORDER BY seq DESC",
                mapper
        );
    }

    @Override
    public void addReviewCount(Long id, int cnt) {
        jdbcTemplate.update("UPDATE products SET review_count = ? WHERE seq = ?", cnt, id);
    }

    static RowMapper<Product> mapper = (rs, rowNum) ->
            new Product.Builder()
                    .seq(rs.getLong("seq"))
                    .name(rs.getString("name"))
                    .details(rs.getString("details"))
                    .reviewCount(rs.getInt("review_count"))
                    .createAt(DateTimeUtils.dateTimeOf(rs.getTimestamp("create_at")))
                    .build();

}
