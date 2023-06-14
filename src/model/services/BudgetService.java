package model.services;

import java.util.List;

import model.dao.BudgetDao;
import model.dao.DaoFactory;
import model.entites.Budget;
import model.entites.Clientes;

public class BudgetService {

    private BudgetDao dao = DaoFactory.createBudgetDao();

    public List<Budget> buscarTodos() {
        return dao.findAll();
    }

    public void salvaAtualizarForm(Budget obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Budget obj) {
        dao.deleteById(obj.getId());
    }


  
}
