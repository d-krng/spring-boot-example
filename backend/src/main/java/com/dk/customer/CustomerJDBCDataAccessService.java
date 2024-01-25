package com.dk.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                select id, name, email, age
                from customer
                """;

        //Different option -> create ur own Mapper and Map the attributes to the right column see->  CustomerRowMapper.java
       /* RowMapper<Customer> customerRowMapper= (rs, rowNum) -> {
           Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getInt("age")
            );
           return customer;
        };*/

        List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper);
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        /*var sql =  """
                select id, name, email, age
                from customer
                where id = +"""+customerId;

          return jdbcTemplate.query(sql,customerRowMapper).stream().findFirst();
               or */

        var sql =  """
                select id, name, email, age
                from customer
                where id = ?
                """;

        return jdbcTemplate.query(sql,customerRowMapper,customerId).stream().findFirst();


    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                insert into customer(name,email,age)
                values(?,?,?)
                """;
        int result = jdbcTemplate.update(sql,customer.getName(),customer.getEmail(),customer.getAge());

        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                select *
                from customer
                where name = ?
                """;

        return jdbcTemplate.query(sql,customerRowMapper,email).stream().findFirst().isPresent();

    }

    @Override
    public void deleteCustomerById(Integer id) {

        var sql = """
                delete from customer
                where id = ?
                """;

        jdbcTemplate.update(sql,id);

    }

    @Override
    public void updateCustomer(Customer customer) {

        var sql = """
                update customer
                set
                name = ?,
                email = ?,
                age = ?
                where id = ?
                """;

        jdbcTemplate.update(sql,customer.getName(),customer.getEmail(),customer.getAge(),customer.getId());
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        var sql = """
               select id, name, email, age
               from customer
               where id = ?
                """;

        return jdbcTemplate.query(sql,customerRowMapper,id).stream().findFirst().isPresent();
    }
}
