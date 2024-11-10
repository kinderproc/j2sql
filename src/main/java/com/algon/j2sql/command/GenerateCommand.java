package com.algon.j2sql.command;

import lombok.Builder;

@Builder
public class GenerateCommand extends Command {

    @Override
    public boolean execute() {
        return false;
    }

}
