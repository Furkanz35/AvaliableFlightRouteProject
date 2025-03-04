package org.fzengin.app.route.service.exception;

import lombok.Getter;
import org.fzengin.app.route.repository.exception.RepositoryException;

@Getter
public class DataServiceException extends RuntimeException {
    private final DataServiceExceptionType dataServiceExceptionType;

    public DataServiceException(String message, Throwable cause) {
        super(message, cause);
        this.dataServiceExceptionType = mapExceptionType(cause);
    }

    public DataServiceException(String message, Throwable cause, DataServiceExceptionType dataServiceExceptionType) {
        super(message, cause);
        this.dataServiceExceptionType = dataServiceExceptionType;
    }

    public DataServiceExceptionType getDataServiceExceptionType() {
        return dataServiceExceptionType;
    }

    private DataServiceExceptionType mapExceptionType(Throwable cause) {
        if (cause instanceof RepositoryException repositoryException) {
            return switch (repositoryException.getRepositoryExceptionType()) {
                case ENTITY_NOT_FOUND -> DataServiceExceptionType.ENTITY_NOT_FOUND;
                case DATA_INTEGRITY_VIOLATION -> DataServiceExceptionType.DATA_INTEGRITY_VIOLATION;
                case DATABASE_ACCESS_ERROR -> DataServiceExceptionType.DATABASE_ACCESS_ERROR;
                case PERSISTENCE_ERROR -> DataServiceExceptionType.PERSISTENCE_ERROR;
                default -> DataServiceExceptionType.UNKNOWN_ERROR;
            };
        } else {
            return DataServiceExceptionType.UNKNOWN_ERROR;
        }
    }
}