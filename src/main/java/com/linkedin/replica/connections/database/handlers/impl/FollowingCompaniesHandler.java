package com.linkedin.replica.connections.database.handlers.impl;

import com.arangodb.ArangoDB;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.handlers.FollowingCompanies;
import com.linkedin.replica.connections.models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FollowingCompaniesHandler extends FollowingCompanies {

    private ArangoDB arangoDB;
    private String dbName;
    public FollowingCompaniesHandler() throws SQLException, IOException, ClassNotFoundException {
        arangoDB = DatabaseConnection.getInstance().getArangodb();
        dbName = Configuration.getInstance().getArangoConfigProp("db.name");
    }

    public void followCompany(String userId, String companyId) {

        String collectionUsers = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        String query = "FOR u IN " + collectionUsers + " FILTER u.userId == @id LET newCompanies = PUSH(u.followedCompanies, @newCompany) UPDATE u WITH{ followedCompanies : newCompanies } IN " + collectionUsers;
        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("id", userId);
        bindVars.put("newCompany", companyId);
        arangoDB.db(dbName).query(query, bindVars, null, null);
    }


    public void unFollowCompany(String userId, String companyId) {
        String collectionUsers = Configuration.getInstance().getArangoConfigProp("collection.users.name");

        String query = "FOR u IN " + collectionUsers + "\n\t FILTER u.userId == @id LET newCompanies = REMOVE_VALUE(u.followedCompanies, @company) UPDATE u WITH{ followedCompanies : newCompanies } IN " + collectionUsers;
        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("id", userId);
        bindVars.put("company", companyId);
        arangoDB.db(dbName).query(query, bindVars, null, User.class);
    }

}
