package com.promptwise.promptchain.repository;

import org.jooq.exception.InvalidResultException;
import org.jooq.exception.TooManyRowsException;

class RepositoryUtils {

  private RepositoryUtils() {
  }

  static boolean validateSingleUpdate(int numberOfEntitiesUpdated) {
    if (numberOfEntitiesUpdated == 1) {
      return true;
    } else if (numberOfEntitiesUpdated == 0) {
      return false;
    } else {
      throw new TooManyRowsException(String.format("""
              The SQL-UPDATE is being rolled back because the expected update-count was 0 or 1 however 
              the actual update-count returned by the DB was '%d'""", numberOfEntitiesUpdated));
    }
  }

  static boolean validateSingleDelete(int numberOfEntitiesDeleted) {
    if (numberOfEntitiesDeleted == 1) {
      return true;
    } else if (numberOfEntitiesDeleted == 0) {
      return false;
    } else {
      throw new TooManyRowsException(String.format("""
              The SQL-DELETE operation is being rolled back because the expected delete-count was 1
              however the actual delete-count returned by the DB was '%d'""", numberOfEntitiesDeleted));
    }
  }

  static void validateSingleInsert(int numberOfEntitiesInserted) {
    if (numberOfEntitiesInserted < 1) {
      throw new InvalidResultException("The record was not inserted by the database due to an unknown reason!");
    } else if (numberOfEntitiesInserted > 1) {
      throw new TooManyRowsException(String.format("""
              The SQL-INSERT operation is being rolled back because the expected insert-count was 1
              however the actual insert-count returned by the DB was '%d'""", numberOfEntitiesInserted));
    }
  }

}
