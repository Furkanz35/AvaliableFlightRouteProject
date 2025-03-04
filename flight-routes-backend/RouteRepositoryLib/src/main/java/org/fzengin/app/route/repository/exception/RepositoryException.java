package org.fzengin.app.route.repository.exception;

import lombok.Getter;

@Getter
public class RepositoryException extends RuntimeException
{
    private final RepositoryExceptionType repositoryExceptionType;
    public RepositoryException(String message, Throwable cause)
    {
        super(message, cause);
        this.repositoryExceptionType = mapExceptionType(cause);
    }
    public RepositoryException(String message, Throwable cause, RepositoryExceptionType repositoryExceptionType)
    {
        super(message, cause);
        this.repositoryExceptionType = repositoryExceptionType;
    }
    private RepositoryExceptionType mapExceptionType(Throwable cause) {
        if (cause instanceof jakarta.persistence.EntityNotFoundException ||
                cause instanceof org.springframework.dao.EmptyResultDataAccessException ||
                cause instanceof org.springframework.orm.jpa.JpaObjectRetrievalFailureException) {
            return RepositoryExceptionType.ENTITY_NOT_FOUND;
        }
        else if (cause instanceof org.springframework.dao.DataIntegrityViolationException) {
            return RepositoryExceptionType.DATA_INTEGRITY_VIOLATION;
        }
        else if (cause instanceof org.springframework.dao.DataAccessException) {
            return RepositoryExceptionType.DATABASE_ACCESS_ERROR;
        }
        else if (cause instanceof jakarta.persistence.PersistenceException) {
            return RepositoryExceptionType.PERSISTENCE_ERROR;
        }
        else {
            return RepositoryExceptionType.UNKNOWN_ERROR;
        }
    }
}
