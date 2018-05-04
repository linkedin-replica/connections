package com.linkedin.replica.connections.commands.impl;

import com.linkedin.replica.connections.commands.Command;
import com.linkedin.replica.connections.database.handlers.FollowingCompanies;
import com.linkedin.replica.connections.database.handlers.impl.FollowingCompaniesHandler;

import java.util.HashMap;

public class FollowCompanyCommand extends Command{


    public FollowCompanyCommand(HashMap<String, Object> args) {super(args);}

    @Override
    public Object execute() {

        validateArgs(new String[]{"userId", "companyId"});

        String userId = (String) args.get("userId");
        String companyId = (String) args.get("companyId");
        FollowingCompaniesHandler dbHandler = (FollowingCompaniesHandler) this.dbHandler;
        dbHandler.followCompany(userId, companyId);

        return null;
    }
}
