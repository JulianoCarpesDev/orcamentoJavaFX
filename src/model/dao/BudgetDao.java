package model.dao;

import java.util.List;

import model.entites.Budget;
import model.entites.Clientes;
import model.entites.Produtos;

public interface BudgetDao {
    void insert(Budget obj);

    void update(Budget obj);

    void deleteById(Integer id);

    Budget findById(Integer id);

    List<Budget> findAll();

    List<Budget> findByBudget(Budget budget);



}
