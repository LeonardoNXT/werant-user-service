package dev.foundation.userservice.infrastructure.db.repositories;

import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.types.ExceptionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public abstract class Repository {
     protected final EntityManager entityManager;
    protected Repository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public <T> T transaction(Function<EntityManager, T> callback) throws InfrastructureException {

        EntityTransaction t = this.entityManager.getTransaction();

        try{

            t.begin();
            T exec = callback.apply(this.entityManager);
            t.commit();

            return exec;

        } catch (Exception e){

            try{
                if(t.isActive()){
                    t.rollback();
                }
            }
            catch (RollbackException rollbackException){
                throw new InfrastructureException(rollbackException.getMessage(), Repository.class.getSimpleName(), "Invalid transaction", ExceptionType.INTERNAL_SERVER_ERROR);
            }

            if(e instanceof IllegalStateException){
                throw new InfrastructureException(e.getMessage(), Repository.class.getSimpleName(), "Invalid transaction", ExceptionType.INTERNAL_SERVER_ERROR);
            }

            throw e;
        }
    }
}
