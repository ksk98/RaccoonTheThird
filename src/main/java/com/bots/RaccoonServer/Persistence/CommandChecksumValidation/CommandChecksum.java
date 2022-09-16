package com.bots.RaccoonServer.Persistence.CommandChecksumValidation;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Utility.Tools;

import javax.persistence.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Entity
public class CommandChecksum {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    private String commandKeyword;
    private String checksum;

    public CommandChecksum() {

    }

    public CommandChecksum(Command command) throws IOException, NoSuchAlgorithmException {
        this.commandKeyword = command.getKeyword();
        this.checksum = Tools.getChecksumForObject(command);
    }

    public boolean checksumEqualsTo(Command command) throws IOException, NoSuchAlgorithmException {
        return checksum.equals(Tools.getChecksumForObject(command));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCommandKeyword() {
        return commandKeyword;
    }

    public void setCommandKeyword(String commandKeyword) {
        this.commandKeyword = commandKeyword;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
